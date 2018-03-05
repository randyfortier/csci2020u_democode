import java.io.*;
import java.net.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

public class BikeShare {
  public static void main(String[] args) {
    try {
      String url = "https://api.mockaroo.com/api/e9cc2e00?count=20&key=e99c5530";
      URL netURL = new URL(url);

      URLConnection conn = netURL.openConnection();
      conn.setDoOutput(false);
      conn.setDoInput(true);

      InputStream inStream = conn.getInputStream();

      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
      Document document = docBuilder.parse(inStream);
      document.getDocumentElement().normalize();

      NodeList itemNodes = document.getElementsByTagName("station");
      for (int i = 0; i < itemNodes.getLength(); i++) {
        Element current = (Element)itemNodes.item(i);

        String id = getTagValue("id", current);
        String name = getTagValue("name", current);
        String lat = getTagValue("lat", current);
        String lon = getTagValue("long", current);
        String numBikes = getTagValue("nbBikes", current);

        System.out.printf("[%s] %s (%s,%s): %s bikes\n",
                          id,
                          name,
                          lat,
                          lon,
                          numBikes);
      }

      inStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static String getTagValue(String tagName, Element element) {
    NodeList tags = element.getElementsByTagName(tagName);
    if (tags.getLength() > 0) {
      return tags.item(0).getTextContent();
    }
    return null;
  }







}
