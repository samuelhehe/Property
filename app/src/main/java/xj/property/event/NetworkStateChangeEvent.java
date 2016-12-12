package xj.property.event;


/**
 * 网络状态监听
 */
public class NetworkStateChangeEvent {

    public NetworkStateChangeEvent(boolean isConnected) {
        this.isConnected = isConnected;
    }

    private boolean isConnected;

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }
}
