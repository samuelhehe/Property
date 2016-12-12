package xj.property.event;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/3/20.
 */
public class FastMarketEvent {
    /**
     * 自定义消息
     */
    public final String message;
    /**
     * item 位置
     */
    public final int position;
    /**
     * 按钮索引，从做到右，从0起
     */
    public final int btnIndex;
    /**
     * 菜品数量
     */
    public int count;

    public final int CMD_CODE;
    public JSONObject json;
    public FastMarketEvent(String message, JSONObject json, int position, int btnIndex, int CME_CODE) {
        this.message = message;
        this.position = position;
        this.json=json ;
        this.btnIndex = btnIndex;
        this.CMD_CODE = CME_CODE;
//        this.count = count;
    }
}
