# 📌 Projet Java - Application de gestion de contacts

## 👥 Présenté par :
- **RASOLOFOMANANA Aina Herilanja** — *126/LA/24-25*
- **RAFIDISON Iangotiana Stacy** — *128/LA/24-25*

---

## ✅ Prérequis

Avant de lancer le projet, assurez-vous d’avoir installé :

- **IntelliJ IDEA Community Edition**
- **OpenJDK 21.0.7**
- **OpenJFX 21.0.7**

> ⚠️ JavaFX doit être correctement configuré dans votre projet.  
> Par exemple, dans IntelliJ, vous pouvez ajouter les modules JavaFX dans `VM options` :

---

## 🛠️ Création de la base de données

Le projet utilise une base de données `GestionContacts` avec une table `contacts`.  
Voici les commandes SQL à exécuter dans votre SGBD (comme MySQL ou MariaDB) :

```sql
-- Création de la base de données
CREATE DATABASE GestionContacts;

-- Utilisation de la base
USE GestionContacts;

-- Création de la table "contacts"
CREATE TABLE contacts (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    email VARCHAR(100)
);
