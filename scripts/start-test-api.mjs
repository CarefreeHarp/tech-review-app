import { createRequire } from 'node:module';
import { resolve } from 'node:path';
import { pathToFileURL } from 'node:url';

// The Android and backend repositories are siblings in this project.
const backend = resolve(import.meta.dirname, '..', '..', 'tech-review-app-backend');
const require = createRequire(resolve(backend, 'package.json'));
const { Client } = require('pg');
const moduleUrl = path => pathToFileURL(resolve(backend, path)).href;
const databaseName = `devicers_codex_check_${Date.now()}`;
const port = Number(process.env.DEVICERS_TEST_PORT || 3001);

let server;
let sequelize;
let admin;
let databaseCreated = false;

try {
  ({ sequelize } = await import(moduleUrl('src/database/database.js')));
  admin = new Client({
    host: sequelize.config.host,
    port: sequelize.config.port,
    user: sequelize.config.username,
    password: sequelize.config.password,
    database: 'postgres',
  });
  await admin.connect();
  await admin.query(`CREATE DATABASE ${databaseName}`);
  databaseCreated = true;

  sequelize.config.database = databaseName;
  sequelize.connectionManager.config.database = databaseName;
  sequelize.options.logging = false;
  await sequelize.authenticate();

  const { setupRelations } = await import(moduleUrl('src/models/relations.js'));
  setupRelations();
  await sequelize.sync();

  const names = [
    'Users', 'Categories', 'Brands', 'Stores', 'Articles', 'Reviews', 'Comments',
    'ReviewLikes', 'CommentLikes', 'Follows', 'ReviewBookmarks', 'ArticleStores',
    'Notifications',
  ];
  const init = Object.fromEntries(await Promise.all(names.map(async name => {
    const module = await import(moduleUrl(`src/database/init${name}.js`));
    return [name, module[`initialize${name}`]];
  })));

  await sequelize.transaction(async transaction => {
    const options = { transaction };
    const users = await init.Users(options);
    const categories = await init.Categories(options);
    const brands = await init.Brands(options);
    const stores = await init.Stores(options);
    const articles = await init.Articles(categories, brands, options);
    const reviews = await init.Reviews(users, articles, options);
    const comments = await init.Comments(users, reviews, options);
    await init.ReviewLikes(users, reviews, options);
    await init.CommentLikes(users, comments, options);
    await init.Follows(users, options);
    await init.ReviewBookmarks(users, reviews, options);
    await init.ArticleStores(articles, stores, options);
    await init.Notifications(users, reviews, comments, options);
  });

  const { default: app } = await import(moduleUrl('src/app.js'));
  server = app.listen(port, '0.0.0.0');
  await new Promise((resolve, reject) => {
    server.once('listening', resolve);
    server.once('error', reject);
  });
  console.log(`Devicers test API ready on port ${port}. Press Enter to stop and remove its temporary database.`);
  await new Promise(resolve => {
    process.once('SIGINT', resolve);
    process.once('SIGTERM', resolve);
    process.stdin.resume();
    process.stdin.once('data', resolve);
  });
} finally {
  process.stdin.pause();
  if (server?.listening) await new Promise(resolve => server.close(resolve));
  if (sequelize) await sequelize.close();
  if (databaseCreated) await admin.query(`DROP DATABASE ${databaseName}`);
  if (admin) await admin.end();
}
