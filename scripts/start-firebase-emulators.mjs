import { spawn } from 'node:child_process';
import { existsSync, readFileSync } from 'node:fs';
import { resolve } from 'node:path';
import { setTimeout as delay } from 'node:timers/promises';

// El modo limpio usa únicamente datos nuevos en memoria; nunca borra ni exporta sobre el respaldo.
const mode = process.argv[2];
if (!['saved', 'clean'].includes(mode)) {
  console.error('Indica saved o clean.');
  process.exit(1);
}

const workspace = resolve(import.meta.dirname, '../CLOUD FUNCTIONS');
const config = JSON.parse(readFileSync(resolve(workspace, 'firebase.emulators.json'), 'utf8'));
const projectId = JSON.parse(readFileSync(resolve(workspace, '.firebaserc'), 'utf8')).projects.default;
const dataDirectory = resolve(workspace, 'emulator-data');
const firestore = config.emulators.firestore;
const base = `http://127.0.0.1:${firestore.port}/v1/projects/${projectId}/databases/(default)/documents`;
const hubPort = config.emulators.hub?.port ?? 4400;

// Evita inicializar por accidente los datos de otra sesión que ya ocupa los mismos puertos.
try {
  const response = await fetch(`http://127.0.0.1:${hubPort}/emulators`, { signal: AbortSignal.timeout(1000) });
  if (response.ok) {
    console.error('Firebase ya está ejecutándose. Detén la otra sesión con Ctrl+C antes de iniciar esta.');
    process.exit(1);
  }
} catch {
  // Sin un hub activo, la CLI verificará los puertos al iniciar los servicios.
}

const args = ['emulators:start', '--config', 'firebase.emulators.json', '--only',
  'firestore,auth,storage,functions', '--project', projectId];
if (mode === 'saved') {
  if (existsSync(resolve(dataDirectory, 'firebase-export-metadata.json'))) {
    args.push('--import', dataDirectory);
  }
  args.push('--export-on-exit', dataDirectory);
}

console.log(mode === 'saved'
  ? 'Iniciando Firebase con persistencia local. Ctrl+C guarda los datos al salir.'
  : 'Iniciando Firebase sin usuarios, artículos ni archivos. El respaldo guardado se conserva.');
const child = spawn('firebase', args, { cwd: workspace, stdio: 'inherit', detached: true });
let stopped = false;
let stopping = false;
const completion = new Promise(resolveExit => {
  child.once('error', error => {
    console.error(`No se pudo iniciar Firebase CLI: ${error.message}`);
    stopped = true;
    resolveExit(1);
  });
  child.once('exit', (code, signal) => {
    stopped = true;
    resolveExit(code ?? (signal === 'SIGINT' ? 0 : 1));
  });
});

// La CLI recibe SIGINT para poder completar su exportación antes de cerrar el proceso padre.
for (const signal of ['SIGINT', 'SIGTERM']) {
  process.on(signal, () => {
    if (stopping) return;
    stopping = true;
    child.kill('SIGINT');
  });
}

try {
  const deadline = Date.now() + 120_000;
  while (!stopped && !stopping) {
    let response;
    try {
      response = await fetch(`${base}/counters/next_id`, {
        headers: { Authorization: 'Bearer owner' }, signal: AbortSignal.timeout(1000),
      });
    } catch {
      if (Date.now() > deadline) throw new Error('Firestore local no inició en dos minutos.');
      await delay(250);
      continue;
    }
    if (response.status === 404) {
      // El registro y la publicación de reseñas requieren este documento incluso en una base vacía.
      const created = await fetch(`${base}/counters/next_id?currentDocument.exists=false`, {
        method: 'PATCH', headers: { Authorization: 'Bearer owner', 'Content-Type': 'application/json' },
        body: JSON.stringify({ fields: Object.fromEntries(
          ['next_user_id', 'next_review_id', 'next_notification_id'].map(key => [key, { integerValue: '1' }]),
        ) }), signal: AbortSignal.timeout(5000),
      });
      if (!created.ok) throw new Error(`No se pudo inicializar el contador local (HTTP ${created.status}).`);
      console.log('Contadores locales inicializados: ya puedes registrar usuarios.');
    } else if (!response.ok) {
      throw new Error(`No se pudo consultar el contador local (HTTP ${response.status}).`);
    }
    break;
  }
} catch (error) {
  console.error(error.message);
  process.exitCode = 1;
  child.kill('SIGINT');
}

const exitCode = await completion;
process.exitCode ||= exitCode;
