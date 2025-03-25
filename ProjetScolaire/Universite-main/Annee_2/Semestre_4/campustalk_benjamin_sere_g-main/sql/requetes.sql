-- =======================================================
-- Requetes TEST SI - CAMPUSTALK
-- =======================================================
-- Ce script contient les requêtes SQL pour tester la base
-- de données du projet CampusTalk.
-- =======================================================

-- =======================================================
-- 1. CONSULTATION DES DONNÉES
-- =======================================================

-- 1.1 - Afficher tous les fils auxquels un utilisateur est abonné
SELECT f.fno, f.titre, f.description, f.dateCreation
FROM Fil f
JOIN Abonnement a ON f.fno = a.fno
WHERE a.uno = 'DoeLeBG';

-- 1.2 - Afficher tous les messages d'un fil donné pour un utilisateur abonné
SELECT m.mno, m.contenu, m.dateEnvoi, f.titre
FROM Message m
JOIN Fil f ON m.fno = f.fno
JOIN Abonnement a ON m.fno = a.fno
WHERE a.uno = 'DoeLeBG' AND m.fno = 3;

-- 1.3 - Vérifier si un utilisateur est ADMIN d’un fil
SELECT COUNT(*) 
FROM Abonnement 
WHERE uno = 'DoeLeBG' AND fno = 3 AND role = 'ADMIN';

-- 1.4 - Tester l’accès interdit aux messages d’un fil non abonné
SELECT m.mno, m.contenu, m.dateEnvoi, f.titre
FROM Message m
JOIN Fil f ON m.fno = f.fno
JOIN Abonnement a ON m.fno = a.fno
WHERE a.uno = 'DoeLeBG' AND m.fno = 2; 

-- 1.5 - Afficher tous les messages des fils auxquels un utilisateur est abonné
SELECT m.mno, m.contenu, m.dateEnvoi, f.titre
FROM Message m
JOIN Abonnement a ON m.fno = a.fno
JOIN Fil f ON f.fno = m.fno
WHERE a.uno = 'Smith59136'
ORDER BY m.dateEnvoi;

-- 1.6 - Compter le nombre de messages par fil
SELECT f.titre, COUNT(m.mno) AS nb_messages
FROM Fil f
LEFT JOIN Message m ON f.fno = m.fno
GROUP BY f.titre;

-- =======================================================
-- 2. MODIFICATION DES DONNÉES
-- =======================================================

-- 2.1 - Ajouter un nouvel abonnement
INSERT INTO Abonnement (uno, fno, role) VALUES ('XxBrownxX', 3, 'MEMBRE');

-- 2.2 - Bannir un utilisateur d’un fil
UPDATE Abonnement 
SET role = 'BANNI' 
WHERE uno = 'Smith59136' AND fno = 3 
AND EXISTS (
    SELECT 1 
    FROM Abonnement 
    WHERE uno = 'AdminUser' AND fno = 3 AND role = 'ADMIN'
);

-- 2.3 - Promouvoir un utilisateur en ADMIN
UPDATE Abonnement 
SET role = 'ADMIN' 
WHERE uno = 'Smith59136' AND fno = 3 
AND EXISTS (
    SELECT 1 
    FROM Abonnement 
    WHERE uno = 'AdminUser' AND fno = 3 AND role = 'ADMIN'
);

-- =======================================================
-- 3. CONTRÔLE D’ACCÈS AUX MESSAGES
-- =======================================================

-- 3.1 - Un ADMIN peut supprimer un message
DELETE FROM Message 
WHERE mno = 5 
AND fno = 3 
AND EXISTS (
    SELECT 1 
    FROM Abonnement 
    WHERE uno = 'AdminUser' AND fno = 3 AND role = 'ADMIN'
);

-- 3.2 - Un MEMBRE ou un ADMIN peut poster un message
INSERT INTO Message (contenu, dateEnvoi, uno, fno)
SELECT 'Mon message important', CURRENT_TIMESTAMP, 'DoeLeBG', 3
WHERE EXISTS (
    SELECT 1 
    FROM Abonnement 
    WHERE uno = 'DoeLeBG' AND fno = 3 AND role <> 'BANNI'
);

-- 3.3 - Filtrer les messages pour que les bannis ne puissent rien voir
SELECT m.mno, m.contenu, m.dateEnvoi, f.titre 
FROM Message m
JOIN Fil f ON m.fno = f.fno
WHERE m.fno = 3
AND EXISTS (
    SELECT 1 
    FROM Abonnement 
    WHERE uno = 'DoeLeBG' AND fno = m.fno AND role <> 'BANNI'
);

-- =======================================================
-- 4. Vérification des Likes
-- =======================================================

-- 4.1 - Vérifier si un utilisateur a aimé un message spécifique
SELECT COUNT(*) 
FROM "Like" 
WHERE uno = 'DoeLeBG' AND mno = 5;

-- 4.2 - Afficher tous les "likes" d'un message spécifique
SELECT l.uno, l.dateLike 
FROM "Like" l 
WHERE l.mno = 5;

-- 4.3 - Afficher tous les messages qu'un utilisateur a aimés
SELECT m.mno, m.contenu, m.dateEnvoi 
FROM Message m 
JOIN "Like" l ON m.mno = l.mno
WHERE l.uno = 'DoeLeBG';

-- 4.4 - Compter le nombre de "likes" sur un message
SELECT COUNT(*) 
FROM "Like" 
WHERE mno = 5;

-- =======================================================
-- 5. Vérification des Notifications
-- =======================================================

-- 5.1 - Afficher toutes les notifications d'un utilisateur pour un fil spécifique
SELECT n.nno, n.type, n.etat, n.dateEnvoi
FROM Notification n
WHERE n.uno = 'DoeLeBG' AND n.fno = 3;

-- 5.2 - Vérifier si une notification est en attente pour un utilisateur et un fil spécifique
SELECT COUNT(*) 
FROM Notification 
WHERE uno = 'DoeLeBG' AND fno = 3 AND etat = 'EN_ATTENTE';

-- 5.3 - Mettre à jour l'état d'une notification (par exemple, de "EN_ATTENTE" à "ACCEPTEE")
UPDATE Notification 
SET etat = 'ACCEPTEE' 
WHERE nno = 10;

-- 5.4 - Ajouter une nouvelle notification pour un utilisateur dans un fil
INSERT INTO Notification (uno, fno, type, etat)
VALUES ('DoeLeBG', 3, 'abonnement', 'EN_ATTENTE');

-- =======================================================
-- 6. Test de Cohérence entre Likes et Notifications
-- =======================================================

-- 6.1 - Vérifier si un utilisateur a reçu une notification suite à un "like"
SELECT n.nno, n.type, n.etat
FROM Notification n
JOIN "Like" l ON n.uno = l.uno
WHERE l.mno = 5 AND n.fno = (SELECT fno FROM Message WHERE mno = 5);

-- =======================================================
-- 7. AUTRES REQUÊTES UTILES
-- =======================================================

-- 7.1 - Lister les abonnements de chaque utilisateur
SELECT u.uno, u.nom, u.prenom, f.fno, f.titre
FROM Utilisateur u
JOIN Abonnement a ON u.uno = a.uno
JOIN Fil f ON a.fno = f.fno
ORDER BY u.uno;

-- 7.2 - Ajouter un nouveau message dans un fil
INSERT INTO Message (contenu, dateEnvoi, uno, fno)
VALUES ('Nouveau message dans General!', CURRENT_TIMESTAMP, 'DoeLeBG', 1);

-- 7.3 - Vérifier l’ajout du message
SELECT m.mno, m.contenu, m.dateEnvoi, u.uno
FROM Message m
JOIN Utilisateur u ON m.uno = u.uno
WHERE m.fno = 1
ORDER BY m.dateEnvoi;

-- =======================================================
-- FIN DU SCRIPT
-- =======================================================
