package xj.property.event;

import xj.property.beans.VoteDetailsRespBean;
import xj.property.beans.VoteIndexRespV3Bean;

/**
 * Created by Administrator on 2015/4/24.
 */
public class NewVotedBackEvent {
    private VoteDetailsRespBean providerDetailsBean;


    public NewVotedBackEvent() {
    }

    public NewVotedBackEvent(VoteDetailsRespBean providerDetailsBean) {
        this.providerDetailsBean = providerDetailsBean;
    }

    public VoteDetailsRespBean getProviderDetailsBean() {
        return providerDetailsBean;
    }

    public void setProviderDetailsBean(VoteDetailsRespBean providerDetailsBean) {
        this.providerDetailsBean = providerDetailsBean;
    }
}
