// Binh Minh TRAN - Marouane NOUARA
// ING1 - APP - BDML 
package main;

import model.Prestation.PrestationTresSale;
import model.Prestation.PrestationSale;
import model.Prestation.PrestationExpress;
import model.Prestation.Prestation;
import model.Client;
import model.RendezVous;

public class StationLavageMain {

    public static void main(String[] args) {

        // =========================
        // Test de la classe Client
        // =========================

        // Création d’un client sans adresse email
        Client c1 = new Client(1, "Dupont", "0612345678");

        // Création d’un client avec une adresse email
        Client c2 = new Client(2, "Martin", "0698765432", "martin@mail.com");

        // Affichage des informations des clients
        System.out.println(c1);
        System.out.println(c2);

        // =========================
        // Test des différentes prestations
        // =========================

        // Prestation express avec nettoyage intérieur
        Prestation p1 = new PrestationExpress("A", true);

        // Prestation pour véhicule sale
        Prestation p2 = new PrestationSale("B");

        // Prestation pour véhicule très sale avec salissure de type 2 (boue)
        Prestation p3 = new PrestationTresSale("C", 2);

        // Affichage du prix des différentes prestations
        System.out.println(p1);
        System.out.println(p2);
        System.out.println(p3);

        // =========================
        // Test de la classe RendezVous
        // =========================

        // Création d’un rendez-vous pour le client c1
        // avec une prestation express
        RendezVous rv1 = new RendezVous(c1, p1);

        // Affichage des informations du rendez-vous
        System.out.println(rv1);
    }
}
