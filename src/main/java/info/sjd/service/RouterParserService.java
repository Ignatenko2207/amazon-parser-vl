package info.sjd.service;

import info.sjd.App;
import info.sjd.model.Item;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import lombok.AllArgsConstructor;

import static java.util.Objects.nonNull;

@AllArgsConstructor
public class RouterParserService extends Thread{

    private static final Logger LOG = Logger.getLogger(RouterParserService.class.getName());

    private final String url;
    private final List<Thread> threads;
    private final Map<String, Item> items;

    @Override
    public void run() {
        Document document = DocumentService.getDocument(url);
        if (isNavigationPage(document)) {
            NavigationParserService navigationParserService =
                    new NavigationParserService(url, threads, items, document);
            threads.add(navigationParserService);
            navigationParserService.start();
        } else if (isItemPage(document)) {
            ItemParserService itemParserService =
                    new ItemParserService(url, items, document);
            threads.add(itemParserService);
            itemParserService.start();
        } else {
            LOG.warning("Can not recognise page\n" + url);
        }
    }

    private boolean isItemPage(final Document document) {
        Element element = document.getElementById("dp-container");
        return nonNull(element);
    }

    private boolean isNavigationPage(final Document document) {
        Elements elements = document.getElementsByAttributeValueStarting("class", "s-result-list");
        return !elements.isEmpty();
    }
}
