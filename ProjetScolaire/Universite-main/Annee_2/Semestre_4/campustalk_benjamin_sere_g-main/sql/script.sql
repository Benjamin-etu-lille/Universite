-- =======================================================
-- SCRIPT PRINCIPAL - CAMPUSTALK
-- =======================================================
-- Ce script SQL permet de réinitialiser, créer et peupler 
-- la base de données du projet CampusTalk. Il effectue les 
-- opérations suivantes :
-- 1 (script.sql). Suppression des tables existantes si elles sont présentes.
-- 2 (script.sql). Création des tables et définition des relations entre elles.
-- 3 (peuplement.sql). Insertion de données de test pour simuler des utilisateurs,
--    des fils de discussion, des abonnements et des messages.
-- 4 (requetes.sql). Requêtes SQL pour tester la base de données du projet. 
-- =======================================================

-- =======================================================
-- ⚠️ ATTENTION : EN MISE EN PRODUCTION
-- Avant de déployer en production, supprimer les lignes :
-- - Ligne 67 du fichier script.sql
-- - Ligne 51 du fichier peuplement.sql
-- Ces lignes contiennent des données de test 
-- et des instructions non adaptées à l'environnement de production.
-- =======================================================


-- =======================================================
-- 1. DROP DES TABLES EXISTANTE
-- =======================================================


-- 1. Supprimer les tables dans l'ordre correct
DROP TABLE IF EXISTS Message CASCADE;
DROP TABLE IF EXISTS Abonnement CASCADE;
DROP TABLE IF EXISTS Fil CASCADE;
DROP TABLE IF EXISTS Notification CASCADE;
DROP TABLE IF EXISTS Utilisateur CASCADE;
DROP TABLE IF EXISTS "Like" CASCADE;

-- =======================================================
-- 2. CREATION DES TABLES EST DE LEURS RELATIONS
-- =======================================================

CREATE TABLE IF NOT EXISTS Utilisateur (
    uno VARCHAR(50) PRIMARY KEY, /* pseudo unique de chaque utilisateur */
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    mdp VARCHAR(25) NOT NULL,
    dateCreation TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS Fil (
    fno SERIAL PRIMARY KEY,
    titre VARCHAR(50) NOT NULL,
    description VARCHAR(500),
    dateCreation TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS Abonnement (
    uno VARCHAR(50),
    fno INT,
    role VARCHAR(50), /*Etat de l'utilisateur par rapport au Fil USER , ADMIN si l'utilisateur est inviter a un fil*/
    PRIMARY KEY (uno, fno),
    FOREIGN KEY (uno) REFERENCES Utilisateur(uno) ON DELETE CASCADE,
    FOREIGN KEY (fno) REFERENCES Fil(fno) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Message (
    mno SERIAL PRIMARY KEY,
    contenu VARCHAR(500) NOT NULL,
    imagePath VARCHAR(255),
    dateEnvoi TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    uno  VARCHAR(50) NOT NULL,
    fno INT NOT NULL,
    FOREIGN KEY (uno) REFERENCES Utilisateur(uno) ON DELETE CASCADE,
    FOREIGN KEY (fno) REFERENCES Fil(fno) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Notification (
    nno SERIAL PRIMARY KEY,          
    uno VARCHAR(50) NOT NULL,         
    fno INT NOT NULL,                 
    type VARCHAR(50) NOT NULL,
    etat VARCHAR(20) DEFAULT 'EN_ATTENTE' NOT NULL, /*REFUSEE , ACCEPTEE */
    dateEnvoi TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (uno) REFERENCES Utilisateur(uno) ON DELETE CASCADE,
    FOREIGN KEY (fno) REFERENCES Fil(fno) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS "Like" ( /* les cotes pour proteger le Like si non il confon avec la fonction like*/
    uno VARCHAR(50) NOT NULL,
    mno INT NOT NULL,
    dateLike TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (uno, mno),
    FOREIGN KEY (uno) REFERENCES Utilisateur(uno) ON DELETE CASCADE,
    FOREIGN KEY (mno) REFERENCES Message(mno) ON DELETE CASCADE
);

--Ligne a commenter lors de la mis en prod.
--\i peuplement.sql 