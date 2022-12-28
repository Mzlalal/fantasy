package com.mzlalal.oss;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.List;

/**
 * @author Mzlalal
 * @date 2022/12/24 16:40
 **/
public class MouseRobot {
    public static void main(String[] args) throws AWTException, InterruptedException {
        // 坐标点
        List<String> positionPointList = CollUtil.newArrayList();
        positionPointList.add("891,825");
        positionPointList.add("1045,661");

        // 初始化机器人
        Robot robot = new Robot();

        // 循环点击
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            // 遍历坐标点
            for (String item : positionPointList) {
                // 切割坐标点
                List<String> point = StrUtil.split(item, ",");
                // 检查坐标点格式是否正确
                if (point.size() != 2) {
                    System.err.println(StrUtil.format("坐标点:{}格式不正确,请重新输入,参考:x,y", item));
                    continue;
                }
                if (!(NumberUtil.isNumber(point.get(0)) && NumberUtil.isNumber(point.get(1)))) {
                    System.err.println(StrUtil.format("坐标点:{}格式不正确,存在非法数字,请重新输入,参考:123,456", item));
                    continue;
                }

                // 移动鼠标
                int x = Integer.parseInt(point.get(0));
                int y = Integer.parseInt(point.get(1));
                System.out.println("预期移动到坐标:" + x + "," + y);
                robot.mouseMove(-1, -1);
                robot.mouseMove((int) (x / 1.25), (int) (y / 1.25));
                Point p = MouseInfo.getPointerInfo().getLocation();
                System.out.println("实际移动到坐标:" + p.getX() + "," + p.getY());

                // 模拟鼠标按下左键
                robot.mousePress(InputEvent.BUTTON1_MASK);
                // 模拟鼠标松开左键
                robot.mouseRelease(InputEvent.BUTTON1_MASK);

                // 模拟鼠标按下左键
                robot.mousePress(InputEvent.BUTTON1_MASK);
                // 模拟鼠标松开左键
                robot.mouseRelease(InputEvent.BUTTON1_MASK);

                // 睡眠3000毫秒
                Thread.sleep(3000);
            }
        }
    }
}
