// Binh Minh TRAN - Marouane NOUARA
// ING1 - APP - BDML 

package service;

import model.Client;
import model.RendezVous;
import model.Prestation.PrestationTresSale;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;


public class Etablissement {
    // Attributs
    private String nom;
    private int nombre_clients;
    private Client[] liste_clients; // Tableau de clients classé par ordre lexicographique
    private static final int MAX_CLIENTS = 100; // Limite du nombre de clients
    private int prochainNumeroClient; // Numéro du prochain client à créer (numérotation automatique)
    
    // Planning des rendez-vous pour les 7 jours suivants
    // Chaque ligne correspond à un créneau horaire (16 créneaux de 30 min de 10h à 18h)
    // Chaque colonne correspond à un jour (7 jours)
    // Créneaux : 10:00, 10:30, 11:00, 11:30, 12:00, 12:30, 13:00, 13:30, 14:00, 14:30, 15:00, 15:30, 16:00, 16:30, 17:00, 17:30
    private static final int NB_CRENEAUX = 16; // 16 créneaux de 30 min de 10h à 18h
    private static final int NB_JOURS = 7; // 7 jours suivants
    private RendezVous[][] planning; // planning[creneau][jour]
    
    // Constructeurs
    public Etablissement(String nom) {
        this.nom = nom;
        this.nombre_clients = 0;
        this.liste_clients = new Client[MAX_CLIENTS];
        this.planning = new RendezVous[NB_CRENEAUX][NB_JOURS];
        this.prochainNumeroClient = 1; // Commencer la numérotation à 1
    }
    
    // Constructeur avec paramètres
    public Etablissement(String nom, Client[] liste_clients){
        this.nom = nom;
        this.nombre_clients = 0;
        this.liste_clients = new Client[MAX_CLIENTS];
        this.planning = new RendezVous[NB_CRENEAUX][NB_JOURS];
        
        // Copier les clients existants et trouver le numéro max
        int maxNumero = 0;
        if (liste_clients != null) {
            for (Client client : liste_clients) {
                if (client != null && this.nombre_clients < MAX_CLIENTS) {
                    this.liste_clients[this.nombre_clients++] = client;
                    maxNumero = Math.max(maxNumero, client.getNumeroClient());
                }
            }
        }
        this.prochainNumeroClient = maxNumero + 1;
    }
    
    //getters
    public String getNom(){
        return this.nom;
    }
    public int getNombre_clients(){
        return this.nombre_clients;
    }
    public Client[] getListe_clients(){
        return this.liste_clients;
    }
    public RendezVous[][] getPlanning(){
        return this.planning;
    }
    
    //Setters
    public void setNom(String nom){
        this.nom = nom;
    }
    public void setNombre_clients(int nombre_clients){
        this.nombre_clients = nombre_clients;
    }
    public void setListe_clients(Client[] liste_clients){
        this.liste_clients = liste_clients;
    }
    public void setPlanning(RendezVous[][] planning){
        this.planning = planning;
    }
    
    /**
     * Recherche un client dans le tableau de clients
     * en fonction de son nom et de son numéro de téléphone.
     * 
     * @param nom le nom du client à rechercher
     * @param telephone le numéro de téléphone du client à rechercher
     * @return le client trouvé ou null s'il n'existe pas
     */
    public Client rechercher(String nom, String telephone) {
        for (int i = 0; i < nombre_clients; i++) {
            if (liste_clients[i] != null &&
                liste_clients[i].getNom().equals(nom) &&
                liste_clients[i].getTelephone().equals(telephone)) {
                return liste_clients[i];
            }
        }
        return null;
    }
    
    /**
     * Ajoute un client dans le tableau de clients.
     * Le tableau est maintenu trié par ordre lexicographique.
     * 
     * @param client le client à ajouter
     * @return true si le client a été ajouté, false si le tableau est plein
     */
    public boolean ajouter(Client client) {
        // Vérifier si le tableau est plein
        if (nombre_clients >= MAX_CLIENTS) { // si le nombre de clients est >= à la limite, 
        // donc on a toujours au moins un element null dans le tableau donc on peut ajouter un client
            return false;
        }
        
        // Vérifier si le client existe déjà
        if (rechercher(client.getNom(), client.getTelephone()) != null) {
            return false; // Client déjà existant
        }
        
        // Trouver la position d'insertion pour maintenir l'ordre lexicographique
        int position = 0;
        while (position < nombre_clients && 
               liste_clients[position] != null &&
               !client.placerApres(liste_clients[position])) {
            position++;
        }
        
        // Décaler les clients vers la droite pour faire de la place
        for (int i = nombre_clients; i > position; i--) {
            liste_clients[i] = liste_clients[i - 1];
        }
        
        // Insérer le nouveau client à la position trouvée
        liste_clients[position] = client;
        nombre_clients++;
        
        return true;
    }
    
    /**
     * Ajoute un client dans le tableau de clients avec son email.
     * Le numéro de client est attribué automatiquement de manière continue.
     * Le tableau est maintenu trié par ordre lexicographique.
     * 
     * @param nom le nom du client
     * @param telephone le numéro de téléphone du client
     * @param email l'adresse email du client
     * @return le client ajouté ou null si l'ajout a échoué
     */
    public Client ajouter(String nom, String telephone, String email) {
        // Créer le client avec le prochain numéro disponible
        Client client = new Client(prochainNumeroClient, nom, telephone, email);
        
        // Utiliser la méthode ajouter(Client) pour éviter la duplication de code
        if (ajouter(client)) {
            // Si l'ajout a réussi, incrémenter le numéro pour le prochain client
            prochainNumeroClient++;
            return client;
        }
        return null;
    }
    
    /**
     * Ajoute un client dans le tableau de clients sans email.
     * Le numéro de client est attribué automatiquement de manière continue.
     * Le tableau est maintenu trié par ordre lexicographique.
     * 
     * @param nom le nom du client
     * @param telephone le numéro de téléphone du client
     * @return le client ajouté ou null si l'ajout a échoué
     */
    public Client ajouter(String nom, String telephone) {
        // Créer le client avec le prochain numéro disponible
        Client client = new Client(prochainNumeroClient, nom, telephone);
        
        // Utiliser la méthode ajouter(Client) pour éviter la duplication de code
        if (ajouter(client)) {
            // Si l'ajout a réussi, incrémenter le numéro pour le prochain client
            prochainNumeroClient++;
            return client;
        }
        return null;
    }
    
    /**
     * Recherche un créneau pour un jour donné.
     * Affiche toutes les heures disponibles pour le jour souhaité,
     * puis fait choisir une heure au client et retourne le créneau correspondant.
     * 
     * @param jour la date pour laquelle rechercher un créneau
     * @return le créneau (LocalDateTime) correspondant ou null si aucun créneau disponible
     */
    public LocalDateTime rechercher(LocalDate jour) {
        // Calculer la date d'aujourd'hui et le nombre de jours écoulés jusqu'au jour demandé
        LocalDate aujourdhui = LocalDate.now();
        long joursEcoules = aujourdhui.until(jour, ChronoUnit.DAYS);
        
        // Vérifier que la date est dans les 7 jours suivants
        // joursEcoules < 0 signifie que la date est dans le passé
        // joursEcoules >= NB_JOURS signifie que la date est au-delà de 7 jours
        if (joursEcoules < 0 || joursEcoules >= NB_JOURS) {
            System.out.println("La date doit être dans les 7 jours suivants.");
            return null;
        }
        
        // Convertir le nombre de jours écoulés en indice de jour (0 = aujourd'hui, 1 = demain, etc.)
        int indiceJour = (int)joursEcoules;
        
        // Afficher les heures disponibles pour ce jour
        System.out.println("Créneaux disponibles pour le " + jour + " :");
        // Tableau pour stocker les indices des créneaux disponibles
        int[] heuresDisponibles = new int[NB_CRENEAUX];
        int nbDisponibles = 0;
        
        // Parcourir tous les créneaux horaires (16 créneaux de 30 min de 10h à 17h30)
        for (int creneau = 0; creneau < NB_CRENEAUX; creneau++) {
            // Si le créneau est libre (null dans le planning)
            if (planning[creneau][indiceJour] == null) {
                // Convertir l'indice du créneau en heures et minutes
                // creneau / 2 donne le nombre d'heures depuis 10h (0->0h, 1->0h, 2->1h, etc.)
                // creneau % 2 donne 0 pour les créneaux pairs (heure pile) ou 1 pour impairs (30 min)
                int heures = 10 + (creneau / 2);
                int minutes = (creneau % 2) * 30;
                
                // Stocker l'indice du créneau disponible
                heuresDisponibles[nbDisponibles] = creneau;
                // Afficher le créneau avec un numéro pour que l'utilisateur puisse choisir
                System.out.println((nbDisponibles + 1) + ". " + 
                    String.format("%02d:%02d", heures, minutes));
                nbDisponibles++;
            }
        }
        
        // Si aucun créneau n'est disponible, retourner null
        if (nbDisponibles == 0) {
            System.out.println("Aucun créneau disponible pour ce jour.");
            return null;
        }
        
        // Faire choisir une heure au client parmi les créneaux disponibles
        // Note: On ne ferme pas le Scanner(System.in) car cela fermerait System.in
        // et empêcherait les utilisations ultérieures
        Scanner scanner = new Scanner(System.in);
        System.out.print("Choisissez un créneau (1-" + nbDisponibles + ") : ");
        int choix = scanner.nextInt();
        
        // Vérifier que le choix est valide (entre 1 et le nombre de créneaux disponibles)
        if (choix < 1 || choix > nbDisponibles) {
            System.out.println("Choix invalide.");
            return null;
        }
        
        // Retourner le créneau choisi sous forme de LocalDateTime
        // Récupérer l'indice du créneau choisi (choix - 1 car l'affichage commence à 1)
        int creneauChoisi = heuresDisponibles[choix - 1];
        // Convertir l'indice du créneau en heures et minutes
        int heures = 10 + (creneauChoisi / 2);
        int minutes = (creneauChoisi % 2) * 30;
        // Créer et retourner le LocalDateTime avec la date et l'heure du créneau
        return LocalDateTime.of(jour, LocalTime.of(heures, minutes));
    }
    
    /**
     * Recherche un créneau pour une heure donnée.
     * Affiche tous les jours disponibles pour l'heure souhaitée,
     * puis fait choisir un jour au client et retourne le créneau correspondant.
     * 
     * @param heure l'heure pour laquelle rechercher un créneau
     * @return le créneau (LocalDateTime) correspondant ou null si aucun créneau disponible
     */
    public LocalDateTime rechercher(LocalTime heure) {
        // Vérifier que l'heure est entre 10h et 18h
        if (heure.isBefore(LocalTime.of(10, 0)) || heure.isAfter(LocalTime.of(18, 0))) {
            System.out.println("L'heure doit être entre 10h et 18h.");
            return null;
        }
        
        // Calculer l'indice du créneau
        int heures = heure.getHour() - 10;
        int minutes = heure.getMinute();
        int indiceCreneau = heures * 2 + (minutes / 30);
        
        if (indiceCreneau < 0 || indiceCreneau >= NB_CRENEAUX) {
            System.out.println("Heure invalide. Les créneaux sont de 30 minutes (10:00, 10:30, etc.).");
            return null;
        }
        
        // Afficher les jours disponibles pour cette heure
        System.out.println("Jours disponibles pour " + heure + " :");
        LocalDate aujourdhui = LocalDate.now();
        LocalDate[] joursDisponibles = new LocalDate[NB_JOURS];
        int nbDisponibles = 0;
        
        for (int jour = 0; jour < NB_JOURS; jour++) {
            LocalDate date = aujourdhui.plusDays(jour);
            if (planning[indiceCreneau][jour] == null) {
                // Créneau libre
                joursDisponibles[nbDisponibles] = date;
                System.out.println((nbDisponibles + 1) + ". " + date);
                nbDisponibles++;
            }
        }
        
        if (nbDisponibles == 0) {
            System.out.println("Aucun jour disponible pour cette heure.");
            return null;
        }
        
        // Faire choisir un jour au client
        // Note: On ne ferme pas le Scanner(System.in) car cela fermerait System.in
        // et empêcherait les utilisations ultérieures
        Scanner scanner = new Scanner(System.in);
        System.out.print("Choisissez un jour (1-" + nbDisponibles + ") : ");
        int choix = scanner.nextInt();
        
        if (choix < 1 || choix > nbDisponibles) {
            System.out.println("Choix invalide.");
            return null;
        }
        
        // Retourner le créneau choisi
        LocalDate jourChoisi = joursDisponibles[choix - 1];
        return LocalDateTime.of(jourChoisi, heure);
    }
    
    /**
     * Convertit une date et une heure en indices de créneau et de jour.
     * 
     * @param date la date du rendez-vous
     * @param heure l'heure du rendez-vous
     * @return un tableau de 2 éléments [indiceCreneau, indiceJour] ou null si invalide
     */
    private int[] convertirEnIndices(LocalDate date, LocalTime heure) {
        LocalDate aujourdhui = LocalDate.now();
        long joursEcoules = aujourdhui.until(date, ChronoUnit.DAYS);
        
        // Vérifier que la date est dans les 7 jours suivants
        if (joursEcoules < 0 || joursEcoules >= NB_JOURS) {
            return null;
        }
        
        // Vérifier que l'heure est entre 10h et 18h
        if (heure.isBefore(LocalTime.of(10, 0)) || heure.isAfter(LocalTime.of(18, 0))) {
            return null;
        }
        
        // Calculer l'indice du créneau (10:00 = 0, 10:30 = 1, ..., 17:30 = 15)
        int heures = heure.getHour() - 10;
        int minutes = heure.getMinute();
        int indiceCreneau = heures * 2 + (minutes / 30);
        
        if (indiceCreneau < 0 || indiceCreneau >= NB_CRENEAUX) {
            return null;
        }
        
        return new int[]{indiceCreneau, (int)joursEcoules};
    }
    
      /**
     * Ajoute un rendez-vous pour une prestation express.
     * 
     * @param client le client concerné
     * @param date la date du rendez-vous
     * @param heure l'heure du rendez-vous
     * @param categorieVehicule la catégorie du véhicule ("A", "B" ou "C")
     * @param nettoyageInterieur true si le nettoyage intérieur est demandé, false sinon
     * @return le rendez-vous ajouté ou null si l'ajout a échoué
     */
    public RendezVous ajouter(Client client, LocalDate date, LocalTime heure, 
                              String categorieVehicule, boolean nettoyageInterieur) {
        int[] indices = convertirEnIndices(date, heure);
        if (indices == null) {
            return null; // Date ou heure invalide
        }
        
        int creneau = indices[0];
        int jour = indices[1];
        
        // Vérifier que le créneau est libre
        if (planning[creneau][jour] != null) {
            return null; // Créneau déjà occupé
        }
        
        // Créer la prestation et le rendez-vous
        model.Prestation.PrestationExpress prestation = 
            new model.Prestation.PrestationExpress(categorieVehicule, nettoyageInterieur);
        RendezVous rdv = new RendezVous(client, prestation);
        
        // Ajouter le rendez-vous au planning
        planning[creneau][jour] = rdv;
        
        return rdv;
    }
    
    /**
     * Ajoute un rendez-vous pour une prestation pour véhicule sale.
     * 
     * @param client le client concerné
     * @param date la date du rendez-vous
     * @param heure l'heure du rendez-vous
     * @param categorieVehicule la catégorie du véhicule ("A", "B" ou "C")
     * @return le rendez-vous ajouté ou null si l'ajout a échoué
     */
    public RendezVous ajouter(Client client, LocalDate date, LocalTime heure, 
                              String categorieVehicule) {
        int[] indices = convertirEnIndices(date, heure);
        if (indices == null) {
            return null; // Date ou heure invalide
        }
        
        int creneau = indices[0];
        int jour = indices[1];
        
        // Vérifier que le créneau est libre
        if (planning[creneau][jour] != null) {
            return null; // Créneau déjà occupé
        }
        
        // Créer la prestation et le rendez-vous
        model.Prestation.PrestationSale prestation = 
            new model.Prestation.PrestationSale(categorieVehicule);
        RendezVous rdv = new RendezVous(client, prestation);
        
        // Ajouter le rendez-vous au planning
        planning[creneau][jour] = rdv;
        
        return rdv;
    }
    
    /**
     * Ajoute un rendez-vous pour une prestation pour véhicule très sale.
     * 
     * @param client le client concerné
     * @param date la date du rendez-vous
     * @param heure l'heure du rendez-vous
     * @param categorieVehicule la catégorie du véhicule ("A", "B" ou "C")
     * @param typeSalissure le type de salissure (1: nourriture, 2: boue, 3: transpiration, 4: graisse)
     * @return le rendez-vous ajouté ou null si l'ajout a échoué
     */
    public RendezVous ajouter(Client client, LocalDate date, LocalTime heure, 
                              String categorieVehicule, int typeSalissure) {
        int[] indices = convertirEnIndices(date, heure);
        if (indices == null) {
            return null; // Date ou heure invalide
        }
        
        int creneau = indices[0];
        int jour = indices[1];
        
        // Vérifier que le créneau est libre
        if (planning[creneau][jour] != null) {
            return null; // Créneau déjà occupé
        }
        
        // Créer la prestation et le rendez-vous
        PrestationTresSale prestation = 
            new model.Prestation.PrestationTresSale(categorieVehicule, typeSalissure);
        RendezVous rdv = new RendezVous(client, prestation);
        
        // Ajouter le rendez-vous au planning
        planning[creneau][jour] = rdv;
        
        return rdv;
    }

    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("\nL'établissement \"")
          .append(nom)
          .append("\" compte ")
          .append(nombre_clients)
          .append(" client(s).\n")
          .append("Liste des clients :\n");
        
        // Afficher les clients de l'établissement sous forme de liste à puces
        for (int i = 0; i < nombre_clients; i++) {
            if (liste_clients[i] != null) {
                sb.append(" - ").append(liste_clients[i]).append("\n");
            }
        }
        return sb.toString();
    }
    
    /**
     * Sauvegarde les clients de l'établissement dans un fichier texte.
     * Chaque client est écrit sur une ligne au format défini par versFichier().
     * 
     * @param nomFichier le nom du fichier dans lequel sauvegarder
     * @throws IOException en cas d'erreur d'écriture
     */
    public void versFichierClients(String nomFichier) throws IOException {
        FileWriter fichier = new FileWriter(nomFichier, false); // false = écraser le fichier
        try {
            for (int i = 0; i < nombre_clients; i++) {
                if (liste_clients[i] != null) {
                    fichier.write(liste_clients[i].versFichier());
                    fichier.write(System.lineSeparator());
                }
            }
        } finally {
            fichier.close();
        }
    }
    
    /**
     * Charge les clients depuis un fichier texte.
     * Le fichier doit contenir une ligne par client au format:
     * "numéro : nom : téléphone" ou "numéro : nom : téléphone : email"
     * 
     * IMPORTANT: Cette méthode est une méthode d'instance (non-statique).
     * Lorsqu'elle est appelée sur un objet Etablissement (ex: E3.depuisFichierClients()),
     * elle modifie DIRECTEMENT les attributs de cet objet (this.liste_clients, 
     * this.nombre_clients, this.prochainNumeroClient).
     * @param nomFichier le nom du fichier à charger
     * @throws IOException en cas d'erreur de lecture
     */
    public void depuisFichierClients(String nomFichier) throws IOException {
        FileReader fichier = new FileReader(nomFichier);
        BufferedReader lecteur = new BufferedReader(fichier);
        try {
            String ligne;
            while ((ligne = lecteur.readLine()) != null) {
                // Ignorer les lignes vides
                if (ligne.trim().isEmpty()) {
                    continue;
                }
                
                // Séparer les champs par " : "
                String[] champs = ligne.split(" : ");
                
                if (champs.length >= 3) {
                    try {
                        int numero = Integer.parseInt(champs[0].trim());
                        String nom = champs[1].trim();
                        String telephone = champs[2].trim();
                        
                        if (champs.length == 4) {
                            // Client avec email
                            String email = champs[3].trim();
                            Client client = new Client(numero, nom, telephone, email);
                            // Utiliser ajouter(Client) pour maintenir l'ordre lexicographique
                            // NOTE: ajouter() modifie this.liste_clients et this.nombre_clients
                            // de l'objet Etablissement sur lequel cette méthode est appelée
                            ajouter(client);
                            // Mettre à jour prochainNumeroClient si nécessaire
                            // NOTE: prochainNumeroClient fait référence à this.prochainNumeroClient
                            // de l'objet qui appelle cette méthode
                            if (numero >= prochainNumeroClient) {
                                prochainNumeroClient = numero + 1;
                            }
                        } else {
                            // Client sans email
                            Client client = new Client(numero, nom, telephone);
                            // NOTE: ajouter() ajoute le client dans this.liste_clients
                            // de l'objet Etablissement actuel (celui qui appelle cette méthode)
                            ajouter(client);
                            // Mettre à jour prochainNumeroClient si nécessaire
                            if (numero >= prochainNumeroClient) {
                                prochainNumeroClient = numero + 1;
                            }
                        }
                    } catch (NumberFormatException e) {
                        // Ignorer les lignes mal formatées
                        System.err.println("Ligne ignorée (format invalide): " + ligne);
                    }
                }
            }
        } finally {
            lecteur.close();
            fichier.close();
        }
    }

    /* Méthode planifier()
    * --------------------
    * Cette méthode est le point d’entrée principal pour réserver un rendez-vous.
    * Elle guide l’utilisateur pas à pas dans toutes les étapes nécessaires pour
    * enregistrer un rendez-vous complet dans le planning.
    *
    * Objectifs :
    *   1. Identifier ou créer automatiquement un client.
    *   2. Proposer les 7 prochains jours et laisser l’utilisateur choisir une date.
    *   3. Trouver le premier créneau horaire disponible ce jour-là.
    *   4. Demander le type de prestation souhaitée (3 options).
    *   5. Ajouter le rendez-vous dans le planning.
    *   6. Afficher le récapitulatif et le prix final.
    *
    * Fonctionnement général :
    *   - La méthode utilise la recherche dans le tableau des clients.
    *   - Elle exploite les surcharges de "ajouter(...)" pour créer un rendez-vous
    *     adapté au type de prestation choisi.
    *   - Elle s’appuie sur la méthode rechercher(LocalDate) pour trouver un créneau
    *     libre dans le planning.
    *   - Elle vérifie toutes les étapes et arrête proprement en cas d’erreur.
    *
    * En résumé :
    *   Cette méthode orchestre toute la logique métier nécessaire pour créer
    *   un rendez-vous : gestion client, gestion du planning, et choix de prestation.
    *   Elle garantit que le rendez-vous est cohérent et correctement enregistré.
    */

    public void planifier() {
        // Note: On ne ferme pas le Scanner(System.in) car cela fermerait System.in
        // et empêcherait les utilisations ultérieures (notamment dans rechercher())
        Scanner sc = new Scanner(System.in);

        System.out.println("\n===== PLANIFIER UN RENDEZ-VOUS =====");

        
        // 1) IDENTIFICATION DU CLIENT (recherche ou création automatique)
        
        System.out.print("Nom du client : ");
        String nom = sc.nextLine();

        System.out.print("Téléphone du client : ");
        String telephone = sc.nextLine();

        // Recherche dans le tableau des clients
        Client client = rechercher(nom, telephone);

        // Si le client n'existe pas → création automatique
        if (client == null) {
            System.out.println("Nouveau client détecté. Ajout...");
            client = ajouter(nom, telephone);

            if (client == null) { // sécurité en cas d'échec
                System.out.println("Erreur : impossible d'ajouter le client.");
                return;
            }
        } else {
            System.out.println("Client trouvé : " + client);
        }

        
        // 2) CHOIX DU JOUR (parmi les 7 prochains)
        
        LocalDate today = LocalDate.now();
        LocalDate[] jours = new LocalDate[NB_JOURS];

        System.out.println("\nChoisissez un jour :");

        // Affichage des 7 dates possibles
        for (int i = 0; i < NB_JOURS; i++) {
            jours[i] = today.plusDays(i);
            System.out.println((i + 1) + ". " + jours[i]);
        }

        // Saisie sécurisée dans l'intervalle 1–7
        int choixJour;
        do {
            System.out.print("Votre choix (1-7) : ");
            choixJour = sc.nextInt();
        } while (choixJour < 1 || choixJour > NB_JOURS);

        LocalDate jourChoisi = jours[choixJour - 1];

        
        // 3) TROUVER LE PREMIER CRÉNEAU DISPONIBLE CE JOUR-LÀ
        
        LocalDateTime dateHeure = rechercher(jourChoisi);

        if (dateHeure == null) {
            System.out.println("Impossible de planifier : aucun créneau disponible.");
            return;
        }

        LocalDate date = dateHeure.toLocalDate();
        LocalTime heure = dateHeure.toLocalTime();

        
        // 4) CHOIX DU TYPE DE PRESTATION
       
        System.out.println("""
        Type de prestation :
        1 - Prestation Express
        2 - Prestation Sale
        3 - Prestation Très Sale
        """);

        System.out.print("Votre choix : ");
        int choix = sc.nextInt();
        sc.nextLine(); // vider le buffer

        System.out.print("Catégorie du véhicule (A/B/C) : ");
        String categorie = sc.nextLine();

        RendezVous rdv = null;

        
        // 5) CRÉATION DU RDV (via les surcharges de ajouter())
        
        switch (choix) {

            //Prestation Express 
            case 1 -> {
                System.out.print("Nettoyage intérieur (true/false) : ");
                boolean interieur = sc.nextBoolean();
                rdv = ajouter(client, date, heure, categorie, interieur);
            }

            //Prestation Sale 
            case 2 -> {
                rdv = ajouter(client, date, heure, categorie);
            }

            //Prestation Très Sale 
            case 3 -> {
                System.out.println("""
                        Type de salissure :
                        1 - Nourriture
                        2 - Boue
                        3 - Transpiration
                        4 - Graisse
                        """);

                System.out.print("Votre choix : ");
                int typeSalissure = sc.nextInt();

                rdv = ajouter(client, date, heure, categorie, typeSalissure);
            }

            //Erreur de choix 
            default -> {
                System.out.println("Choix invalide.");
                return;
            }
        }

        // 6) AFFICHAGE FINAL
        if (rdv == null) {
            System.out.println("❌ Le créneau était déjà occupé. Rendez-vous non enregistré.");
        } else {
            System.out.println("\n✔ Rendez-vous enregistré !");
            System.out.println(rdv);
            System.out.println("Prix total : " + rdv.getPrix() + " €");
        }
    }


    
 // À FAIRE : reconsidérer la docstring : on n’a besoin que de la fonctionnalité
 // globale de la méthode, puis lister les arguments s’ils existent et le type
 // de valeur retournée. Tu peux sauvegarder cette version de la docstring
 // quelque part pour t’aider lors de la soutenance, mais pas dans le script.

     /**
     * Méthode afficherClient()
     * -------------------------------------
     * Cette méthode permet de rechercher des clients dans l'établissement en
     * fonction d’un nom et/ou d’un numéro de téléphone saisis par l’utilisateur.
     *
     * Elle gère quatre cas de figure :
     *
     *   1) Nom seulement :
     *        → Affiche tous les clients portant ce nom.
     *
     *   2) Téléphone seulement :
     *        → Affiche tous les clients ayant ce numéro.
     *
     *   3) Nom + téléphone :
     *        → Affiche uniquement les clients pour lesquels
     *          le nom ET le téléphone correspondent.
     *
     *   4) Aucun des deux :
     *        → Affiche un message d’erreur et redemande automatiquement jusqu’à obtenir au moins un critère.
     *
     * Logique utilisée :
     *   - On lit le nom et le téléphone comme informations facultatives.
     *   - On détermine quel type de recherche effectuer selon les champs remplis.
     *   - La boucle parcourt tous les clients de l’établissement.
     *   - En fonction des critères saisis, on applique la bonne condition :
     *        nom OU téléphone si un seul champ renseigné,
     *        nom ET téléphone si les deux sont renseignés.
     *   - Tous les clients correspondants sont affichés (même en cas de doublons).
     *
     * Objectif :
     *   - Fournir une recherche souple, intuitive et claire pour l’utilisateur.
     *   - Éviter les rigidités des recherches exactes et offrir plusieurs cas d'usage.
     */

    public void afficherClient() {
        // Note: On ne ferme pas le Scanner(System.in) car cela fermerait System.in
        // et empêcherait les utilisations ultérieures
        Scanner sc = new Scanner(System.in);

        System.out.println("\n=== Recherche de client ===");
        
        String nom = "";
        String telephone = "";

        // Boucle jusqu'à ce qu'au moins un critère soit renseigné
        while (nom.isBlank() && telephone.isBlank()) {

            // Lecture du nom (optionnel)
            System.out.print("Entrez un nom (ou vide) : ");
            nom = sc.nextLine().trim();

            // Lecture du téléphone (optionnel)
            System.out.print("Entrez un téléphone (ou vide) : ");
            telephone = sc.nextLine().trim();

            // Cas 1 : aucun critère
            if (nom.isBlank() && telephone.isBlank()) {
                System.out.println(" Vous devez saisir au moins un critère.\n");
            
            }
        }

        boolean trouve = false;

        System.out.println("\n--- Clients trouvés ---");

        for (int i = 0; i < nombre_clients; i++) {

            Client c = liste_clients[i];
            if (c == null) continue;

            boolean matchNom = !nom.isBlank() && c.getNom().equalsIgnoreCase(nom);
            boolean matchTel = !telephone.isBlank() && c.getTelephone().equals(telephone);

            // Cas 2 : recherche *nom seulement*
            if (!nom.isBlank() && telephone.isBlank()) {
                if (matchNom) {
                    System.out.println(" - " + c);
                    trouve = true;
                }
            }

            // Cas 3 : recherche *téléphone seulement*
            else if (nom.isBlank() && !telephone.isBlank()) {
                if (matchTel) {
                    System.out.println(" - " + c);
                    trouve = true;
                }
            }

            // Cas 4 : recherche *nom + téléphone* → match NOM **ET** TEL
            else {
                if (matchNom && matchTel) {
                    System.out.println(" - " + c);
                    trouve = true;
                }
            }
        }

        // Aucun résultat
        if (!trouve) {
            System.out.println("Aucun client ne correspond à la recherche.");
        }
    }
    
 // À FAIRE : reconsidérer la docstring : on n’a besoin que de la fonctionnalité
 // globale de la méthode, puis lister les arguments s’ils existent et le type
 // de valeur retournée. Tu peux sauvegarder cette version de la docstring
 // quelque part pour t’aider lors de la soutenance, mais pas dans le script.

    
     /**
     * Méthode afficherPlanning()
     * ---------------------------
     * Cette méthode demande une date à l'utilisateur, vérifie qu’elle appartient
     * aux 7 jours gérés par le planning, puis affiche tous les créneaux du jour.
     *
     * Fonctionnement :
     *   1. Demande une date au format YYYY-MM-DD.
     *   2. Calcule l’indice du jour (0 à 6) en comparant avec today + x jours.
     *   3. Redemande si la date n’est pas valide.
     *   4. Affiche les créneaux horaires (toutes les 30 minutes de 10h à 18h).
     *   5. Pour chaque créneau, indique :
     *        - [Libre]
     *        - Ou l'objet RendezVous correspondant.
     *
     * En résumé :
     *   Cette méthode regroupe l’interaction console + validation + affichage du planning
     *   en une seule opération simple et cohérente.
     */
    public void afficherPlanning() {
        // Note: On ne ferme pas le Scanner(System.in) car cela fermerait System.in
        // et empêcherait les utilisations ultérieures
        Scanner sc = new Scanner(System.in);

        LocalDate date = null;
        int index = -1;
        LocalDate today = LocalDate.now();

        // 1) Demande + validation de la date
        while (index == -1) {
            System.out.print("Entrez une date (YYYY-MM-DD) : ");
            date = LocalDate.parse(sc.nextLine());

            // Calcul de l'indice : 0 = aujourd'hui, 1 = demain, ..., 6 = +6 jours
            long diff = ChronoUnit.DAYS.between(today, date);

            if (diff >= 0 && diff < NB_JOURS) {
                index = (int) diff;  // OK : date valide
            } else {
                System.out.println("❌ Date invalide (pas dans les 7 jours). Réessayez.\n");
            }
        }

        // 2) Affichage du planning du jour
        System.out.println("\n=== PLANNING DU " + date + " ===");

        for (int c = 0; c < NB_CRENEAUX; c++) {

            int h = 10 + (c / 2);
            int m = (c % 2) * 30;

            String horaire = String.format("%02d:%02d", h, m);

            RendezVous rdv = planning[c][index];

            if (rdv == null)
                System.out.println(horaire + " : [Libre]");
            else
                System.out.println(horaire + " : " + rdv);
        }
    }


    
 // À FAIRE : reconsidérer la docstring : on n’a besoin que de la fonctionnalité
 // globale de la méthode, puis lister les arguments s’ils existent et le type
 // de valeur retournée. Tu peux sauvegarder cette version de la docstring
 // quelque part pour t’aider lors de la soutenance, mais pas dans le script.


     /**
     * Méthode afficherRendezVousParNumeroClient()
     * -------------------------------------------
     * Cette méthode recherche et affiche tous les rendez-vous d’un client
     * en demandant son numéro via la console.
     *
     * Fonctionnement simplifié :
     *   1. L’utilisateur saisit un numéro client.
     *   2. Le programme parcourt l’ensemble du planning.
     *   3. À chaque créneau, si un RDV existe et appartient à ce client,
     *      il est affiché avec la date et l’heure correspondantes.
     *   4. Si aucun RDV n'est trouvé, un message informatif s’affiche.
     *
     * Objectif :
     *   - Offrir une consultation simple et rapide des rendez-vous d’un client.
     */

    public void afficherRendezVousParNumeroClient() {
        // Note: On ne ferme pas le Scanner(System.in) car cela fermerait System.in
        // et empêcherait les utilisations ultérieures
        Scanner sc = new Scanner(System.in);

        System.out.println("\n=== Recherche des rendez-vous ===");
        System.out.print("Numéro du client : ");
        int numeroClient = sc.nextInt();

        boolean trouve = false;

        // Parcours du planning complet
        for (int jour = 0; jour < NB_JOURS; jour++) {
            for (int creneau = 0; creneau < NB_CRENEAUX; creneau++) {

                RendezVous rdv = planning[creneau][jour];

                // Match strict du numéro client
                if (rdv != null && rdv.getClient().getNumeroClient() == numeroClient) {

                    // Convertir créneau en heure
                    int heures = 10 + (creneau / 2);
                    int minutes = (creneau % 2) * 30;

                    // Convertir jour en date
                    LocalDate date = LocalDate.now().plusDays(jour);

                    // Afficher
                    System.out.println(
                        date + " " + String.format("%02d:%02d", heures, minutes) 
                        + " → " + rdv
                    );

                    trouve = true;
                }
            }
        }

        if (!trouve) {
            System.out.println("Aucun rendez-vous trouvé pour ce client.");
        }
    }
    
}