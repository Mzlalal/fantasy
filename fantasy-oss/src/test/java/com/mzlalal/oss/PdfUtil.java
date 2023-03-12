package com.mzlalal.oss;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mzlalal
 * @date 2023/3/5 23:37
 **/
@Slf4j
public class PdfUtil {
    /**
     * base64解密
     */
    static BASE64Decoder decoder = new BASE64Decoder();

    public static void main(String[] args) {
        try {
//            List<String> obsPathList = new PdfUtil().pdf2jpg("D:\\tempfiles\\test.pdf", 2.5f);
            List<String> obsPathList = new PdfUtil().base642jpg("JVBERi0xLjQKJeLjz9MKMSAwIG9iaiA8PC9UeXBlL1hPYmplY3QvQ29sb3JTcGFjZVsvSW5kZXhl\nZC9EZXZpY2VSR0IgMjU1KNiFjPV6hvnm6P/9/fzz89Ndacg5R+Sco/bZ29+Rmepqd+ZXZum0ufiL\nlPhpd9drdfRWaOzBxeaCieVHV/LN0eeorqkbKthKWvbHyuiJkMtFU/mCjdp4gdxyeuh1f/K1uddj\nbfOUnc1TX/dyfttTYOlOYOJibfKiqvNOY/e8wfRhcbpJVPtcXG/zrLTGZ3H73N344eLtrq/cpKnr\nXm7+9/j67u/OX2rFQ0/98PDdPlHv0dTLd3+0OkbmkZHoR17///8AAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAApXS9TdWJ0eXBlL0lt\nYWdlL0JpdHNQZXJDb21wb25lbnQgOC9XaWR0aCAxNDAvTGVuZ3RoIDUwODcvSGVpZ2h0IDE0MC9G\n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\n5QCZeO/KK4S08Q5cjBqBYaeMqrLNf5gVmAyzsn691J9BSRZaxgQAGGhvPKa67RlIo0EI5cAgZray\nvOcm9W9iy3WTdBQYEMztL0GBkwbMrHO0B80TTBP253/ihao2iua8cGcKxjMb9G+NhR+P3C8aKAET\nuLOd5rtPf+238vKzCan5W7+i5gJz1t5p6t281PfkoH+4TOvbP+iG2AY0C3P7F1SzRc7AxH/yA+oD\nw7RXGn7+xiPwBQmeBs5gan6BVkrA8MMZgFk4P//oPQJvaGS2npU//PU/OI3aBjsJzs+v/PK/IAeC\nRq3p+c9+bZ+ggVS5WlnYeflCNZtJII/Pg8G0dW7+XodaNAdG3bW1neCMvzo0npwd0xy0Bsrfeo0G\nNdRu1ZlbE/PlC2+LCE7vigk90GDw/DffdsIFo4XWmc811X15a3LhQH6R0Aeatoats/S337vC8Ytd\nt7tZdjR38PJWT38cSwa/vIk5nU7BQP/gDTAcx2Pf79DX8aj0pStbNri/C7dnWUNS0ECwXAidwRT9\nqzfjcAG/oI8wHOfWYoEZCI/nJ/mJbNnD23a/JU+6NDDN0a+RORq6zNCkxvmXrwzieGbhW/PVagfu\nox13KsjCdQXXxa77a+K6qtsaMq2piff/w6uUOC44mKrLzC/d7nHZvSyP3W6XejWjMi1GbVEXUQ7/\n3zumOHAeU/V3oCGw2PF4ucxnrmu26Cu4kPKPTVOOJwhARS4ejSbY3AcBT98A9icw/gNH7rpRCmVu\nZHN0cmVhbQplbmRvYmoKMiAwIG9iaiA8PC9MZW5ndGggMTAvRmlsdGVyL0ZsYXRlRGVjb2RlPj5z\ndHJlYW0KeJwr5AIAAO4AfAplbmRzdHJlYW0KZW5kb2JqCjMgMCBvYmogPDwvTGVuZ3RoIDQ0L0Zp\nbHRlci9GbGF0ZURlY29kZT4+c3RyZWFtCnicUwjkKuQqVDA3UABBIGVibKBgamGgkJyroB+RaaDg\nkq8QyBXIBQCmYghjCmVuZHN0cmVhbQplbmRvYmoKNCAwIG9iajw8L0Jhc2VGb250L1NUU29uZy1M\naWdodC1VbmlHQi1VQ1MyLUgvRGVzY2VuZGFudEZvbnRzWzUgMCBSXS9UeXBlL0ZvbnQvU3VidHlw\nZS9UeXBlMC9FbmNvZGluZy9VbmlHQi1VQ1MyLUg+PgplbmRvYmoKNiAwIG9iaiA8PC9MZW5ndGgg\nMTA2MS9GaWx0ZXIvRmxhdGVEZWNvZGU+PnN0cmVhbQp4nJVXTYgcRRQuPURoYUMQ9pBcysuwK+5M\nVXX13x4kJO5GDfQw2zNZ4jQGWnsW3CyYNdmwJsYfcmsmixCXCOriIl70oBfB4x704kUPSkDQJbAg\nSIKHkKhRxlfVPcukurqHppjaqXr1va++eq9e7ZwzjrUN08YusXH7FYMRp27hGeaIQWOeYurids+Y\nOvPH9a82zvZrrdvJznT7VWMmXUgkxvLqNh+CphBOF6SzcgXBM9Tl4ttc22gZ5wxS9yzmgVX5u7ok\n9uIQhilx656NKczFRq8MwDyxk2oYILFdtzJJNYwgcXh1kkoYQWKT6iSVMIKE29VJKmEEiVk98BqM\nyDCGX4C0OwF4C1+USUVtbDETw1dYFIwY1MwZ2ijnYlLaLVa3uGIvSLyhmRO7zkvguhxUqKW9mLoQ\nnlEXwnWZqVILewl1EXxIXQTX5atKLewl1EXwIXURXJfFKrWwl1AXwYfURXBdbqvUZmmaFcKH1EVw\nQU0ZNh0iljGiw9J97MN2wFq2K68Nz5y2xHtBoazDIWPPFBcAVrVX0seCyMeiM9/9OtkOOsF91IjW\nl58M7g8OI4zm0YuIIhuZ0FvIRQx5MKKIQHORAyMxbyEm3g94KEaITJPXbU9D5X/aeyJzz8ANA+ck\n+1BwSaV7QWnByJFUFMiotLvIy1OlmhhTiK597v+X7KBwKraXn4m/QeE0kHJoQhGD/imlUbBZiOcJ\nXJZWFa5KuSkct8PV9T8Hh/Mwym09bvmXhXC6+dnL236y8El0NFj0v1g4EB3VHCLjaUHSM4snvl+L\nHB05h8zTQrPwURlMSx6Gi8wi0bZHFHx6muNE53HXt8Lw0Euva0RSSF2rkGkoEjXQ8+jZEql5BzJt\nuJQrmpVJb8hksmEs0oxBc7RplR6AoyawuCfv/XjjozL1OdDmfPO1xQPxeY18h6flV4WE4cHWB2V6\nc4jBHigSCuuiLwyprd4UcSWjk1du970yTTnY5r1yRTnAWEU5BCgqVJPFh7sKpvVPvzYuPjnQ+Pjk\nIGPV5BDj4pMpMnO3HeJz+dJiUqYoBxL/+2vUWKkaLUd87/27ZYJyoP0KPiPrd9pjWcdnZYWZFTMa\nhxReMEdxdvqxs79DhSbyRporJ5Ofbqyv3UE4bvg3N37oe+Bs5PRax4v3ydRCsPbzG7s6YaaLbZKr\nGpQ8svS0+Nnkf3shnGqGIfSR4z++8ebSXnKr+RYMB0eEsxE3Vlp84ESG4KXwvP+g2Wneib1ocuXd\n2Fu7Ogh7iyf82Xg3fi7Z6U3knbiZE3N/BxPRoeCvYOv098mXZzp+uHqpJt34ew/Nz7a+Gxy5QP1r\nlx9c+S3YOrUbTcZzmx++Hfa6/75zd+3qqV+DTjTZ/btfCzr+RHc7crofb86JkKs7cNIdjDKNLhdM\n5NHsIP8HFBxHHgplbmRzdHJlYW0KZW5kb2JqCjcgMCBvYmo8PC9UeXBlL1BhZ2VzL0NvdW50IDEv\nS2lkc1s4IDAgUl0+PgplbmRvYmoKOCAwIG9iajw8L1BhcmVudCA3IDAgUi9UeXBlL1BhZ2UvQ29u\ndGVudHNbMiAwIFIgNiAwIFIgMyAwIFJdL1Jlc291cmNlczw8L1hPYmplY3Q8PC9YaTAgMSAwIFI+\nPi9Qcm9jU2V0IFsvUERGIC9UZXh0IC9JbWFnZUIgL0ltYWdlQyAvSW1hZ2VJXS9Gb250PDwvRjEg\nNCAwIFI+Pj4+L01lZGlhQm94WzAgMCA1OTUgODQyXT4+CmVuZG9iago5IDAgb2JqPDwvQ2FwSGVp\nZ2h0IDg4MC9Gb250QkJveFstMjUgLTI1NCAxMDAwIDg4MF0vU3R5bGU8PC9QYW5vc2UoAQUCAgQA\nAAAAAAAAKT4+L1R5cGUvRm9udERlc2NyaXB0b3IvRGVzY2VudCAtMTIwL1N0ZW1WIDkzL0ZsYWdz\nIDYvQXNjZW50IDg4MC9Gb250TmFtZS9TVFNvbmctTGlnaHQvSXRhbGljQW5nbGUgMD4+CmVuZG9i\nago1IDAgb2JqPDwvQmFzZUZvbnQvU1RTb25nLUxpZ2h0L0NJRFN5c3RlbUluZm88PC9PcmRlcmlu\nZyhHQjEpL1JlZ2lzdHJ5KEFkb2JlKS9TdXBwbGVtZW50IDQ+Pi9UeXBlL0ZvbnQvV1sxWzIwN10g\nOSAxMCAzNzQgMTFbNDIzXSAxNFszNzUgMjM4IDMzNF0gMTcgMjYgNDYyIDI3WzIzOF0gMzdbNzM5\nXSAzOVs1MTFdIDQyWzMxOF0gNTlbNjA3XV0vU3VidHlwZS9DSURGb250VHlwZTAvRm9udERlc2Ny\naXB0b3IgOSAwIFIvRFcgMTAwMD4+CmVuZG9iagoxMCAwIG9iajw8L1R5cGUvQ2F0YWxvZy9QYWdl\ncyA3IDAgUj4+CmVuZG9iagoxMSAwIG9iajw8L1Byb2R1Y2VyKGlUZXh0IDIuMC4wIFwoYnkgbG93\nYWdpZS5jb21cKSkvTW9kRGF0ZShEOjIwMjMwMzExMjAyNjQ2KzA4JzAwJykvQ3JlYXRpb25EYXRl\nKEQ6MjAyMzAzMTEyMDI2NDYrMDgnMDAnKT4+CmVuZG9iagp4cmVmCjAgMTIKMDAwMDAwMDAwMCA2\nNTUzNSBmIAowMDAwMDAwMDE1IDAwMDAwIG4gCjAwMDAwMDYwNTcgMDAwMDAgbiAKMDAwMDAwNjEz\nMyAwMDAwMCBuIAowMDAwMDA2MjQzIDAwMDAwIG4gCjAwMDAwMDc5MjUgMDAwMDAgbiAKMDAwMDAw\nNjM2NiAwMDAwMCBuIAowMDAwMDA3NDk1IDAwMDAwIG4gCjAwMDAwMDc1NDUgMDAwMDAgbiAKMDAw\nMDAwNzczNiAwMDAwMCBuIAowMDAwMDA4MTgwIDAwMDAwIG4gCjAwMDAwMDgyMjUgMDAwMDAgbiAK\ndHJhaWxlcgo8PC9Sb290IDEwIDAgUi9JbmZvIDExIDAgUi9TaXplIDEyPj4Kc3RhcnR4cmVmCjgz\nNTcKJSVFT0YK", "", 2.5f);
            System.out.println(obsPathList);
        } catch (PDFException | PDFSecurityException | IOException | InterruptedException e) {
            System.err.println("出错啦: " + e.getMessage());
        }
    }

    /**
     * 将指定pdf文件的首页转换为指定路径的缩略图
     *
     * @param filePath 原文件路径，例如d:/test.pdf
     * @param zoom     缩略图显示倍数，1表示不缩放，0.3则缩小到30%
     * @return PDF每页转换后的地址
     */
    public static List<String> pdf2jpg(String filePath, float zoom) throws PDFException, PDFSecurityException, IOException, InterruptedException {
        Document document = new Document();
        // 检查是文件路径还是URL
        if (Validator.isUrl(filePath)) {
            document.setUrl(URLUtil.url(filePath));
        } else {
            document.setFile(filePath);
        }

        // 转换为图片
        return processPdf2Image(document, zoom);
    }

    /**
     * 将指定pdf文件的首页转换为指定路径的缩略图
     *
     * @param base64       base64
     * @param originalName 源名称,文件地址或者URL地址
     * @param zoom         缩略图显示倍数，1表示不缩放，0.3则缩小到30%
     * @return PDF每页转换后的地址
     */
    public static List<String> base642jpg(String base64, String originalName, float zoom) throws PDFException, PDFSecurityException, IOException, InterruptedException {
        // base64编码内容转换为字节数组
        byte[] bytes = decoder.decodeBuffer(base64);

        // 创建文档读取字节数组
        Document document = new Document();
        document.setByteArray(bytes, 0, bytes.length, StrUtil.blankToDefault(originalName, SecureUtil.md5(base64)));

        // 转换为图片
        return processPdf2Image(document, zoom);
    }

    /**
     * 处理PDF变成图片
     * 快钱专用方法
     *
     * @param document 文档
     * @param zoom     缩放
     * @return PDF每页转换后的地址
     */
    public static List<String> processPdf2Image(Document document, float zoom) throws IOException, InterruptedException {
        // 文档为空,返回
        if (document == null) {
            return new ArrayList<>();
        }
        // PDF每页转换后的地址
        List<String> obsPathList = new ArrayList<>();
        // 循环转换每页
        for (int i = 0; i < document.getNumberOfPages(); i++) {
            // PDF获取图片,rotation-旋转,zoom-缩放
            BufferedImage pageImage = (BufferedImage) document.getPageImage(i, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, 0f, zoom);
            // 裁剪图片,从指定像素裁剪指定宽和高
            Image cutImage = ImgUtil.cut(pageImage, new Rectangle(65, 200, 1350, 550));

            // 文件名
            String fileName = StrUtil.format("{}-{}.{}", FileUtil.getName(document.getDocumentOrigin()), i + 1, ImgUtil.IMAGE_TYPE_PNG);
            // 输出文件
            File file = new File(fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ImageOutputStream outImage = ImageIO.createImageOutputStream(fileOutputStream);
            // 输出PNG图片
            ImgUtil.writePng(cutImage, outImage);
            // 关闭流
            IoUtil.close(fileOutputStream);
            IoUtil.close(outImage);

            // 上传到OBS


            // 添加到返回结果
            obsPathList.add(file.getAbsolutePath());

            // 删除临时文件
            boolean delete = file.delete();
            log.info("临时文件:{} 是否删除:{}", file.getAbsolutePath(), delete);
        }
        return obsPathList;
    }
}
