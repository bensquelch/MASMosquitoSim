import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CSVWriter {
     static FileWriter fileWriter;
     static PrintWriter printWriter;
	
	public static void createFile() {
        // Define the file name
		
        String fileName = Constants.SEED + " " + Constants.NOISESEED + ".csv";

        try {
            // Create a FileWriter with the file name
            fileWriter = new FileWriter(fileName);

            // Create a PrintWriter to write to the FileWriter
            printWriter = new PrintWriter(fileWriter);

            // Write header row
            printWriter.println("Mosquito Count, Egg Count, Total Count");

            System.out.println("CSV file created successfully.");
        } catch (IOException e) {
            // Handle IOException if any
            e.printStackTrace();
        }
    }
	
	public static void print(int mosquitoCount, int eggCount, int totalCount) {
        printWriter.println(mosquitoCount + "," + eggCount + "," + totalCount);
	}
	
	public static void close() {
        printWriter.close();
	}
    
    
}
