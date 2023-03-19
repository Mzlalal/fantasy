package com.mzlalal.oss;

import cn.hutool.core.lang.Assert;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * 图片转换为PDF
 *
 * @author Mzlalal
 **/
@Slf4j
public class Image2PdfUtil {

    private static void image2pdf(List<String> obsPathList, int sizeOfPage) throws Exception {
        Assert.isTrue(sizeOfPage > 0, "sizeOfPage必须大于0");

        // 创建A4纸大小的文档
        Document doc = new Document(PageSize.A4, 0, 0, 0, 0);
        log.info("A4纸宽:{} 高:{}", PageSize.A4.getWidth(), PageSize.A4.getHeight());
        // 获取PDF书写器
        PdfWriter.getInstance(doc, new FileOutputStream("1.pdf"));
        // 打开文档
        doc.open();
        // 循环添加图片
        for (String item : obsPathList) {
            // 获取图片
            Image img = Image.getInstance(new URL(item));
            log.info("图片宽:{} 高:{}", img.getWidth(), img.getHeight());
            // 使图片与A4纸张大小自适应
            img.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight() / sizeOfPage);
            // 添加图片到PDF
            doc.add(img);
        }

        // 关闭文档
        doc.close();
    }

    public static void main(String[] args) {
        List<String> obsPathList = new ArrayList<>();
        obsPathList.add("https://jyb-test.obs.cn-south-1.myhuaweicloud.com:443/kuaiqianVoucher%2Fpng%2F2023-03%2F2d88509c-bd8f-49a5-8e62-043743508a13.png");
        obsPathList.add("https://jyb-test.obs.cn-south-1.myhuaweicloud.com:443/kuaiqianVoucher%2Fpng%2F2023-03%2F001420e8-1a08-46c8-8a87-10c0a38b6f4f.png");
        obsPathList.add("https://jyb-test.obs.cn-south-1.myhuaweicloud.com:443/kuaiqianVoucher%2Fpng%2F2023-03%2F31f985c9-4462-4f4d-8ede-e549630bc165.png");
        obsPathList.add("https://jyb-test.obs.cn-south-1.myhuaweicloud.com:443/kuaiqianVoucher%2Fpng%2F2023-03%2Fa144cb55-6087-43ae-911b-7c621f39be6b.png");
        obsPathList.add("https://jyb-test.obs.cn-south-1.myhuaweicloud.com:443/kuaiqianVoucher%2Fpng%2F2023-03%2Fa144cb55-6087-43ae-911b-7c621f39be6b.png");
        obsPathList.add("https://jyb-test.obs.cn-south-1.myhuaweicloud.com:443/kuaiqianVoucher%2Fpng%2F2023-03%2F2d88509c-bd8f-49a5-8e62-043743508a13.png");
        obsPathList.add("https://jyb-test.obs.cn-south-1.myhuaweicloud.com:443/kuaiqianVoucher%2Fpng%2F2023-03%2F001420e8-1a08-46c8-8a87-10c0a38b6f4f.png");
        obsPathList.add("https://jyb-test.obs.cn-south-1.myhuaweicloud.com:443/kuaiqianVoucher%2Fpng%2F2023-03%2F31f985c9-4462-4f4d-8ede-e549630bc165.png");
        obsPathList.add("https://jyb-test.obs.cn-south-1.myhuaweicloud.com:443/kuaiqianVoucher%2Fpng%2F2023-03%2Fa144cb55-6087-43ae-911b-7c621f39be6b.png");
        obsPathList.add("https://jyb-test.obs.cn-south-1.myhuaweicloud.com:443/kuaiqianVoucher%2Fpng%2F2023-03%2Fa144cb55-6087-43ae-911b-7c621f39be6b.png");
        try {
            Image2PdfUtil.image2pdf(obsPathList, 2);
        } catch (Exception e) {
            log.error("图片转换PDF出错", e);
        }
    }
}
