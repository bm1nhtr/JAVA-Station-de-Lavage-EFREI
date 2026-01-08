// Binh Minh TRAN - Marouane NOUARA
// ING1 - APP - BDML 

package model.Prestation;


public abstract class Prestation {
    
// Catégorie du véhicule concerné par la prestation.
// Elle permet d’identifier la taille du véhicule afin d’appliquer
// les bons tarifs lors du calcul du prix.
// Valeurs possibles :
//  - A : citadine
//  - B : berline
//  - C : monospace ou 4x4
// directement par les classes filles 
    protected String categorieVehicule; 
    
 //Constructeur de la classe Prestation
 //il initialise la catégorie du véhicule pour la prestation
 // Cette information sera utilisée par les méthodes de calcul
 // (lavage, séchage, prélavage) afin d’appliquer les majorations
 // correspondantes à la catégorie du véhicule.
    
    protected Prestation(String categorieVehicule) {
        this.categorieVehicule = categorieVehicule;
    }

    
   // Prix du lavage
   //Calcule le prix du lavage du véhicule en fonction de sa catégorie
   //Le tarif de base correspond à un véhicule de catégorie A (citadine)
   //Des majorations sont ensuite appliquées selon la catégorie du véhicule :
    
   //- catégorie B (berline) : majoration de 50 %
   // - catégorie C (monospace / 4x4) : majoration de 75 %
    
   //cette méthode est protégée afin d’être utilisée uniquement
   //par la classe Prestation et ses classes filles lors du calcul
   //du prix total d’une prestation.
    
    protected double lavage() {
        double prix = 20;  // Prix de base du lavage pour un véhicule de catégorie A
        // Application des majorations selon la catégorie du véhicule
        
        if (categorieVehicule.equals("B")) {
            prix *= 1.5; // +50 % pour une berline
        } else if (categorieVehicule.equals("C")) {
            prix *= 1.75; // +75 % pour un monospace ou 4x4
        }
        
        // Retour du prix final du lavage
        return prix;
    }

    
    // Prix du séchage
    
    //Même principe que pour lavage le tarif de base correspond à un véhicule de catégorie A (citadine)
    //Calcule le prix du séchage du véhicule en fonction de sa catégorie
    //- catégorie B (berline) : majoration de 5 %
    //- catégorie C (monospace / 4x4) : majoration de 10 %
 
    protected double sechage() {
        double prix = 10; // catégorie A

        if (categorieVehicule.equals("B")) {
            prix *= 1.05; // +5%
        } else if (categorieVehicule.equals("C")) {
            prix *= 1.10; // +10%
        }

        return prix;
    }

    
    // Prix du prélavage
    
    //Même principe que pour lavage le tarif de base correspond à un véhicule de catégorie A (citadine)
    //calcule le prix du séchage du véhicule en fonction de sa catégorie
    //- catégorie B (berline) : majoration de 50 %
    //- catégorie C (monospace / 4x4) : majoration de 75 %
    
    protected double prelavage() {
        double prix = 5; // catégorie A

        if (categorieVehicule.equals("B")) {
            prix *= 1.5; // +50%
        } else if (categorieVehicule.equals("C")) {
            prix *= 1.75; // +75%
        }

        return prix;
    }
    
    //Prix d'un nettoyage intérieure
    
    //calcule le prix du nettoyage de l’intérieur du véhicule (sièges et moquette) en fonction de la catégorie du véhicule
    //le tarif appliqué est le suivant :
    //- véhicules de catégorie A ou B : 30 €
    //- véhicules de catégorie C (monospace / 4x4) : 40 €
    
    //Cette méthode est placée dans la classe Prestation
    //car le calcul du prix du nettoyage intérieur est commun
    //à plusieurs types de prestations
         
    //Elle est déclarée protected afin de pouvoir être utilisée par les classes filles :
    //- PrestationExpress, où le nettoyage intérieur est optionnel
    //- PrestationSale, où le nettoyage intérieur est obligatoire
    
    protected double prixNettoyageInterieur(){
        
        if (categorieVehicule.equals("C")){
            return 40;}// si le véhicule est de catégorie C tarif est majoré
        
        return 30;// tarif standard pour les catégories A et B
    }

    
    // Prix total de la prestation
    
    public abstract double nettoyage();
    
    
    /**
    * Retourne une représentation textuelle d’une prestation.
    *
    * Cette méthode permet d’afficher de manière lisible
    * la catégorie du véhicule associée à la prestation.
    * Elle est utilisée automatiquement lorsque l’objet
    * Prestation est affiché (par exemple avec System.out.println).
    *
    * @return une description textuelle de la prestation
    */
    @Override
    public String toString() {
        return " [categorieVehicule=" + categorieVehicule + "]";
    }
}

