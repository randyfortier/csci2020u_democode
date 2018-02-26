import java.io.*;
import java.net.*;
import org.json.*;

public class YouTube {
  public static void main(String[] args) {
    try {
      String apiKey = "AIzaSyA2rJ3kuns7kM0B-qLeGl6-BLpM_9whQr0";
      String keyword = "SpaceX";
      String url = "https://www.googleapis.com/youtube/v3/search" +
              "?part=id%2Csnippet&q=" + keyword + "&key=" + apiKey;
      URL netURL = new URL(url);

      URLConnection conn = netURL.openConnection();
      conn.setDoOutput(false);
      conn.setDoInput(true);

      InputStream inStream = conn.getInputStream();
      BufferedReader in = new BufferedReader(
        new InputStreamReader(inStream)
      );

      System.out.println(url);

      StringBuffer buffer = new StringBuffer();
      String line;
      while ((line = in.readLine()) != null) {
        buffer.append(line);
      }
      String jsonData = buffer.toString();

      JSONObject data = new JSONObject(jsonData);
      JSONArray items = data.getJSONArray("items");
      for (int i = 0; i < items.length(); i++) {
        JSONObject item = items.getJSONObject(i);
        JSONObject id = item.getJSONObject("id");
        String kind = id.getString("kind");

        if (kind.equals("youtube#video")) {
          JSONObject snippet = item.getJSONObject("snippet");

          String title = snippet.getString("title");
          String published = snippet.getString("publishedAt");

          System.out.printf("Found: %s (%s)\n", title, published);
        }
      }

      inStream.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
