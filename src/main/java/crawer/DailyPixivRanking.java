package crawer;

import java.util.ArrayList;
import java.util.List;


import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import pipeline.SimplePagePipeLine;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;

/**
 * 最初的测试类
 *
 * @author jrc
 */
public class DailyPixivRanking implements PageProcessor {
    private static int NUMBER = 5;
    private Site site = Site.me().setRetryTimes(3).setTimeOut(10000)
            .setUserAgent("Mozilla/5.0 (Windows; U; Windows NT 5.1; it; rv:1.8.1.11) Gecko/20071127 Firefox/2.0.0.11")
            .addHeader("Authorization","Bearer 8mMXXWT9iuwdJvsVIvQsFYDwuZpRCM")
            .addHeader("Content-Type","application/x-www-form-urlencoded")
            .addHeader("Referer","https://www.pixiv.net/ranking.php");

    public void process(Page page) {
        Json json = page.getJson();
        DocumentContext context = JsonPath.parse(json.toString());
        List<String> list = context.read("$.contents[*].url");
        List<String> urls = new ArrayList<String>();
        for (int i = 0; i < NUMBER; i++) {
            String url = list.get(i);
            urls.add(url);
        }
        page.putField("urls", urls);
    }

    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        try {
            Spider.create(new DailyPixivRanking()).addUrl("https://www.pixiv.net/ranking.php?mode=daily&format=json").thread(3)
                    .addPipeline(new SimplePagePipeLine()).run();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
