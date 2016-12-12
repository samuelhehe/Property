package xj.property.beans;

/**
 * 作者：che on 2016/1/19 18:22
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 *
 * v3 2016/03/21
 */
public class MobliePhone extends xj.property.netbase.BaseBean {
     private String name;
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
