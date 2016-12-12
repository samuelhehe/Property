package xj.property.beans;

/**
 * Created by n on 2015/5/4.
 * v3 2016/03/18
 */
public class UpdateUserGenderRequest extends xj.property.netbase.BaseBean {
    private String gender; //    "gender": "{性别：f->女,m->男,s->保密}"

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
