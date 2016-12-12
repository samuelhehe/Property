package xj.property.utils;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

/**
 * Created by n on 2015/8/12.
 */
public class QRCodeUtil {

    private static  int QR_WIDTH = 500;
    private static int QR_HEIGHT = 500;
    private static int[] pixels = new int[QR_WIDTH * QR_HEIGHT];

    /**
     * 根据输入的文本生成一个二维码
     *
     * @param numbers
     */
    public static Bitmap generateCode(long numbers) {
        try {
            QRCodeWriter writer = new QRCodeWriter();

            if (numbers == 0) {
                return null;
            }

            // 把输入的文本转为二维码
            BitMatrix martix = writer.encode(numbers + "", BarcodeFormat.QR_CODE,
                    QR_WIDTH, QR_HEIGHT);

            System.out.println("w:" + martix.getWidth() + "h:"
                    + martix.getHeight());

            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(numbers + "",
                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }

                }
            }

            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Bitmap.Config.ARGB_8888);

            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
}
