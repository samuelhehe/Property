package xj.property.netbase;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Administrator on 2015/5/25.
 */
public class BaseBean  implements Serializable{

    public String nonce;
    public int timestamp;
    public String v;

    public BaseBean() {
        this.nonce = "Android" + UUID.randomUUID().toString().replaceAll("-", "");
        this.timestamp = (int) (System.currentTimeMillis() / 1000L);
    }

    public String getNonce() {
        return nonce;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }
}
