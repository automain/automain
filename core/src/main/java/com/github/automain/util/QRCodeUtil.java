package com.github.automain.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <img src="data:image/png;base64,${imgStr}"/>
 */
public class QRCodeUtil {

    public static final int QR_CODE_DEFAULT_HEIGHT = 300;
    public static final int QR_CODE_DEFAULT_WIDTH = 300;

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    public static String createBase64QRCode(String data) throws Exception {
        return createBase64QRCode(data, QR_CODE_DEFAULT_WIDTH, QR_CODE_DEFAULT_HEIGHT, null);
    }

    public static String createBase64QRCode(String data, String logoFilePath) throws Exception {
        return createBase64QRCode(data, QR_CODE_DEFAULT_WIDTH, QR_CODE_DEFAULT_HEIGHT, logoFilePath);
    }

    public static String createBase64QRCode(String data, int width, int height) throws Exception {
        return createBase64QRCode(data, width, height, null);
    }

    public static String createBase64QRCode(String data, int width, int height, String logoFilePath) throws Exception {
        BufferedImage image = createQRCode(data, width, height, logoFilePath);
        return bufferedImageToBase64(image);
    }

    public static BufferedImage createQRCode(String data) throws Exception {
        return createQRCode(data, QR_CODE_DEFAULT_WIDTH, QR_CODE_DEFAULT_HEIGHT, null);
    }

    public static BufferedImage createQRCode(String data, String logoFilePath) throws Exception {
        return createQRCode(data, QR_CODE_DEFAULT_WIDTH, QR_CODE_DEFAULT_HEIGHT, logoFilePath);
    }

    public static BufferedImage createQRCode(String data, int width, int height) throws Exception {
        return createQRCode(data, width, height, null);
    }

    public static BufferedImage createQRCode(String data, int width, int height, String logoFilePath) throws Exception {
        BufferedImage qrCode = null;
        Map<EncodeHintType, Object> hint = new HashMap<EncodeHintType, Object>();
        hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hint.put(EncodeHintType.CHARACTER_SET, PropertiesUtil.DEFAULT_CHARSET);
        hint.put(EncodeHintType.MARGIN, 0);
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix matrix = multiFormatWriter.encode(data, BarcodeFormat.QR_CODE, width, height, hint);
        qrCode = bitMatrixToBufferedImage(matrix);
        if (logoFilePath != null) {
            File logoFile = new File(logoFilePath);
            if (SystemUtil.checkFileAvailable(logoFile)) {
                BufferedImage logo = ImageIO.read(logoFile);
                Graphics2D g = (Graphics2D) qrCode.getGraphics();
                int smallWidth = width / 5;
                int smallHeight = height / 5;
                int bigWidth = smallWidth * 2;
                int bigHeight = smallHeight * 2;
                g.drawImage(logo, bigWidth, bigHeight, smallWidth, smallHeight, null);
                BasicStroke stroke = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
                g.setStroke(stroke);
                RoundRectangle2D.Float round = new RoundRectangle2D.Float(bigWidth, bigHeight, smallWidth, smallHeight, 20, 20);
                g.setColor(Color.white);
                g.draw(round);
                BasicStroke stroke2 = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
                g.setStroke(stroke2);
                RoundRectangle2D.Float round2 = new RoundRectangle2D.Float(bigWidth + 2, bigHeight + 2, smallWidth - 4, smallHeight - 4, 20, 20);
                g.setColor(new Color(128, 128, 128));
                g.draw(round2);
                g.dispose();
            }
        }
        qrCode.flush();
        return qrCode;
    }

    private static BufferedImage bitMatrixToBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    private static String bufferedImageToBase64(BufferedImage image) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", bos);
            return EncryptUtil.BASE64Encode(bos.toByteArray());
        }
    }

}
