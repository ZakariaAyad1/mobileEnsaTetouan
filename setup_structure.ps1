$baseJava = "app/src/main/java/com/example/ensatecertnotes"
$baseRes = "app/src/main/res/layout"
$baseAssets = "app/src/main/assets"

# Create Directories
$dirs = @(
    "$baseJava/api",
    "$baseJava/db",
    "$baseJava/db/dao",
    "$baseJava/model",
    "$baseJava/ui",
    "$baseJava/ui/auth",
    "$baseJava/ui/prof",
    "$baseJava/ui/student",
    "$baseJava/ui/admin",
    "$baseJava/ui/admin/agent",
    "$baseJava/ui/admin/manager",
    "$baseJava/ui/adapters",
    "$baseJava/utils",
    "$baseRes",
    "$baseAssets"
)

foreach ($dir in $dirs) {
    if (-not (Test-Path $dir)) {
        New-Item -ItemType Directory -Force -Path $dir | Out-Null
        Write-Host "Created directory: $dir"
    }
}

# Define Java Files
$javaFiles = @(
    "$baseJava/db/DatabaseHelper.java",
    "$baseJava/db/dao/UserDao.java",
    "$baseJava/db/dao/ProfesseurDao.java",
    "$baseJava/db/dao/EtudiantDao.java",
    "$baseJava/db/dao/NoteDao.java",
    "$baseJava/db/dao/ModuleDao.java",
    "$baseJava/db/dao/CertificatDao.java",
    "$baseJava/model/User.java",
    "$baseJava/model/Professeur.java",
    "$baseJava/model/Etudiant.java",
    "$baseJava/model/Admin.java",
    "$baseJava/model/Module.java",
    "$baseJava/model/Note.java",
    "$baseJava/model/Certificat.java",
    "$baseJava/model/CertificationPro.java",
    "$baseJava/model/Notification.java",
    "$baseJava/ui/auth/LoginActivity.java",
    "$baseJava/ui/auth/RoleSelectionActivity.java",
    "$baseJava/ui/prof/ProfDashboardActivity.java",
    "$baseJava/ui/prof/ProfProfileActivity.java",
    "$baseJava/ui/prof/ListeEtudiantsActivity.java",
    "$baseJava/ui/prof/SaisieNoteActivity.java",
    "$baseJava/ui/prof/HistoriqueNotesActivity.java",
    "$baseJava/ui/student/StudentDashboardActivity.java",
    "$baseJava/ui/student/StudentProfileActivity.java",
    "$baseJava/ui/student/MesNotesActivity.java",
    "$baseJava/ui/student/DetailModuleActivity.java",
    "$baseJava/ui/student/CertificatRequestActivity.java",
    "$baseJava/ui/student/StatsGraphActivity.java",
    "$baseJava/ui/admin/AdminDashboardActivity.java",
    "$baseJava/ui/admin/agent/GestionDemandesActivity.java",
    "$baseJava/ui/admin/agent/ArchiveCertificatsActivity.java",
    "$baseJava/ui/admin/manager/GestionCertificationsActivity.java",
    "$baseJava/ui/admin/manager/CreateCertificationActivity.java",
    "$baseJava/ui/admin/manager/SuiviParticipantsActivity.java",
    "$baseJava/ui/adapters/ModuleAdapter.java",
    "$baseJava/ui/adapters/NoteAdapter.java",
    "$baseJava/ui/adapters/EtudiantAdapter.java",
    "$baseJava/ui/adapters/CertificatAdapter.java",
    "$baseJava/utils/SessionManager.java",
    "$baseJava/utils/PDFGenerator.java",
    "$baseJava/utils/Constants.java",
    "$baseJava/utils/DateUtils.java"
)

# Create Java Files with package declaration
foreach ($file in $javaFiles) {
    if (-not (Test-Path $file)) {
        # Extract package name from path
        $dirPath = Split-Path $file -Parent
        $relativePath = $dirPath.Substring($baseJava.Length + 1).Replace("/", ".").Replace("\", ".")
        $packageName = "package com.example.ensatecertnotes" + (if ($relativePath) { ".$relativePath" } else { "" }) + ";"
        
        Set-Content -Path $file -Value "$packageName`n`npublic class $([System.IO.Path]::GetFileNameWithoutExtension($file)) {`n    // Todo: Implement`n}"
        Write-Host "Created file: $file"
    }
}

# Define XML Files
$xmlFiles = @(
    "$baseRes/activity_main.xml",
    "$baseRes/activity_login.xml",
    "$baseRes/activity_prof_dashboard.xml",
    "$baseRes/item_module_prof.xml",
    "$baseRes/activity_saisie_note.xml",
    "$baseRes/activity_liste_etudiants.xml",
    "$baseRes/activity_student_dashboard.xml",
    "$baseRes/activity_mes_notes.xml",
    "$baseRes/activity_demande_certificat.xml",
    "$baseRes/item_note_student.xml",
    "$baseRes/activity_admin_dashboard.xml",
    "$baseRes/activity_gestion_demandes.xml",
    "$baseRes/item_demande_certificat.xml"
)

# Create XML Files
foreach ($file in $xmlFiles) {
    if (-not (Test-Path $file)) {
        $content = "<?xml version=""1.0"" encoding=""utf-8""?>`n<LinearLayout xmlns:android=""http://schemas.android.com/apk/res/android""`n    android:layout_width=""match_parent""`n    android:layout_height=""match_parent""`n    android:orientation=""vertical"">`n`n</LinearLayout>"
        Set-Content -Path $file -Value $content
        Write-Host "Created file: $file"
    }
}

# Copy db.sql if it exists
if (Test-Path "idee/db.sql") {
    Copy-Item "idee/db.sql" -Destination "$baseAssets/db.sql"
    Write-Host "Copied db.sql to assets"
}
