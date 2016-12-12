package xj.property.beans;

import java.util.List;

/**
 * Created by che on 2015/8/31.
 */
public class RequstEndedCodeBean {

    /**
     * status : yes
     * info : {"codes":["019022994509","828174173530"]}
     */
    private String status;
    private InfoEntity info;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setInfo(InfoEntity info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public InfoEntity getInfo() {
        return info;
    }

    public class InfoEntity {
        /**
         * codes : ["019022994509","828174173530"]
         */
        private List<String> codes;

        public void setCodes(List<String> codes) {
            this.codes = codes;
        }

        public List<String> getCodes() {
            return codes;
        }
    }
}
