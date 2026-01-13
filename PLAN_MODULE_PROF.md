# Plan d'Implémentation - Module Professeur

Ce plan détaille les étapes pour réaliser les fonctionnalités du module Professeur : Authentification, Gestion des Modules, Liste des Étudiants, et Saisie des Notes.

## 1. Couche de Données (Model & DAO)

Nous devons d'abord préparer les objets Java qui reflètent la base de données et les classes d'accès aux données.

### A. Modèles (POJO)
*   **User** : `id`, `email`, `password`, `role`.
*   **Professeur** : `id`, `userId`, `nom`, `prenom`, `departement`, `telephone`.
*   **Module** : `id`, `code`, `nom`, `semestre`, `profId`.
*   **Note** : `id`, `etudiantId`, `moduleId`, `noteExamen`, `noteTd`, `noteTp`, `noteFinale`, `statut`.
*   **Etudiant** : Informations de base pour l'affichage liste.

### B. DAOs (Data Access Objects)
*   **UserDao** :
    *   `login(email, password)` -> Retourne un objet User ou null.
*   **ProfesseurDao** :
    *   `getProfesseurByUserId(userId)` -> Retourne les infos du prof.
    *   `updateProfil(professeur)` -> Mise à jour infos.
*   **ModuleDao** :
    *   `getModulesByProfesseur(profId)` -> Liste des modules enseignés.
*   **EtudiantDao** :
    *   `getEtudiantsByModule(moduleId)` -> Liste des étudiants inscrits à un module (via table `inscriptions`).
*   **NoteDao** :
    *   `getNote(etudiantId, moduleId)` -> Récupère la note existante.
    *   `saveOrUpdateNote(note)` -> Insère ou met à jour les notes (Examen, TD, TP).

## 2. Gestion de Session
*   **SessionManager** : Utilitaire pour sauvegarder l'utilisateur connecté (`SharedPreferences`).
    *   `createLoginSession(userId, role, ...)`
    *   `getUserDetails()`
    *   `logoutUser()`

## 3. Interface Utilisateur (UI)

### A. Authentification
*   **LoginActivity** :
    *   Champs Email/Password.
    *   Appel `UserDao.login()`.
    *   Si succès -> `SessionManager.createLoginSession` -> Redirection vers `ProfDashboardActivity`.

### B. Dashboard Professeur
*   **ProfDashboardActivity** :
    *   Affichage du nom du prof ("Bonjour, M. X").
    *   `RecyclerView` listant les modules (`ModuleAdapter`).
    *   Clic sur un module -> Ouvre `ListeEtudiantsActivity`.

### C. Liste Étudiants & Saisie
*   **ListeEtudiantsActivity** :
    *   Récupère `moduleId` de l'intent.
    *   Affichage liste étudiants (`EtudiantAdapter`).
    *   Clic sur un étudiant -> Ouvre `SaisieNoteActivity`.
*   **SaisieNoteActivity** :
    *   Formulaire : Note Examen, TP, TD.
    *   Bouton "Valider" -> Appel `NoteDao.saveOrUpdateNote()`.
    *   Affichage "Note Finale" et "Statut" (calculés/récupérés).

### D. Profil
*   **ProfProfileActivity** :
    *   Modification téléphone / photo (si implémenté).
    *   Bouton Déconnexion.

## Ordre d'Exécution
1.  Implémentation des **Modèles** (User, Prof, Module, Note).
2.  Implémentation des **DAOs** (Logique SQL).
3.  Implémentation du **SessionManager**.
4.  Mise en place des **Activités** et **Adapters**.
