// Binh Minh TRAN - Marouane NOUARA
// ING1 - APP - BDML 

package model.Prestation;


public class PrestationExpress extends Prestation {
    
    // Indique si le nettoyage de l’intérieur du véhicule (sièges et moquette) est inclus dans la prestation express
    // Dans le cas d’une prestation express ce nettoyage est optionnel et dépend du choix du client.
    private final boolean nettoyageInterieur;
    
    //Constructeur il initialise la catégorie du véhicule ainsi que le choix du client concernant le nettoyage de l’intérieur
    public PrestationExpress(String categorieVehicule, boolean nettoyageInterieur) {
        
        super(categorieVehicule);//La catégorie du véhicule est transmise à la classe mère afin de permettre le calcul des différents coûts
        this.nettoyageInterieur = nettoyageInterieur;//true si le client souhaite le nettoyage de l’intérieur du véhicule, false sinon
    }
    
    
    //calcule le prix total d’une prestation express 
    //qui a obligatoirementlavage + sechage
    //le nettoyage de l’intérieur du véhicule est optionnel
    //il est ajouté au prix total uniquement si le client a choisi cette option
    //cette méthode redéfinit la méthode abstraite nettoyage de la classe Prestation
    
    @Override
    public double nettoyage() {
        // Calcul du prix de base : lavage + séchage
        double total = lavage() + sechage();

        // Ajout du nettoyage intérieur si l’option a été choisie (si nettoyageInterieur = true)
        if (nettoyageInterieur) {
            total += prixNettoyageInterieur();// méthode héritée de Prestation
        }
        
        // Retour du prix total de la prestation express
        return total;
    }
    
    
    /**
    * Retourne une représentation textuelle d’une prestation express.
    *
    * L’affichage inclut :
    *  - la catégorie du véhicule
    *  - l’information indiquant si le nettoyage intérieur est inclus
    *
    * Cette méthode permet d’obtenir un affichage clair
    * lors de l’affichage d’un rendez-vous ou lors des tests.
    *
    * @return une description textuelle de la prestation express
    */
    /**
     * Retourne si le nettoyage intérieur est inclus.
     * 
     * @return true si le nettoyage intérieur est inclus, false sinon
     */
    public boolean getNettoyageInterieur() {
        return nettoyageInterieur;
    }
    
    /**
     * Retourne les informations de la prestation express sous forme de chaîne
     * de caractères pour l'écriture dans un fichier texte.
     * Format: "catégorie : true/false : prix"
     * 
     * @return une chaîne de caractères formatée pour le fichier
     */
    @Override
    public String versFichier() {
        return categorieVehicule + " : " + nettoyageInterieur + " : " + (int)nettoyage();
    }
    
    @Override
    
    public String toString() {
        
        String interieur;
        if(nettoyageInterieur) {interieur=" Nettoyage interieure";}
        else{interieur=" Pas de nettoyage interieure";}
        
        return "Prestation Express : categorie Vehicule = " + categorieVehicule + ", "+
                interieur + ", prix = " + nettoyage() + " euro";
    }
    
//    @Override
//    public String versFichier() {
//        return categorieVehicule 
//                + " : " + nettoyageInterieur 
//                + " : " + nettoyage();
//    }



}

