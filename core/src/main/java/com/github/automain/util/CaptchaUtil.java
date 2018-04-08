package com.github.automain.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

public class CaptchaUtil {

    // 随机字符数组,去掉1,0,i,o几个容易混淆的字符
    private static final char[] CAPTCHA_CODES = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9'};
    // 随机数
    private static final Random RANDOM = new Random();
    // 默认验证码字符数
    private static final int CAPTCHA_SIZE = 4;
    // 图片
    private BufferedImage image;
    // 验证码
    private String captcha;

    public BufferedImage getImage() {
        return image;
    }

    public String getCaptcha() {
        return captcha;
    }

    private int getRandomNumber(int number) {
        return RANDOM.nextInt(number);
    }

    public CaptchaUtil(int width, int height) {
        this.createImage(width, height, CAPTCHA_SIZE);
    }

    public CaptchaUtil(int width, int height, int captchaSize) {
        this.createImage(width, height, captchaSize);
    }

    public String getBase64Image() throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", bos);
            return Base64.getEncoder().encodeToString(bos.toByteArray());
        }
    }

    public void createImage(int width, int height, int captchaSize) {
        int codesLen = CAPTCHA_CODES.length;
        StringBuilder tempCaptcha = new StringBuilder(captchaSize);
        for (int i = 0; i < captchaSize; i++) {
            tempCaptcha.append(CAPTCHA_CODES[getRandomNumber(codesLen - 1)]);
        }
        captcha = tempCaptcha.toString();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getRandColor(90, 150));
        g2.fillRect(0, 0, width, height);

        Color c = getRandColor(200, 250);
        g2.setColor(c);
        g2.fillRect(0, 2, width, height - 5);

        g2.setColor(getRandColor(140, 180));
        for (int i = 0; i < 50; i++) {
            int x = getRandomNumber(width - 1);
            int y = getRandomNumber(height - 1);
            int xl = getRandomNumber(12) + 1;
            int yl = getRandomNumber(12) + 1;
            g2.drawLine(x, y, x + xl + 40, y + yl + 20);
        }

        int area = (int) (0.05f * width * height);
        for (int i = 0; i < area; i++) {
            int x = getRandomNumber(width);
            int y = getRandomNumber(height);
            int rgb = getRandomIntColor();
            image.setRGB(x, y, rgb);
        }

        shear(g2, width, height, c);

        g2.setColor(getRandColor(100, 160));
        int fontSize = height - 4;
        g2.setFont(new Font("Algerian", Font.ITALIC, fontSize));
        char[] chars = captcha.toCharArray();
        for (int i = 0; i < captchaSize; i++) {
            AffineTransform affine = new AffineTransform();
            affine.setToRotation(Math.PI / 4 * RANDOM.nextDouble() * (RANDOM.nextBoolean() ? 1 : -1), (width / captchaSize) * i + fontSize / 2, height / 2);
            g2.setTransform(affine);
            g2.drawChars(chars, i, 1, ((width - 10) / captchaSize) * i + 5, height / 2 + fontSize / 2 - 10);
        }
        g2.dispose();
        this.image = image;
    }

    private Color getRandColor(int fc, int bc) {
        fc = fc > 255 ? 255 : fc;
        bc = bc > 255 ? 255 : bc;
        int r = fc + getRandomNumber(bc - fc);
        int g = fc + getRandomNumber(bc - fc);
        int b = fc + getRandomNumber(bc - fc);
        return new Color(r, g, b);
    }

    private int getRandomIntColor() {
        int color = 0;
        for (int i = 0; i < 3; i++) {
            color = color << 8;
            color = color | getRandomNumber(255);
        }
        return color;
    }

    private void shear(Graphics g, int width, int height, Color color) {
        shearX(g, width, height, color);
        shearY(g, width, height, color);
    }

    private void shearX(Graphics g, int width, int height, Color color) {
        int period = getRandomNumber(2);
        int frames = 1;
        int phase = getRandomNumber(2);
        for (int i = 0; i < height; i++) {
            double d = (double) (period >> 1)
                    * Math.sin((double) i / (double) period
                    + (6.2831853071795862D * (double) phase)
                    / (double) frames);
            g.copyArea(0, i, width, 1, (int) d, 0);
            g.setColor(color);
            g.drawLine((int) d, i, 0, i);
            g.drawLine((int) d + width, i, width, i);
        }

    }

    private void shearY(Graphics g, int width, int height, Color color) {
        int period = getRandomNumber(40) + 10;
        int frames = 20;
        int phase = 7;
        for (int i = 0; i < width; i++) {
            double d = (double) (period >> 1)
                    * Math.sin((double) i / (double) period
                    + (6.2831853071795862D * (double) phase)
                    / (double) frames);
            g.copyArea(i, 0, 1, height, 0, (int) d);
            g.setColor(color);
            g.drawLine(i, (int) d, i, 0);
            g.drawLine(i, (int) d + height, i, height);
        }

    }

}
