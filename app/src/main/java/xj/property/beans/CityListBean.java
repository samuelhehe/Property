package xj.property.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by n on 2015/4/8.
 */
public class CityListBean  implements  XJ{

    private String status;
    private List<Info> info;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Info> getInfo() {
        return info;
    }

    public void setInfo(List<Info> info) {
        this.info = info;
    }

    public static class  Info implements XJ {
        private int cityId;
        private String city;
        private int countryId;
        private long lastUpdate;
        private String cityLetter;

        public int getCityId() {
            return cityId;
        }

        public void setCityId(int cityId) {
            this.cityId = cityId;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getCountryId() {
            return countryId;
        }

        public void setCountryId(int countryId) {
            this.countryId = countryId;
        }

        public long getLastUpdate() {
            return lastUpdate;
        }

        public void setLastUpdate(long lastUpdate) {
            this.lastUpdate = lastUpdate;
        }

        public String getCityLetter() {
            return cityLetter;
        }

        public void setCityLetter(String cityLetter) {
            this.cityLetter = cityLetter;
        }

    }

}
