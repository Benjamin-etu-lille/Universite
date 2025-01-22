package view;

import java.util.function.Supplier;

public class Timer {
    // Interface fonctionnelle pour une méthode sans paramètres et sans retour
    @FunctionalInterface
    public interface MethodToTime {
        void execute();
    }

    private static boolean timingEnabled = true; // Par défaut, le timing est activé

    // Méthode pour définir l'état du timing
    public static void setTimingEnabled(boolean enabled) {
        timingEnabled = enabled;
    }

    // Méthode pour mesurer le temps d'exécution d'une méthode sans retour
    public static void measureExecutionTime(MethodToTime method, String methodName) {
        if (!timingEnabled) {
            method.execute(); // Exécute la méthode sans mesurer le temps
            return;
        }

        long startTime = System.nanoTime();  // Démarre le timer
        method.execute();  // Exécute la méthode
        long endTime = System.nanoTime();  // Arrête le timer
        long duration = endTime - startTime;  // Calcul du temps en nanosecondes

        // Conversion de la durée en millisecondes
        double durationInMillis = duration / 1_000_000.0; // Diviser par 1 000 000 pour obtenir les millisecondes
        System.out.println("La méthode " + methodName + " a pris " + durationInMillis + " millisecondes.");
    }

    // Méthode pour mesurer le temps d'exécution d'une méthode qui retourne une valeur
    public static <T> T measureExecutionTime(Supplier<T> supplier, String methodName) {
        if (!timingEnabled) {
            return supplier.get(); // Appel de la méthode sans mesurer le temps
        }

        long startTime = System.currentTimeMillis(); // Démarre le timer
        T result = supplier.get(); // Appel de la méthode
        long endTime = System.currentTimeMillis(); // Arrête le timer

        System.out.println(methodName + " a pris " + (endTime - startTime) + " millisecondes");
        return result; // Retourne le résultat de la méthode
    }
}
