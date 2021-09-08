package com.mzlalal.oss;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author zhaofengming
 * @date 2021/8/3 19:34
 */
public class CodeTransferTest {

    /**
     * 删除指定扩展名的文件
     */
    @Test
    public void deleteAssignExtTest() {
        // 支持的扩展名
        String[] supportFileExt = new String[]{"java_mz"};
        // 根目录
        String input = "D:\\gitee\\fantasy";
        // 编辑文件夹
        List<File> loopFileList = FileUtil.loopFiles(new File(input));
        // 删除数量
        int deleteNum = 0;
        // 遍历
        for (File item : loopFileList) {
            // 获取扩展名
            String extName = FileUtil.extName(item);
            // 支持的扩展名则进行s删除
            if (StrUtil.equalsAny(extName, supportFileExt)) {
                System.out.println(StrUtil.format("删除文件:{}", item.getAbsolutePath()));
                // 删除文件
                FileUtil.del(item);
                // 递增
                deleteNum++;
            }
        }
        System.out.println(StrUtil.format("共计删除{}个文件", deleteNum));
    }

    /**
     * 转换指定扩展名文件
     */
    @Test
    public void codeTransferTest() {
        // 支持的扩展名
        String[] supportFileExt = new String[]{"java"};
        // 根目录
        String input = "D:\\gitee\\fantasy";
        // 编辑文件夹
        List<File> loopFileList = FileUtil.loopFiles(new File(input));
        // 转换数量
        int transferNum = 0;
        // 遍历
        for (File item : loopFileList) {
            // 获取扩展名
            String extName = FileUtil.extName(item);
            // 支持的扩展名则进行下一步
            if (!StrUtil.equalsAny(extName, supportFileExt)) {
                continue;
            }
            // 获取所有行
            List<String> rowList = FileUtil.readLines(item, Charset.defaultCharset());
            // 目标文件
            File destFile = new File(item.getAbsolutePath() + "_mz");
            // 输出
            FileUtil.writeLines(rowList, destFile, Charset.defaultCharset());
            System.out.println(StrUtil.format("转换文件:{}", item.getAbsolutePath()));
            // 递增
            transferNum++;
        }
        System.out.println(StrUtil.format("共计转换{}个文件", transferNum));
    }

    /**
     * 反转回原文件
     */
    @Test
    public void codeReverseTest() {
        // 支持的扩展名
        String[] supportFileExt = new String[]{"java_mz"};
        // 根目录
        String input = "D:\\gitee\\fantasy";
        // 编辑文件夹
        List<File> loopFileList = FileUtil.loopFiles(new File(input));
        // 反转数量
        int transferNum = 0;
        // 遍历
        for (File item : loopFileList) {
            // 获取扩展名
            String extName = FileUtil.extName(item);
            // 支持的扩展名则进行下一步
            if (!StrUtil.equalsAny(extName, supportFileExt)) {
                continue;
            }
            // 获取所有行
            List<String> rowList = FileUtil.readLines(item, Charset.defaultCharset());
            // 目标文件
            File destFile = new File(StrUtil.replace(item.getAbsolutePath(), "_mz", ""));
            // 输出
            FileUtil.writeLines(rowList, destFile, Charset.defaultCharset());
            System.out.println(StrUtil.format("反转文件:{}", item.getAbsolutePath()));
            // 递增
            transferNum++;
        }
        System.out.println(StrUtil.format("共计反转{}个文件", transferNum));
    }
}