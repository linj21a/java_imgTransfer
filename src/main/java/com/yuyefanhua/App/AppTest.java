/**
 * @author 60417
 * @date 2022/2/8
 * @time 12:27
 * @todo
 */
package com.yuyefanhua.App;

import com.yuyefanhua.Utils.Filter_image;

import java.io.IOException;
import java.util.Arrays;

public class AppTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        String path = "I:\\学习文档\\java\\Spring\\Spring第二部分.md";
//        String regex = "!\\[.*\\]\\(.*\\)";
        String regex = "(\\(?)[a-zA-Z]*:.*.(png|jpe?g|gif|svg)(\\)?)";
//        String imagePath = Filter_image.getImagePath(path,regex);
//        Filter_image.getImagePath(path,regex);
        //开启一线程，监听动作 ctrl -v\
        GuiAction guiAction = new GuiAction(regex,"");
    }
}
