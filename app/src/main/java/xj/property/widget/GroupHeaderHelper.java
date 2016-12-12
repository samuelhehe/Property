package xj.property.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.activeandroid.query.Select;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import xj.property.XjApplication;
import xj.property.beans.UserGroupBean;
import xj.property.cache.GroupHeader;
import xj.property.utils.image.utils.ImageUtils;
import xj.property.utils.other.Config;

/**
 * Created by Administrator on 2015/4/14.
 *
 *
 */
public class GroupHeaderHelper {
    private static int width = 300, height = 300;

    private static Paint p = new Paint();


    public static void createGroupId(final List<UserGroupBean> beans, final String groupId, final Context context) {
        if (beans.size() > 1 && !XjApplication.getInstance().groupIds.contains(groupId))
            XjApplication.getInstance().pool.execute(new Runnable() {
                @Override
                public void run() {
                    XjApplication.getInstance().groupIds.add(groupId);
                    List<Bitmap> bms = new ArrayList<Bitmap>();
                    for (int i = 0; i < beans.size(); i++) {
                        Bitmap bm = ImageUtils.GetLocalOrNetBitmap(beans.get(i).getAvatar());
//                        Bitmap bm = ImageUtils.GetLocalOrNetBitmap(beans.get(i).getAvatar()+"?imageView/1/w/150/h/150");
                        if (bm != null)
                            bms.add(bm);
                        if (bms.size() > 9) break;
                    }
                    GroupHeader header = new Select().from(GroupHeader.class).where("group_id = ?", groupId).executeSingle();
                    File cacheDir = StorageUtils.getOwnCacheDirectory(context, Config.BASE_GROUP_CACHE);
                    File file = new File(cacheDir, groupId + ".png");
                    if (header == null || header.getNum() < 10) {
//                        while (bms.contains(null)){
//                            bms.remove(null);
//                        }
                        header = new GroupHeader(groupId, file.getAbsolutePath(), bms.size());
                        header.save();
                        //TODO 屏蔽没有头像的用户
//                        for(int i=bms.size();i<beans.size()&&i<10;i++){
//                            bms.add(BitmapFactory.decodeResource(XjApplication.getInstance().getResources(),R.drawable.head_portrait_personage));
//                        }
                    }
                    Bitmap bm = GroupHeaderHelper.getGroupBitmap(bms);
                    try {
                        FileOutputStream outputStream = new FileOutputStream(file);
                        bm.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (XjApplication.getInstance().groupIds.contains(groupId))
                            XjApplication.getInstance().groupIds.remove(groupId);
                    }
                }
            });
        else {

        }
    }

    public static Bitmap getGroupBitmap(List<Bitmap> bms) {
        int lineSize = 30;
        int imageWidth = width + 50;
        int imageHeight = height + 50;
        int wlineNum = 0;
        int hlineNum = 0;
        int everyRectW = 0;
        int everyRectH = 0;
        int startx = 0;
        int starty = 0;

        if (bms == null || bms.size() == 0) return null;
        Bitmap screenshot = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(screenshot);
        int top = 0;
        int left = 0;
        List<Bitmap> bitmaps = new ArrayList<>();
        p.setColor(Color.parseColor("#d9dfdf"));

        switch (bms.size()) {
            case 1:
                for (int i = 0; i < bms.size(); i++) {
                    bitmaps.add(ImageUtils.zoomImg(bms.get(i), width, height));
                }
                canvas.drawRect(0, 0, imageWidth, imageHeight, p);
                canvas.drawBitmap(bitmaps.get(0), lineSize, lineSize, p);
                break;
            case 2: //左右剧中
//                    top  =  (height- bms.get(0).getHeight())/2;
                wlineNum = 2;
                for (int i = 0; i < bms.size(); i++) {
                    bitmaps.add(ImageUtils.zoomImg(bms.get(i), width / 2, height / 2));
                }
                canvas.drawRect(0, 0, imageWidth, imageHeight, p);

                everyRectW = imageWidth / wlineNum;
                startx = (everyRectW - bitmaps.get(0).getWidth()) / 2;

                canvas.drawBitmap(bitmaps.get(0), startx + startx / 2, imageHeight / 2 - bitmaps.get(0).getHeight() / 2, p);
                canvas.drawBitmap(bitmaps.get(1), everyRectW + startx / 2, imageHeight / 2 - bitmaps.get(0).getHeight() / 2, p);
                break;
            case 3:// 品字
//                    left= (width-bms.get(0).getWidth())/2;
                wlineNum = 2;
                hlineNum = 2;

                everyRectW = imageWidth / wlineNum;
                everyRectH = imageHeight / hlineNum;

                for (int i = 0; i < bms.size(); i++) {
                    bitmaps.add(ImageUtils.zoomImg(bms.get(i), width / 2, height / 2));
                }

                startx = (everyRectW - bitmaps.get(0).getWidth()) / 2;
                starty = (everyRectH - bitmaps.get(0).getHeight()) / 2;

                canvas.drawRect(0, 0, imageWidth, imageHeight, p);

                canvas.drawBitmap(bitmaps.get(0), imageWidth / 2 - (bitmaps.get(0).getWidth() / 2), starty, p);
                canvas.drawBitmap(bitmaps.get(1), startx + startx / 2, everyRectH + starty, p);
                canvas.drawBitmap(bitmaps.get(2), everyRectW + startx / 2, everyRectH + starty, p);
                break;
            case 4://正方形

                wlineNum = 2;
                hlineNum = 2;

                everyRectW = imageWidth / wlineNum;
                everyRectH = imageHeight / hlineNum;


                for (int i = 0; i < bms.size(); i++) {
                    bitmaps.add(ImageUtils.zoomImg(bms.get(i), width / 2, height / 2));
                }

                startx = (everyRectW - bitmaps.get(0).getWidth()) / 2;
                starty = (everyRectH - bitmaps.get(0).getHeight()) / 2;

                canvas.drawRect(0, 0, imageWidth, imageHeight, p);

                canvas.drawBitmap(bitmaps.get(0), startx + startx / 2, starty + starty / 2, p);
                canvas.drawBitmap(bitmaps.get(1), everyRectW + startx / 2, starty + starty / 2, p);
                canvas.drawBitmap(bitmaps.get(2), startx + startx / 2, everyRectH + starty / 2, p);
                canvas.drawBitmap(bitmaps.get(3), everyRectW + startx / 2, everyRectH + starty / 2, p);
                break;
            case 5://上二下三
                for (int i = 0; i < bms.size(); i++) {
                    bitmaps.add(ImageUtils.zoomImg(bms.get(i), width / 3, height / 3));
                }

                wlineNum = 2;
                hlineNum = 2;

                everyRectW = imageWidth / wlineNum;
                everyRectH = imageHeight / hlineNum;

                startx = (everyRectW - bitmaps.get(0).getWidth()) / 2;
                starty = (everyRectH - bitmaps.get(0).getHeight()) / 2;

                canvas.drawRect(0, 0, imageWidth, imageHeight, p);
                canvas.drawBitmap(bitmaps.get(0), startx + startx, starty + starty + starty / 4, p);
                canvas.drawBitmap(bitmaps.get(1), everyRectW + startx - startx / 2, starty + starty + starty / 4, p);

                wlineNum = 3;
                everyRectW = imageWidth / wlineNum;
                startx = (everyRectW - bitmaps.get(0).getWidth()) / 2;

                canvas.drawBitmap(bitmaps.get(2), startx + startx / 2, everyRectH + starty / 2 - starty / 4, p);
                canvas.drawBitmap(bitmaps.get(3), everyRectW + startx, everyRectH + starty / 2 - starty / 4, p);
                canvas.drawBitmap(bitmaps.get(4), everyRectW * 2 + startx / 2, everyRectH + starty / 2 - starty / 4, p);
                break;
            case 6://两排剧终
                for (int i = 0; i < bms.size(); i++) {
                    bitmaps.add(ImageUtils.zoomImg(bms.get(i), width / 3, height / 3));
                }

                wlineNum = 3;
                hlineNum = 2;

                everyRectW = imageWidth / wlineNum;
                everyRectH = imageHeight / hlineNum;

                startx = (everyRectW - bitmaps.get(0).getWidth()) / 2;
                starty = (everyRectH - bitmaps.get(0).getHeight()) / 2;

                canvas.drawRect(0, 0, imageWidth, imageHeight, p);
                canvas.drawBitmap(bitmaps.get(0), startx + startx / 2, starty + starty / 2, p);
                canvas.drawBitmap(bitmaps.get(1), everyRectW + startx, starty + starty / 2, p);
                canvas.drawBitmap(bitmaps.get(2), everyRectW * 2 + startx / 2, starty + starty / 2, p);

                canvas.drawBitmap(bitmaps.get(3), startx + startx / 2, everyRectH + starty / 2, p);
                canvas.drawBitmap(bitmaps.get(4), everyRectW + startx, everyRectH + starty / 2, p);
                canvas.drawBitmap(bitmaps.get(5), everyRectW * 2 + startx / 2, everyRectH + starty / 2, p);
                break;
            case 7:
            case 8:
            case 9://九宫格
            default:

                for (int i = 0; i < bms.size() && i < 9; i++) {
                    bitmaps.add(i, ImageUtils.zoomImg(bms.get(i), width / 3, height / 3));
                }
                wlineNum = 3;
                hlineNum = 3;

                everyRectW = imageWidth / wlineNum;
                everyRectH = imageHeight / hlineNum;

                startx = (everyRectW - bitmaps.get(0).getWidth()) / 2;
                starty = (everyRectH - bitmaps.get(0).getHeight()) / 2;

                canvas.drawRect(0, 0, imageWidth, imageHeight, p);
                for (int i = 0; i < bitmaps.size(); i++) {
                    top = imageHeight / 3 * (i / 3) + starty;
                    left = imageWidth / 3 * (i % 3) + startx;
                    canvas.drawBitmap(bitmaps.get(i), left, top, p);
                    bitmaps.get(i).recycle();
                }
                break;

        }
        return screenshot;
    }

}
