// Binh Minh TRAN - Marouane NOUARA
// ING1 - APP - BDML 

package model.Prestation;

public class PrestationSale extends Prestation {
       
    //Constructeur
    public PrestationSale(String categorieVehicule) {
        //initialise la catégorie du véhicule en appelant le constructeur 
        //de la classe mère Prestation
        super(categorieVehicule);
    }

    
    //Calcule le prix total d’une prestation pour véhicule sale
    //Cette prestation comprend obligatoirement les phases suivantes :
    //- un prélavage
    //- un lavage
    //- un séchage
    //- un nettoyage de l’intérieur du véhicule
    
    //Le prix total est obtenu en additionnant le coût de chacune de ces phases
    //conformément aux règles de tarification définies dans l’énoncé
    
    //Cette méthode redéfinit la méthode abstraite nettoyage de la classe Prestation
    @Override
    public double nettoyage() {
        // Somme des différentes phases de nettoyage incluses
        return prelavage() + lavage()  + sechage()+ prixNettoyageInterieur();// méthode héritée de Prestation
    }

    
    
    /**
    * Retourne une représentation textuelle d’une prestation
    * pour véhicule sale.
    *
    * L’affichage indique uniquement la catégorie du véhicule,
    * les phases incluses étant implicites pour ce type de prestation.
    *
    * @return une description textuelle de la prestation pour véhicule sale
    */
    /**
     * Retourne les informations de la prestation sale sous forme de chaîne
     * de caractères pour l'écriture dans un fichier texte.
     * Format: "catégorie : prix"
     * 
     * @return une chaîne de caractères formatée pour le fichier
     */
    @Override
    public String versFichier() {
        return categorieVehicule + " : " + (int)nettoyage();
    }
    
    @Override
    
    public String toString() {
        return "Prestation Sale : categorie Vehicule = " + categorieVehicule +", prix = "+ nettoyage() + " euro";
    }
    
//    @Override
//    public String versFichier() {
//        return categorieVehicule 
//                + " : " + nettoyage();
//    }


    
}

