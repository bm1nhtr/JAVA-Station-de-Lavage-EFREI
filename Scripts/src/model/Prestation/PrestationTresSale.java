// Binh Minh TRAN - Marouane NOUARA
// ING1 - APP - BDML 

package model.Prestation;

/**
 * Représente une prestation pour véhicule très sale.
 *
 * Cette classe hérite de {@code PrestationSale} et reprend
 * l’ensemble des phases de nettoyage associées à un véhicule sale
 * (prélavage, lavage, séchage et nettoyage intérieur).
 *
 * Elle ajoute un surcoût supplémentaire en fonction du type
 * de salissure à traiter (boue, nourriture, graisse, etc.),
 * conformément aux règles de tarification de l’énoncé.
 */
public class PrestationTresSale extends PrestationSale {

    // Type de salissure à nettoyer :
    // 1 : taches de nourriture
    // 2 : taches de boue
    // 3 : taches de transpiration
    // 4 : taches de graisse
    private final int typeSalissure;

    /**
     * Constructeur de la classe PrestationTresSale.
     * Il initialise la catégorie du véhicule ainsi que
     * le type de salissure à traiter.
     *
     * La catégorie du véhicule est transmise à la classe mère
     * afin de permettre le calcul des différents coûts de base.
     *
     * @param categorieVehicule catégorie du véhicule ("A", "B" ou "C")
     * @param typeSalissure type de salissure à nettoyer
     */
    public PrestationTresSale(String categorieVehicule, int typeSalissure) {
        super(categorieVehicule);
        this.typeSalissure = typeSalissure;
    }

    /**
     * Calcule le prix total d’une prestation pour véhicule très sale.
     *
     * Le calcul commence par le prix d’une prestation pour véhicule sale,
     * obtenu grâce à l’appel à la méthode {@code super.nettoyage()}.
     *
     * Un surcoût est ensuite ajouté en fonction du type de salissure,
     * afin de prendre en compte l’utilisation de produits spécifiques
     * et un temps de nettoyage plus important.
     *
     * @return le prix total de la prestation pour véhicule très sale
     */
    @Override
    public double nettoyage() {

        // Prix de base correspondant à une prestation véhicule sale
        double total = super.nettoyage();

        // Ajout du surcoût selon le type de salissure
        switch (typeSalissure) {
            case 1 -> total += 10;
            // taches de nourriture
            case 2 -> total += 15;
            // taches de boue
            case 3 -> total += 12;
            // taches de transpiration
            case 4 -> total += 20;
            // taches de graisse
        }

        // Retour du prix total de la prestation très sale
        return total;
    }
    
    
    /**
    * Retourne une représentation textuelle d’une prestation
    * pour véhicule très sale.
    *
    * En plus de la catégorie du véhicule, l’affichage
    * précise le type de salissure à traiter.
    *
    * @return une description textuelle de la prestation pour véhicule très sale
    */
    @Override
    
    public String toString() {
        return "Prestation Tres Sale [categorie Vehicule ="  + categorieVehicule +
            ", type de salissure = " + libelleTypeSalissure() + ", prix = "+ nettoyage() + " euro]";
    }
    
    /**
     * Retourne le type de salissure.
     * 
     * @return le type de salissure (1, 2, 3 ou 4)
     */
    public int getTypeSalissure() {
        return typeSalissure;
    }
    
    /**
     * Retourne les informations de la prestation très sale sous forme de chaîne
     * de caractères pour l'écriture dans un fichier texte.
     * Format: "catégorie : typeSalissure : prix"
     * 
     * @return une chaîne de caractères formatée pour le fichier
     */
    @Override
    public String versFichier() {
        return categorieVehicule + " : " + typeSalissure + " : " + (int)nettoyage();
    }
    
    /**
    * Retourne le libellé correspondant au type de salissure.
    *
    * @return le type de salissure sous forme textuelle
    */
    private String libelleTypeSalissure() {
        return switch (typeSalissure) {
            case 1 -> "taches de nourriture";
            case 2 -> "taches de boue";
            case 3 -> "taches de transpiration";
            case 4 -> "taches de graisse";
            default -> "salissure inconnue";
        };
    }
    
//    @Override
//    public String versFichier() {
//        return categorieVehicule 
//                + " : " + typeSalissure 
//                + " : " + nettoyage();
//    }


}
