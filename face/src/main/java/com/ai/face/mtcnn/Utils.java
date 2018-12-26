package com.ai.face.mtcnn;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Vector;

/**
 * <b>Project:</b> FaceRecognition <br>
 * <b>Create Date:</b> 2018/12/23 <br>
 * <b>@author:</b> qy <br>
 * <b>Address:</b> qingyongai@gmail.com <br>
 * <b>Description:</b>  <br>
 */
public class Utils {

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;

        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//
            // 这里压缩options%，把压缩后的数据存放到baos中
            if (options > 10) {//设置最小值，防止低于0时出异常
                options -= 10;// 每次都减少10
            }
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//
        // 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 图片转base 64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * 复制图片，并设置isMutable=true
     *
     * @param bitmap
     * @return
     */
    public static Bitmap copyBitmap(Bitmap bitmap) {
        return bitmap.copy(bitmap.getConfig(), true);
    }

    /**
     * 在bitmap中画矩形
     *
     * @param bitmap
     * @param rect
     */
    public static void drawRect(Bitmap bitmap, Rect rect) {
        try {
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            int r = 255;//(int)(Math.random()*255);
            int g = 0;//(int)(Math.random()*255);
            int b = 0;//(int)(Math.random()*255);
            paint.setColor(Color.rgb(r, g, b));
            paint.setStrokeWidth(1 + bitmap.getWidth() / 500);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(rect, paint);
        } catch (Exception e) {
            Log.e("Utils", "[*] error" + e);
        }
    }

    /**
     * 在图中画点
     *
     * @param bitmap
     * @param landmark
     */
    public static void drawPoints(Bitmap bitmap, Point[] landmark) {
        for (int i = 0; i < landmark.length; i++) {
            int x = landmark[i].x;
            int y = landmark[i].y;
            //Log.i("Utils","[*] landmarkd "+x+ "  "+y);
            drawRect(bitmap, new Rect(x - 1, y - 1, x + 1, y + 1));
        }
    }

    /**
     * Flip alone diagonal
     * 对角线翻转。data大小原先为h*w*stride，翻转后变成w*h*stride
     *
     * @param data
     * @param h
     * @param w
     * @param stride
     */
    public static void flip_diag(float[] data, int h, int w, int stride) {
        float[] tmp = new float[w * h * stride];
        for (int i = 0; i < w * h * stride; i++) {
            tmp[i] = data[i];
        }
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                for (int z = 0; z < stride; z++) {
                    data[(x * h + y) * stride + z] = tmp[(y * w + x) * stride + z];
                }
            }
        }
    }

    /**
     * src转为二维存放到dst中
     *
     * @param src
     * @param dst
     */
    public static void expand(float[] src, float[][] dst) {
        int idx = 0;
        for (int y = 0; y < dst.length; y++)
            for (int x = 0; x < dst[0].length; x++)
                dst[y][x] = src[idx++];
    }

    /**
     * src转为三维存放到dst中
     *
     * @param src
     * @param dst
     */
    public static void expand(float[] src, float[][][] dst) {
        int idx = 0;
        for (int y = 0; y < dst.length; y++)
            for (int x = 0; x < dst[0].length; x++)
                for (int c = 0; c < dst[0][0].length; c++)
                    dst[y][x][c] = src[idx++];

    }

    /**
     * dst=src[:,:,1]
     *
     * @param src
     * @param dst
     */
    public static void expandProb(float[] src, float[][] dst) {
        int idx = 0;
        for (int y = 0; y < dst.length; y++)
            for (int x = 0; x < dst[0].length; x++)
                dst[y][x] = src[idx++ * 2 + 1];
    }

    /**
     * box转化为rect
     *
     * @param boxes
     * @return
     */
    public static Rect[] boxes2rects(Vector<Box> boxes) {
        int cnt = 0;
        for (int i = 0; i < boxes.size(); i++) if (!boxes.get(i).deleted) cnt++;
        Rect[] r = new Rect[cnt];
        int idx = 0;
        for (int i = 0; i < boxes.size(); i++)
            if (!boxes.get(i).deleted)
                r[idx++] = boxes.get(i).transform2Rect();
        return r;
    }

    /**
     * 删除做了delete标记的box
     *
     * @param boxes
     * @return
     */
    public static Vector<Box> updateBoxes(Vector<Box> boxes) {
        Vector<Box> b = new Vector<Box>();
        for (int i = 0; i < boxes.size(); i++)
            if (!boxes.get(i).deleted)
                b.addElement(boxes.get(i));
        return b;
    }

    static public void showPixel(int v) {
        Log.i("MainActivity", "[*]Pixel:R" + ((v >> 16) & 0xff) + "G:" + ((v >> 8) & 0xff) + " B:" + (v & 0xff));
    }
}
