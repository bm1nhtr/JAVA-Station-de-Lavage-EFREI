/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.stationlavage;

import com.mycompany.stationlavage.Prestation.Prestation;

/**
 *
 * @author nouar
 */


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

    /**
     * Retourne une représentation textuelle du rendez-vous.
     *
     * @return une description du rendez-vous
     */
    @Override
    public String toString() {
        return "RendezVous : " + client + ", " + prestation ;
    }
}

