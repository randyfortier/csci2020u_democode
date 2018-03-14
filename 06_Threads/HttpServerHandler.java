import java.io.*;
import java.net.*;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;
import java.util.Date;

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
        File baseDir = new File(webRoot);
        sendFile(baseDir, uri);
      } else {
        sendError(405, "Method Not Allowed", "You cannot use the '" + command + "' command on this server.");
      }
    } catch (NoSuchElementException e) {
      e.printStackTrace();
    }
  }

  private void sendFile(File baseDir, String uri) throws IOException {
    File file = new File(baseDir, uri);

    if (!file.exists()) {
      sendError(404, "Not Found", "The file '" + uri + "' could not be located.");
    } else {
      String contentType = getContentType(file.getName());
      byte[] content = new byte[(int)file.length()];
      FileInputStream fileIn = new FileInputStream(file);
      fileIn.read(content);
      fileIn.close();
      sendResponse("HTTP/1.1 200 Ok\r\n", contentType, content);
    }
  }

  private String getContentType(String filename) {
    if (filename.endsWith(".html") || filename.endsWith(".htm")) {
      return "text/html";
    } else if (filename.endsWith(".css")) {
      return "text/css";
    } else if (filename.endsWith(".js")) {
      return "text/javascript";
    } else if (filename.endsWith(".png")) {
      return "image/png";
    } else if (filename.endsWith(".gif")) {
      return "image/gif";
    } else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
        return "image/jpeg";
    } else {
      return "unknown";
    }
  }

  private void sendResponse(String responseCode,
                            String contentType,
                            byte[] content)
                                                 throws IOException {
    responseOutput.writeBytes(responseCode);

    responseOutput.writeBytes("Content-Type: " + contentType + "\r\n");
    responseOutput.writeBytes("Date: " + (new Date()) + "\r\n");
    responseOutput.writeBytes("Server: Simple-Http-Server v1.0.0\r\n");
    responseOutput.writeBytes("Content-Length: " + content.length + "\r\n");
    responseOutput.writeBytes("Connection: Close\r\n\r\n");

    responseOutput.write(content);
    responseOutput.flush();
  }

  private void sendError(int errorCode,
                         String errorMessage,
                         String description) throws IOException {
     String responseCode = "HTTP/1.1 " + errorCode + " " + errorMessage + "\r\n";
     String content =  "<!DOCTYPE html>" +
                       "<html>" +
                       "  <head>" +
                       "    <title>" + errorCode + ": " + errorMessage + "</title>" +
                       "  </head>" +
                       "  <body>" +
                       "    <h1>" + errorCode + ": " + errorMessage + "</h1>" +
                       "    <p>" + description + "</p>" +
                       "  </body>" +
                       "</html>";
      sendResponse(responseCode, "text/html", content.getBytes());
   }







}
