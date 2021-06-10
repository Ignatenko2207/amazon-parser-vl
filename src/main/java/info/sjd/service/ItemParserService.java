package info.sjd.service;

import info.sjd.model.Item;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ItemParserService extends Thread{

    private final String url;
    private final Map<String, Item> items;
    private final Document document;

    @Override
    public void run() {
        String name = getItemName(document);
        int price = getPrice(document);
        String imageUrl = getImageUrl(document);
        String itemCode = getItemCode(document);

        Item item =  Item.builder()
                .name(name)
                .itemCode(itemCode)
                .imageUrl(imageUrl)
                .url(url)
                .price(price)
                .build();
        items.put(itemCode, item);
    }

    private static String getItemCode(final Document document) {
        Element element = document.getElementById("productDetails_detailBullets_sections1");
        Elements elements = element.getElementsByTag("tr");
        String result = null;
        for (Element trElement : elements) {
            if (trElement.text().contains("ASIN")) {
                Element tdElement = trElement.getElementsByTag("td").first();
                result = tdElement.text();
            }
        }
        return result;
    }

    private static String getImageUrl(final Document document) {
        Element element = document.getElementById("landingImage");
        return element.attr("src");
    }

    private static int getPrice(final Document document) {
        String priceText = document.getElementById("priceblock_ourprice").text();
        if (priceText != null && !priceText.isEmpty()) {
            return Integer.parseInt(priceText.replaceAll("\\D", ""));
        }
        return 0;
    }

    private static String getItemName(final Document document) {
        Element element = document.getElementById("productTitle");
        return element.text();
    }
}
