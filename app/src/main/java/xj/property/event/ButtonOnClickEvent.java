package xj.property.event;

import android.view.View;

import org.json.JSONObject;

/**
 * Created by maxwell on 15/1/30.
 */
public class ButtonOnClickEvent {
    /**
     * 自定义消息
     */
    public final String message;
    /**
     * item 位置
     */
    public int position;
    /**
     * 按钮索引，从做到右，从0起
     */
    public final int btnIndex;
    /**
     * 菜品数量
     */
    public int count;

    public final int CMD_CODE;


    public View view;

    public ButtonOnClickEvent(String message, int position, int btnIndex, int CME_CODE) {
        this.message = message;
        this.position = position;
        this.btnIndex = btnIndex;
        this.CMD_CODE = CME_CODE;
//        this.count = count;
    }

    public ButtonOnClickEvent(String message, View view, int btnIndex, int CME_CODE) {
        this.message = message;
        this.view = view;
        this.btnIndex = btnIndex;
        this.CMD_CODE = CME_CODE;
//        this.count = count;
    }
}
