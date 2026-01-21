# Guide de Setup - Station de Lavage

## ⚠️ IMPORTANT

**Ouvrir le dossier `Scripts/` en cliquant sur `Open Project` dans NetBeans, PAS le dossier racine du repo !**

> **Note:** NetBeans créera automatiquement le fichier `build-impl.xml` s'il manque. 

> **Important:** NetBeans ne reconnaîtra le projet que s'il contient les dossiers que NetBeans peut identifier (comme `nbproject/`, `src/`, `build.xml`). C'est pourquoi il faut ouvrir le dossier `Scripts/` et non la racine du repo !

## 📁 Structure

```
Scripts/
├── src/                      # Dossier source principal
│   ├── main/                 # package: main
│   │   └── StationLavageMain.java
│   ├── model/                # package: model
│   │   ├── Client.java
│   │   ├── RendezVous.java
│   │   └── Prestation/       # package: model.Prestation
│   │       ├── Prestation.java
│   │       ├── PrestationExpress.java
│   │       ├── PrestationSale.java
│   │       └── PrestationTresSale.java
│   └── service/              # package: service
│       └── Etablissement.java
├── nbproject/                # Configuration NetBeans
├── build.xml                 # Script de build Ant
└── manifest.mf               # Manifest pour le JAR
```

## Setup Rapide

1. **Pull le code :**
   ```bash
   git pull origin <branch>
   ```

2. **Ouvrir dans NetBeans :**
   - File → Open Project
   - **Choisir le dossier `Scripts/`** (pas la racine !)
   - Cliquer "Open Project"
   - NetBeans créera automatiquement les fichiers manquants si nécessaire

3. **Vérifier :**
   - Right-click projet → Properties → Sources
   - Source Package Folders doit pointer vers `src` (relatif au dossier Scripts/)
   - Main class : `main.StationLavageMain`

## ❌ À NE PAS FAIRE

- ❌ Créer un nouveau projet Maven/Gradle
- ❌ Ajouter des packages comme `com.company`
- ❌ Modifier la structure `src/`
- ❌ Commiter les fichiers build (`build/`, `dist/`)
- ❌ **Pousser le code directement depuis NetBeans !** NetBeans ne travaille que dans le dossier `Scripts/`, donc si tu pousses depuis NetBeans, les autres fichiers du repo (README.md, SETUP.md, .gitignore, etc.) seront manquants. Utilise Git depuis le terminal ou depuis la racine du repo !

## ✅ À FAIRE

- ✅ Toujours `git pull` avant de commencer
- ✅ Commiter uniquement le code source dans `src/` (et les fichiers de config si nécessaire)
- ✅ Tester avant de push
- ✅ Utiliser des messages de commit clairs et descriptifs


## Git Workflow

> **⚠️ IMPORTANT:** Ne jamais utiliser la fonction Git intégrée de NetBeans ! Toujours utiliser Git depuis le terminal ou depuis la **racine du repo** (pas depuis le dossier `Scripts/`) pour éviter de perdre les fichiers comme README.md, SETUP.md, .gitignore, etc.

```bash
# Depuis la RACINE du repo (pas depuis Scripts/)
# Avant de travailler
git branch                    # vérifier sur quelle branche on est
git checkout develop          # ou <ta branche>
git pull origin develop       # récupérer les dernières modifications

# Après modifications
git add .
git commit -m "message clair et descriptif"
git push origin develop       # ou <ta branche>
```

## Packages

- `StationLavageMain.java` : `package main;`
- `Client.java`, `RendezVous.java` : `package model;`
- Fichiers dans `Prestation/` : `package model.Prestation;`
- `Etablissement.java` : `package service;`

**Le nom du package doit correspondre à la structure des dossiers !**
