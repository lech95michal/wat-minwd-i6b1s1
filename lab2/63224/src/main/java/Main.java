import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void exploreOTOMOTO() {
        String searchUrl = "https://www.otomoto.pl/osobowe/mercedes-benz/e-klasa/?search%5Bfilter_float_price%3Afrom%5D=30000&search%5Bfilter_float_price%3Ato%5D=100000&search%5Bfilter_float_mileage%3Ato%5D=200000&search%5Bnew_used%5D=on";

        JSONArray output = new JSONArray();

        try {
            Document doc = Jsoup.connect(searchUrl).get();

            Elements items = doc.select("article.offer-item");

            if(items == null) {
                System.out.println("Nie ma");
            } else {
                for (Element item : items) {
                    JSONObject jo = new JSONObject();

                    String link = item.attr("data-href");
                    jo.put("link", link);

                    // SZCZEGÓŁY
                    String title = item.selectFirst("div.offer-item__title > h2.offer-title > a").text();
                    jo.put("tytul", title);
                    Elements data = item.select("ul.ds-params-block > li.ds-param");

                    for(Element e : data) {
                        if(e.attr("data-code").equals("year")) {
                            jo.put("rocznik", e.text());
                        } else if(e.attr("data-code").equals("mileage")) {
                            jo.put("przebieg", e.text());
                        } else if(e.attr("data-code").equals("engine_capacity")) {
                            jo.put("pojemnosc", e.text());
                        } else if(e.attr("data-code").equals("fuel_type")) {
                            jo.put("typ", e.text());
                        }
                    }

                    jo.put("cena", item.selectFirst("span.offer-price__number").text());

                    jo.put("miejscowosc", item.selectFirst("span.ds-location-city").text());
                    jo.put("wojewodztwo", item.selectFirst("span.ds-location-region").text());
                    // ZDJĘCIE
                    Element image = item.selectFirst("a.offer-item__photo-link");
                    String imageSrc = image.selectFirst("img").attr("data-src");

                    URL url = new URL(imageSrc);
                    BufferedImage img = ImageIO.read(url);
                    File file = new File("img/downloaded.jpg");
                    ImageIO.write(img, "jpg", file);

                    byte[] fileContent = FileUtils.readFileToByteArray(new File("img/downloaded.jpg"));
                    String encodedString = Base64.getEncoder().encodeToString(fileContent);

                    jo.put("image", encodedString);

                    output.put(jo);

                }

                System.out.println(output);

                String filename = "jsonFiles/json_"+ System.currentTimeMillis();
                FileWriter fileWriter = new FileWriter(filename+".json");
                PrintWriter printWriter = new PrintWriter(fileWriter);
                output.write(printWriter);

                printWriter.close();
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        exploreOTOMOTO();
    }
}

