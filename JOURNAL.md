# Jour 1 : initialisation du dépot

## Première étape
- Initialisation du dépot avec la création d'une application via Spring Boot Initializer
- Enrichissement du fichier pom.xml de telle sorte qu'on puisse :
-- Définir l'artefact
-- Ajouter du checkstyle pour renforcer les règles de lint
-- Utiliser Jacoco pour le coverage des tests unitaires
-- Utiliser PMD pour renforcer les bonnes pratiques / code-smells
- Création d'un tout premier controller avec un test unitaire qui couvre
- Mise en place d'une CI pour GitHub Actions

## Deuxième étape : Un endpoint avec DTO et documentation OpenApi
- Création d'un DTO `EventSummary` qui représente un événément sans validation
- Création d'un DTO `CreateEventRequest` qui représente un payload de création d'événement avec validation des propriétés par Jakarta qui ressemble comme deux gouttes d'eau à Symfony/Component/Validator/Constraints
- Création d'un controller `EventsController` qui utilise un "repository" inMemory pour lister les événements et créer un événément
- Test unitaire simple pour tester la validation Jakarta du `CreateEventRequest`
- Test d'intégration SpringBoot pour tester les endpoints de `EventsController`

- Mise en place d'une configuration OpenApi `OpenApiConfig.java` qui donne les informations globales de notre API (titre, desc, etc)
- Mise en place d'annotations OpenAPI sur notre controller avec un tag `events`
- Mise en place de tests unitaires sur la documentation OpenAPI elle même ("culture contrat")
- Mise en place aussi de settings pour VSCode afin de lancer les tests à chaque sauvegarde
- Mise en place de "HMR" dans le serveur web

# Jour 2 : JPA
- Mapping de Event et Registration avec la base de données via JPA
- Création des repositorys (bizarre mais ça passe par des interfaces)
- Tests des repositories avec les @Testcontainer qui permettent de ne pas avoir à créer un container docker pour les tests, ça tourne directement dans la suite
- Tests des controllers qui incluent maintenant toute la stack grâce à @SpringBootTest et @AutoconfigureMockMvc pour garder l'objet mvc
- Création d'un makefile pour faciliter les tâches de test, run, migration flyway etc
