// Binh Minh TRAN - Marouane NOUARA
// ING1 - APP - BDML 

package model;

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

    // Setters
    public void setNumeroClient(int numeroClient) {
        this.numeroClient = numeroClient;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Compare deux clients pour déterminer l'ordre lexicographique.
     * 
     * Cette méthode retourne true si le client correspondant à l'objet appelant
     * doit être placé après l'autre client dans le tableau des clients,
     * false sinon.
     * 
     * La comparaison se fait d'abord sur le nom (ordre lexicographique),
     * puis sur le numéro de téléphone si les noms sont identiques.
     * 
     * @param autre l'autre client à comparer
     * @return true si ce client doit être placé après l'autre, false sinon
     */
    public boolean placerApres(Client autre) {
        // Comparaison lexicographique des noms
        int comparaisonNom = this.nom.compareToIgnoreCase(autre.nom);
        
        if (comparaisonNom > 0) {
            // Le nom de ce client est après celui de l'autre
            return true;
        } else if (comparaisonNom < 0) {
            // Le nom de ce client est avant celui de l'autre
            return false;
        } else {
            // Les noms sont identiques, on compare les numéros de téléphone
            return this.telephone.compareTo(autre.telephone) > 0;
        }
    }
    
    /**
     * Retourne une représentation textuelle du client.
     * 
     * @return une description du client
     */
    @Override
    public String toString() {
        String msg = "Client numéro " + numeroClient
                   + ", nom " + nom
                   + ", téléphone " + telephone;
        if (email != null && !email.isEmpty()) {
            msg += ", email " + email;
        }
        return msg;
    }

    /**
     * Retourne les informations du client sous forme de chaîne de caractères
     * pour l'écriture dans un fichier texte.
     * Format: "numéro : nom : téléphone" ou "numéro : nom : téléphone : email"
     * 
     * @return une chaîne de caractères formatée pour le fichier
     */
    public String versFichier() {
        if (email != null && !email.trim().isEmpty()) {
            return numeroClient + " : " + nom + " : " + telephone + " : " + email;
        } else {
            return numeroClient + " : " + nom + " : " + telephone;
        }
    }
}