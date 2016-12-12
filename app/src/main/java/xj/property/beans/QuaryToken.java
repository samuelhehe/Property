package xj.property.beans;

/**
 * Created by Administrator on 2015/3/12.
 */
public class QuaryToken extends BaseBean{
    public String qiNiuId = "2";
    public String qiNiuType = "user";

    public QuaryToken(String qiNiuId, String qiNiuType) {
        super();
        this.qiNiuId = qiNiuId;
        this.qiNiuType = qiNiuType;
    }

    public QuaryToken() {
    }

    public String getQiNiuId() {
        return qiNiuId;
    }

    public void setQiNiuId(String qiNiuId) {
        this.qiNiuId = qiNiuId;
    }

    public String getQiNiuType() {
        return qiNiuType;
    }

    public void setQiNiuType(String qiNiuType) {
        this.qiNiuType = qiNiuType;
    }
}
