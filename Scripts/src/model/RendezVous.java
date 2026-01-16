// Binh Minh TRAN - Marouane NOUARA
// ING1 - APP - BDML 

package model;

import model.Prestation.Prestation;
import java.time.LocalDateTime;



/**
 * Représente un rendez-vous dans la station de nettoyage.
 *
 * Un rendez-vous associe :
 *  - un client
 *  - une prestation choisie
 *  - le prix correspondant à cette prestation
 *
 * Le prix n’est pas saisi manuellement : il est calculé
 * automatiquement à partir de la prestation.
 */
public class RendezVous {

    // Client concerné par le rendez-vous
    private Client client;

    // Prestation choisie par le client
    private Prestation prestation;

    // Prix de la prestation
    private double prix;

    /**
     * Constructeur de la classe RendezVous.
     * Il initialise le client et la prestation,
     * puis calcule automatiquement le prix
     * de la prestation choisie.
     *
     * @param client le client concerné par le rendez-vous
     * @param prestation la prestation choisie
     */
    public RendezVous(Client client, Prestation prestation) {
        this.client = client;
        this.prestation = prestation;
        this.prix = prestation.nettoyage();
    }

  
    // Getters

    public Client getClient() {
        return client;
    }

    public Prestation getPrestation() {
        return prestation;
    }

    public double getPrix() {
        return prix;
    }
    

    // Setters
    public void setClient(Client client) {
        this.client = client;
    }

    public void setPrestation(Prestation prestation) {
        this.prestation = prestation;
        this.prix = prestation.nettoyage(); // recalcul automatique du prix
    }

    //on a choisie de ne pas mettre de setters pour le prix car le prix est un calcule il n'a pas a etres modifier manuellement 
    //mais on peut changer de prestationt ou de client si on ce trompe 

    /**
     * Retourne les informations du rendez-vous sous forme de chaîne de caractères
     * pour l'écriture dans un fichier texte.
     * Format: 3 lignes séparées par System.lineSeparator():
     * - Ligne 1: date et heure (format: yyyy-MM-ddThh:mm)
     * - Ligne 2: numéro du client
     * - Ligne 3: informations de la prestation (format dépend du type)
     * 
     * Note: Cette méthode nécessite que le rendez-vous ait une date/heure.
     * Pour l'instant, elle retourne uniquement les informations disponibles.
     * 
     * @param dateHeure la date et l'heure du rendez-vous
     * @return une chaîne de caractères formatée pour le fichier (3 lignes)
     */
    public String versFichier(LocalDateTime dateHeure) {
        StringBuilder sb = new StringBuilder();
        // Ligne 1: date et heure
        sb.append(dateHeure.toString().substring(0, 16)); // Format: yyyy-MM-ddThh:mm
        sb.append(System.lineSeparator());
        // Ligne 2: numéro du client
        sb.append(client.getNumeroClient());
        sb.append(System.lineSeparator());
        // Ligne 3: informations de la prestation
        sb.append(prestation.versFichier());
        return sb.toString();
    }

    /**
     * Retourne une représentation textuelle du rendez-vous.
     *
     * @return une description du rendez-vous
     */
    @Override
    public String toString() {
        return "\n- " + client + "\n- " + prestation;
    }

}