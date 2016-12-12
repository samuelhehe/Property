package xj.property.event;

import com.easemob.chat.EMMessage;

public class NewFriendsProcessedBackEvent {

    private boolean refreshMessageList;

    private EMMessage   emessage;

    public NewFriendsProcessedBackEvent(boolean refreshMessageList, EMMessage  emsg) {
        this.refreshMessageList = refreshMessageList;
        this.emessage = emsg;
    }


    public boolean isRefreshMessageList() {
        return refreshMessageList;
    }

    public void setRefreshMessageList(boolean refreshMessageList) {
        this.refreshMessageList = refreshMessageList;
    }

    public EMMessage getEmessage() {
        return emessage;
    }

    public void setEmessage(EMMessage emessage) {
        this.emessage = emessage;
    }
}
