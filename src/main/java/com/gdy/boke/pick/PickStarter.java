package com.gdy.boke.pick;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.UUID;

public class PickStarter {

    //    https://unsplash.com/napi/search/photos?query=china&xp=&per_page=20&page=3

    private static final Integer FIND_PAGE = 10;

    public static void main(String[] args) throws Exception {
       // parseImgUrl("https://unsplash.com/napi/search?query=girl&xp=&per_page=20\n");
        parseImgUrl("girl",1,0);
    }

    private static void parseImgUrl(String searcheName,Integer page,Integer totalPage) throws Exception {
        String url = "https://unsplash.com/napi/search?query=" + searcheName + "&xp=&per_page=20" + "&page="+page+"\n";
        URL onlineUrl = new URL(url);
        Document doc = Jsoup.parse(onlineUrl.openConnection().getInputStream(),"utf-8",url);
        String object = doc.text();
        JSONObject jsonObject = JSONObject.parseObject(object);
        JSONObject photos = JSONObject.parseObject(jsonObject.get("photos").toString());
        //获取到photosInfo
        Photos photosInfo = JSONObject.parseObject(photos.toString(), Photos.class);
        Long totalPages = photosInfo.getTotal_pages();
        String results = photos.get("results").toString();
        List<Results> resultsList = JSONArray.parseArray(results, Results.class);
        circleDownload(resultsList,searcheName);
        if (page < 3) {
           page = page +1;
           parseImgUrl(searcheName,page,totalPage);
       }
        System.out.println(resultsList);
    }

    public static void  circleDownload(List<Results> resultsList,String searcheName) throws Exception {
        for(Results resultsLL : resultsList){
            Object downLoadUrl = resultsLL.getUrls().get("small");
            download(downLoadUrl.toString(),searcheName);
        }
    }


    public static void download(String urlString,String searcheName) throws Exception {
        String savePath = "d://pic"+"//"+searcheName;
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        //设置请求超时为5s
        con.setConnectTimeout(100*1000);
        // 输入流
        InputStream is = con.getInputStream();

        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        File sf=new File(savePath);
        if(!sf.exists()){
            sf.mkdirs();
        }
        String id = UUID.randomUUID().toString();
        OutputStream os = new FileOutputStream(sf.getPath()+"\\"+id+".jpg");
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();
    }
}
