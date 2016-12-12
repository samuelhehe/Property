package xj.property.utils.message;

import com.google.gson.Gson;

import xj.property.hxMessageExtModel.Hx200203Model;
import xj.property.hxMessageExtModel.Hx201Model;


/**
 * Created by maxwell on 15/2/11.
 */
public class MessageExtBuilder {
    /**
     * CMD CODE
     */
//    private int CMD_CODE;
//
//    private String CMD_DETAIL;
//
//    private String avatar;
//
//    private String nickname;
//
//    private int clickable;
//
//    private int isShowAvatar;
//
//    private String serial;
//
//    private String msgId;

    public static String buildMessageExt(int CMD_CODE){
        String result = "";
        Gson gson = new Gson();
        switch (CMD_CODE){
            case 200:
                break;
            case 201:
                Hx201Model hx201Model = new Hx201Model();
                hx201Model.setSerial("1234");
                hx201Model.setIsClickable(1);
                hx201Model.setIsShowAvatar(1);

                result = gson.toJson(hx201Model,Hx201Model.class);
                break;

            case 203:
                Hx200203Model hx200203Model = new Hx200203Model();
                hx200203Model.setBuyer("小红");
                hx200203Model.setAddress("beijing");
                hx200203Model.setPayMethod(0);
                result = gson.toJson(hx200203Model,Hx200203Model.class);
                break;

            case 204:


                break;

            default:
                break;
        }
        return result;

    }
}
