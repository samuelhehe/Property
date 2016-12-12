package xj.property.beans;

import java.util.List;

/**
 * 作者：asia on 2016/3/3 17:01
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class MyTagsChangeRespV3Bean extends BaseBean {

    private Long time;
    private List<String> list;

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
