package info.sjd.service;

import info.sjd.model.Item;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StartPageParserService{

    private final String url;
    private final List<Thread> threads;
    private final Map<String, Item> items;

    public void parseFullResource(final List<Thread> threads, final Map<String, Item> items) {
        List<String> categories = extractCategories();
        for (String categoryUrl : categories) {
            RouterParserService routerParserService = new RouterParserService(categoryUrl, threads, items);
            threads.add(routerParserService);
            routerParserService.start();
        }
    }

    public void parseByKeyword(final List<Thread> threads, final Map<String, Item> items, final String keyword) {
        String searchLink = url + "/s?k=" + keyword.replaceAll(" ", "+");
        RouterParserService routerParserService = new RouterParserService(searchLink, threads, items);
        threads.add(routerParserService);
        routerParserService.start();
    }

    private List<String> extractCategories() {
        Document mainPageDocument = DocumentService.getDocument(url);

        // TODO: implement

        return new ArrayList<>();
    }
}
