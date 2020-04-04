package com.gdy.boke.util;

import com.alibaba.fastjson.JSON;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpClientUtil {

    private static final String CONTENT_TYPE = "Content-type";
    private static final String ENCODING = "utf-8";
    private static final  String CONTENT_TYPE_JSON = "application/json";
    private static final int CONNECT_TIMEOUT = 1000;
    private static final int SOCKET_TIMEOUT = 1000;

    public static HttpClientResoult doPost(String url,Map<String,Object> headers,Map<String,Object> params) throws IOException, CloneNotSupportedException {
        //创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        //设置超时时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpPost.setConfig(requestConfig);
        String contentType = (String)headers.get(CONTENT_TYPE);
        packageHeaders(headers,httpPost);
        //封装请求参数
        packageParams(params,contentType,httpPost);
        //发送请求
        CloseableHttpResponse httpResponse = null;
        try{
            //执行请求获取响应结果
            return getHttpClientResult(httpResponse,httpClient,httpPost);
        }finally {
            httpPost.clone();
            httpClient.close();
        }

    }

    private static void packageParams(Map<String, Object> params, String contentType, HttpPost httpMethod) throws UnsupportedEncodingException {
        if(params!=null){
            if(CONTENT_TYPE_JSON.equals(contentType)){
                StringEntity stringEntity = new StringEntity(JSON.toJSONString(params),ENCODING);
                httpMethod.setEntity(stringEntity);
            }else {
                List<NameValuePair> nvps = new ArrayList<>();
                Set<Map.Entry<String, Object>> entries = params.entrySet();
                for(Map.Entry e : entries){
                    nvps.add(new BasicNameValuePair((String) e.getKey(),e.getValue()==null?null:e.getValue().toString()));
                }
                //设置到请求的http对象中
                httpMethod.setEntity(new UrlEncodedFormEntity(nvps,ENCODING));
            }

        }
    }

    public static HttpClientResoult doGet(String url,Map<String,Object> headers,Map<String,Object> params) throws URISyntaxException, IOException {

        //创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建访问的地址
        URIBuilder ub = new URIBuilder(url);
        if(params!=null){
            Set<Map.Entry<String, Object>> entries = params.entrySet();
            for(Map.Entry e : entries){
                ub.setParameter((String) e.getKey(),e.getValue()==null?null:e.getValue().toString());
            }
        }

        //创建http对象
        HttpGet httpGet = new HttpGet(ub.build());
        //设置连接时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpGet.setConfig(requestConfig);

        packageHeaders(headers,httpGet);
        //创建httpResponse对象
        CloseableHttpResponse httpResponse = null;

        try {

            return getHttpClientResult(httpResponse,httpClient,httpGet);
        }finally {
            //释放资源
            httpResponse.close();
            httpClient.close();
        }


    }

    /**
     * 获得响应结果
     * @param httpResponse
     * @param httpClient
     * @param httpMethod
     * @return
     * @throws IOException
     */

    private static HttpClientResoult getHttpClientResult(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient, HttpRequestBase httpMethod) throws IOException {
        httpResponse = httpClient.execute(httpMethod);
        String content = "";
        if(httpResponse.getEntity()!=null && !httpResponse.getEntity().equals("")){
            content = EntityUtils.toString(httpResponse.getEntity(),CONTENT_TYPE);
        }else {
            return new HttpClientResoult(1,"信息获取失败");
        }
        return new HttpClientResoult(0,content);
    }

    private static void packageHeaders(Map<String, Object> headers, HttpRequestBase httpGet) {
        if(headers!=null){
            Set<Map.Entry<String, Object>> entries = headers.entrySet();
            for(Map.Entry e : entries){
                httpGet.setHeader((String) e.getKey(),e.getValue()==null?null:e.getValue().toString());
            }
        }
    }
}
