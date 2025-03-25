-- =======================================================
-- PEUPLEMENT - CAMPUSTALK
-- =======================================================
-- Ce script SQL d'effectuer une insertion de données de test pour simuler des utilisateurs,
-- des fils de discussion, des abonnements et des messages.
-- =======================================================

INSERT INTO Utilisateur (uno, nom, prenom, email, mdp)
VALUES 
('john_doe', 'Doe', 'John', 'john.doe@example.com', 'password123'),
('jane_smith', 'Smith', 'Jane', 'jane.smith@example.com', 'password456'),
('paul_walker', 'Walker', 'Paul', 'paul.walker@example.com', 'password789'),
('lucas_roy', 'Roy', 'Lucas', 'lucas.roy@example.com', 'password321'),
('emma_brown', 'Brown', 'Emma', 'emma.brown@example.com', 'password147'),
('oliver_jones', 'Jones', 'Oliver', 'oliver.jones@example.com', 'password258'),
('noah_miller', 'Miller', 'Noah', 'noah.miller@example.com', 'password369'),
('ava_davis', 'Davis', 'Ava', 'ava.davis@example.com', 'password456'),
('isabella_moore', 'Moore', 'Isabella', 'isabella.moore@example.com', 'password654'),
('liam_taylor', 'Taylor', 'Liam', 'liam.taylor@example.com', 'password987'),
('mason_anderson', 'Anderson', 'Mason', 'mason.anderson@example.com', 'password159'),
('sophia_jackson', 'Jackson', 'Sophia', 'sophia.jackson@example.com', 'password753'),
('ethan_white', 'White', 'Ethan', 'ethan.white@example.com', 'password258'),
('harper_harris', 'Harris', 'Harper', 'harper.harris@example.com', 'password654'),
('jackson_clark', 'Clark', 'Jackson', 'jackson.clark@example.com', 'password369'),
('ella_lewis', 'Lewis', 'Ella', 'ella.lewis@example.com', 'password741'),
('sebastian_wilson', 'Wilson', 'Sebastian', 'sebastian.wilson@example.com', 'password852'),
('mila_scarlett', 'Scarlett', 'Mila', 'mila.scarlett@example.com', 'password963'),
('leo_martin', 'Martin', 'Leo', 'leo.martin@example.com', 'password741'),
('zara_taylor', 'Taylor', 'Zara', 'zara.taylor@example.com', 'password369');

INSERT INTO Fil (titre, description)
VALUES 
('Tech News', 'Fil de discussion sur les dernières nouvelles technologiques'),
('Fitness', 'Fil pour discuter de l’entraînement et de la nutrition'),
('Cinema', 'Fil pour discuter des derniers films sortis'),
('Jeux Vidéo', 'Fil pour les passionnés de jeux vidéo'),
('Musique', 'Fil de discussion sur les tendances musicales actuelles'),
('Voyages', 'Fil pour discuter des meilleures destinations de voyage'),
('Art et Design', 'Fil pour discuter des tendances artistiques et du design'),
('Développement Web', 'Fil pour discuter du développement web et des nouvelles technologies'),
('Football', 'Fil pour discuter des dernières actualités du football'),
('Cuisine', 'Fil pour échanger des recettes et conseils culinaires');

INSERT INTO Abonnement (uno, fno, role)
VALUES
('john_doe', 1, 'ADMIN'),
('jane_smith', 1, 'USER'),
('paul_walker', 1, 'USER'),
('lucas_roy', 2, 'USER'),
('emma_brown', 2, 'ADMIN'),
('oliver_jones', 3, 'USER'),
('noah_miller', 3, 'ADMIN'),
('ava_davis', 4, 'USER'),
('isabella_moore', 4, 'ADMIN'),
('liam_taylor', 5, 'USER'),
('mason_anderson', 5, 'ADMIN'),
('sophia_jackson', 6, 'USER'),
('ethan_white', 6, 'ADMIN'),
('harper_harris', 7, 'USER'),
('jackson_clark', 7, 'ADMIN'),
('ella_lewis', 8, 'USER'),
('sebastian_wilson', 8, 'ADMIN'),
('mila_scarlett', 9, 'USER'),
('leo_martin', 9, 'ADMIN'),
('zara_taylor', 10, 'USER');

INSERT INTO Message (contenu, uno, fno)
VALUES 
('Découvrez les dernières tendances technologiques en IA.', 'john_doe', 1),
('Quelles sont vos routines de fitness matinales ?', 'lucas_roy', 2),
('Quel est le dernier film que vous avez vu ?', 'noah_miller', 3),
('J’ai terminé un jeu récemment, vous devriez l’essayer!', 'isabella_moore', 4),
('J’ai une nouvelle playlist à partager, qui veut l’écouter ?', 'emma_brown', 5),
('Où comptez-vous voyager cet été ?', 'harper_harris', 6),
('Le dernier projet de design que j’ai vu est incroyable!', 'ella_lewis', 7),
('J’ai vu un match incroyable hier soir!', 'leo_martin', 8),
('Je viens de préparer une nouvelle recette, à tester absolument!', 'jackson_clark', 9),
('Qui a suivi le dernier match de football ce weekend?', 'sophia_jackson', 10);

INSERT INTO Notification (uno, fno, type, etat)
VALUES
('john_doe', 1, 'invitation', 'EN_ATTENTE'),
('jane_smith', 2, 'invitation', 'ACCEPTEE'),
('paul_walker', 3, 'invitation', 'REFUSEE'),
('lucas_roy', 4, 'invitation', 'EN_ATTENTE'),
('emma_brown', 5, 'invitation', 'ACCEPTEE'),
('oliver_jones', 6, 'invitation', 'REFUSEE'),
('noah_miller', 7, 'invitation', 'EN_ATTENTE'),
('ava_davis', 8, 'invitation', 'ACCEPTEE'),
('isabella_moore', 9, 'invitation', 'REFUSEE'),
('liam_taylor', 10, 'invitation', 'EN_ATTENTE');

INSERT INTO "Like" (uno, mno)
VALUES
('john_doe', 1),
('jane_smith', 2),
('paul_walker', 3),
('lucas_roy', 4),
('emma_brown', 5),
('oliver_jones', 6),
('noah_miller', 7),
('ava_davis', 8),
('isabella_moore', 9),
('liam_taylor', 10);

--Ligne a commenter lors de la mis en prod.
--\i requetes.sql 