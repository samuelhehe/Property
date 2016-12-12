package xj.property.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by asia on 2015/11/23.
 */
public class PropertyPayHistoryInfoBean implements Serializable {
    private int limit;
    private int page;
    private List<PropertyPayHistoryBean> data;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<PropertyPayHistoryBean> getData() {
        return data;
    }

    public void setData(List<PropertyPayHistoryBean> data) {
        this.data = data;
    }
}
