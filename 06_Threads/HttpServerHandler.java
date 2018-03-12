import java.io.*;
import java.net.*;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;

public class HttpServerHandler implements Runnable {
  public String webRoot = "www";
  private Socket socket = null;
  private BufferedReader requestInput = null;
  private DataOutputStream responseOutput = null;

  public HttpServerHandler(Socket socket) throws IOException {
    this.socket = socket;
    requestInput = new BufferedReader(
                         new InputStreamReader(socket.getInputStream()));
    responseOutput = new DataOutputStream(socket.getOutputStream());
  }

  // GET /index.html HTTP/1.1
  public void run() {
    String firstLine = null;
    try {
      firstLine = requestInput.readLine();
      handleRequest(firstLine);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        requestInput.close();
        responseOutput.close();
        socket.close();
      } catch (IOException e) {
        System.err.println("Will this ever happen?");
        e.printStackTrace();
      }
    }
  }

  public void handleRequest(String request) throws IOException {
    try {
      StringTokenizer tokenizer = new StringTokenizer(request);
      String command = tokenizer.nextToken();
      String uri = tokenizer.nextToken();

      if (command.equalsIgnoreCase("GET") ||
          command.equalsIgnoreCase("POST")) {

      } else {

      }
    } catch (NoSuchElementException e) {
      e.printStackTrace();
    }
  }

  private void sendError(int errorCode,
                         String errorMessage,
                         String description) throws IOException {
     /*
     HTTP/1.1 200 OK
     <headers>
     <html>
     ...
     </html>
     */
     String header = "HTTP/1.1 " + errorCode + " " + errorMessage + "\r\n";
   }







}
