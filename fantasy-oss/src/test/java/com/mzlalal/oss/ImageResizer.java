package com.mzlalal.oss;

import cn.hutool.core.io.FileUtil;

import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.util.List;

public class ImageResizer {
    public static void main(String[] args) {
        System.setProperty("com.sun.media.jai.disableMediaLib", "true");
        // 读取要进行无损放大的图像文件
        List<File> fileList = FileUtil.loopFiles("C:\\Users\\DELL\\Desktop\\OCR");
        for (File item : fileList) {
            RenderedImage inputImage = JAI.create("fileload", item.getAbsolutePath());

            // 定义放大倍数和放大算法
            float scaleFactor = 2.0f;
            Interpolation interpolation = new InterpolationNearest();

            // 进行无损放大操作
            ParameterBlock pb = new ParameterBlock();
            pb.addSource(inputImage);
            pb.add(scaleFactor);
            pb.add(scaleFactor);
            pb.add(0.0F);
            pb.add(0.0F);
            pb.add(interpolation);
            RenderedImage outputImage = JAI.create("scale", pb);

            // 将放大后的图像保存到文件
            String outputFileName = "C:\\Users\\DELL\\Desktop\\OCR放大-1\\" + item.getName();
            File outputFile = new File(outputFileName);
            JAI.create("filestore", outputImage, outputFile.getAbsolutePath(), "PNG");
        }
    }
}
