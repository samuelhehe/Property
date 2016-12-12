package xj.property.beans;

/**
 * Created by n on 2015/4/24.
 */
public class FixBonusStatusResultBean implements XJ {
  private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "FixBonusStatusResultBean{" +
                "status='" + status + '\'' +
                '}';
    }
}
