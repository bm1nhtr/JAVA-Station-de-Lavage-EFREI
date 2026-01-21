# Station de Lavage

## Projet Java OOP

**Auteurs:**  Binh Minh TRAN et Marouane NOUARA

**[Lien vers la description du projet](/Projet%20-%20BDML%201-2%20-%20Enoncé.pdf)**

## Structure du Projet

```
ProjetFinal/
├── .gitattributes            # Configuration Git
├── .gitignore                # Fichiers ignorés par Git
├── README.md                 # Documentation principale
├── SETUP.md                  # Guide de configuration (pour développeurs)
├── Projet - BDML 1-2 - Enoncé.pdf  # Énoncé du projet
└── Scripts/                  # Projet NetBeans
    ├── src/                  # Dossier source principal
    │   ├── main/             # package: main
    │   │   └── StationLavageMain.java
    │   ├── model/            # package: model
    │   │   ├── Client.java
    │   │   ├── RendezVous.java
    │   │   └── Prestation/   # package: model.Prestation
    │   │       ├── Prestation.java
    │   │       ├── PrestationExpress.java
    │   │       ├── PrestationSale.java
    │   │       └── PrestationTresSale.java
    │   └── service/          # package: service
    │       └── Etablissement.java
    ├── nbproject/            # Configuration NetBeans
    ├── build.xml             # Script de build Ant
    ├── manifest.mf           # Manifest pour le JAR
    └── clients.txt           # Fichier de données clients (créé lors de l'exécution)
```

## Démarrage Rapide

1. **Cloner le repository**
   ```bash
   git clone <url-du-repo>
   ```

2. **Ouvrir le projet dans NetBeans**
   - File → Open Project
   - **Sélectionner le dossier `Scripts/`** (pas la racine du repository !)
   - Cliquer "Open Project"

3. **Compiler et exécuter**
   - Right-click sur le projet → Clean and Build
   - Right-click sur `StationLavageMain.java` → Run File
   - Ou exécuter la classe principale : `main.StationLavageMain`

---

> **Note pour les développeurs :** Voir [SETUP.md](SETUP.md) pour les instructions détaillées de configuration et le workflow Git.
