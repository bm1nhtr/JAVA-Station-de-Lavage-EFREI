// Binh Minh TRAN - Marouane NOUARA
// ING1 - APP - BDML 

package model;


import java.time.LocalDateTime;
import model.Prestation.Prestation;

/**
 * Représente un rendez-vous dans la station de lavage.
 *
 * Un rendez-vous associe :
 *  - un client
 *  - une prestation choisie
 *  - la date et l'heure du rendez-vous
 *  - le prix calculé automatiquement selon la prestation
 *
 * La classe permet également de convertir un rendez-vous
 * dans un format texte afin d’être sauvegardé dans un fichier.
 */
public class RendezVous {

    // Client concerné par le rendez-vous
    private Client client;

    // Prestation choisie par le client
    private Prestation prestation;

    // Prix total calculé automatiquement (selon la prestation)
    private double prix;

    // Date et heure du rendez-vous
    // Permet la sauvegarde au format YYYY-MM-DDTHH:MM
    private LocalDateTime dateHeure;

    /**
     * Constructeur d’un rendez-vous.
     *
     * @param client       le client concerné
     * @param prestation   la prestation choisie par le client
     * @param dateHeure    la date et l’heure prévues du rendez-vous
     *
     * Le prix est calculé automatiquement via la méthode nettoyage()
     * de la prestation.
     */
    public RendezVous(Client client, Prestation prestation) {
        this.client = client;
        this.prestation = prestation;
        this.prix = prestation.nettoyage();
    }


    // =======================
    //        GETTERS
    // =======================

    public Client getClient() {
        return client;
    }

    public Prestation getPrestation() {
        return prestation;
    }

    public double getPrix() {
        return prix;
    }

    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    // =======================
    //        SETTERS
    // =======================

    /**
     * Modifie le client du rendez-vous.
     * (Peut être utile si une erreur a été commise lors de la saisie.)
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Modifie la prestation du rendez-vous.
     * Le prix est recalculé automatiquement.
     */
    public void setPrestation(Prestation prestation) {
        this.prestation = prestation;
        this.prix = prestation.nettoyage(); // recalcul automatique
    }

    /**
     * Modifie la date et l'heure du rendez-vous.
     */
    public void setDateHeure(LocalDateTime dateHeure) {
        this.dateHeure = dateHeure;
    }

    // =======================
    //     SAUVEGARDE FICHIER
    // =======================

    /**
     * Retourne une représentation textuelle du rendez-vous
     * au format imposé pour la sauvegarde dans le fichier :
     *
     *  Ligne 1 : date et heure (YYYY-MM-DDTHH:MM)
     *  Ligne 2 : numéro du client
     *  Ligne 3 : prestation sous forme "categorie : info : prix"
     *
     * @return une chaîne de caractères prête à être écrite dans le fichier
     */
    public String versFichier() {
        return dateHeure.toString() + "\n"
             + client.getNumeroClient() + "\n"
             + prestation.versFichier();
    }

    // =======================
    //       TO STRING
    // =======================

    /**
     * Retourne une description complète du rendez-vous,
     * affichée lors des tests ou dans le planning.
     */
    @Override
    public String toString() {
        return "RendezVous : " + dateHeure
                + ", " + client
                + ", " + prestation;
    }
}
