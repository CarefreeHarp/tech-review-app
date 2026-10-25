SHELL := /bin/bash
.DEFAULT_GOAL := help

.PHONY: help firebase-clean seed-local seed-remote

help:
	@echo "make firebase-clean Inicia Firebase vacío para pruebas, sin modificar el respaldo."
	@echo "make seed-local     Carga datos ficticios en Firestore local (requiere el emulador encendido)."
	@echo "make seed-remote    Carga datos ficticios en Firebase real, previa confirmación."

# Carga Node 24 y su Firebase CLI también desde terminales que todavía usan Node del sistema.
firebase-clean:
	@set -e; \
	if [[ -s "$$HOME/.nvm/nvm.sh" ]]; then \
		source "$$HOME/.nvm/nvm.sh"; nvm use 24 > /dev/null; \
	fi; \
	exec node scripts/start-firebase-emulators.mjs clean

seed-local:
	@set -e; \
	if [[ -s "$$HOME/.nvm/nvm.sh" ]]; then \
		source "$$HOME/.nvm/nvm.sh"; nvm use 24 > /dev/null; \
	fi; \
	exec env FIRESTORE_EMULATOR_HOST=127.0.0.1:8080 node SEED_DATA/seed.js

seed-remote:
	@set -e; \
	read -r -p "Esto puede sobrescribir datos reales. Escribe devicers-d04dd para continuar: " confirmation; \
	[[ "$$confirmation" == "devicers-d04dd" ]] || { echo "Carga cancelada."; exit 1; }; \
	if [[ -s "$$HOME/.nvm/nvm.sh" ]]; then \
		source "$$HOME/.nvm/nvm.sh"; nvm use 24 > /dev/null; \
	fi; \
	exec env -u FIRESTORE_EMULATOR_HOST node SEED_DATA/seed.js
