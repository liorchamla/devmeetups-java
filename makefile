# Makefile — DevMeetups (macOS/Linux)
# Usage rapide :
#   make run                 # lance l'app (profil dev, port 8080 par défaut)
#   make run PORT=5000       # lance sur un autre port
#   make test                # tests unitaires rapides
#   make verify              # pipeline complet (lint + tests + coverage)
#   make fmt                 # corrige format/imports (Spotless)
#   make lint                # Checkstyle + PMD (sans tests)
#   make migrate             # applique les migrations Flyway (scripts/migrate.sh)
#   make db-up / db-down     # démarre/arrête MariaDB via docker-compose

SHELL := /bin/bash

# Détecte mvnw si présent, sinon fallback sur mvn
MVNW := $(shell [ -x ./mvnw ] && echo ./mvnw || echo mvn)
MVN  := $(MVNW) -f app/pom.xml -ntp

# Paramètres app
SPRING_PROFILE ?= dev
PORT           ?= 8080
SPRING_ARGS    ?= --spring.profiles.active=$(SPRING_PROFILE) --server.port=$(PORT)

# DB docker-compose
COMPOSE_FILE ?= docker-compose.yml
DB_SERVICE   ?= db

.DEFAULT_GOAL := help

.PHONY: help
help: ## Affiche cette aide
	@grep -E '^[a-zA-Z0-9_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS=":.*?## "}; {printf "  \033[36m%-18s\033[0m %s\n", $$1, $$2}'

# -------------------------
# Run / Build
# -------------------------

.PHONY: run
run: ## Lance l'app Spring Boot (profil=$(SPRING_PROFILE), port=$(PORT))
	$(MVN) spring-boot:run -Dspring-boot.run.arguments="$(SPRING_ARGS)"

.PHONY: clean
clean: ## Nettoie les artefacts Maven
	$(MVN) -q clean

# -------------------------
# Tests & Qualité
# -------------------------

.PHONY: test
test: ## Tests unitaires rapides (sans IT), profil fast-tests si défini
	$(MVN) -q -DskipITs -Pfast-tests test

.PHONY: verify
verify: ## Vérification complète (lint + tests + coverage) pour la CI
	$(MVN) -q verify

.PHONY: lint
lint: ## Lint Checkstyle + PMD (sans tests)
	$(MVN) -q -DskipTests checkstyle:check pmd:check

.PHONY: fmt
fmt: ## Auto-format (Spotless)
	$(MVN) -q spotless:apply

.PHONY: fmt-check
fmt-check: ## Vérifie le format (Spotless) sans modifier
	$(MVN) -q spotless:check

# -------------------------
# Migrations Flyway
# -------------------------

.PHONY: migrate migrate-info migrate-repair
migrate: ## Applique les migrations Flyway (scripts/migrate.sh)
	$(MVN) -Dflyway.url=jdbc:mariadb://localhost:3310/devmeetups \
    -Dflyway.user=devmeetups \
    -Dflyway.password=devmeetups \
    -Dflyway.locations=classpath:db/migrations \
    flyway:migrate

migrate-info: ## Affiche l'historique Flyway
	$(MVN) -Dflyway.url=jdbc:mariadb://localhost:3310/devmeetups \
    -Dflyway.user=devmeetups \
    -Dflyway.password=devmeetups \
    -Dflyway.locations=classpath:db/migrations \
    flyway:info

migrate-repair: ## Répare l'historique Flyway (checksums) — à utiliser avec prudence
	$(MVN) -Dflyway.url=jdbc:mariadb://localhost:3310/devmeetups \
    -Dflyway.user=devmeetups \
    -Dflyway.password=devmeetups \
    -Dflyway.locations=classpath:db/migrations \
    flyway:repair

# -------------------------
# Docker (DB locale)
# -------------------------

.PHONY: db-up db-down db-logs
db-up: ## Démarre MariaDB via docker-compose
	docker compose -f $(COMPOSE_FILE) up -d $(DB_SERVICE)

db-down: ## Stoppe MariaDB
	docker compose -f $(COMPOSE_FILE) down

db-logs: ## Suivi des logs MariaDB
	docker compose -f $(COMPOSE_FILE) logs -f $(DB_SERVICE)
