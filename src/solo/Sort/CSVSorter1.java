package solo.Sort;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.*;
import java.util.Collections;


public class CSVSorter1 {
    private ArrayList<String> readCSV(String filename) {
        String line = "";
        String cvsSplitBy = ",";
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            while ((line = br.readLine()) != null) {
                // use comma as separator
            	if (line.endsWith(",")) {
            		line = line.substring(0, (line.length()-1));
            	}
                lines.add(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    private void writeToCSV(ArrayList<String> lines, String fileName) {
        try {
            FileWriter csvWriter = new FileWriter(fileName);
            for (String l : lines) {
                csvWriter.append(l);
                csvWriter.append("\n");
            }
            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void run(String from, String to){
        ArrayList<String> lines = readCSV(from);
        ArrayList<Integer> numbers = new ArrayList<>();
        for (String line: lines) {
            numbers.add(Integer.parseInt(line));
        }
        boolean fin = false;
        int val;
        while (fin == false){
            fin = true;
            for (int i = 1; i<numbers.size()-1; i++) { //  changed i = 0 to i = 1
                if (numbers.get(i) > numbers.get(i+1)){
                    fin = false;
                    val = numbers.get(i+1);
                    numbers.set(i+1,numbers.get(i));
                    numbers.set(i,val);
                }
            }
        }
        lines.clear();
        for (int num: numbers) {
            lines.add(Integer.toString(num)+",");
        }
        writeToCSV(lines, to);
    }


    public static void main(String[] args) {
        String from = args[0];
        String to = args[1];
        CSVSorter1 csvSorter = new CSVSorter1();
        csvSorter.run(from, to);
    }
}