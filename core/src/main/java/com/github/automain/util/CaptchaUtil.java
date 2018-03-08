package com.github.automain.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

public class CaptchaUtil {

    private static final Color BACKGROUND = new Color(232, 232, 232);

    private static Random random = new Random();

    // 图片的宽度。
    private int width;
    // 图片的高度。
    private int height;
    // 百分比位置
    private int percentPosition;
    // 图片
    private BufferedImage image = null;

    public CaptchaUtil(int width, int height) {
        this.width = width;
        this.height = height;
        this.createImg();
    }

    public int getPercentPosition() {
        return percentPosition;
    }

    public BufferedImage getImage() {
        return image;
    }

    private void createImg() {
        Random r = new Random();
        int bound = width - 2 * height;
        percentPosition = r.nextInt(bound) + height;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        // 整体背景灰色
        g.setColor(BACKGROUND);
        g.fillRect(0, 0, width, height);
        // 绘制干扰线
        for (int i = 0; i < 10; i++) {
            drawLine(g, getRandomNumber(width), getRandomNumber(height), getRandomNumber(width), getRandomNumber(height));
        }
        // 绘制目标网格
        int step = height / 8;
        for (int i = 0; i < 8; i++) {
            int range = i * step;
            drawLine(g, percentPosition, range, percentPosition + height - range, height);
            drawLine(g, percentPosition + range, 0, percentPosition + height, height - range);
            drawLine(g, percentPosition, range, percentPosition + range, 0);
            drawLine(g, percentPosition + range, height, percentPosition + height, range);
        }
        g.dispose();
    }

    /**
     * 返回base64编码的图片
     *
     * @return
     */
    public String getBase64Image() throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", bos);
            return EncryptUtil.BASE64Encode(bos.toByteArray());
        }
    }

    private void drawLine(Graphics2D g, int xs, int ys, int xe, int ye) {
        g.setColor(new Color(getRandomNumber(255), getRandomNumber(255), getRandomNumber(255)));
        g.drawLine(xs, ys, xe, ye);
    }

    /**
     * 获取随机数
     *
     * @param number
     * @return
     */
    private int getRandomNumber(int number) {
        return random.nextInt(number);
    }

}
