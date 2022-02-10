/**
 * @author 60417
 * @date 2022/2/8
 * @time 12:17
 * @todo
 */
package com.yuyefanhua.Utils;

import com.yuyefanhua.App.GuiAction;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.Raster;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 查找出文档里面 包含的image图片
 */
public class Filter_image {
    public static final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    /**
     * 获取md文件里面的图片路径
     * @param path
     * @return 返回一个String【】
     * @throws IOException
     */
    public static String getImagePath(String path,String regex) throws IOException {
        File file = new File(path);
        if (file.isDirectory() || !file.exists()) {
            throw new IllegalArgumentException("不是文件或文件不存在！");
        }
        return getImagePath(regex,file);
    }
    public static String getImagePath(String regex,File file) throws IOException {
        //打开文件，读取文件：
        BufferedReader bufferedReader = new BufferedReader
                (new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));

        StringBuffer sb = new StringBuffer();
        String temp;
//        BufferedWriter writer = new BufferedWriter(new FileWriter("1.txt"));
//        writer.write("#############################");
//        writer.write("\n");
//        writer.write(file.getAbsolutePath());//文件
//        writer.write("#############################");
//        writer.write("\n");
        Pattern pattern = Pattern.compile(regex);
        while ((temp = bufferedReader.readLine()) != null) {
            Matcher matcher = pattern.matcher(temp);
            if (matcher.find()) {
//                int start = matcher.start();
//                int end = matcher.end();
//                temp = temp.substring(start, end);
                temp = matcher.group();//返回匹配到的子字符串
                String[] templist = temp.split("!");//以!来切分数据
//                System.out.println(Arrays.deepToString(templist));
                for(int i=0;i<templist.length;i++){
                    if(templist[i].length()!=0){
                        String pic = templist[i];

//[, [1642320406503](I:\学习文档\my_picture_learning\1642320406503.png),
// [1642320406503](I:\学习文档\my_picture_learning\1642320406503.png)]
                        String s;//I:\学习文档\my_picture_learning\1642320406503.png)
                        String[] strings = pic.split("\\(");
                        String pic_path;
                        if(strings.length<2){
//                            s = strings[0];//I:\学习文档\my_picture_learning\1642320406503.png
                            pic_path = strings[0];
                        }else {//包含(的分割
//                            s = strings[1];
                            //I:\学习文档\my_picture_learning\1642320406503.png)
                            pic_path = strings[1].substring(0,strings[1].length()-1);
                        }

                        sb.append(pic_path);
//                        writer.write(pic_path);
                        sb.append("\n");
//                        writer.write("\n");
                    }
                }
            }
        }
//        writer.write("#############################");
//        writer.write("\n\n");
//        writer.close();
        bufferedReader.close();
        return sb.toString();
    }

    /**
     * 遍历图片路径，对于每个图片路径内容，将其内容复制到剪切板
     * @param imagePath
     * @throws IOException
     * @throws InterruptedException
     */
    public  void imagesList(String[] imagePath,int i) throws IOException, InterruptedException {
        for(;i<imagePath.length;i++){
            String image = imagePath[i];
            while(getClipboardString() ==null){
                setClipboardString(image);
            }
        }
    }
    public static void setClipboardString(String text){
        // 封装文本内容
        Transferable trans = new StringSelection(text);
        // 把文本内容设置到系统剪贴板
        clipboard.setContents(trans, null);
    }
    /**
     * 从剪贴板中获取文本（粘贴）
     */
    public static String getClipboardString() {
        // 获取剪贴板中的内容
        Transferable trans = clipboard.getContents(null);
        if (trans != null) {
            // 判断剪贴板中的内容是否支持文本
            if (trans.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    // 获取剪贴板中的文本内容
                    String text = (String) trans.getTransferData(DataFlavor.stringFlavor);
                    return text;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
    /***
     * 复制图片到剪切板
     * @param image
     */
    public static void setClipboardImage(final Image image) {
        Transferable trans = new Transferable() {
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[]{DataFlavor.imageFlavor};
            }

            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return DataFlavor.imageFlavor.equals(flavor);
            }

            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                if (!DataFlavor.imageFlavor.equals(flavor)) {throw new UnsupportedFlavorException(flavor);}
                return image;
            }
        };
//Registered service providers failed to encode BufferedImage@39d4b442
        //JAEG限制不支持了 apache openJDK移除了对jpeg的支持
        clipboard.setContents(trans, null);
    }
    /**
     * 读取图片地址，把图片写入图片缓冲区
     * @param imagePath
     * @return
     * @throws IOException
     */
    public static BufferedImage readImage(String imagePath) throws IOException{
//        System.out.println(imagePath+"-------------------");
        File f = new File(imagePath);
        BufferedImage image = ImageIO.read(f);
//        BufferedImage image = (BufferedImage) imageIcon.getImage();
        BufferedImage rgbImage = new BufferedImage(image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_3BYTE_BGR);
        // then do a funky color convert
        ColorConvertOp op = new ColorConvertOp(null);
        op.filter(image, rgbImage);
        return rgbImage;
    }

}
