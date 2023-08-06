package cn.itcast.jd.utils;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HttpUtils {
    private PoolingHttpClientConnectionManager cm;

    public HttpUtils() {
        this.cm = new PoolingHttpClientConnectionManager();

        this.cm.setMaxTotal(100);

        this.cm.setDefaultMaxPerRoute(10);
    }

    // 封装httpClient方法相关

    /**
     * 根据请求地址下载页面数据
     * @param url
     * @return 页面数据
     */
    public String doGetHtml(String url) {
        // 根据请求地址下载页面数据
        // 为了提高效率 => 准备一个连接池
        return null;
    }

    /**
     * 下载图片
     * @param url
     * @return 图片名称
     */
    public String doGetImage(String url) throws IOException {
        // 获取HttpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager((HttpClientConnectionManager) this.cm).build();

        // 准备一个httpGet请求对象
        HttpGet httpGet = new HttpGet(url);

        // 使用httpClient发起请求 获得响应
        CloseableHttpResponse response = null;

        try {
            response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == 200) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

//    private RequestConfig getConfig() {
//        RequestConfig config = RequestConfig.custom()
//                .setConnectionTimeout(1000)  // 创建连接的最长时间
//                .setConnectionRequestTimeout(1000)
//                .setSocketTimeout(10000)
//                .build();
//        return
//    }
}
