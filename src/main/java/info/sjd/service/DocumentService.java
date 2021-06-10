package info.sjd.service;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class DocumentService {

    public static Document getDocument(String url) {
        Connection connection = Jsoup.connect(url);
        connection.header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        connection.header("Accept-Encoding","gzip, deflate, br");
        connection.header("Accept-Language","en-US,en;q=0.5");

        try {
            return connection.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Document was not downloaded");
    }

}
