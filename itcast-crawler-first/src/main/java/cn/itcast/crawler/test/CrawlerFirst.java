package cn.itcast.crawler.test;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.checkerframework.checker.units.qual.C;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class CrawlerFirst {
    public static void main(String[] args) throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
//        // 2.输入网址
        HttpGet request = new HttpGet("https://www.cankaoxiaoxi.com/json/channel/gj/list.json?_t=1687924503731");

        try {
            CloseableHttpResponse response = httpClient.execute(request);
            if (response.getCode() == 200) {
                System.out.println("响应成功！");
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity);
                List<String> imageCoverList = new ArrayList<>();
                List<String> titleList = new ArrayList<>();
                List<String> urlList = new ArrayList<>();
                if (entity != null) {
                    JSONObject jsonObject = new JSONObject(responseString);

                    // 在这里对 JSON 数据进行进一步的操作和处理
                    // 例如，可以获取特定字段的值
                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        JSONObject data = item.getJSONObject("data");

                        String mCoverImg = data.getString("mCoverImg_s");
                        String title = data.getString("title");
                        String url = data.getString("url");

                        // 在这里处理提取到的字段值
//                        System.out.println("mCoverImg: " + mCoverImg);
//                        System.out.println("title: " + title);
//                        System.out.println("url: " + url);
                        imageCoverList.add(mCoverImg);
                        titleList.add(title);
                        urlList.add(url);
                    }
                    System.out.println(imageCoverList.size());
                    System.out.println(titleList.size());
                    System.out.println(urlList.size());
                }
                // 下载内容
                for (int i = 0; i < urlList.size(); i++) {
                    getContent(urlList.get(i), i);
                }

                // 下载标题
                for (int i = 0; i < titleList.size(); i++) {
                    String filePath = "D:\\javaweb\\download\\titles\\title" + i + ".txt";

                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                        String text = titleList.get(i);
                        writer.write(text);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // 下载图片
                for (int i = 0; i < imageCoverList.size(); i++) {
                    if (imageCoverList.get(i).equals("")) continue;
                    getImage(imageCoverList.get(i), i);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // next 2 task : first -> download the image, second cope with each url(get the content) etc.
    private static void getContent(String url, int index) throws Exception{
        // 设置 ChromeDriver 路径
        System.setProperty("webdriver.chrome.driver", "D:\\javaweb\\chromedriver.exe");

        WebDriver driver = new ChromeDriver(new ChromeOptions().setHeadless(true));

        // 打开网页
        driver.get(url);

        // 等待页面加载完成（可以根据实际情况调整等待时间）
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 获取所有 <p> 标签
        List<WebElement> paragraphs = driver.findElements(By.tagName("p"));

        String filePath = "D:\\javaweb\\download\\content\\output" + index + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            int paragraphCount = paragraphs.size();
            for (int i = 0; i < paragraphCount - 1; i++) {
                WebElement paragraph = paragraphs.get(i);
                String text = paragraph.getText();
                System.out.println(text);
                writer.write(text);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 关闭浏览器
        driver.quit();
    }

    private static void getImage(String imageUrl, int index) {
        String savePath = "D:\\javaweb\\download\\images\\image" + index + ".jpg"; // 图片保存的本地路径

        try {
            // 创建URL对象
            URL url = new URL(imageUrl);

            // 打开连接
            URLConnection connection = url.openConnection();

            // 获取输入流
            InputStream inputStream = connection.getInputStream();

            // 创建输出流
            FileOutputStream outputStream = new FileOutputStream(savePath);

            // 读取数据并写入文件
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // 关闭流
            inputStream.close();
            outputStream.close();

            System.out.println("图片下载完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
