package xj.property.beans;

import java.util.List;

/**
 * Created by n on 2015/8/13.
 */
public class PanicBuyingResponseBean {

    /**
     * status : yes
     * info : {"total":100,"createTime":462365,"title":"物美价廉3","emobId":"34347c343003e57232a5d21f14fe399e","crazySalesId":12,"skimCount":12,"remain":98,"status":"ongoing","perLimit":2,"crazySalesUser":[{"createTime":1439381791,"crazySalesId":12,"crazySalesUserId":0,"status":"unuse","userEmobId":"34347c343003e57232a5d21f14fe399e","code":"164925222204","shopEmobId":"34347c343003e57232a5d21f14fe399e","useTime":0},{"createTime":1439381791,"crazySalesId":12,"crazySalesUserId":0,"status":"unuse","userEmobId":"34347c343003e57232a5d21f14fe399e","code":"482231149650","shopEmobId":"34347c343003e57232a5d21f14fe399e","useTime":0}],"description":"好东西，物美价廉3","endTime":1436684409}
     */
    private String status;
    private String errorCode;
    private String message;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private InfoOfPanicBuying info;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setInfo(InfoOfPanicBuying info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public InfoOfPanicBuying getInfo() {
        return info;
    }


}
