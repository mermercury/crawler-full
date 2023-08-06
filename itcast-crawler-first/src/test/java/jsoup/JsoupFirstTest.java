package jsoup;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.io.File;
import java.net.URL;

public class JsoupFirstTest {
    @Test
    public void testUrl() throws Exception {
        // 1、parse URL address 【和httpClient一样的功能，只是在实际开发中不常用】
        Document dom = Jsoup.parse(new URL("https://www.harmonytech.cn"), 10000);
        // 使用标签选择器 获取<title>标签中的内容
        String title = dom.getElementsByTag("title").first().text();
        System.out.println(title);
    }

    @Test
    public void testString() throws Exception {
        // 使用工具类读取文件=>获取字符串
        String content = FileUtils.readFileToString(new File("C:\\Users\\JaypawMercury\\Desktop\\index.html"), "utf8");

        // 解析字符串
        Document dom = Jsoup.parse(content);
        String title = dom.getElementsByTag("title").first().text();
        System.out.println(title);

    }

    @Test
    public void testFile() throws Exception {
        // 解析字符串
        Document dom = Jsoup.parse(new File("C:\\Users\\JaypawMercury\\Desktop\\index.html"), "utf8");
        String title = dom.getElementsByTag("title").first().text();
        System.out.println(title);

    }

    @Test
    public void testDOM() throws Exception {
        Document dom = Jsoup.parse(new File("C:\\Users\\JaypawMercury\\Desktop\\index.html"), "utf8");
        // 1-id
        Element element = dom.getElementById("map");
        System.out.println(element.text());

        // 2-tag标签
        String h3 = dom.getElementsByTag("h3").first().text();
        System.out.println(h3);

        // 3-class
        String text = dom.getElementsByClass("btn-danger").first().text();
        System.out.println(text);

        // 4-getElementsByAttribute
//        Element element1 = dom.getElementsByAttribute("abc").first();
        Element element1 = dom.getElementsByAttributeValue("abc", "123").first();
        System.out.println(element1.text());

    }
}
