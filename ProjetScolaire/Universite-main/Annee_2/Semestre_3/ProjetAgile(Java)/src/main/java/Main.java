import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String CLEAR_TERMINAL = "\033\143";

    private static Scanner inputReader = new Scanner(System.in); // Scanner partagé pour l'ensemble du programme

    // Méthode pour afficher un menu et obtenir une sélection de l'utilisateur
    public static int selection(int nbChoix, String menu) {
        try {
            System.out.println(menu);
            StringBuilder message = new StringBuilder("Sélectionnez une option (1");
            for (int i = 2; i <= nbChoix; i++) {
                message.append(" ou ").append(i);
            }
            message.append("): ");
            System.out.println(message);

            inputReader.hasNext();
            int choix = inputReader.nextInt();
            while (choix < 1 || choix > nbChoix) {
                System.out.println(ANSI_RED + "Sélection invalide !" + ANSI_RESET);
                System.out.print("Veuillez choisir une option valide (1-" + nbChoix + "): ");
                choix = inputReader.nextInt();
            }
            return choix;
        } catch (InputMismatchException e) {
            System.out.println(ANSI_RED + "Entrée invalide. Veuillez entrer un nombre." + ANSI_RESET);
            inputReader.next(); // Consomme l'entrée incorrecte pour éviter une boucle infinie
            return -1;
        }
    }

    // Méthode pour afficher le menu utilisateur avec un switch simple
    public static void menuUtilisateur(AjouterIngredient ajout) throws IOException {
        boolean continuer = true; // Boucle pour retourner au menu utilisateur
        while (continuer) {
            int choixUtilisateur = selection(3, Menu.menuUtilisateur());

            switch (choixUtilisateur) {
                case 1 -> {
                    menuChoixRecette(ajout);
                    
                }
                case 2 -> {
                    
                }
                case 3 -> {
                    continuer = false; // Quitter la boucle et retourner au menu principal
                    System.out.println("Retour au menu principal.");
                }
                default -> System.out.println(ANSI_RED + "Choix non valide !" + ANSI_RESET);
            }
        }
    }

    // Méthode pour afficher le menu administrateur
    public static void menuAdmin() {
        boolean continuer = true;
        while (continuer) {
            System.out.println(CLEAR_TERMINAL);
            System.out.println(Menu.mdpAdmin());

            String mdpAdmin = inputReader.next();
            while (!Admin.correctMdp(mdpAdmin)) {
                System.out.println(CLEAR_TERMINAL);
                System.out.println(Menu.mdpAdmin());
                System.out.println(ANSI_RED + "Mauvais mot de passe !" + ANSI_WHITE);
                mdpAdmin = inputReader.next();
            }

            int choixAdmin = selection(4, Menu.adminpannel());

            switch (choixAdmin) {
                case 1 -> System.out.println("Gestion des utilisateurs...");
                case 2 -> System.out.println("Gestion des stocks...");
                case 3 -> {
                    continuer = false; // Quitter la boucle et retourner au menu principal
                    System.out.println("Retour au menu principal.");
                }
                case 4 -> continuer = false;
                default -> System.out.println(ANSI_RED + "Choix non valide !" + ANSI_RESET);
            }
        }
    }

    public static void menuChoixRecette(AjouterIngredient ajout) throws IOException{
        boolean continuer = true;
        while (continuer){
            int choixUtilisateur = selection(4, Menu.choixRecette());

            switch (choixUtilisateur) {
                case 1 -> {
                    System.out.println(CLEAR_TERMINAL);
                    ajout.ajouterIngredient();
                    ajout.saveIngredient();
                }
                case 2 -> {
                    afficherLesIngredients();
                }
                case 3 -> {
                    afficherRcettesDispo();
                }
                case 4 -> {
                    continuer = false; // Quitter la boucle et retourner au menu principal
                    System.out.println("Retour au menu principal.");
                }
                default -> System.out.println(ANSI_RED + "Choix non valide !" + ANSI_RESET);
            }
        }
    }

    public static void afficherLesIngredients() throws IOException {
        boolean continuer = true;
        while (continuer){
            System.out.println(CLEAR_TERMINAL);
            AjouterIngredient.afficherIngredients();
            int choixUtilisateur = selection(1, "1 : retour");
            if(choixUtilisateur == 1) {
                continuer = false;
                System.out.println("Retour au précèdent");
            }
        }
    }
    public static void afficherRcettesDispo() throws IOException {
        boolean continuer = true;
        while (continuer){
            System.out.println(CLEAR_TERMINAL);
            Recette<String> recette = new Recette<>();
            recette.chercherRecette();
            ChargerRecette chargerRecette = new ChargerRecette();
            chargerRecette.chargerDonnee();
            recette.chercherRecette();
            int choixUtilisateur = selection(1, "1 : retour");
            if(choixUtilisateur == 1) {
                continuer = false;
                System.out.println("Retour au précèdent");
            }
        }
    }

    public static void main(String[] args) {
        try {
            AjouterIngredient ajout = new AjouterIngredient(); // Gestion des ingrédients

            boolean continuer = true;
            while (continuer) {
                // Menu principal avec un switch
                int choixPrincipal = selection(3, Menu.premierEcran());

                switch (choixPrincipal) {
                    case 1 -> menuUtilisateur(ajout); // Sous-menu utilisateur
                    case 2 -> menuAdmin(); // Sous-menu administrateur
                    case 3 -> {
                        continuer = false; // Quitter le programme
                        System.out.println("Fermeture du programme.");
                    }
                    default -> System.out.println(ANSI_RED + "Choix non valide !" + ANSI_RESET);
                }
            }
        } catch (InputMismatchException | IOException e) {
            System.out.println(ANSI_RED + "Une erreur est survenue : " + e.getMessage() + ANSI_RESET);
            e.printStackTrace();
        } finally {
            inputReader.close(); // Fermeture du scanner en fin de programme
        }
    }
}

