/**
 * @author 60417
 * @date 2022/2/8
 * @time 16:08
 * @todo
 */
package com.yuyefanhua.App;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    Image img;
    public ImagePanel(Image img){
        this.img = img;
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);

    }
}
