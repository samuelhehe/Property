package xj.property.beans;

import android.os.Parcelable;

/**
 * Created by Administrator on 2015/9/16.
 */
public class WelfareInfo {

    /**
     * status : yes
     * info : {"welfareId":1,"title":"五常大米 9.9元","poster":"http://xxxx.com","summary":"优质 好吃 真品 只要九块九 手快有 手慢无","content":"http://www.tu.com/1,http://www.tu.com/2,http://www.tu.com/3","charactervalues":5,"total":20,"rule":"此福利需至少50人才可生效，10月24日由物业处统一发放领取，需排队发放，妥善保管自己的福利码， 排队发放需注意安全","phone":"15877449663","address":"十号楼101室，物业管理中心","status":"ongoing","startTime":1442299992,"endTime":1442299992,"price":100,"provideTime":1442299992,"createTime":1442299992,"modifyTime":1442299992,"reason":"天干物燥","buyed":3,"users":[{"userId":3177,"emobId":"2011eb792db7b1029341faab3ad65919","nickname":"lei","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fjk1cqP2gezuV6GS2Wd-AIrbvFVt"},{"userId":3177,"emobId":"2011eb792db7b1029341faab3ad65919","nickname":"lei","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fjk1cqP2gezuV6GS2Wd-AIrbvFVt"},{"userId":434,"emobId":"58a4e33fbc97abca4051130ad9b2e2cf","nickname":"我是009","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fjk1cqP2gezuV6GS2Wd-AIrbvFVt"}]}
     */

    private String status;


    private String message;

    private WelfareInfoEntity info;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setInfo(WelfareInfoEntity info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public WelfareInfoEntity getInfo() {
        return info;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
