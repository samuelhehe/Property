package xj.property.beans;

import java.util.Date;
        import java.util.UUID;

/**
 * Created by Administrator on 2015/5/25.
 */
public class BaseBean {
    public  String nonce;
    public  String method;
    public  int timestamp;

    public BaseBean() {
//        this.nonce = new Date().getTime()+"";
        //// 新的签名方式  2015/10/23 21:50
        this.nonce = "Android"+ UUID.randomUUID().toString().replaceAll("-", "");
        this.method = "POST";
        this.timestamp=(int)(new Date().getTime()/1000);
    }

    public String getNonce() {
        return nonce;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getTimestamp() {
        return timestamp;
    }

}
