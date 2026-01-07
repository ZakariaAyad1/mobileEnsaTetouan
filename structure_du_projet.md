# Structure du Projet Mobile (Android/Java/SQLite)

Ce document détaille la structure complète des fichiers et dossiers pour le projet **CertifNotes**, basée sur les exigences du fichier `idea.txt` et le schéma `db.sql`.

## 1. Organisation des Packages Java
Le package racine sera : `com.example.ensatecertnotes`

```text
app/src/main/java/com/example/ensatecertnotes/
├── api/                        # (Optionnel) Pour futures connexions réseau
├── db/                         # Gestion de la Base de Données SQLite
│   ├── DatabaseHelper.java     # Création de la DB, gestion de version, instance Singleton
│   ├── dao/                    # Data Access Objects (Requêtes SQL par table)
│   │   ├── UserDao.java
│   │   ├── ProfesseurDao.java
│   │   ├── EtudiantDao.java
│   │   ├── NoteDao.java
│   │   ├── ModuleDao.java
│   │   └── CertificatDao.java
├── model/                      # POJO (Plain Old Java Objects) mappant les tables SQL
│   ├── User.java
│   ├── Professeur.java
│   ├── Etudiant.java
│   ├── Admin.java              # Inclut Agent et Manager
│   ├── Module.java
│   ├── Note.java
│   ├── Certificat.java         # "demandes_certificats"
│   ├── CertificationPro.java
│   └── Notification.java
├── ui/                         # Interface Utilisateur (Activités et Fragments)
│   ├── auth/                   # Authentification et Accueil
│   │   ├── LoginActivity.java
│   │   └── RoleSelectionActivity.java
│   ├── prof/                   # Module Professeur
│   │   ├── ProfDashboardActivity.java
│   │   ├── ProfProfileActivity.java
│   │   ├── ListeEtudiantsActivity.java
│   │   ├── SaisieNoteActivity.java
│   │   └── HistoriqueNotesActivity.java
│   ├── student/                # Module Étudiant
│   │   ├── StudentDashboardActivity.java
│   │   ├── StudentProfileActivity.java
│   │   ├── MesNotesActivity.java
│   │   ├── DetailModuleActivity.java
│   │   ├── CertificatRequestActivity.java
│   │   └── StatsGraphActivity.java
│   ├── admin/                  # Module Administration
│   │   ├── AdminDashboardActivity.java  # Dashboard général (redirige selon rôle Agent/Manager)
│   │   ├── agent/              # Sous-module pour Agent de scolarité
│   │   │   ├── GestionDemandesActivity.java
│   │   │   └── ArchiveCertificatsActivity.java
│   │   └── manager/            # Sous-module pour Manager
│   │   │   ├── GestionCertificationsActivity.java
│   │   │   ├── CreateCertificationActivity.java
│   │   │   └── SuiviParticipantsActivity.java
│   └── adapters/               # Adaptateurs pour les RecyclerViews (Listes)
│       ├── ModuleAdapter.java
│       ├── NoteAdapter.java
│       ├── EtudiantAdapter.java
│       └── CertificatAdapter.java
└── utils/                      # Utilitaires divers
    ├── SessionManager.java     # Gestion de la session utilisateur (SharedPreferences)
    ├── PDFGenerator.java       # Génération des certificats PDF
    ├── Constants.java          # Constantes globales
    └── DateUtils.java          # Formatage des dates
```

## 2. Structure des Ressources (XML)

```text
app/src/main/res/
├── layout/                     # Fichiers de mise en page (écrans)
│   ├── activity_main.xml       # Rôle Selection
│   ├── activity_login.xml      # Écran de connexion générique
│   │
│   │   <!-- Layouts Professeur -->
│   ├── activity_prof_dashboard.xml
│   ├── item_module_prof.xml    # Item liste module
│   ├── activity_saisie_note.xml
│   ├── activity_liste_etudiants.xml
│   │
│   │   <!-- Layouts Étudiant -->
│   ├── activity_student_dashboard.xml
│   ├── activity_mes_notes.xml
│   ├── activity_demande_certificat.xml
│   ├── item_note_student.xml
│   │
│   │   <!-- Layouts Admin -->
│   ├── activity_admin_dashboard.xml
│   ├── activity_gestion_demandes.xml
│   ├── item_demande_certificat.xml
│   │
│   └── ... (autres layouts spécifiques)
│
├── values/
│   ├── strings.xml             # Textes de l'application
│   ├── colors.xml              # Palette de couleurs (Charte graphique)
│   ├── themes.xml              # Thèmes (Clair/Sombre)
│   └── dimens.xml              # Marges et tailles polices
│
└── drawable/                   # Images et Icônes
    ├── ic_logo.xml
    ├── ic_user_prof.xml
    ├── ic_user_student.xml
    ├── bg_button_rounded.xml
    └── ...
├── assets/                     # Fichiers bruts
│   └── db.sql                  # Script SQL initial (à charger dans DatabaseHelper)
```

## 3. Détail des Classes Clés

### A. DatabaseHelper (Singleton)
Cette classe étendra `SQLiteOpenHelper`.
- **onCreate()**: Exécutera le script `db.sql` pour créer les tables.
- **onUpgrade()**: Gérera les mises à jour de schéma.
- **Méthodes statiques**: `getInstance(Context)` pour l'accès global.

### B. Modèles (Model)
Chaque classe Java (ex: `Etudiant.java`) contiendra :
- Attributs privés correspondant aux colonnes de la table.
- Constructeurs (vide et complet).
- Getters et Setters.

### C. Gestion de Session (SessionManager)
Utilisation de `SharedPreferences` pour stocker :
- `isLoggedIn` (boolean)
- `userId` (int)
- `userRole` (String)
- `token` (String, si nécessaire)

## 4. Flux de Navigation (Workflow)

1.  **Lancement** : `RoleSelectionActivity` (Ou écran commun Login).
2.  **Login** : L'utilisateur entre Email/CNE et Password.
3.  **Vérification DB** : `UserDao.checkLogin(email, password)`.
4.  **Redirection** :
    -   Si Role = PROFESSEUR → `ProfDashboardActivity`
    -   Si Role = ETUDIANT → `StudentDashboardActivity`
    -   Si Role = AGENT/MANAGER → `AdminDashboardActivity`

## 5. Bibliothèques Recommandées (build.gradle)

Pour faciliter le développement, nous utiliserons :
-   **MPAndroidChart** : Pour les graphiques (Module Étudiant).
-   **iText** ou **PdfDocument** (Android natif) : Pour la génération PDF.
-   **Material Components** : Pour un design moderne.
