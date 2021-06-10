package info.sjd;

import info.sjd.model.Item;
import info.sjd.service.ItemParserService;
import info.sjd.service.StartPageParserService;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class App {

    private static final Logger LOG = Logger.getLogger(App.class.getName());
    private static final String RESOURCE = "https://www.amazon.com";

    public static void main( String[] args ) {
        List<Thread> threads = new ArrayList<>();
        Map<String, Item> items = new HashMap<>();

        StartPageParserService startPageParserService =
                new StartPageParserService(RESOURCE, threads, items);

        if(args.length == 0) {
            startPageParserService.parseFullResource(threads, items);
        } else {
            String keyword = StringUtils.joinWith(" ", args);
            startPageParserService.parseByKeyword(threads, items, keyword);
        }

        boolean threadsAreNotProcessed;
        do{
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            threadsAreNotProcessed = checkThreads(threads);
        } while (threadsAreNotProcessed);

        LOG.info(items.size() + " were extracted");
    }

    private static boolean checkThreads(final List<Thread> threads) {
        for (Thread thread : threads) {
            if (thread.isAlive() || thread.getState().equals(Thread.State.NEW)) {
                return true;
            }
        }
        return false;
    }
}
