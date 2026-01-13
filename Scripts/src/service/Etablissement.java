// Binh Minh TRAN - Marouane NOUARA
// ING1 - APP - BDML 

package service;

import model.Client;
import model.RendezVous;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;


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
        LocalDate aujourdhui = LocalDate.now();
        long joursEcoules = aujourdhui.until(jour, ChronoUnit.DAYS);
        
        // Vérifier que la date est dans les 7 jours suivants
        if (joursEcoules < 0 || joursEcoules >= NB_JOURS) {
            System.out.println("La date doit être dans les 7 jours suivants.");
            return null;
        }
        
        int indiceJour = (int)joursEcoules;
        
        // Afficher les heures disponibles pour ce jour
        System.out.println("Créneaux disponibles pour le " + jour + " :");
        int[] heuresDisponibles = new int[NB_CRENEAUX];
        int nbDisponibles = 0;
        
        for (int creneau = 0; creneau < NB_CRENEAUX; creneau++) {
            if (planning[creneau][indiceJour] == null) {
                // Créneau libre
                int heures = 10 + (creneau / 2);
                int minutes = (creneau % 2) * 30;
                heuresDisponibles[nbDisponibles] = creneau;
                System.out.println((nbDisponibles + 1) + ". " + 
                    String.format("%02d:%02d", heures, minutes));
                nbDisponibles++;
            }
        }
        
        if (nbDisponibles == 0) {
            System.out.println("Aucun créneau disponible pour ce jour.");
            return null;
        }
        
        // Faire choisir une heure au client
        Scanner scanner = new Scanner(System.in);
        System.out.print("Choisissez un créneau (1-" + nbDisponibles + ") : ");
        int choix = scanner.nextInt();
        
        if (choix < 1 || choix > nbDisponibles) {
            System.out.println("Choix invalide.");
            return null;
        }
        
        // Retourner le créneau choisi
        int creneauChoisi = heuresDisponibles[choix - 1];
        int heures = 10 + (creneauChoisi / 2);
        int minutes = (creneauChoisi % 2) * 30;
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
        model.Prestation.PrestationTresSale prestation = 
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
    
    public void planifier() {
      Scanner sc = new Scanner(System.in);

      System.out.println("\n===== PLANIFIER UN RENDEZ-VOUS =====");

      // 1) Identification du client
      System.out.print("Nom du client : ");
      String nom = sc.nextLine();

      System.out.print("Téléphone du client : ");
      String telephone = sc.nextLine();

      // Recherche client
      Client client = rechercher(nom, telephone);

      // Si nouveau client → ajouter automatiquement avec numéro auto
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

      // 2) Choix du jour (dans les 7 jours)
      System.out.println("\nChoisissez un jour dans les 7 prochains :");
      LocalDate today = LocalDate.now();
      LocalDate[] jours = new LocalDate[7];

      for (int i = 0; i < 7; i++) {
          jours[i] = today.plusDays(i);
          System.out.println((i + 1) + ". " + jours[i]);
      }

      int choixJour;
      do {
          System.out.print("Votre choix (1-7) : ");
          choixJour = sc.nextInt();
      } while (choixJour < 1 || choixJour > 7);

      LocalDate jourChoisi = jours[choixJour - 1];

      // 3) Choix de l'heure via ta méthode rechercher(LocalDate)
      LocalDateTime dateHeure = rechercher(jourChoisi);

      if (dateHeure == null) {
          System.out.println("Impossible de planifier : aucun créneau disponible.");
          return;
      }

      LocalDate date = dateHeure.toLocalDate();
      LocalTime heure = dateHeure.toLocalTime();

      // 4) Type de prestation
      System.out.println("\nType de prestation :");
      System.out.println("1 - Prestation Express");
      System.out.println("2 - Prestation Sale");
      System.out.println("3 - Prestation Très Sale");
      System.out.print("Votre choix : ");

      int choixPrestation = sc.nextInt();
      sc.nextLine();

      // Saisie catégorie véhicule
      System.out.print("Catégorie du véhicule (A/B/C) : ");
      String categorie = sc.nextLine();

      RendezVous rdv = null;

      switch (choixPrestation) {
          case 1 -> {
              System.out.print("Nettoyage intérieur (true/false) : ");
              boolean interieur = sc.nextBoolean();
              rdv = ajouter(client, date, heure, categorie, interieur);
          }

          case 2 -> {
              rdv = ajouter(client, date, heure, categorie);
          }

          case 3 -> {
              System.out.println("Type de salissure :");
              System.out.println("1 - Nourriture");
              System.out.println("2 - Boue");
              System.out.println("3 - Transpiration");
              System.out.println("4 - Graisse");
              System.out.print("Votre choix : ");
              int typeSalissure = sc.nextInt();

              rdv = ajouter(client, date, heure, categorie, typeSalissure);
          }

          default -> {
              System.out.println("Choix invalide.");
              return;
          }
      }

      // 5) Validation
      if (rdv == null) {
          System.out.println("Échec de l'ajout du rendez-vous (créneau occupé).");
      } else {
          System.out.println("\nRendez-vous enregistré !");
          System.out.println(rdv);
          System.out.println("Prix total : " + rdv.getPrix() + " €");
      }
  }
    
  /**
 * Affiche le planning des rendez-vous pour un jour donné.
 *
 * @param jour la date à afficher (doit être dans les 7 prochains jours)
 */
    public void afficher(LocalDate jour) {

        LocalDate aujourdHui = LocalDate.now();
        long indexJour = ChronoUnit.DAYS.between(aujourdHui, jour);

        if (indexJour < 0 || indexJour >= NB_JOURS) {
            System.out.println("La date doit être dans les 7 jours suivants.");
            return;
        }

        int j = (int) indexJour;

        System.out.println("\n=== PLANNING DU " + jour + " ===");

        for (int c = 0; c < NB_CRENEAUX; c++) {

            int h = 10 + (c / 2);
            int m = (c % 2) * 30;
            String horaire = String.format("%02d:%02d", h, m);

            RendezVous rdv = planning[c][j];

            if (rdv == null) {
                System.out.println(horaire + " : [Libre]");
            } else {
                System.out.println(horaire + " : " + rdv);
            }
        }
    }
    
        /**
     * Affiche les clients dont le nom OU le téléphone correspond
     * à la recherche.
     *
     * @param nom le nom recherché (peut être vide)
     * @param telephone le téléphone recherché (peut être vide)
     */
    public void afficher(String nom, String telephone) {

        System.out.println("\n=== CLIENTS CORRESPONDANTS ===");

        boolean trouve = false;

        for (int i = 0; i < nombre_clients; i++) {
            Client c = liste_clients[i];

            if (c == null) continue;

            boolean matchNom = !nom.isEmpty() &&
                               c.getNom().equalsIgnoreCase(nom);

            boolean matchTel = !telephone.isEmpty() &&
                               c.getTelephone().equals(telephone);

            if (matchNom || matchTel) {
                System.out.println(" - " + c);
                trouve = true;
            }
        }

        if (!trouve) {
            System.out.println("Aucun client trouvé.");
        }
    }
    
    
        /**
     * Affiche les rendez-vous pris par un client donné
     * grâce à son numéro client.
     *
     * @param numeroClient le numéro du client recherché
     */
    public void afficher(int numeroClient) {

        System.out.println("\n=== RENDEZ-VOUS DU CLIENT n°" + numeroClient + " ===");

        boolean trouve = false;

        for (int j = 0; j < NB_JOURS; j++) {
            for (int c = 0; c < NB_CRENEAUX; c++) {

                RendezVous rdv = planning[c][j];
                if (rdv == null) continue;

                if (rdv.getClient().getNumeroClient() == numeroClient) {

                    int h = 10 + (c / 2);
                    int m = (c % 2) * 30;
                    LocalDate date = LocalDate.now().plusDays(j);

                    System.out.println(
                        date + " " +
                        String.format("%02d:%02d", h, m) +
                        " → " + rdv
                    );
                    trouve = true;
                }
            }
        }

        if (!trouve) {
            System.out.println("Aucun rendez-vous trouvé pour ce client.");
        }
    }
    
        /**
     * Vérifie si une date appartient aux 7 jours du planning.
     * @return l'indice du jour (0 à 6) ou -1 si hors tableau
     */
    public int obtenirIndiceJour(LocalDate date) {

        LocalDate today = LocalDate.now();

        for (int j = 0; j < NB_JOURS; j++) {
            if (date.equals(today.plusDays(j))) {
                return j; // date trouvée → renvoie l'indice
            }
        }

        return -1; // date introuvable → pas dans le planning
    }
    
    /**
 * Demande à l'utilisateur un nom ou un téléphone
 * et affiche le(s) client(s) correspondant(s).
 */
    public void afficherClientDepuisConsole() {

        Scanner sc = new Scanner(System.in);

        System.out.println("\n=== Recherche de client ===");

        System.out.print("Entrez un nom (ou laissez vide pour num) : ");
        String nom = sc.nextLine().trim();

        System.out.print("Entrez un téléphone : ");
        String telephone = sc.nextLine().trim();

        if (nom.isBlank() && telephone.isBlank()) {
            System.out.println(" Vous devez saisir un nom OU un téléphone.");
            return;
        }

        boolean trouve = false;
        System.out.println("\n--- Clients trouvés ---");

        for (int i = 0; i < nombre_clients; i++) {
            Client c = liste_clients[i];
            if (c == null) continue;

            boolean matchNom = !nom.isBlank() && c.getNom().equalsIgnoreCase(nom);
            boolean matchTel = !telephone.isBlank() && c.getTelephone().equals(telephone);

            if (matchNom || matchTel) {
                System.out.println(" - " + c);
                trouve = true;
            }
        }

        if (!trouve) {
            System.out.println("Aucun client ne correspond à votre recherche.");
        }
    }
    
    public void afficherRendezVousParNumeroClient() {

        Scanner sc = new Scanner(System.in);

        System.out.println("\n=== Recherche des rendez-vous ===");
        System.out.print("Numéro du client : ");
        int numeroClient = sc.nextInt();

        boolean trouve = false;

        for (int jour = 0; jour < NB_JOURS; jour++) {
            for (int creneau = 0; creneau < NB_CRENEAUX; creneau++) {

                RendezVous rdv = planning[creneau][jour];

                if (rdv != null && rdv.getClient().getNumeroClient() == numeroClient) {

                    // EXACTEMENT la même logique que rechercher() et afficher(LocalDate)
                    int heures = 10 + (creneau / 2);
                    int minutes = (creneau % 2) * 30;

                    LocalDate date = LocalDate.now().plusDays(jour);

                    System.out.println(
                        date + " " + String.format("%02d:%02d", heures, minutes)
                        + " → " + rdv
                    );

                    trouve = true;
                }
            }
        }

        if (!trouve) {
            System.out.println("Aucun rendez-vous trouvé.");
        }
    }
    
    public void versFichierClients(String nomFichier) {

        try (FileWriter fw = new FileWriter(nomFichier, false)) {

            for (int i = 0; i < nombre_clients; i++) {
                Client c = liste_clients[i];
                if (c != null) {
                    fw.write(c.versFichier() + System.lineSeparator());
                }
            }

            System.out.println("Clients sauvegardés dans " + nomFichier);

        } catch (Exception e) {
            System.out.println("Erreur lors de la sauvegarde des clients : " + e.getMessage());
        }
    }
    
    
        public void depuisFichierClients(String nomFichier) {

        try (BufferedReader br = new BufferedReader(new FileReader(nomFichier))) {

            String ligne;
            nombre_clients = 0;  // on vide l'ancien contenu
            prochainNumeroClient = 1;

            while ((ligne = br.readLine()) != null) {

                String[] t = ligne.split(" : ");

                if (t.length == 3) {
                    // numéro, nom, téléphone
                    int num = Integer.parseInt(t[0].trim());
                    String nom = t[1].trim();
                    String tel = t[2].trim();

                    liste_clients[nombre_clients++] = new Client(num, nom, tel);
                    prochainNumeroClient = Math.max(prochainNumeroClient, num + 1);
                }
                else if (t.length == 4) {
                    // numéro, nom, téléphone, email
                    int num = Integer.parseInt(t[0].trim());
                    String nom = t[1].trim();
                    String tel = t[2].trim();
                    String email = t[3].trim();

                    liste_clients[nombre_clients++] = new Client(num, nom, tel, email);
                    prochainNumeroClient = Math.max(prochainNumeroClient, num + 1);
                }
            }

            System.out.println("Clients chargés depuis " + nomFichier);

        } catch (Exception e) {
            System.out.println("Erreur lors du chargement des clients : " + e.getMessage());
        }
    }

    
}
