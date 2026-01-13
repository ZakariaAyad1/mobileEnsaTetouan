-- ============================================
-- BASE DE DONNÉES SQLite
-- Application Gestion Notes & Certificats
-- ENSA Tétouan
-- ============================================

-- ============================================
-- TABLE: users (Table mère pour tous les utilisateurs)
-- ============================================
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    email TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    role TEXT NOT NULL CHECK(role IN ('PROFESSEUR', 'ETUDIANT', 'AGENT', 'MANAGER')),
    created_at TEXT DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- TABLE: professeurs
-- ============================================
CREATE TABLE professeurs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    nom TEXT NOT NULL,
    prenom TEXT NOT NULL,
    departement TEXT,
    telephone TEXT,
    photo_url TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ============================================
-- TABLE: etudiants
-- ============================================
CREATE TABLE etudiants (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    cne TEXT UNIQUE NOT NULL,
    nom TEXT NOT NULL,
    prenom TEXT NOT NULL,
    date_naissance TEXT,
    lieu_naissance TEXT,
    filiere TEXT NOT NULL,
    annee_etude TEXT NOT NULL,
    photo_url TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ============================================
-- TABLE: admins
-- ============================================
CREATE TABLE admins (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    nom TEXT NOT NULL,
    prenom TEXT NOT NULL,
    fonction TEXT NOT NULL CHECK(fonction IN ('AGENT_SCOLARITE', 'RESPONSABLE_APOGEE', 'MANAGER')),
    telephone TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ============================================
-- TABLE: modules
-- ============================================
CREATE TABLE modules (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    code_module TEXT UNIQUE NOT NULL,
    nom_module TEXT NOT NULL,
    semestre INTEGER NOT NULL CHECK(semestre BETWEEN 1 AND 10),
    coefficient REAL NOT NULL DEFAULT 1.0,
    professeur_id INTEGER,
    annee_universitaire TEXT NOT NULL,
    FOREIGN KEY (professeur_id) REFERENCES professeurs(id) ON DELETE SET NULL
);

-- ============================================
-- TABLE: inscriptions (Relation Etudiant-Module)
-- ============================================
CREATE TABLE inscriptions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    etudiant_id INTEGER NOT NULL,
    module_id INTEGER NOT NULL,
    annee_universitaire TEXT NOT NULL,
    date_inscription TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (etudiant_id) REFERENCES etudiants(id) ON DELETE CASCADE,
    FOREIGN KEY (module_id) REFERENCES modules(id) ON DELETE CASCADE,
    UNIQUE(etudiant_id, module_id, annee_universitaire)
);

-- ============================================
-- TABLE: notes
-- ============================================
CREATE TABLE notes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    etudiant_id INTEGER NOT NULL,
    module_id INTEGER NOT NULL,
    note_examen REAL CHECK(note_examen BETWEEN 0 AND 20),
    note_td REAL CHECK(note_td BETWEEN 0 AND 20),
    note_tp REAL CHECK(note_tp BETWEEN 0 AND 20),
    note_finale REAL CHECK(note_finale BETWEEN 0 AND 20),
    statut TEXT CHECK(statut IN ('VALIDE', 'NON_VALIDE', 'RATTRAPAGE')),
    date_examen TEXT,
    date_saisie TEXT DEFAULT CURRENT_TIMESTAMP,
    observation TEXT,
    FOREIGN KEY (etudiant_id) REFERENCES etudiants(id) ON DELETE CASCADE,
    FOREIGN KEY (module_id) REFERENCES modules(id) ON DELETE CASCADE,
    UNIQUE(etudiant_id, module_id)
);

-- ============================================
-- TABLE: demandes_certificats
-- ============================================
CREATE TABLE demandes_certificats (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    etudiant_id INTEGER NOT NULL,
    type_certificat TEXT NOT NULL CHECK(type_certificat IN (
        'CERTIFICAT_SCOLARITE',
        'ATTESTATION_REUSSITE',
        'RELEVE_NOTES',
        'ATTESTATION_INSCRIPTION'
    )),
    motif TEXT,
    statut TEXT NOT NULL DEFAULT 'EN_ATTENTE' CHECK(statut IN ('EN_ATTENTE', 'VALIDE', 'REJETE')),
    date_demande TEXT DEFAULT CURRENT_TIMESTAMP,
    date_traitement TEXT,
    agent_id INTEGER,
    commentaire TEXT,
    fichier_pdf_url TEXT,
    FOREIGN KEY (etudiant_id) REFERENCES etudiants(id) ON DELETE CASCADE,
    FOREIGN KEY (agent_id) REFERENCES admins(id) ON DELETE SET NULL
);

-- ============================================
-- TABLE: demandes_diplomes
-- ============================================
CREATE TABLE demandes_diplomes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    etudiant_id INTEGER NOT NULL,
    date_demande TEXT DEFAULT CURRENT_TIMESTAMP,
    statut TEXT NOT NULL DEFAULT 'EN_ATTENTE' CHECK(statut IN ('EN_ATTENTE', 'VALIDE', 'REJETE', 'PRET', 'DELIVRE')),
    date_traitement TEXT,
    adresse_livraison TEXT,
    telephone TEXT,
    commentaire TEXT,
    FOREIGN KEY (etudiant_id) REFERENCES etudiants(id) ON DELETE CASCADE
);

-- ============================================
-- TABLE: certifications_professionnelles
-- ============================================
CREATE TABLE certifications_professionnelles (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nom TEXT NOT NULL,
    description TEXT,
    organisme TEXT NOT NULL,
    type TEXT CHECK(type IN ('ACADEMIQUE', 'PROFESSIONNELLE')),
    date_debut TEXT NOT NULL,
    date_fin TEXT NOT NULL,
    places_max INTEGER DEFAULT 30,
    lien_cours TEXT,
    prix REAL DEFAULT 0,
    manager_id INTEGER,
    statut TEXT DEFAULT 'OUVERT' CHECK(statut IN ('OUVERT', 'FERME', 'TERMINE')),
    FOREIGN KEY (manager_id) REFERENCES admins(id) ON DELETE SET NULL
);

-- ============================================
-- TABLE: cours (Cours associés aux certifications)
-- ============================================
CREATE TABLE cours (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    certification_id INTEGER NOT NULL,
    titre TEXT NOT NULL,
    description TEXT,
    duree_heures INTEGER,
    ordre INTEGER,
    lien_ressource TEXT,
    FOREIGN KEY (certification_id) REFERENCES certifications_professionnelles(id) ON DELETE CASCADE
);

-- ============================================
-- TABLE: participants (Inscriptions aux certifications)
-- ============================================
CREATE TABLE participants (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    certification_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    nom TEXT NOT NULL,
    prenom TEXT NOT NULL,
    email TEXT NOT NULL,
    progression REAL DEFAULT 0 CHECK(progression BETWEEN 0 AND 100),
    score_final REAL CHECK(score_final BETWEEN 0 AND 100),
    statut TEXT DEFAULT 'INSCRIT' CHECK(statut IN ('INSCRIT', 'EN_COURS', 'REUSSI', 'ECHOUE')),
    date_inscription TEXT DEFAULT CURRENT_TIMESTAMP,
    date_completion TEXT,
    certificat_url TEXT,
    FOREIGN KEY (certification_id) REFERENCES certifications_professionnelles(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE(certification_id, user_id)
);

-- ============================================
-- TABLE: notifications
-- ============================================
CREATE TABLE notifications (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    titre TEXT NOT NULL,
    message TEXT NOT NULL,
    type TEXT CHECK(type IN ('INFO', 'SUCCES', 'ALERTE', 'URGENT')),
    lu INTEGER DEFAULT 0 CHECK(lu IN (0, 1)),
    date_creation TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ============================================
-- TABLE: sessions (Gestion des sessions utilisateurs)
-- ============================================
CREATE TABLE sessions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    token TEXT UNIQUE NOT NULL,
    date_connexion TEXT DEFAULT CURRENT_TIMESTAMP,
    date_expiration TEXT,
    device_info TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ============================================
-- INDEX pour améliorer les performances
-- ============================================
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_etudiants_cne ON etudiants(cne);
CREATE INDEX idx_notes_etudiant ON notes(etudiant_id);
CREATE INDEX idx_notes_module ON notes(module_id);
CREATE INDEX idx_demandes_etudiant ON demandes_certificats(etudiant_id);
CREATE INDEX idx_demandes_statut ON demandes_certificats(statut);
CREATE INDEX idx_participants_certification ON participants(certification_id);
CREATE INDEX idx_notifications_user ON notifications(user_id);

-- ============================================
-- VUES (Views) pour faciliter les requêtes
-- ============================================

-- Vue: Liste complète des professeurs avec leurs infos de connexion
CREATE VIEW vue_professeurs AS
SELECT 
    p.id,
    p.nom,
    p.prenom,
    p.departement,
    p.telephone,
    p.photo_url,
    u.email,
    u.created_at
FROM professeurs p
JOIN users u ON p.user_id = u.id;

-- Vue: Liste complète des étudiants avec leurs infos
CREATE VIEW vue_etudiants AS
SELECT 
    e.id,
    e.cne,
    e.nom,
    e.prenom,
    e.date_naissance,
    e.filiere,
    e.annee_etude,
    e.photo_url,
    u.email
FROM etudiants e
JOIN users u ON e.user_id = u.id;

-- Vue: Notes complètes avec informations étudiant et module
CREATE VIEW vue_notes_completes AS
SELECT 
    n.id,
    e.cne,
    e.nom AS nom_etudiant,
    e.prenom AS prenom_etudiant,
    m.code_module,
    m.nom_module,
    m.semestre,
    n.note_examen,
    n.note_td,
    n.note_tp,
    n.note_finale,
    n.statut,
    n.date_examen,
    n.date_saisie
FROM notes n
JOIN etudiants e ON n.etudiant_id = e.id
JOIN modules m ON n.module_id = m.id;

-- Vue: Demandes de certificats avec détails
CREATE VIEW vue_demandes_completes AS
SELECT 
    d.id,
    e.cne,
    e.nom,
    e.prenom,
    e.filiere,
    d.type_certificat,
    d.motif,
    d.statut,
    d.date_demande,
    d.date_traitement,
    a.nom AS agent_nom,
    a.prenom AS agent_prenom,
    d.fichier_pdf_url
FROM demandes_certificats d
JOIN etudiants e ON d.etudiant_id = e.id
LEFT JOIN admins a ON d.agent_id = a.id;

-- Vue: Statistiques par étudiant
CREATE VIEW vue_stats_etudiants AS
SELECT 
    e.id,
    e.cne,
    e.nom,
    e.prenom,
    COUNT(n.id) AS nombre_modules,
    AVG(n.note_finale) AS moyenne_generale,
    SUM(CASE WHEN n.statut = 'VALIDE' THEN 1 ELSE 0 END) AS modules_valides,
    SUM(CASE WHEN n.statut = 'NON_VALIDE' THEN 1 ELSE 0 END) AS modules_non_valides
FROM etudiants e
LEFT JOIN notes n ON e.id = n.etudiant_id
GROUP BY e.id;

-- ============================================
-- TRIGGERS pour automatiser certaines tâches
-- ============================================

-- Trigger: Calculer automatiquement la note finale
CREATE TRIGGER calculate_note_finale
AFTER INSERT ON notes
FOR EACH ROW
WHEN NEW.note_finale IS NULL
BEGIN
    UPDATE notes
    SET note_finale = (
        COALESCE(NEW.note_examen, 0) * 0.4 + 
        COALESCE(NEW.note_td, 0) * 0.3 + 
        COALESCE(NEW.note_tp, 0) * 0.3
    )
    WHERE id = NEW.id;
END;

-- Trigger: Définir automatiquement le statut selon la note finale
CREATE TRIGGER set_statut_validation
AFTER UPDATE OF note_finale ON notes
FOR EACH ROW
BEGIN
    UPDATE notes
    SET statut = CASE 
        WHEN NEW.note_finale >= 10 THEN 'VALIDE'
        ELSE 'NON_VALIDE'
    END
    WHERE id = NEW.id AND statut IS NULL;
END;

-- Trigger: Créer une notification lors d'une nouvelle note
CREATE TRIGGER notif_nouvelle_note
AFTER INSERT ON notes
FOR EACH ROW
BEGIN
    INSERT INTO notifications (user_id, titre, message, type)
    SELECT 
        e.user_id,
        'Nouvelle note disponible',
        'Votre note pour le module ' || m.nom_module || ' est disponible',
        'INFO'
    FROM etudiants e
    JOIN modules m ON m.id = NEW.module_id
    WHERE e.id = NEW.etudiant_id;
END;



-- ============================================
-- DONNÉES DE TEST
-- ============================================

-- Insertion utilisateurs de test
INSERT INTO users (email, password, role) VALUES
('ahmed.alami@ensa.ma', 'prof123', 'PROFESSEUR'),
('fatima.benali@ensa.ma', 'prof123', 'PROFESSEUR'),
('sara.benani@etu.ensa.ma', 'etu123', 'ETUDIANT'),
('karim.mansouri@etu.ensa.ma', 'etu123', 'ETUDIANT'),
('amina.amrani@ensa.ma', 'admin123', 'AGENT'),
('omar.idrissi@ensa.ma', 'manager123', 'MANAGER');

-- Insertion professeurs
INSERT INTO professeurs (user_id, nom, prenom, departement) VALUES
(1, 'ALAMI', 'Ahmed', 'Informatique'),
(2, 'BENALI', 'Fatima', 'Mathématiques');

-- Insertion étudiants
INSERT INTO etudiants (user_id, cne, nom, prenom, filiere, annee_etude) VALUES
(3, 'R123456789', 'BENANI', 'Sara', 'Génie Informatique', '3ème année'),
(4, 'R987654321', 'MANSOURI', 'Karim', 'Génie Informatique', '3ème année');

/*salma*/
-- Insertion nouvel étudiant de test
INSERT INTO users (email, password, role) VALUES
('salma.dh@ensa.ma', 'etu123', 'ETUDIANT');

INSERT INTO etudiants (user_id, cne, nom, prenom, filiere, annee_etude) VALUES
(7, 'S123456', 'DH', 'Salma', 'Génie Informatique', '4ème année');
/*salma*/

-- Insertion admins
INSERT INTO admins (user_id, nom, prenom, fonction) VALUES
(5, 'AMRANI', 'Amina', 'AGENT_SCOLARITE'),
(6, 'IDRISSI', 'Omar', 'MANAGER');

-- Insertion modules
INSERT INTO modules (code_module, nom_module, semestre, coefficient, professeur_id, annee_universitaire) VALUES
('INF301', 'Programmation Orientée Objet', 5, 2.0, 1, '2024-2025'),
('INF302', 'Base de Données', 5, 2.0, 1, '2024-2025'),
('MAT301', 'Analyse Numérique', 5, 1.5, 2, '2024-2025'),
('INF303', 'Réseaux Informatiques', 5, 2.0, 1, '2024-2025');

-- Insertion inscriptions
INSERT INTO inscriptions (etudiant_id, module_id, annee_universitaire) VALUES
(1, 1, '2024-2025'),
(1, 2, '2024-2025'),
(1, 3, '2024-2025'),
(1, 4, '2024-2025'),
(2, 1, '2024-2025'),
(2, 2, '2024-2025'),
(2, 3, '2024-2025'),
(2, 4, '2024-2025');

-- Insertion notes
INSERT INTO notes (etudiant_id, module_id, note_examen, note_td, note_tp) VALUES
(1, 1, 15.5, 16.0, 17.0),
(1, 2, 14.0, 13.5, 15.0),
(1, 3, 12.0, 11.5, 13.0),
(2, 1, 13.0, 14.0, 12.5),
(2, 2, 16.0, 15.5, 17.0);

-- Insertion demandes de certificats
INSERT INTO demandes_certificats (etudiant_id, type_certificat, motif) VALUES
(1, 'CERTIFICAT_SCOLARITE', 'Demande de bourse'),
(2, 'RELEVE_NOTES', 'Candidature Master');

-- Insertion certifications professionnelles
INSERT INTO certifications_professionnelles (nom, organisme, type, date_debut, date_fin, manager_id) VALUES
('AWS Cloud Practitioner', 'Amazon Web Services', 'PROFESSIONNELLE', '2025-02-01', '2025-06-30', 2),
('Google Data Analytics', 'Google', 'PROFESSIONNELLE', '2025-03-01', '2025-07-31', 2);

-- ============================================
-- REQUÊTES UTILES POUR L'APPLICATION
-- ============================================

-- Récupérer tous les modules d'un professeur
-- SELECT * FROM modules WHERE professeur_id = ?;

-- Récupérer toutes les notes d'un étudiant
-- SELECT * FROM vue_notes_completes WHERE cne = ?;

-- Récupérer la moyenne d'un étudiant par semestre
-- SELECT semestre, AVG(note_finale) as moyenne 
-- FROM notes n 
-- JOIN modules m ON n.module_id = m.id 
-- WHERE n.etudiant_id = ? 
-- GROUP BY m.semestre;

-- Récupérer les demandes en attente pour un agent
-- SELECT * FROM vue_demandes_completes WHERE statut = 'EN_ATTENTE';

-- Récupérer les participants d'une certification
-- SELECT * FROM participants WHERE certification_id = ?;

-- Récupérer les notifications non lues d'un utilisateur
-- SELECT * FROM notifications WHERE user_id = ? AND lu = 0 ORDER BY date_creation DESC;