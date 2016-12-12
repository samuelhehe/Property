package xj.property.beans;

import java.util.List;

/**
 * Created by che on 2015/8/31.
 */
public class RequestEndedCodesBean extends BaseBean {

    /**
     * codes : ["133222761293","389150618522","019022994509","569079900249","828174173530"]
     */
    private List<String> codes;

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    public List<String> getCodes() {
        return codes;
    }
}
