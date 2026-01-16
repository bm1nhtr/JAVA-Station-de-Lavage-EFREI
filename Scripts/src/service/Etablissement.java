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
import java.time.format.DateTimeParseException;



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

        /**
     * Méthode planifier()
     * --------------------
     * Permet de créer un rendez-vous complet en interagissant avec l'utilisateur.
     * Étapes :
     *   1. Identification du client (recherche ou création automatique)
     *   2. Choix du jour parmi les 7 prochains
     *   3. Recherche du premier créneau disponible
     *   4. Choix du type de prestation (Express, Sale, Très Sale)
     *   5. Création du rendez-vous selon la prestation choisie
     *   6. Affichage du rendez-vous avec son prix
     *
     * La méthode inclut des protections avec try/catch pour éviter les saisies invalides.
     */
    public void planifier() {

        
        Scanner sc = new Scanner(System.in);

        System.out.println("\n===== PLANIFIER UN RENDEZ-VOUS =====");

        
        // 1) IDENTIFICATION DU CLIENT
        

        System.out.print("Nom du client : ");
        String nom = sc.nextLine();

        System.out.print("Téléphone du client : ");
        String telephone = sc.nextLine();

        // Recherche d'un client existant dans la base
        Client client = rechercher(nom, telephone);

        // Création automatique si le client n'existe pas
        if (client == null) {
            System.out.println("Nouveau client détecté. Ajout...");
            client = ajouter(nom, telephone);

            if (client == null) {
                System.out.println("Erreur : impossible d'ajouter le client.");
                return;
            }
        } else {
            System.out.println("Client trouvé : " + client);
        }

       
        // 2) CHOIX DU JOUR (1 à 7)
        

        LocalDate today = LocalDate.now();
        LocalDate[] jours = new LocalDate[NB_JOURS];

        System.out.println("\nChoisissez un jour :");

        // Affichage des 7 jours disponibles
        for (int i = 0; i < NB_JOURS; i++) {
            jours[i] = today.plusDays(i);
            System.out.println((i + 1) + ". " + jours[i]);
        }

        // Saisie sécurisée
        int choixJour = 0;
        while (true) {
            try {
                System.out.print("Votre choix (1-7) : ");
                choixJour = sc.nextInt();

                if (choixJour >= 1 && choixJour <= NB_JOURS) break;

                System.out.println(" Le jour doit être entre 1 et 7.");

            } catch (Exception e) {
                System.out.println(" Erreur : veuillez saisir un nombre.");
                sc.nextLine(); // vide le buffer
            }
        }

        LocalDate jourChoisi = jours[choixJour - 1];

        
        // 3) CRÉNEAU DISPONIBLE
        

        LocalDateTime dateHeure = rechercher(jourChoisi);

        if (dateHeure == null) {
            System.out.println(" Aucun créneau disponible ce jour-là.");
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

        int choixPrestation = 0;

        // Saisie sécurisée du type de prestation
        while (true) {
            try {
                System.out.print("Votre choix : ");
                choixPrestation = sc.nextInt();

                if (choixPrestation >= 1 && choixPrestation <= 3) break;

                System.out.println(" Choix invalide. Veuillez saisir 1, 2 ou 3.");

            } catch (Exception e) {
                System.out.println(" Erreur : veuillez saisir un nombre.");
                sc.nextLine();
            }
        }

        //sc.nextLine();

        // Saisie de la catégorie du véhicule
        String categorie = "";

        while (true) {
            System.out.print("Catégorie du véhicule (A/B/C) : ");
            categorie = sc.nextLine().trim().toUpperCase();

            if (categorie.equals("A") || categorie.equals("B") || categorie.equals("C")) {
                break;
            }

            System.out.println(" Catégorie invalide. Veuillez saisir A, B ou C.\n");
        }


        RendezVous rdv = null;

       
        // 5) CRÉATION DU RENDEZ-VOUS SELON LE TYPE DE PRESTATION
        

        switch (choixPrestation) {

            //  PRESTATION EXPRESS
            case 1 -> {
                System.out.print("Nettoyage intérieur (true/false) : ");

                boolean interieur;

                while (true) {
                    try {
                        interieur = sc.nextBoolean();
                        break;
                    } catch (Exception e) {
                        System.out.println(" Saisissez true ou false.");
                        sc.nextLine();
                    }
                }

                rdv = ajouter(client, date, heure, categorie, interieur);
            }

            //  PRESTATION SALE 
            case 2 -> {
                rdv = ajouter(client, date, heure, categorie);
            }

            //  PRESTATION TRÈS SALE 
            case 3 -> {
                System.out.println("""
                    Type de salissure :
                    1 - Nourriture
                    2 - Boue
                    3 - Transpiration
                    4 - Graisse
                    """);

                int typeSalissure = 0;

                // Saisie sécurisée
                while (true) {
                    try {
                        System.out.print("Votre choix : ");
                        typeSalissure = sc.nextInt();

                        if (typeSalissure >= 1 && typeSalissure <= 4) break;

                        System.out.println("Choix invalide. Saisir 1 à 4.");

                    } catch (Exception e) {
                        System.out.println(" Erreur : veuillez saisir un nombre.");
                        sc.nextLine();
                    }
                }

                rdv = ajouter(client, date, heure, categorie, typeSalissure);
            }
        }

        
        // 6) AFFICHAGE FINAL
        

        if (rdv == null) {
            System.out.println(" Le créneau était déjà occupé. Rendez-vous non enregistré.");
        } else {
            System.out.println("\n Rendez-vous enregistré !");
            System.out.println(rdv);
            System.out.println("Prix total : " + rdv.getPrix() + " €");
        }
    }


   

     /**
    * Méthode afficherClient()
    * -------------------------
    * Fonctionnalité globale :
    *     Permet d'effectuer une recherche de clients dans l’établissement à partir
    *     d’un nom et/ou d’un numéro de téléphone saisis par l’utilisateur.
    *     Affiche ensuite tous les clients correspondant aux critères fournis.
    *
    * Arguments :
    *     Aucun (la saisie se fait directement via la console).
    *
    * Valeur retournée :
    *     Aucune (type void). La méthode effectue uniquement un affichage.
     */

    public void afficherClient() {

        Scanner sc = new Scanner(System.in);

        System.out.println("\n=== Recherche de client ===");

        String nom = "";
        String telephone = "";

        // Boucle jusqu'à au moins un critère
        while (nom.isBlank() && telephone.isBlank()) {

            System.out.print("Entrez un nom (ou vide) : ");
            nom = sc.nextLine().trim();

            System.out.print("Entrez un téléphone (ou vide) : ");
            telephone = sc.nextLine().trim();

            if (nom.isBlank() && telephone.isBlank()) {
                System.out.println(" Vous devez saisir au moins un critère.\n");
            }
        }

        boolean trouve = false;
        System.out.println("\n--- Clients trouvés ---");

        for (int i = 0; i < nombre_clients; i++) {

            Client c = liste_clients[i];
            if (c == null) continue;

            // Correction : recherche flexible et robuste
            boolean matchNom =
                !nom.isBlank() &&
                c.getNom().toLowerCase().contains(nom.toLowerCase().trim());

            boolean matchTel =
                !telephone.isBlank() &&
                c.getTelephone().equals(telephone);

            // Nom seul
            if (!nom.isBlank() && telephone.isBlank()) {
                if (matchNom) {
                    System.out.println(" - " + c);
                    trouve = true;
                }
            }
            // Téléphone seul
            else if (nom.isBlank() && !telephone.isBlank()) {
                if (matchTel) {
                    System.out.println(" - " + c);
                    trouve = true;
                }
            }
            // Nom + téléphone
            else {
                if (matchNom && matchTel) {
                    System.out.println(" - " + c);
                    trouve = true;
                }
            }
        }

        if (!trouve) {
            System.out.println("Aucun client ne correspond à la recherche.");
        }
    }

    
    
    
    /**
    * Affiche le planning complet d'un jour donné.
    * L'utilisateur saisit une date au format YYYY-MM-DD.
    * La méthode vérifie que la date appartient aux 7 jours gérés,
    * puis affiche tous les créneaux du jour, avec rendez-vous ou [Libre].
    */
   public void afficherPlanning() {

       
       Scanner sc = new Scanner(System.in);

       LocalDate date = null;      // La date saisie par l'utilisateur
       int index = -1;             // Index du jour dans le planning
       LocalDate today = LocalDate.now();

       
       // 1) DEMANDE ET VALIDATION DE LA DATE
       

       while (index == -1) {

           System.out.print("Entrez une date (YYYY-MM-DD) : ");
           String saisie = sc.nextLine();

           try {
               // Tentative de conversion de la saisie en LocalDate
               date = LocalDate.parse(saisie);

               // Calcul de la différence en jours
               long diff = ChronoUnit.DAYS.between(today, date);

               if (diff >= 0 && diff < NB_JOURS) {
                   index = (int) diff;   // Date valide : sortir de la boucle
               } else {
                   System.out.println(" La date doit être dans les 7 prochains jours.\n");
               }

           } catch (DateTimeParseException e) {
               // L'utilisateur a saisi "2022-2-3", "2025-13-50", "abc", etc.
               System.out.println(" Format invalide. Utilisez YYYY-MM-DD (ex: 2024-08-12).\n");
           }
       }

       
       // 2) AFFICHAGE DU PLANNING DU JOUR
       

       System.out.println("\n=== PLANNING DU " + date + " ===");

       for (int c = 0; c < NB_CRENEAUX; c++) {

           // Calcul de l'heure du créneau
           int h = 10 + (c / 2);       // Chaque 2 créneaux = +1 heure
           int m = (c % 2) * 30;       // 0 ou 30

           String horaire = String.format("%02d:%02d", h, m);

           RendezVous rdv = planning[c][index];

           if (rdv == null) {
               System.out.println(horaire + " : [Libre]");
           } else {
               System.out.println(horaire + " : " + rdv);
           }
       }
   }



    
    /**
    * Affiche tous les rendez-vous appartenant à un client donné.
    *
    * Fonctionnalité :
    *     L'utilisateur saisit un numéro client, et la méthode parcourt l'ensemble
    *     du planning pour afficher tous les rendez-vous associés à ce numéro.
    *
    * Argument :
    *     Aucun (la saisie se fait via la console).
    *
    * Valeur retournée :
    *     Aucune (type void). La méthode effectue uniquement un affichage.
    *
    * Notes :
    *     - Cette méthode corrige un bug où certains numéros clients n'étaient pas
    *       trouvés à cause du typage de la saisie.
    */
   public void afficherRendezVousParNumeroClient() {

       
       Scanner sc = new Scanner(System.in);

       System.out.println("\n=== Recherche des rendez-vous ===");

       int numeroClient = -1;

       
       // 1) Saisie sécurisée du numéro client 
       
       while (true) {
           try {
               System.out.print("Numéro du client : ");
               numeroClient = Integer.parseInt(sc.nextLine().trim());
               break; // OK
           }
           catch (NumberFormatException e) {
               System.out.println(" Erreur : veuillez saisir un nombre valide.\n");
           }
       }

       boolean trouve = false;

       
       // 2) Parcours du planning (7 jours × NB_CRENEAUX)
       
       for (int jour = 0; jour < NB_JOURS; jour++) {

           for (int creneau = 0; creneau < NB_CRENEAUX; creneau++) {

               RendezVous rdv = planning[creneau][jour];

               // Vérifie que le créneau est occupé ET appartient au client recherché
               if (rdv != null && rdv.getClient().getNumeroClient() == numeroClient) {

                   // Convertit le créneau en heure (10h00 → 18h00, toutes les 30 min)
                   int heures = 10 + (creneau / 2);
                   int minutes = (creneau % 2) * 30;

                   // Convertit l'indice du jour en date réelle
                   LocalDate date = LocalDate.now().plusDays(jour);

                   // Affichage du rendez-vous trouvé
                   System.out.println(
                       date + " " + String.format("%02d:%02d", heures, minutes)
                       + " → " + rdv
                   );

                   trouve = true;
               }
           }
       }

       
       // 3) Aucun rendez-vous trouvé
       
       if (!trouve) {
           System.out.println("Aucun rendez-vous trouvé pour ce client.");
       }
   }

}