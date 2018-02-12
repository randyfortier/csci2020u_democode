import java.io.*;
import java.util.*;

public class WordCounter {
  private Map<String,Integer> wordCounts;

  public WordCounter() {
    wordCounts = new TreeMap<>();
  }

  public void processFile(File file) throws IOException {
    if (file.isDirectory()) {
      // process all the files in that directory
      File[] contents = file.listFiles();
      for (File current: contents) {
        processFile(current);
      }
    } if (file.exists()) {
      // count the words in this file
      Scanner scanner = new Scanner(file);
      scanner.useDelimiter("\\s");//"[\s\.;:\?\!,]");//" \t\n.;,!?-/\\");
      while (scanner.hasNext()) {
        String word = scanner.next();
        if (isWord(word)) {
          countWord(word);
        }
      }
    }
  }

  private boolean isWord(String word) {
    String pattern = "^[a-zA-Z]+$";
    if (word.matches(pattern)) {
      return true;
    } else {
      return false;
    }

    // also fine:
    //return word.matches(pattern);
  }

  private void countWord(String word) {
    if (wordCounts.containsKey(word)) {
      int oldCount = wordCounts.get(word);
      wordCounts.put(word, oldCount+1);
    } else {
      wordCounts.put(word, 1);
    }
  }

  public void outputWordCounts(int minCount, File outFile)
                              throws IOException {
    System.out.println("Saving word counts to " + outFile.getAbsolutePath());
    if (!outFile.exists() && outFile.canWrite()) {
      PrintWriter fileOut = new PrintWriter(outFile);

      Set<String> keys = wordCounts.keySet();
      Iterator<String> keyIterator = keys.iterator();

      while (keyIterator.hasNext()) {
        String key = keyIterator.next();
        int count = wordCounts.get(key);

        if (count >= minCount) {
          fileOut.println(key + ": " + count);
        }
      }

      fileOut.close();
    } else {
      System.err.println("Error:  Cannot write to file " + outFile.getAbsolutePath());
    }
  }

  public static void main(String[] args) {
    if (args.length < 2) {
      System.err.println("Usage: java WordCounter <dir> <outfile>");
      System.exit(0);
    }

    WordCounter wordCounter = new WordCounter();
    File dataDir = new File(args[0]);
    File outFile = new File(args[1]);

    try {
      wordCounter.processFile(dataDir);
      wordCounter.outputWordCounts(2, outFile);
    } catch (FileNotFoundException e) {
      System.err.println("Invalid input dir: " + dataDir.getAbsolutePath());
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
