package com.mzlalal.oss;

import java.awt.*;

/**
 * @author Mzlalal
 * @date 2022/12/24 16:40
 **/
public class MousePoint {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            // 输出当前鼠标
            Point p = MouseInfo.getPointerInfo().getLocation();
            System.out.println(p.getX() + "," + p.getY());

            // 睡眠1000毫秒
            Thread.sleep(1000);
        }
    }
}
