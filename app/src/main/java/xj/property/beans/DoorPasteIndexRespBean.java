package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2016/3/24.
 */
public class DoorPasteIndexRespBean {

    private int page;
    private int limit;
    private List<DoorPasteIndexBean> data;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<DoorPasteIndexBean> getData() {
        return data;
    }

    public void setData(List<DoorPasteIndexBean> data) {
        this.data = data;
    }
}
