// Binh Minh TRAN - Marouane NOUARA
// ING1 - APP - BDML 
package main;

import model.Prestation.PrestationTresSale;
import model.Prestation.PrestationSale;
import model.Prestation.PrestationExpress;
import model.Prestation.Prestation;
import model.Client;
import model.RendezVous;
import service.Etablissement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

public class StationLavageMain {

    public static void main(String[] args) {


    System.out.println("==========================================");
    System.out.println("TEST PARTIE 1 - STATION DE LAVAGE");
    System.out.println("==========================================\n");

    // =========================
    // Test de la classe Client
    // =========================
    System.out.println("--- Test classe Client ---");

    // Création d'un client sans adresse email
    Client c1 = new Client(1, "Dupont", "0612345678");

	//Création d'un client avec une adresse email
	Client c2 = new Client(2, "Martin", "0698765432", "martin@mail.com");

    // Affichage des informations des clients
    System.out.println(c1);
    System.out.println(c2);
    
    // Test de la méthode placerApres()
    System.out.println("\nTest placerApres():");
    System.out.println("c1.placerApres(c2) = " + c1.placerApres(c2));
    System.out.println("c2.placerApres(c1) = " + c2.placerApres(c1));

    // =========================
    // Test des différentes prestations
    // =========================
    System.out.println("\n--- Test classes Prestation ---");

    // Prestation express avec nettoyage intérieur
    Prestation p1 = new PrestationExpress("A", true);
    Prestation p1b = new PrestationExpress("A", false); // Sans nettoyage intérieur
    Prestation p1c = new PrestationExpress("B", true); // Catégorie B

    // Prestation pour véhicule sale
    Prestation p2 = new PrestationSale("B");
    Prestation p2b = new PrestationSale("C"); // Catégorie C

    // Prestation pour véhicule très sale avec différents types de salissure
    Prestation p3 = new PrestationTresSale("C", 2); // Boue
    Prestation p3b = new PrestationTresSale("A", 1); // Nourriture
    Prestation p3c = new PrestationTresSale("B", 4); // Graisse

    // Affichage du prix des différentes prestations
    System.out.println("\nPrestations Express:");
    System.out.println("  " + p1);
    System.out.println("  " + p1b);
    System.out.println("  " + p1c);

	System.out.println("\nPrestations Sale:");
	System.out.println("  " + p2);
	System.out.println("  " + p2b);
	
	System.out.println("\nPrestations Tres Sale:");
	System.out.println("  " + p3);
	System.out.println("  " + p3b);
	System.out.println("  " + p3c);
	
	// =========================
	// Test de la classe RendezVous
	// =========================
	System.out.println("\n--- Test classe RendezVous ---");
	
	// Création de plusieurs rendez-vous
	RendezVous rv1 = new RendezVous(c1, p1);
	RendezVous rv2 = new RendezVous(c2, p2);
	RendezVous rv3 = new RendezVous(c1, p3);
	
    // Affichage des informations des rendez-vous
    System.out.println("Rendez-vous 1: " + rv1);
    System.out.println("Prix rv1: " + rv1.getPrix() + " €");
    System.out.println("Rendez-vous 2: " + rv2);
    System.out.println("Prix rv2: " + rv2.getPrix() + " €");
    System.out.println("Rendez-vous 3: " + rv3);
    System.out.println("Prix rv3: " + rv3.getPrix() + " €");
    
    // =========================
    // Test de l'etablissement
    //==========================
    System.out.println("\n--- Test classe Etablissement ---");
    
    // Création d'un établissement vide
    Etablissement E1 = new Etablissement("Station Lavage Pro");
    System.out.println("Etablissement créé: " + E1.getNom());
    
    // Test ajouter clients avec numérotation automatique
    System.out.println("\n--- Test ajouter clients (numérotation automatique) ---");
    Client nouveau1 = E1.ajouter("Bernard", "0711111111");
    Client nouveau2 = E1.ajouter("Alice", "0722222222", "alice@mail.com");
    Client nouveau3 = E1.ajouter("Zorro", "0733333333");
    
    System.out.println("Client ajouté 1: " + nouveau1);
    System.out.println("Client ajouté 2: " + nouveau2);
    System.out.println("Client ajouté 3: " + nouveau3);
    System.out.println("\nListe des clients (triée par ordre lexicographique):");
    System.out.println(E1);
    
    // Test rechercher client
    System.out.println("--- Test rechercher client ---");
    Client trouve = E1.rechercher("Alice", "0722222222");
    System.out.println("Recherche Alice: " + (trouve != null ? trouve : "Non trouvé"));
    
    Client nonTrouve = E1.rechercher("Inexistant", "0000000000");
    System.out.println("Recherche Inexistant: " + (nonTrouve != null ? nonTrouve : "Non trouvé"));
    
    // Test ajouter rendez-vous
    System.out.println("\n--- Test ajouter rendez-vous ---");
    LocalDate demain = LocalDate.now().plusDays(1);
    LocalTime heure1 = LocalTime.of(10, 30);
    LocalTime heure2 = LocalTime.of(14, 0);
    LocalTime heure3 = LocalTime.of(16, 30);
    
    RendezVous rdv1 = E1.ajouter(nouveau1, demain, heure1, "A", true);
    RendezVous rdv2 = E1.ajouter(nouveau2, demain, heure2, "B");
    RendezVous rdv3 = E1.ajouter(nouveau3, demain, heure3, "C", 3);
    
    System.out.println("Rendez-vous Express ajouté: " + (rdv1 != null ? "Oui - " + rdv1.getPrix() + " €" : "Non"));
    System.out.println("Rendez-vous Sale ajouté: " + (rdv2 != null ? "Oui - " + rdv2.getPrix() + " €" : "Non"));
    System.out.println("Rendez-vous Tres Sale ajouté: " + (rdv3 != null ? "Oui - " + rdv3.getPrix() + " €" : "Non"));
    
    // Test rechercher créneaux (commenté car nécessite interaction utilisateur)
    System.out.println("\n--- Test rechercher créneaux ---");
    LocalDateTime creneau1 = E1.rechercher(demain);
    LocalDateTime creneau2 = E1.rechercher(heure1);

    System.out.println(creneau1);
    System.out.println(creneau2);
        
    
    // Test avec établissement initialisé avec clients
    System.out.println("\n--- Test établissement avec clients initiaux ---");
    Client[] liste_clients = {c1, c2};
    Etablissement E2 = new Etablissement("Station Test", liste_clients);
    System.out.println(E2);

    System.out.println("\n==========================================");
    System.out.println("FIN DES TESTS PARTIE 1");
    System.out.println("==========================================");
    
    // =========================
    // TEST PARTIE 2 - Gestion des fichiers
    // =========================
    System.out.println("==========================================");
    System.out.println("TEST PARTIE 2 - GESTION DES FICHIERS");
    System.out.println("==========================================\n");
   
    
    //TEST METHODE PLANIFIER()
    System.out.println("\n--- TEST METHODE PLANIFIER() ---");
    E1.planifier();


    //TEST METHODE AFFICHER PLANNING 
    System.out.println("\n--- TEST METHODE AFFICHER PLANNING ---");
    E1.afficherPlanning();


    
    // TEST METHODE AFFICHER CLIENT
    System.out.println("\n--- TEST METHODE AFFICHER CLIENT ---");
    E1.afficherClient();


    
    //TEST AFFICHER RDV PAR NUMÉRO CLIENT
    System.out.println("\n--- TEST AFFICHER RDV PAR NUMÉRO CLIENT ---");
    E1.afficherRendezVousParNumeroClient();
    
    // Test versFichier() pour Client
    System.out.println("--- Test versFichier() - Client ---");
    System.out.println("c1.versFichier() = " + c1.versFichier());
    System.out.println("c2.versFichier() = " + c2.versFichier());
    
    // Test versFichier() pour Prestations
    System.out.println("\n--- Test versFichier() - Prestations ---");
    System.out.println("p1 (Express).versFichier() = " + p1.versFichier());
    System.out.println("p2 (Sale).versFichier() = " + p2.versFichier());
    System.out.println("p3 (TresSale).versFichier() = " + p3.versFichier());
    
    // Test versFichier() pour RendezVous
    System.out.println("\n--- Test versFichier() - RendezVous ---");
    LocalDateTime dateHeure1 = LocalDateTime.of(demain, heure1);
    LocalDateTime dateHeure2 = LocalDateTime.of(demain, heure2);
    LocalDateTime dateHeure3 = LocalDateTime.of(demain, heure3);
    
    if (rdv1 != null) {
        System.out.println("rdv1.versFichier():");
        System.out.println(rdv1.versFichier(dateHeure1));
    }
    if (rdv2 != null) {
        System.out.println("\nrdv2.versFichier():");
        System.out.println(rdv2.versFichier(dateHeure2));
    }
    if (rdv3 != null) {
        System.out.println("\nrdv3.versFichier():");
        System.out.println(rdv3.versFichier(dateHeure3));
    }
    
    // Test versFichierClients() et depuisFichierClients()
    System.out.println("\n--- Test gestion fichiers clients ---");
    String nomFichierClients = "clients.txt";
    
    try {
        // Sauvegarder les clients
        System.out.println("Sauvegarde des clients dans " + nomFichierClients + "...");
        E1.versFichierClients(nomFichierClients);
        System.out.println("✓ Clients sauvegardés avec succès");
        
        // Créer un nouvel établissement et charger les clients
        System.out.println("\nCréation d'un nouvel établissement et chargement des clients...");
        Etablissement E3 = new Etablissement("Station Chargée");
        
        // EXPLICATION: Pourquoi E3 est modifié après cet appel ?
        // - depuisFichierClients() est une méthode d'instance (non-statique)
        // - Quand on appelle E3.depuisFichierClients(), la méthode travaille sur l'objet E3
        E3.depuisFichierClients(nomFichierClients);
        System.out.println("✓ Clients chargés avec succès");
        System.out.println("\nClients chargés depuis le fichier:");
        System.out.println(E3);
        
        // Vérifier que la numérotation continue correctement
        System.out.println("Test numérotation continue après chargement:");
        Client nouveau4 = E3.ajouter("Test", "0744444444");
        System.out.println("Nouveau client ajouté: " + nouveau4);
        System.out.println("(Le numéro devrait continuer après le dernier client chargé)");
        
    } catch (java.io.IOException e) {
        System.err.println("Erreur lors de la gestion des fichiers: " + e.getMessage());
        e.printStackTrace();
    }
    
    System.out.println("\n==========================================");
    System.out.println("FIN DES TESTS PARTIE 2");
    System.out.println("==========================================");
    }
}
