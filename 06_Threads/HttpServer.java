import java.io.*;
import java.net.*;

public class HttpServer {
  private ServerSocket serverSocket = null;

  public HttpServer(int port) throws IOException {
    serverSocket = new ServerSocket(port);
  }

  public void handleRequests() throws IOException {
    System.out.println("Listening for requests.");

    while (true) {
      Socket clientSocket = serverSocket.accept();
      HttpServerHandler handler = new HttpServerHandler(clientSocket);
      Thread handlerThread = new Thread(handler);
      handlerThread.start();
    }
  }

  public static void main(String[] args) {
    int port = 8080;

    if (args.length > 0) {
      port = Integer.parseInt(args[0]);
    }

    try {
      HttpServer server = new HttpServer(port);
      server.handleRequests();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
