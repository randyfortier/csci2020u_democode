import java.io.*;

public class FindAverage {
  public static void main(String[] args) {
    if (args.length < 2) {
      System.err.println("Usage: java FindAverage <inputFile> <columnName>");
      System.exit(0);
    }

    File inFile = new File(args[0]);
    String columnName = args[1].trim();

    try {
      FileReader fileInput = new FileReader(inFile);
      BufferedReader input = new BufferedReader(fileInput);

      // read the first line
      String line = input.readLine();

      // find the index of the named column
      int columnIndex = -1;
      String[] columnNames = line.split(",");
      for (int i = 0; i < columnNames.length; i++) {
        if (columnNames[i].equals(columnName)) {
          columnIndex = i;
          break;
        }
      }

      if (columnIndex < 0) {
        System.err.println("Error: Column name not found");
        System.exit(0);
      }

      // calculate the average for that column
      float total = 0f;
      int count = 0;
      while ((line = input.readLine()) != null) {
        String[] data = line.split(",");
        float nextVal = Float.parseFloat(data[columnIndex]);
        total += nextVal;
        count++;
      }

      System.out.printf("The average for %s is %.2f.\n", columnName, total/count);

      input.close();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
  }
}
