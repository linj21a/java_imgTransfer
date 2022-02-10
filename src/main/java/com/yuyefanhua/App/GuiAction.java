/**
 * @author 60417
 * @date 2022/2/8
 * @time 14:17
 * @todo
 */
package com.yuyefanhua.App;

import com.yuyefanhua.Utils.Filter_image;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.regex.PatternSyntaxException;

public class GuiAction extends JFrame {
    JTextArea txaDisplay = new JTextArea(2,100);
    String []imagePathList;
    JFileChooser jFileChooser = new JFileChooser();
    String regex ;
    private File f;//打开的文件
    String initRegex = "(\\(?)[a-zA-Z]*:.*.(png|jpe?g|gif|svg)(\\)?)";
    JTextField jTextField = new JTextField();
    private void setjTextFiel(){
        jTextField.setText(regex);

    }


    /**
     *
     * @param regex 匹配图片的正则表达式
     * @param imagePath 匹配得到的图片资源路径  字符串 以换行符分割
     * @throws HeadlessException
     * @throws IOException
     */
    public GuiAction(String regex,String imagePath) throws HeadlessException, IOException {
        super("文档自动化复制图片");
        this.regex = regex;
        setjTextFiel();//显示正则表达式
        if(imagePath==null||imagePath.equals("")){
            imagePathList = new String[0];
        }
//        this.setExtendedState(JFrame.MAXIMIZED_BOTH);//全屏显示
//        setResizable(false);//禁止用户改变窗体大小
        setLayout(new BorderLayout());
        Filter_image.setClipboardString(null);//清空剪切版
        setSize(800,500);
        //添加滚动栏
        addPanel();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        txaDisplay.setText(imagePathList[0]);
        txaDisplay.setText("");
        txaDisplay.append("\t图片数量:"+imagePathList.length+"\t当前是第"+(i+1)+"张");

//        addKeyListener(new KeyListener() {
//            private int i = 0;
//            @Override
//            public void keyTyped(KeyEvent e) {}
//            @Override
//            public void keyPressed(KeyEvent e) {}
//            @Override
//            public void keyReleased(KeyEvent e) {
//                System.out.println(e.getKeyCode());
//                if (e.getKeyCode() == 86) {
//                    System.out.println("ctrl+v:黏贴");
////                    System.out.println(Filter_image.getClipboardString());
//                    Filter_image.setClipboardString(null);//清空剪切版
//                }else if (e.getKeyCode() == 67) {
//                    System.out.println("ctrl+c:复制字符");
//                    Filter_image.setClipboardString(imagePathList[i++]);
//                    System.out.println(Filter_image.getClipboardString());
//                }else if(e.getKeyCode()==71){
//                    System.out.println("ctrl+g:复制图片");
//                    if (i < imagePathList.length) {
//                        try {
//                            BufferedImage bufferedImage = Filter_image.readImage(imagePathList[i++]);
////                            GuiAction.this.setContentPane(new ImagePanel(bufferedImage));
//                            Filter_image.setClipboardImage(bufferedImage);
//                        } catch (IOException ex) {
//                            ex.printStackTrace();
//                        }
//                    }
//                }
//            }
//        });
    }
    public static int i = 0;
    public static int  length =0 ;
    //jpanel2 center用来显示图片 通过jlabel
    JLabel jLabel = new JLabel();
    private void addPanel() throws UnsupportedEncodingException {
        txaDisplay.setText("");//清空内容
        txaDisplay.setEditable(false);//不可编辑
        txaDisplay.setSelectedTextColor(Color.RED);
        txaDisplay.setLineWrap(true);//设置自己主动换行，之后则不须要设置水平滚动栏

       // Dimension size=txaDisplay.getPreferredSize();    //获得文本域的首选大小
        //把定义的JTextArea放到JScrollPane里面去
        JScrollPane scroll = new JScrollPane(txaDisplay);

//        scroll.setBounds(110,90,size.width,size.height);
//        scroll.setBounds(13,10,100,100);
        //默认的设置是超过文本框才会显示滚动条，以下设置让滚动条一直显示
        //分别设置水平和垂直滚动条总是出现
//        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel contentPane = new JPanel(new BorderLayout()) ;
        //-------最上面的滚动栏文本显示
        JPanel jpanel1 = new JPanel(new BorderLayout());
        jpanel1.add(scroll,BorderLayout.NORTH);
        //添加一个文本域：
        jpanel1.add(jTextField,BorderLayout.CENTER);
        Button button = new Button("修改匹配");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newregex =  jTextField.getText();
                if(f!=null){
                    try {
                        String imagePath = Filter_image.getImagePath(newregex, f);
                        if(imagePath.equals("")){
                            JOptionPane.showMessageDialog(GuiAction.this, "文件找不到这样格式的图片！", "警告", JOptionPane.ERROR_MESSAGE);
                        }
                        imagePathList = imagePath.split("\n");
                        regex = newregex;//更新匹配规则
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(GuiAction.this, "不合法表达式！", "警告", JOptionPane.ERROR_MESSAGE);
                        jTextField.setText(regex);
                    }catch (PatternSyntaxException e1){
                        JOptionPane.showMessageDialog(GuiAction.this, "不合法regex表达式！", "警告", JOptionPane.ERROR_MESSAGE);
                        regex = initRegex;
                    }
                    finally {
                        length = imagePathList.length;
                        i = 0;
                        txaDisplay.setText("\t图片数量:"+imagePathList.length+"\t当前是第"+(i+1)+"张\n");
                        txaDisplay.append(imagePathList[i]);
                        createLableImage(jLabel);//刷新图片显示
                    }
                }else{
                    regex = newregex;//更新匹配规则
                }

            }
        });
        jpanel1.add(button,BorderLayout.EAST);
        contentPane.add(jpanel1,BorderLayout.NORTH);

        //----------左边的按钮显示位置
        JPanel jpanel2 = new JPanel(new FlowLayout());
        Button button_up = new Button("上一个图片");//上一个图片
        button_up.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                i--;
                if(i<0){
                    i=0;
                }
                //可以尝试抽取代码
                txaDisplay.setText("\t图片数量:"+imagePathList.length+"\t当前是第"+(i+1)+"张\n");
                txaDisplay.append(imagePathList[i]);
                createLableImage(jLabel);//给jlabel添加图片
            }
        });
        Button button_on = new Button("下一个图片");//下一个图片
        button_on.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                i++;
                if(i>=length){
                    i = length-1;
                }
                txaDisplay.setText("\t图片数量:"+imagePathList.length+"\t当前是第"+(i+1)+"张\n");
                txaDisplay.append(imagePathList[i]);
                createLableImage(jLabel);//给jlabel添加图片
            }
        });
        Button button1 = new Button("copy image");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    BufferedImage bufferedImage = Filter_image.readImage(imagePathList[i]);
//                        i++;
                    Filter_image.setClipboardImage(bufferedImage);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(GuiAction.this, "异常，从头开始！", "警告", JOptionPane.ERROR_MESSAGE);
                    i = 0;
                }finally {
                    txaDisplay.setText("\t图片数量:"+imagePathList.length+"\t当前是第"+(i+1)+"张\n");
                    txaDisplay.append(imagePathList[i]);
                }
            }
        });
        Button button2 = new Button("copy path");
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (f == null || imagePathList == null || imagePathList.length == 0) {
                    return;
                }
                Filter_image.setClipboardString(imagePathList[i]);
            }
        });
        Button button_openfile = new Button("选择文件");
        button_openfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.showOpenDialog(null);
                f = jFileChooser.getSelectedFile();
                try {
                    String imagePath = Filter_image.getImagePath(regex, f);
                    if(imagePath.equals("")){
                        JOptionPane.showMessageDialog(GuiAction.this, "找不到这样格式的图片路径！", "警告", JOptionPane.ERROR_MESSAGE);
                        f = null;
                    }
                    imagePathList = imagePath.split("\n");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(GuiAction.this, "不是文件！", "警告", JOptionPane.ERROR_MESSAGE);
                }catch (PatternSyntaxException e1){
                    JOptionPane.showMessageDialog(GuiAction.this, "不合法regex表达式！", "警告", JOptionPane.ERROR_MESSAGE);
                    regex = initRegex;
                }
                finally {
                    length = imagePathList.length;
                    i = 0;
                    txaDisplay.setText("\t图片数量:"+imagePathList.length+"\t当前是第"+(i+1)+"张\n");
                    txaDisplay.append(imagePathList[i]);
                    jTextField.setText(regex);
                    createLableImage(jLabel);//刷新图片显示
                }
            }
        });
        Button about_me = new Button("关于软件");
        about_me.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //String url = "http://www.baidu.com";
                    String url = "https://blog.csdn.net/qq_44861675/article/details/122847079";
                    java.net.URI uri = java.net.URI.create(url);
                    // 获取当前系统桌面扩展
                    java.awt.Desktop dp = java.awt.Desktop.getDesktop();
                    // 判断系统桌面是否支持要执行的功能
                    if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
                        //File file = new File("D:\\aa.txt");
                        //dp.edit(file);// 　编辑文件
                        dp.browse(uri);// 获取系统默认浏览器打开链接
                        // dp.open(file);// 用默认方式打开文件
                        // dp.print(file);// 用打印机打印文件
                    }
                } catch (NullPointerException | IOException exception) {
                    // 此为uri为空时抛出异常
                    exception.printStackTrace();
                } // 此为无法获取系统默认浏览器

            }
        });
        Button zeor = new Button("选择第几张");
        zeor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(f==null){
                    JOptionPane.showMessageDialog(null,
                            "请先打开文件！","错误！",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String input = JOptionPane.showInputDialog(null,
                        "输入图片的索引(从0开始，小于"+length,"修改要复制的图片",JOptionPane.QUESTION_MESSAGE);
                if(input==null||input.equals("")){
                    return;
                }
                try{
                    int index = Integer.parseInt(input);
                    if(index<1||index>length){
                        JOptionPane.showMessageDialog(null,
                                "输入不是合法范围","错误！",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    i = index-1;
                    txaDisplay.setText("\t图片数量:"+imagePathList.length+"\t当前是第"+(i+1)+"张\n");
                    txaDisplay.append(imagePathList[i]);
                    createLableImage(jLabel);//刷新图片显示
                }catch (Exception exception){
                    JOptionPane.showMessageDialog(null,
                            "输入不是整数","错误！",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        jpanel2.add(button_up);
        jpanel2.add(button_on);
        jpanel2.add(zeor);
        jpanel2.add(button1);
        jpanel2.add(button2);
        jpanel2.add(button_openfile);
        jpanel2.add(about_me);

        //------------显示图片的jpanel
        JPanel pictureJpanel = new JPanel(new BorderLayout());
        int width = Math.max(pictureJpanel.getWidth()-10,10);
                int height =  Math.max(pictureJpanel.getHeight()-10,10);
        jLabel.setSize(width,height);
        pictureJpanel.add(jLabel);
        contentPane.add(pictureJpanel,BorderLayout.CENTER);
        contentPane.add(jpanel2,BorderLayout.SOUTH);
        this.setContentPane(contentPane);
    }

    /**
     * 按照实际的比例设置图片大小到jlabel
     * @param jLabel
     */
    private void createLableImage(JLabel jLabel){
        ImageIcon imageIcon = new ImageIcon(imagePathList[i]);
        Image image = imageIcon.getImage();
        int[] ints = dealPicture(imageIcon, jLabel);
        image = image.getScaledInstance(ints[0],ints[1], Image.SCALE_DEFAULT);
        imageIcon.setImage(image);
        jLabel.setIcon(imageIcon);
    }
    private int[] dealPicture(ImageIcon imageIcon,JLabel jLabel) {
        int width = imageIcon.getIconWidth();
        int height = imageIcon.getIconHeight();
        int jw = jLabel.getWidth();
        int jh = jLabel.getHeight();
//        System.out.println(jw+" jlabel  "+jh);
//        System.out.println(width+" picture   "+height);
        if(height<=0){
            return new int[]{100,100};
        }
        if(width<=jw && height<=jh){//无需缩放
            return new int[]{width,height};
        }
        //必须缩放 同时保证缩放得到的width和height都小于label的width和height
        double c1 = jw*1.0/width;
        double c2 = jh*1.0/height;
//        System.out.println(c1+"倍数"+c2);
        //必然有一个是正数
        double max = Math.min(c1,c2);
        width = (int)(width*max);
        height = (int)(height*max);
        return new int[]{width,height};
    }
//    获取等比例图片：


}
