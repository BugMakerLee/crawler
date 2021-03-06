package pipeline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import utils.FileUtils;
import utils.PicUtils;

/**
 * 处理单一界面图片下载的PipeLine
 *
 * @author jrc
 */
public class SimplePagePipeLine implements Pipeline {
    public static String DEFAULT_DOWNLOAD_PATH = FileUtils.getDefaultDownloadPath();
    public static int DEFAULT_DOWNLOAD_NUMBER = 5;

    private String basePath = DEFAULT_DOWNLOAD_PATH; // default
    private int number = DEFAULT_DOWNLOAD_NUMBER; // default
    private List<String> urls = new ArrayList<>();

    public String getBasePath() {
        return basePath;
    }

    public SimplePagePipeLine path(String basePath) {
        this.basePath = basePath;
        return this;
    }

    public SimplePagePipeLine number(int number) {
        this.number = number;
        return this;
    }

    public int getNumber() {
        return number;
    }

    public SimplePagePipeLine() {
    }

    public SimplePagePipeLine(String basePath, int number) {
        this.basePath = basePath;
        this.number = number;
    }

    public SimplePagePipeLine(int number) {
        this.number = number;
    }

    public SimplePagePipeLine(String basePath) {
        this.basePath = basePath;
    }

    public void process(ResultItems resultItems, Task task) {
        List<String> list = resultItems.get("urls");
        setUrls(list);
        int num = this.number < list.size() ? this.number : list.size();
        int i = 0;
        while (i < num) {
            String url = list.get(i);
            String filename = url.substring(url.lastIndexOf("/"));
            try {
                if (PicUtils.download(basePath, filename, url)) {
                    i++;
                }
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }


}
