package com.mzlalal.oss;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.ChineseDate;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.mzlalal.minio.service.MinioService;
import io.minio.errors.MinioException;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mzlalal
 * @date 2021/9/06 13:44
 **/
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = OssApplication.class)
public class OssTest {

    @Autowired
    private MinioService minioService;

    @Test
    public void loopFileUploadToAli() {
        // 根路径
        String rootPath = "D:\\tempfiles\\photo\\fantasy-oss";
        if (!FileUtil.exist(rootPath)) {
            System.out.println("文件夹不存在");
            return;
        }
        // 获取所有文件
        List<File> fileList = FileUtil.loopFiles(new File(rootPath));
        // 文件夹为空
        if (CollUtil.isEmpty(fileList)) {
            System.out.println("文件夹为空");
            return;
        }
        // 遍历上传
        System.out.println("文件开始上传...");
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "8");
        fileList.parallelStream().forEach(item -> {
            try {
                String fsPath = minioService.upload(item);
                System.out.println("\t文件上传成功:" + fsPath);
            } catch (MinioException e) {
                e.printStackTrace();
                System.out.println("文件上传失败");
            }
        });
        System.out.println("文件结束上传...");
    }

    @Test
    public void createJson() throws JSONException {
        List<JSONObject> jsonObjectList = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            JSONObject jsonObject = new JSONObject();
            if (i < 10) {
                jsonObject.put("code", "0" + String.valueOf(i));
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

}