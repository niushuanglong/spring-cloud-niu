package com.niu.study.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 爬虫工具类
 */
public class JsoupUtils {
    public static void main(String[] args) throws Exception{

        new JsoupUtils().parseUrl("java").forEach(System.err::println);

    }

    /**
     * 搜索参数
     * @param urlParam
     * @return
     * @throws Exception
     */
    public List<Goods> parseUrl(String urlParam) throws Exception{
        String url="https://search.jd.com/Search?keyword=" +urlParam;
        //Document都是js 对象
        Document document = Jsoup.parse(new URL(url), 30000);
//        Elements price = document.getElementsByClass("p-price");
//        Elements name = document.getElementsByClass("p-name");
        Element element = document.getElementById("J_goodsList");
        List<Goods> list=new ArrayList<>();
        //多个<li></li>标签
        Elements lis = element.getElementsByTag("li");
//        lis.forEach(c->{
//            String img = c.getElementsByTag("img").eq(0).attr("source-data-lazy-img");
//            String goodsPrice = c.getElementsByTag("p-price").eq(0).text();
//            String title = c.getElementsByTag("p-name").eq(0).text();
//            Goods goods = new Goods();
//            goods.setGoodsPrice(goodsPrice);
//            goods.setImg(img);
//            goods.setTitle(title);
//            list.add(goods);
//        });
        for (Element li:lis) {
            String img = li.getElementsByTag("img").eq(0).attr("source-data-lazy-img");
            String goodsPrice = li.getElementsByClass("p-price").eq(0).text();
            String title = li.getElementsByClass("p-name").eq(0).text();

            Goods goods = new Goods();
            goods.setGoodsPrice(goodsPrice);
            goods.setImg(img);
            goods.setTitle(title);
            list.add(goods);
        }


        return list;
    }

}
