# Documentation d'Intégration Base de Données (SQLite)

Ce document explique comment le projet Android est lié à la base de données SQLite via le fichier `db.sql`.

## 1. Principe de Fonctionnement

L'application utilise une approche "Script-First" pour la création de la base de données.
- Le fichier `app/src/main/assets/db.sql` contient la définition complète de la base (Tables, Vues, Triggers, Données de test).
- La classe `DatabaseHelper` lit et exécute ce script lors de la première installation.

## 2. La classe `DatabaseHelper`

Située dans `com.example.ensatecertnotes.db.DatabaseHelper`, cette classe gère le cycle de vie de la base de données.

### Méthodes Clés :
- `getInstance(Context context)` : Retourne l'instance unique (Singleton) de la connexion.
- `onCreate(SQLiteDatabase db)` : Appelé si la base n'existe pas. Il lit `db.sql` ligne par ligne et exécute les commandes.
- `onUpgrade(...)` : Appelé si vous changez le numéro de version (`DATABASE_VERSION`). Actuellement, il supprime tout et recrée la base (idéal pour le développement).

## 3. Comment l'utiliser dans votre code

Pour accéder à la base de données depuis une Activity ou un DAO (Data Access Object), utilisez :

```java
// 1. Obtenir l'instance
DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);

// 2. Ouvrir la base en écriture (ou lecture)
SQLiteDatabase db = dbHelper.getWritableDatabase();

// 3. Exécuter une requête brute (déconseillé, utilisez les DAO)
Cursor cursor = db.rawQuery("SELECT * FROM users", null);
```

## 4. Modification du Schéma (Pendant le Développement)

Si vous modifiez le fichier `idee/db.sql` ou `assets/db.sql` :

1.  **Copiez** le nouveau fichier dans `app/src/main/assets/db.sql` (le script `setup_structure.ps1` le faisait, mais manuellement c'est un copier-coller).
2.  **Désinstallez** l'application de l'émulateur/téléphone OU effacez les données de l'application (Stockage -> Effacer les données).
    *   *Alternative* : Incrémentez la variable `DATABASE_VERSION` dans `DatabaseHelper.java`.
3.  Relancez l'application. La méthode `onUpgrade` ou `onCreate` sera appelée et la nouvelle structure sera appliquée.

## 5. Gestion des Erreurs

Si la base ne se crée pas correctement :
- Vérifiez le **Logcat** avec le tag `DatabaseHelper`.
- Assurez-vous que vos requêtes SQL dans `db.sql` se terminent bien par un point-virgule `;`.
- Le parseur intégré gère les `TRIGGER` basiques mais peut échouer sur des structures SQL très complexes. Respectez le format standard.
