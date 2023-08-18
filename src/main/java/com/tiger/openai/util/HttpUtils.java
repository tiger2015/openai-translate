package com.tiger.openai.util;

import com.alibaba.fastjson.JSON;
import com.tiger.openai.model.HttpJsonResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Zenghu
 * @Date 2023年08月10日 19:37
 * @Description
 * @Version: 1.0
 **/
public class HttpUtils {

    private static HttpClientBuilder clientBuilder;

    static {
        clientBuilder = HttpClientBuilder.create();
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)
                .setConnectTimeout(30000)
                .setConnectionRequestTimeout(30000)
                .build();
        clientBuilder.setDefaultRequestConfig(requestConfig);
        try {
            SSLContext sslContext = SSLContext.getDefault();
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", new SSLConnectionSocketFactory(sslContext, new String[]{"TLSv1.2"}, null,
                            SSLConnectionSocketFactory.getDefaultHostnameVerifier()))
                    .build();
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
            connectionManager.setMaxTotal(32);
            connectionManager.setDefaultMaxPerRoute(32);
            clientBuilder.setConnectionManagerShared(true);
            clientBuilder.setConnectionManager(connectionManager);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }


    /**
     * 发送post请求
     *
     * @param url
     * @param headers 请求头
     * @param params  请求参数
     * @return
     * @throws Exception
     */
    public static HttpJsonResponse sendPostRequest(String url, Map<String, String> headers,
                                                   Map<String, Object> params) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        // 添加请求头
        if (!ObjectUtils.isEmpty(headers)) {
            headers.forEach(httpPost::addHeader);
        }
        // 设置请求体
        if (!ObjectUtils.isEmpty(params)) {
            String requestBody = JSON.toJSONString(params);
            httpPost.setEntity(new StringEntity(requestBody, StandardCharsets.UTF_8));
        }
        HttpJsonResponse response = new HttpJsonResponse();
        try (CloseableHttpClient httpClient = clientBuilder.build()) {
            final CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            final Header[] allHeaders = httpResponse.getAllHeaders();
            response.setStatusCode(httpResponse.getStatusLine().getStatusCode());
            Map<String, String> responseHeader = new HashMap<>();
            Arrays.stream(allHeaders).forEach(header -> responseHeader.put(header.getName(), header.getValue()));
            response.setHeaders(responseHeader);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                final HttpEntity entity = httpResponse.getEntity();
                final String responseBoy = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                response.setBody(responseBoy);
            }
        }
        return response;
    }
}
