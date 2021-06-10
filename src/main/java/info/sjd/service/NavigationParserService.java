package info.sjd.service;

import info.sjd.model.Item;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NavigationParserService extends Thread {

    private final String url;
    private final List<Thread> threads;
    private final Map<String, Item> items;
    private final Document document;

    @Override
    public void run() {
        // first step - extract item urls
        Element searchBlockElement =
                document.getElementsByAttributeValueContaining("class", "s-main-slot")
                        .first();
        Elements itemBlockElements =
                searchBlockElement.getElementsByAttributeValueStarting("class", "s-result-item");
        Set<String> itemLinks = new HashSet<>();
        for (Element itemBlockElement : itemBlockElements) {
            Element itemLinkElement =
                    itemBlockElement.getElementsByAttributeValueStarting("class", "a-link-normal")
                            .first();
            itemLinks.add(itemLinkElement.absUrl("href"));
        }
        for (String itemUrl : itemLinks) {
            RouterParserService routerParserService = new RouterParserService(itemUrl, threads, items);
            threads.add(routerParserService);
            routerParserService.start();
        }

        // extract other navigation pages
        Elements nextPageElements = document.getElementsByClass("a-last");
        if (!nextPageElements.isEmpty()) {
            String nextPageLink =
                    nextPageElements.first()
                            .getElementsByTag("a").first()
                            .absUrl("href");
            RouterParserService routerParserService = new RouterParserService(nextPageLink, threads, items);
            threads.add(routerParserService);
            routerParserService.start();
        }

    }
}
