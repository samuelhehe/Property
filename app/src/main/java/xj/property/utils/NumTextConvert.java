package xj.property.utils;

/**
 * Created by Administrator on 2015/11/20.
 *
 */
public class NumTextConvert {


    static String[] numArray = {"一","二", "三", "四", "五", "六", "七", "八", "九" ,"十"};

    /**
     * 100 以内 阿拉伯数字转汉字数字
     *
     * @param num
     * @return
     */
    public  static  String convertNumToText(int num){
        String result = null;
        if(num>=20){
            result = numArray[num/10-1]+"十"+ (num%10==0?"":numArray[num%10-1]);
        }else if(num>10){
            result = "十"+ (num%10==0?"":numArray[num%10-1]);
        }else{
            result = numArray[num-1];
        }
        return result;
    }


}
