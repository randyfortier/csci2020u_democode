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
      scanner.setDelimiter(" \t\n.;,!?-/\\");
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
  }

  private void countWord(String word) {
    // TODO: Next week!
  }

  public static void main(String[] args) {

  }
}
