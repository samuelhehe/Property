package xj.property.beans;

/**
 * Created by maxwell on 15/2/3.
 */
public class CityBean {

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
