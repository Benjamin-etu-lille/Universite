

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ventilateur {

    public static <T> List<T[]> ventilation(String filename) throws IOException {
        File file = new File(filename);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        T[] tokenizedLine;
        List<T[]> arr = new ArrayList<>();

        while ((line = bufferedReader.readLine()) != null) {
            tokenizedLine = (T[]) parse(line);
            arr.add(tokenizedLine);
        } 
        bufferedReader.close();
        return arr;
    }

    private static String[] parse(String line) {
        return line.split(";");
    }

    public static <T> String toString(List<T[]> arr) {
        StringBuilder sb = new StringBuilder();
        System.out.println("Liste des Ingrédients :\n");
        for (T[] array : arr) {
            sb.append(Arrays.toString(array)).append("\n");
        }
        return sb.toString();
    }

    public static void saveToCSV(String filename, List<String[]> data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String[] line : data) {
                writer.write(String.join(";", line));
                writer.newLine();
            }
        }
    }

}
