package xj.property.event;

/**
 * Created by che on 2015/9/22.
 */
public class WXPayRequestEvent {

    public WXPayRequestEvent(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    private boolean isSuccess;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
