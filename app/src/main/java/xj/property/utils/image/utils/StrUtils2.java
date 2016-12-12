package xj.property.utils.image.utils;

import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xj.property.beans.BaseBean;


/**
 * 旧的签名方式 2015/10/23 21:49
 */
public class StrUtils2 {

    /**
     * 判断字符串为网址, 提取如 www.p-pass.com/123456zx 中的 123456
     *
     * @param content
     * @return
     */
    public static String regexZxContent(String content) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }

        String src = content.toLowerCase();
        String reg = "(http://|https://|www){1}[\\w\\.\\-/:]+";
        Pattern pt = Pattern.compile(reg);
        Matcher mt = pt.matcher(src);

        String zxBar = "";
        while (mt.find()) {
            zxBar = mt.group();
            break;
        }

        if (!TextUtils.isEmpty(zxBar) && zxBar.equals(src)
                && (zxBar.lastIndexOf("/") > 0)
                && zxBar.toLowerCase().endsWith("zx")) {
            try {
                String endStr = zxBar.substring(zxBar.lastIndexOf("/") + 1,
                        zxBar.length() - "zx".length());
                if (!TextUtils.isEmpty(endStr)
                        && endStr.equals(getBeginNumber(endStr))) {
                    return endStr;
                } else {
                    return "";
                }
            } catch (IndexOutOfBoundsException e) {
                return "";
            } catch (Exception e) {
                return "";
            }
        }

        return "";
    }

    /**
     * 匹配数字
     */
    public static String findNum(String str) {
        str = str.trim();
        String str2 = "";
        if (str != null && !"".equals(str)) {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
                    str2 += str.charAt(i);
                }
            }

        }
        return str2;
    }

    /**
     * gbk 转 utf8
     *
     * @param str
     * @return
     */
    public static String gb2utf8(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }

        String newstr = "";
        try {
            newstr = new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            newstr = str;
        }

        return newstr;
    }

    /**
     * String 转 int 值
     *
     * @param str
     * @return
     */
    public static int strToInt(String str) {
        int i = 0;
        if (TextUtils.isEmpty(str) || str.equals("null")) {
            return 0;
        } else {
            try {
                i = Integer.parseInt(str);
            } catch (Exception e) {
                i = 0;
            }
        }

        return i;
    }

    /**
     * 去掉空指针或为null情况
     *
     * @param str
     * @return
     */
    public static String tripNull(String str) {
        if (TextUtils.isEmpty(str) || str.equals("null")) {
            return "";
        } else {
            return str;
        }
    }

    /**
     * 将URL中的中文转为UTF-8编码
     *
     * @param str
     * @return
     */
    public static String decodeUrl(String str) {
        return URLDecoder.decode(str);
    }

    /**
     * 将字符串中的中文转为URL编码
     *
     * @param str
     * @return
     */
    public static String encodeUrl(String str) {
        String tstr = "";
        try {
            tstr = URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            tstr = str;
        }

        return tstr;
    }

    /**
     * 将字符串转为md5
     *
     * @param str
     * @return
     */
    public static String string2md5(String str) {
        MessageDigest msgDigest = null;

        try {
            msgDigest = MessageDigest.getInstance("MD5");
            msgDigest.reset();
            msgDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return str;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] byteArray = msgDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(
                        Integer.toHexString(0xFF & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }

        return md5StrBuff.toString();
    }

    /**
     * 获取字符串内部第一段数字子串
     *
     * @param srcStr
     * @return
     */
    public static String getBeginNumber(String srcStr) {
        String childStr = "";
        if (TextUtils.isEmpty(srcStr)) {
            return childStr;
        }

        String reg = "\\d*";
        Pattern pt = Pattern.compile(reg);
        Matcher mt = pt.matcher(srcStr);
        while (mt.find()) {
            childStr = mt.group();
            break;
        }

        if (!(TextUtils.isEmpty(childStr))) {
            if (!(srcStr.startsWith(childStr))) {
                childStr = "";
            }
        }

        return childStr;
    }

    /**
     * 根据路径创建图片的url
     *
     * @return
     */
/*	public static String createImgUrl(String path) {
        return new StringBuffer("http://" + Config.SERVER_HOST_NAME + ":"
				+ Config.SERVER_PORT + "/" + path).toString();
	}*/
    public static String subString(String str, int len) {
        if (str.length() < len)
            return str;
        int size = str.length() / len;
        boolean flag = (str.length() % len == 0);
        StringBuilder mstr = new StringBuilder();
        for (int i = 0; i < size; i++) {
            mstr.append(str.substring(i * len, (i + 1) * len));
            mstr.append("\n");
        }
        if (!flag) {
            mstr.append(str.substring(size * len));
        } else {
            mstr.deleteCharAt(mstr.length() - 1);
        }
        return mstr.toString();
    }

    /**
     * 判断是不是手机号
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNum(String mobiles) {
        Pattern p = Pattern
                .compile("^(0|86|17951)?(13[0-9]|15[012356789]|17[6780]|18[0-9]|14[57])[0-9]{8}$");
        Matcher m = p.matcher(mobiles);

        return m.matches();
    }

    //获取天数
    public static String getDay4Date(String begin, String end)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long date = sdf.parse(end).getTime() - sdf.parse(begin).getTime();
        return date / (1000 * 60 * 60 * 24) + "";
    }

    //获取时长
    public static String getTime(int millions) {
        int hour = millions / (60 * 60);
        int minute = (millions / 60) % 60;
        int second = millions % 60;
//        int day=millions/360/24;
        return hour + "小时" + minute + "分" + second + "秒";
    }

    //判断在不在时间段内
    public static boolean isUnServiceTime(String startTime, String endTime) throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
//
//        Calendar calendarStart=Calendar.getInstance();
//        calendarStart.setTime(sdf.parse(startTime));
//        Calendar calendarEnd=Calendar.getInstance();
//        calendarEnd.setTime(sdf.parse(endTime));
//        Calendar calendarNow=Calendar.getInstance();
//        calendarEnd.setTime(new Date());
//        if(calendarNow.get(Calendar.HOUR_OF_DAY)<calendarStart.get(Calendar.HOUR_OF_DAY)||)
////        Log.i("onion", endTime + "endTime" + sdf.parse(endTime).getTime());
////        int day=millions/360/24;
        SimpleDateFormat formatters = new SimpleDateFormat("HH:mm");
        Date curDates = new Date(System.currentTimeMillis());// 获取当前时间
        String strs = formatters.format(curDates);
        //开始时间
        String starts[]=new String[]{};
        starts=startTime.split(":");

        int sth = Integer.parseInt(starts[0]);//小时
        int stm =Integer.parseInt(starts[1]) ;//秒
        //结束时间
        String ends[]=new String[]{};
        ends=endTime.split(":");
        int eth = Integer.parseInt(ends[0]);//小时
        int etm = Integer.parseInt(ends[1]);//秒

        String[] dds = new String[] {};

        // 分取系统时间 小时分
        dds = strs.split(":");
        int dhs = Integer.parseInt(dds[0]);
        int dms = Integer.parseInt(dds[1]);

        if (sth <dhs && dhs < eth) {
           Log.i("onion","在范围内");
            return  true;
        } else if(sth==dhs&& stm<=dms){
            Log.i("onion","在范围内");
            return true;
        }else  if(eth==dhs&&dms<=etm){
            Log.i("onion","在范围内");
return true;
        }else
            Log.i("onion","在范围外");
        return false;
    }

    //获取日期
    public static String getDate4Second(int millions) {

        Date date = new Date(millions*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    //获取日期
    public static String getDate4LifeCircleDay(int millions) {

        Date date = new Date(millions*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        return sdf.format(date);
    }

    //获取日期
    public static String getTime4Millions(long millions) {

        Date date = new Date(millions);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        return sdf.format(date);
    }

    //获取日期时间
    public static String getTime6Millions(long millions) {

        Date date = new Date(millions);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sdf.format(date);
    }

    //获取日期
    public static String getTime42Millions(long millions) {
        Date date = new Date(millions);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    //获取星期
    public static String getDate4Millions(long millions) {

        Date date = new Date(millions);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        String[] weekOfDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return 1 + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "\t" + weekOfDays[w];
    }


    //获取星期
    public static String getDate24Millions(long millions) {

        Date date = new Date(millions);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        String[] weekOfDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return 1 + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
    }


    //获取星期
    public static String getWeek4Millions(long millions) {

        Date date = new Date(millions);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        String[] weekOfDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekOfDays[w];
    }


    //流转String
    public static String Stream2String(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is), 16 * 1024); //强制缓存大小为16KB，一般Java类默认为8KB
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {  //处理换行符
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    /**
     * 装配商品列表item字符串
     */
    public static String str2SingleGoods(String name, TextPaint paint, int textviewwidth) {
        name = name.trim();
        if (paint.measureText(name) < textviewwidth)
            return name + "\n";
        else {
            if (name.length() > 15) name = name.substring(0, 15);
            if (paint.measureText(name) > textviewwidth) {
                while (true) {
                    name = name.substring(0, name.length() - 1);
                    if (paint.measureText(name) < textviewwidth - 40)
                        return name + "..\n";
                }

            }
        }
        return "";
    }
    /**
     * 装配通知字符串
     */
    public static String str2SingleNotify(String name, TextPaint paint, int textviewwidth) {
        name = name.trim();
        if (paint.measureText(name) < textviewwidth)
            return name + "\n";
        else {
            int sublength=0;
            if (name.length() > 40) {
                sublength=40;
            }else {
                sublength=name.length();
            }
            String  in="";
            int i=1;
            if (paint.measureText(name) > textviewwidth) {
                while (true) {
                    in = name.substring(0, sublength - i++);
                    if (paint.measureText(in) < textviewwidth - 40)
                        return in + "\n"+str2SingleNotify(name.substring(in.length(),name.length()),paint,textviewwidth);
                }

            }
        }
        return name;
    }
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static String dataStr(JSONObject jsonObject, String key) {
        StringBuffer stringBuffer = new StringBuffer("");
        List<String> keyList = null;
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            int len = jsonArray.length();

            for (int i = 0; i < len; i++) {
                keyList = new ArrayList<>();
                jsonObject = (JSONObject) jsonArray.get(i);
//				JSONObject jsonObject2 = new JSONObject(object);
                Iterator<String> it = jsonObject.keys();
                while (it.hasNext()) {
                    String str = it.next();
                    keyList.add(str);
                }
                Collections.sort(keyList);
                int keyListLen = keyList.size();
                StringBuffer buffer = new StringBuffer("");
                for (int j = 0; j < keyListLen; j++) {
                    key = keyList.get(j);
                    Object objectTemp = null;

                    objectTemp = dataStr(jsonObject, key);
                    if ("".equals(buffer.toString())) {
                        buffer.append(key).append("=").append(objectTemp);
                    } else {
                        buffer.append(",").append(key).append("=").append(objectTemp);
                    }
                }

                if ("".equals(stringBuffer.toString())) {
                    stringBuffer.append(buffer.toString());
                } else {
                    stringBuffer.append(",").append(buffer.toString());
                }
                buffer.setLength(0);
            }
        } catch (Exception e) {
            // TODO: handle exception
//			System.out.println("不是数组");
            try {
                stringBuffer.append(jsonObject.get(key));
                return stringBuffer.toString();
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }
        return stringBuffer.toString();
    }

    //public static void addFiled(JSONObject jsonObject,String method){
//    try {
//        jsonObject.put("nonce", new Date().getTime());
//        jsonObject.put("timestamp", new Date().getTime() / 1000);
//        jsonObject.put("method", method);
//    }catch (Exception e){
//        e.printStackTrace();
//    }
//}
    public static String dataHeader(BaseBean object) {
        try {
//           jsonObject.put("nonce",new Date().getTime());
//           jsonObject.put("timestamp",new Date().getTime()/1000);
//           jsonObject.put("method", method);

            return addSignatureItem(new JSONObject(new Gson().toJson(object)));
        } catch (Exception e) {
            Log.e("onion", "e:" + e.toString());
        }
        return "";
    }
    public static String dataHeader(JSONObject object) {
        try {
//           jsonObject.put("nonce",new Date().getTime());
//           jsonObject.put("timestamp",new Date().getTime()/1000);
//           jsonObject.put("method", method);
            return addSignatureItem(object);
        } catch (Exception e) {
            Log.e("onion", "e:" + e.toString());
        }
        return "";
    }

    private static String addSignatureItem(JSONObject jsonObject) throws JSONException {
        ArrayList<String> keyList = new ArrayList<>();
        Iterator<String> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String str = iterator.next();
            keyList.add(str);
        }
        Collections.sort(keyList);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < keyList.size(); i++) {
            stringBuilder.append(keyList.get(i)).append("=");
            Object object = jsonObject.get(keyList.get(i));
            if (object instanceof JSONObject) {
                stringBuilder.append(addSignatureItem(((JSONObject) object)) + ",");
            } else if (object instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) object;
                for (int j = 0; j < jsonArray.length(); j++) {
                    stringBuilder.append(dataHeader(jsonArray.getJSONObject(j)) + ",");
                }
                if (jsonArray.length() > 0) stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            } else {
                stringBuilder.append(object);
            }
            stringBuilder.append(",");
        }
        if (!keyList.isEmpty()) stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public static String dataHeader(BaseBean object,boolean b) {
        try {
//           jsonObject.put("nonce",new Date().getTime());
//           jsonObject.put("timestamp",new Date().getTime()/1000);
//           jsonObject.put("method", method);
            return addSignatureItem(new JSONObject(new Gson().toJson(object)),b);
        } catch (Exception e) {
            Log.e("onion", "e:" + e.toString());
        }
        return "";
    }

    private static String addSignatureItem(JSONObject jsonObject,boolean b) throws JSONException {
        ArrayList<String> keyList = new ArrayList<>();
        Iterator<String> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String str = iterator.next();
            keyList.add(str);
        }
        for (int i = 0; i < keyList.size(); i++) {
        }
        Collections.sort(keyList);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < keyList.size(); i++) {
            stringBuilder.append(keyList.get(i)).append("=");
            Object object = jsonObject.get(keyList.get(i));
            if (object instanceof JSONObject) {
                stringBuilder.append(addSignatureItem(((JSONObject) object)) + ",");
            }  else {
                stringBuilder.append(object);
            }
            stringBuilder.append(",");
        }
        if (!keyList.isEmpty()) stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
    public static  boolean isInDay(long time){

        Date currentTime=new Date(time*1000);

        Date today=new Date();
        Calendar calendarStart=Calendar.getInstance();
        calendarStart.setTime(today);
        calendarStart.set(Calendar.HOUR_OF_DAY,0);
        calendarStart.set(Calendar.MINUTE,0);

        Calendar calendarEnd=Calendar.getInstance();
        calendarEnd.setTime(today);
        calendarEnd.set(Calendar.HOUR_OF_DAY,23);
        calendarEnd.set(Calendar.MINUTE,59);

        return currentTime.after(calendarStart.getTime())&&currentTime.before(calendarEnd.getTime());

    }

    public static String getPrecent(double precent){

        DecimalFormat formater = new DecimalFormat("#0.00");
      return   formater.format(precent);
    }
}
