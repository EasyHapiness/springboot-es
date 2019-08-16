package com.song.es.es.client;

import com.alibaba.fastjson.JSON;
import com.song.es.es.entity.ColorModeBean;
import com.song.es.es.entity.HuaWeiPhoneBean;
import com.song.es.es.entity.PhoneModel;
import com.song.es.es.repository.PhoneRepository;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @Desc
 * @Author
 * @Date 2019/8/16
 */
@Component
public class HttpClientForData {

    @Resource
    PhoneRepository phoneRepository;

    public void huawei() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault(); // 创建httpclient实例
        HttpGet httpget = new HttpGet("https://consumer.huawei.com/cn/phones/?ic_medium=hwdc&ic_source=corp_header_consumer"); // 创建httpget实例

        CloseableHttpResponse response = httpclient.execute(httpget); // 执行get请求
        HttpEntity entity=response.getEntity(); // 获取返回实体
        //System.out.println("网页内容："+ EntityUtils.toString(entity, "utf-8")); // 指定编码打印网页内容
        String content = EntityUtils.toString(entity, "utf-8");
        response.close(); // 关闭流和释放系统资源

        Document document = Jsoup.parse(content);
        Elements elements = document.select("#content-v3-plp #pagehidedata .plphidedata");
        for (Element element : elements) {
            String jsonStr = element.text();
            List<HuaWeiPhoneBean> list = JSON.parseArray(jsonStr, HuaWeiPhoneBean.class);
            for (HuaWeiPhoneBean bean : list) {
                String productName = bean.getProductName();
                List<ColorModeBean> colorsItemModeList = bean.getColorsItemMode();

                StringBuilder colors = new StringBuilder();
                for (ColorModeBean colorModeBean : colorsItemModeList) {
                    String colorName = colorModeBean.getColorName();
                    colors.append(colorName).append(";");
                }

                List<String> sellingPointList = bean.getSellingPoints();
                StringBuilder sellingPoints = new StringBuilder();
                for (String sellingPoint : sellingPointList) {
                    sellingPoints.append(sellingPoint).append(";");
                }
                PhoneModel phoneModel = new PhoneModel()
                        .setName(productName)
                        .setColors(colors.substring(0, colors.length() - 1))
                        .setSellingPoint(sellingPoints.substring(0, sellingPoints.length() - 1))
                        .setCreateTime(new Date());
                phoneRepository.save(phoneModel);
            }
        }
    }
}
