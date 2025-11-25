# 💬 Eventy Interactions Service

Le **Interactions Service** gère la couche sociale et la modération de la plateforme Eventy. Il permet aux utilisateurs de communiquer, de s'évaluer mutuellement et de signaler des comportements ou contenus inappropriés.

## 🚀 Fonctionnalités

* **Messagerie Instantanée** :
    * Envoi et réception de messages entre utilisateurs (Acheteur <-> Vendeur).
    * Gestion des conversations liées ou non à un billet spécifique.
    * Suivi du statut de lecture.
* **Système d'Avis (Reviews)** :
    * Notation (1 à 5 étoiles) et commentaires sur les utilisateurs après une transaction.
    * Calcul de la réputation des vendeurs.
* **Signalements (Reports)** :
    * Création de signalements pour utilisateurs, billets ou événements frauduleux.
    * Gestion du cycle de vie des signalements (En attente, En cours, Résolu, Rejeté) pour l'administration.

## 🛠️ Stack Technique

* **Langage** : Java 21
* **Framework** : Spring Boot 3.5.x
* **Base de données** : PostgreSQL 15
* **Communication** :
    * **OpenFeign** : Récupération des profils utilisateurs (Noms, Avatars) via `eventy-users-service`.
* **Découverte** : Netflix Eureka Client
* **Outils** : Lombok, Maven, Docker

## ⚙️ Installation et Démarrage

### Prérequis
* JDK 21 installé
* Docker & Docker Compose

### Démarrage en local (Docker Compose)

Ce service s'intègre dans la stack complète Eventy.

# Depuis la racine du projet backend global
docker-compose up -d --build eventy-interactions-service

Le service sera accessible sur le port **8086**.

### Démarrage autonome (Développement)

 ./mvnw spring-boot:run   

🔧 Configuration
----------------

Variables d'environnement principales (docker-compose.yml) :

📚 API Reference
----------------

### Messagerie (/messages)

*   POST /messages : Envoyer un message.
    
*   GET /messages/conversation/{conversationId} : Récupérer l'historique d'une conversation.
    
*   GET /messages/user/{userId} : Lister les conversations d'un utilisateur.
    
*   PUT /messages/{id}/read : Marquer un message comme lu.
    

### Avis (/reviews)

*   POST /reviews : Laisser un avis sur un utilisateur.
    
*   GET /reviews/user/{userId} : Voir les avis reçus par un utilisateur.
    

### Signalements (/reports)

*   POST /reports : Créer un signalement.
    
*   GET /reports : Lister tous les signalements (Admin).
    
*   PATCH /reports/{id}/status : Mettre à jour le statut d'un signalement (Admin).
    

© 2025 Eventy Project
