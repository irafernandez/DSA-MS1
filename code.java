package dsadraft;

import java.io.*;
import java.util.*;

public class dsams1 {

    private static final String CSV_FILE_PATH = "C:\\Users\\vvele\\Downloads\\DSA\\src\\dsadraft\\InventoryData.csv";

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        while (true) { // loop
            System.out.println("\n============= MotorPH Inventory Management System =============\n");
            System.out.println("1. Add New Stocks");
            System.out.println("2. Delete Incorrect Stocks");
            System.out.println("3. Sort Stocks According to Brand");
            System.out.println("4. Search for Existing Inventory");
            System.out.println("5. Exit");
            System.out.print("\nSelect an option from the list above: ");

            String selection = scan.nextLine();

            try {
                switch (selection) {
                    case "1":
                        addNewStock(scan);
                        break;
                    case "2":
                        deleteStock(scan);
                        break;
                    case "3":
                        sortStocks();
                        break;
                    case "4":
                        searchStock(scan);
                        break;
                    case "5":
                        System.out.println("Thank you for using the MotorPH Inventory Management System. Have a great day! :)");
                        return; // exit
                    default:
                        System.out.println("Invalid selection. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void addNewStock(Scanner scan) throws IOException {
        System.out.println("Enter the following details for the new stock:");

        System.out.print("Date Entered (MM/DD/YYYY): ");
        String dateEntered = scan.nextLine();

        System.out.print("Stock Label (New/Old): ");
        String stockLabel = scan.nextLine();

        System.out.print("Brand: ");
        String brand = scan.nextLine();

        System.out.print("Engine Number: ");
        String engineNumber = scan.nextLine();

        System.out.print("Status (On-hand/Sold): ");
        String status = scan.nextLine();

        String newStock = String.join(",", dateEntered, stockLabel, brand, engineNumber, status);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE_PATH, true))) {
            writer.newLine();
            writer.write(newStock);
        }
        System.out.println("New stock added successfully!");
    }

    private static void deleteStock(Scanner scan) throws IOException {
        System.out.print("Enter the Engine Number of the stock to delete:");
        String engineNumberToDelete = scan.nextLine();

        File inputFile = new File(CSV_FILE_PATH);
        File tempFile = new File("temp.csv");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                if (line.contains(engineNumberToDelete)) {
                    found = true;
                    continue; 
                }
                writer.write(line);
                writer.newLine();
            }

            if (found) {
                System.out.println("Stock deleted successfully!");
            } else {
                System.out.println("Stock not found.");
            }
        }

        
        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            System.out.println("Error updating the file.");
        }
    }

    private static void sortStocks() throws IOException {
        List<String[]> stockData = new ArrayList<>();
        String header;

        // Read data from the CSV
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            header = reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 5) { 
                    stockData.add(fields);
                }
            }
        }

        // sort(index 2)
        stockData.sort(Comparator.comparing(stock -> stock[2]));

        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            writer.write(header);
            writer.newLine();
            for (String[] row : stockData) {
                writer.write(String.join(",", row));
                writer.newLine();
            }
        }

        System.out.println("Stocks sorted by brand successfully!");
    }

    private static void searchStock(Scanner scan) throws IOException {
        System.out.print("Enter the Engine Number of the stock to search:");
        String engineNumberToSearch = scan.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String header = reader.readLine();
            String[] headers = header.split(",");
            boolean found = false;

            System.out.println("\nSearch Results:");
            System.out.println("---------------");

            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[3].equalsIgnoreCase(engineNumberToSearch)) { 
                    System.out.println(headers[0] + ": " + fields[0]);
                    System.out.println(headers[1] + ": " + fields[1]);
                    System.out.println(headers[2] + ": " + fields[2]);
                    System.out.println(headers[3] + ": " + fields[3]);
                    System.out.println(headers[4] + ": " + fields[4]);
                    System.out.println("---------------");
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No stock found with Engine Number: " + engineNumberToSearch);
            }
        }
    }
}
