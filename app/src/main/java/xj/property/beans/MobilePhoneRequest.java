package xj.property.beans;

import java.util.List;

/**
 * 作者：asia on 2016/1/19 21:01
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 * v3 2016/03/21
 */
public class MobilePhoneRequest extends xj.property.netbase.BaseBean {

    private List<MobliePhone> phones;

    public List<MobliePhone> getPhones() {
        return phones;
    }

    public void setPhones(List<MobliePhone> phones) {
        this.phones = phones;
    }
}
