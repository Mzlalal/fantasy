package com.mzlalal.oss;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * @author Mzlalal
 * @date 2022/12/24 16:40
 **/
public class MouseRobotJWT extends JFrame {

    public MouseRobotJWT() {
        this.setSize(500, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("mouse click");
        this.setVisible(true);
        this.addKeyListener(new KeyPressListener());
    }

    /**
     * 按键监听
     */
    static class KeyPressListener extends KeyAdapter {

        public boolean clicking = true;

        @SneakyThrows
        @Override
        public void keyPressed(KeyEvent e) {
            System.out.println(e.getKeyChar());
            if (e.getKeyChar() == 'p') {
                // 坐标点
                List<String> positionPointList = CollUtil.newArrayList();
                positionPointList.add("608,799");
                positionPointList.add("721,635");

                // 初始化机器人
                Robot robot = new Robot();

                // 循环点击
                while (this.clicking) {
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
                        robot.mouseMove(x, y);

                        // 模拟鼠标按下左键
                        robot.mousePress(InputEvent.BUTTON1_MASK);
                        // 模拟鼠标松开左键
                        robot.mouseRelease(InputEvent.BUTTON1_MASK);

                        // 睡眠100毫秒
                        Thread.sleep(100);
                    }
                }
            } else {
                this.clicking = false;
            }
        }
    }

    public static void main(String[] args) {
        new MouseRobotJWT();
    }
}
