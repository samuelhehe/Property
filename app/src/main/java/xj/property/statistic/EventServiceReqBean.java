package xj.property.statistic;

import java.util.List;

import xj.property.beans.BaseBean;

/**
 * Created by Administrator on 2016/1/12.
 */
public class EventServiceReqBean extends BaseBean {

    public List<EventServiceBean> data;

    public List<EventServiceBean> getData() {
        return data;
    }

    public void setData(List<EventServiceBean> data) {
        this.data = data;
    }
}
