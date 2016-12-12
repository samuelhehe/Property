package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/10/30.
 */
public class TagsA2BAddReqBean extends  xj.property.netbase.BaseBean {


    /**
     * labelList : [{"emobIdFrom":"a0027fcac74d3126e22fa2a18430059d","emobIdTo":"30fbf0be239f5afd52440cf31d98f23e","labelContent":"111"},{"emobIdFrom":"a0027fcac74d3126e22fa2a18430059d","emobIdTo":"30fbf0be239f5afd52440cf31d98f23e","labelContent":"113"}]
     */

    private List<LabelListEntity> labelList;

    public void setLabelList(List<LabelListEntity> labelList) {
        this.labelList = labelList;
    }

    public List<LabelListEntity> getLabelList() {
        return labelList;
    }

    public static class LabelListEntity {
        /**
         * emobIdFrom : a0027fcac74d3126e22fa2a18430059d
         * emobIdTo : 30fbf0be239f5afd52440cf31d98f23e
         * labelContent : 111
         */

        private String emobIdFrom;
        private String emobIdTo;
        private String labelContent;

        public void setEmobIdFrom(String emobIdFrom) {
            this.emobIdFrom = emobIdFrom;
        }

        public void setEmobIdTo(String emobIdTo) {
            this.emobIdTo = emobIdTo;
        }

        public void setLabelContent(String labelContent) {
            this.labelContent = labelContent;
        }

        public String getEmobIdFrom() {
            return emobIdFrom;
        }

        public String getEmobIdTo() {
            return emobIdTo;
        }

        public String getLabelContent() {
            return labelContent;
        }
    }
}
