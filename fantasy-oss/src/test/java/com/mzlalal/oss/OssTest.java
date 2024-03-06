package com.mzlalal.oss;


import cn.hutool.core.date.ChineseDate;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mzlalal
 * @date 2021/9/06 13:44
 **/
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = OssApplication.class)
public class OssTest {

    @Test
    public void createJson() throws JSONException {
        List<JSONObject> jsonObjectList = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            JSONObject jsonObject = new JSONObject();
            if (i < 10) {
                jsonObject.put("code", "0" + i);
            } else {
                jsonObject.put("code", String.valueOf(i));
            }
            jsonObject.put("label", i + "日");
            jsonObjectList.add(jsonObject);
        }
        System.out.println(jsonObjectList);
    }

    @Test
    public void dateCompare() {
        DateTime now = DateUtil.date();
        DateTime dateTime = DateUtil.parse(now.year() + "-03-14 14:30");
        System.out.println(dateTime);
        System.out.println(DateUtil.compare(now, dateTime));
    }

    @Test
    public void currentTime() {
        System.out.println(DateUtil.format(DateUtil.date(), DatePattern.NORM_DATETIME_MINUTE_PATTERN));
    }

    @Test
    public void chineseTime() {
        ChineseDate chineseDate = new ChineseDate(2023, 13, 6);
        System.out.println(new DateTime(chineseDate.getGregorianCalendar()));
    }

    @Test
    public void extractUrlByText() {
        String text = "发票PDF下载： http://www.fapiao.com/dzfp-web/pdf/download?request=h1-wpcDiW9sVUPRDQKwdbss-RZMdAKimLhljW-K4npQwWZryG5oYfxtW9kwMmZ4C%5EbBDHchjDg\n" +
                "\n" +
                "发票图片下载： https://www.fapiao.com/dzfp-web/pdf/view?request=h1-wpcDiW9sVUPRDQKwdbss-RZMdAKimLhljW-K4npQwWZryG5oYfxtW9kwMmZ4C%5EbBDHchjDg";
        List<String> urlList = ReUtil.findAllGroup0("(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]", text);
        System.out.println(urlList);
    }

    @Test
    public void msTime() {
        System.out.println(DateUtil.format(DateUtil.date(), DatePattern.NORM_DATETIME_MS_PATTERN));
    }
}