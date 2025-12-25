/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.stationlavage;

/**
 *
 * @author nouar
 */
public class Client {

    // Attributs
    
    private int numeroClient;
    private String nom;
    private String telephone;
    private String email; // optionnel

    // Constructeur sans email car email optionelle comme sa quand on veux 
    //ajouter un client sans email c'est possible sans probleme 
    public Client(int numeroClient, String nom, String telephone) {
        this.numeroClient = numeroClient;
        this.nom = nom;
        this.telephone = telephone;
        this.email = null;//vos null car c'est le constructeur sans email
    }

    // Constructeur avec email ce constructeur sera utlie quand on veux ajouter 
    //un client et preciser sont email si on l'a 
    public Client(int numeroClient, String nom, String telephone, String email) {
        this.numeroClient = numeroClient;
        this.nom = nom;
        this.telephone = telephone;
        this.email = email;
    }

    // Getters
    public int getNumeroClient() {
        return numeroClient;
    }

    public String getNom() {
        return nom;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    
    // Méthode toString pour afficher les donner du client quand on fait println 
   
    @Override
    public String toString() {
        if (email != null) {
            return "Client [numero= " + numeroClient +", nom= " + nom +", telephone= " + telephone +", email= " + email + "]";
        } else {
            return "Client [numero= " + numeroClient +", nom= " + nom +", telephone= " + telephone + "]";
        }
    }
}
