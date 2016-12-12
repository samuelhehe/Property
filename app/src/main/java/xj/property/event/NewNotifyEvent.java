package xj.property.event;

import com.easemob.chat.EMMessage;

import xj.property.cache.XJNotify;

/**
 * Created by Administrator on 2015/4/1.
 */
public class NewNotifyEvent {
    public XJNotify notify;
    public boolean isNew=true;

    public NewNotifyEvent(XJNotify notify, boolean isNew) {
        this.notify = notify;
        this.isNew = isNew;
    }
}
