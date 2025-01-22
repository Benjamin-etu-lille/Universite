

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Menu {
    
    public Menu(){

    }

    public static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }  

    public static String premierEcran(){
        try{
            Menu.clearScreen();
            String content = Files.readString(Paths.get("res/premierecran.txt"));
            return content;
        }catch (IOException e){
            System.out.println("Le fichier n'a pas été trouvé");
            e.printStackTrace();
        }return("ERROR");
    }

    public static String menuUtilisateur(){
        try{
            Menu.clearScreen();
            String content = Files.readString(Paths.get("res/menuUtilisateur.txt"));
            return content;
        }catch (IOException e){
            System.out.println("Le fichier n'a pas été trouvé");
            e.printStackTrace();
        }return("ERROR");
    }

    public static String adminpannel(){
        try{
            Menu.clearScreen();
            String content = Files.readString(Paths.get("res/adminpanel.txt"));
            return content;
        }catch (IOException e){
            System.out.println("Le fichier n'a pas été trouvé");
            e.printStackTrace();
        }return("ERROR");
    }

    public static String mdpAdmin(){
        try{
            Menu.clearScreen();
            String content = Files.readString(Paths.get("res/mdpAdmin.txt"));
            return content;
        }catch (IOException e){
            System.out.println("Le fichier n'a pas été trouvé");
            e.printStackTrace();
        }return("ERROR");
    }

    public static String choixRecette(){
        try{
            Menu.clearScreen();
            String content = Files.readString(Paths.get("res/choixRecette.txt"));
            return content;
        }catch (IOException e){
            System.out.println("Le fichier n'a pas été trouvé");
            e.printStackTrace();
        }return("ERROR");
    }

    public static void main(String[] args){
        System.out.println(Menu.adminpannel());
    }
}



