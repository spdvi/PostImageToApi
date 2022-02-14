package spdvi.postimagetoapi;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageUtils {
    public static final String DEFAULT_PROFILE_IMAGE_TYPE = "jpg";
    
    public static InputStream resizeImage(File image, int desiredWidth, int desiredHeight) {
        int newHeight = 0;
        int newWidth = 0;
        BufferedImage originalBufferedImage = null;
        ByteArrayInputStream bais = null;
        try {
            originalBufferedImage = ImageIO.read(image);
            float aspectRatio = (float) originalBufferedImage.getWidth() / originalBufferedImage.getHeight();
            if (originalBufferedImage.getWidth() > originalBufferedImage.getHeight()) {
                newWidth = desiredWidth;
                newHeight = Math.round(desiredWidth / aspectRatio);
            } else {
                newHeight = desiredHeight;
                newWidth = Math.round(desiredHeight * aspectRatio);
            }
            BufferedImage resultingImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = resultingImage.createGraphics();
            graphics2D.drawImage(originalBufferedImage, 0, 0, newWidth, newHeight, null);
            graphics2D.dispose();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resultingImage, DEFAULT_PROFILE_IMAGE_TYPE, baos);
            bais = new ByteArrayInputStream(baos.toByteArray());

            baos.close();
            bais.close();

        } catch (IOException ex) {
            Logger.getLogger(ImageUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return bais;
    }

    public static ImageIcon resizeImageIcon(File image, int desiredWidth, int desiredHeight) {
        int newHeight = 0;
        int newWidth = 0;
        BufferedImage originalBufferedImage = null;
        ImageIcon icon = null;

        try {
            originalBufferedImage = ImageIO.read(image);
            float aspectRatio = (float) originalBufferedImage.getWidth() / originalBufferedImage.getHeight();
            if (originalBufferedImage.getWidth() > originalBufferedImage.getHeight()) {
                newWidth = desiredWidth;
                newHeight = Math.round(desiredWidth / aspectRatio);
            } else {
                newHeight = desiredHeight;
                newWidth = Math.round(desiredHeight * aspectRatio);
            }
            BufferedImage resultingImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = resultingImage.createGraphics();
            graphics2D.drawImage(originalBufferedImage, 0, 0, newWidth, newHeight, null);
            graphics2D.dispose();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resultingImage, DEFAULT_PROFILE_IMAGE_TYPE, baos);
            icon = new ImageIcon(baos.toByteArray());

            baos.close();

        } catch (IOException ex) {
            Logger.getLogger(ImageUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return icon;
    }

    public static byte[] readAllBytes(InputStream inputStream) throws IOException {
        final int bufLen = 1024;
        byte[] buf = new byte[bufLen];
        int readLen;
        IOException exception = null;

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            while ((readLen = inputStream.read(buf, 0, bufLen)) != -1) {
                outputStream.write(buf, 0, readLen);
            }

            return outputStream.toByteArray();
        } catch (IOException e) {
            exception = e;
            throw e;
        } finally {
            if (exception == null) {
                inputStream.close();
            } else
                try {
                    inputStream.close();
                } catch (IOException e) {
                    exception.addSuppressed(e);
                }
        }
    }

    // Adapted from
    // http://www.java2s.com/example/java-utility-method/image-to-byte-array/tobytearray-image-image-1eb3b.html
    public static byte[] toByteArray(Image image) throws Exception {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(toBufferedImage(image), "jpg", bos);
        return bos.toByteArray();

    }// from www .ja v a2s . c o m

    public static BufferedImage toBufferedImage(Image image) throws Exception {

        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }

        image = new ImageIcon(image).getImage();

        boolean hasAlpha = hasAlpha(image);

        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        int transparency = Transparency.OPAQUE;

        if (hasAlpha) {

            transparency = Transparency.BITMASK;

        }

        GraphicsDevice gs = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gs.getDefaultConfiguration();
        bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);

        if (bimage == null) {

            int type = BufferedImage.TYPE_INT_RGB;

            if (hasAlpha) {

                type = BufferedImage.TYPE_INT_ARGB;

            }

            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);

        }

        Graphics g = bimage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
    }

    public static boolean hasAlpha(Image image) throws InterruptedException {

        if (image instanceof BufferedImage) {

            BufferedImage bi = (BufferedImage) image;
            return bi.getColorModel().hasAlpha();

        }

        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        pg.grabPixels();

        ColorModel cm = pg.getColorModel();
        return cm.hasAlpha();

    }

    public static ImageIcon byteArrayToImage(byte[] profilePicture) {
        ImageIcon icon = new ImageIcon(profilePicture);
        return icon;
    }
}

