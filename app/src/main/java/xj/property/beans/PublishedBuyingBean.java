package xj.property.beans;


import java.util.List;

/**
 * Created by n on 2015/8/11.
 */
public class PublishedBuyingBean {


    /**
     * status : yes
     * info : {"rowCount":16,"pageSize":10,"num":1,"startRow":0,"next":2,"prev":1,"pageCount":2,"begin":1,"end":2,"first":1,"last":2,"navNum":10,"pageData":[{"crazySalesId":9,"emobId":"895d517dce39750651b743b19d55efb9","total":520,"perLimit":5,"title":"西域美农核桃","createTime":1440073852,"endTime":1446325177,"descr":"大核桃，好吃","remain":495,"icon":"http://7d9lcl.com2.z0.glb.qiniucdn.com/crazysale_class_activity_icon_1.png","distance":0,"crazySalesImg":[{"crazySalesImgId":9,"imgUrl":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fvo5vXaD0UJyWzLoZYIgFuZi8qIM","crazySalesId":9}]},{"crazySalesId":137,"emobId":"deea8edd9aa932fd0ba796406914ba3f","total":100,"perLimit":1,"title":"跟你","createTime":1443604147,"endTime":1446196139,"descr":"哦","remain":95,"icon":"http://7d9lcl.com2.z0.glb.qiniucdn.com/crazysale_class_activity_icon_4.png","distance":24,"crazySalesImg":[{"crazySalesImgId":104,"imgUrl":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FlEn_-CqjOQgenQGflTQWSpIKluJ","crazySalesId":137}]},{"crazySalesId":1,"emobId":"38dfc72648939d5f0150f5cbdcb62a95","total":2585,"perLimit":5,"title":"天上飞的","createTime":1440070638,"endTime":1446325177,"descr":"香港韩村河村 vuvu 就不亢不卑看看你能比 i 股份土豆土豆条虽然身体需要 v 环保局崩溃可能你看看你比 uv 预测系统提醒日子让新人新动态","remain":2564,"icon":"http://7d9lcl.com2.z0.glb.qiniucdn.com/crazysale_class_activity_icon_2.png","distance":200,"crazySalesImg":[{"crazySalesImgId":1,"imgUrl":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FvaaGESoa0-VQ5wVHjASzXFO0Sob","crazySalesId":1}]},{"crazySalesId":15,"emobId":"a8ef6937e3c00b10576d0159e270fedf","total":6446,"perLimit":3,"title":"寂寞","createTime":1440075342,"endTime":1446325177,"descr":"噢情有可原","remain":6446,"icon":"http://7d9lcl.com2.z0.glb.qiniucdn.com/crazysale_class_activity_icon_2.png","distance":0,"crazySalesImg":[{"crazySalesImgId":15,"imgUrl":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fipv-peq3-iULha_nxcxDS5ijdkK","crazySalesId":15}]},{"crazySalesId":14,"emobId":"a8ef6937e3c00b10576d0159e270fedf","total":8,"perLimit":3,"title":"试试吧","createTime":1440074682,"endTime":1446325177,"descr":"责任心","remain":8,"icon":"http://7d9lcl.com2.z0.glb.qiniucdn.com/crazysale_class_activity_icon_3.png","distance":0,"crazySalesImg":[{"crazySalesImgId":14,"imgUrl":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FkJ_PNGyikcTBnwBxal6Mvlt0WGj","crazySalesId":14}]},{"crazySalesId":12,"emobId":"895d517dce39750651b743b19d55efb9","total":100,"perLimit":5,"title":"苏打水1元一瓶","createTime":1440074593,"endTime":1446325177,"descr":"哈喽","remain":98,"icon":"http://7d9lcl.com2.z0.glb.qiniucdn.com/crazysale_class_activity_icon_0.png","distance":0,"crazySalesImg":[{"crazySalesImgId":12,"imgUrl":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FpRRFlUYon4WHqHQFRbSMfXhbeKq","crazySalesId":12}]},{"crazySalesId":11,"emobId":"dd16d7a8ed0fedcc85c1ba164e847a87","total":5,"perLimit":1,"title":"同","createTime":1440074426,"endTime":1446325177,"descr":"恩","remain":3,"icon":"http://7d9lcl.com2.z0.glb.qiniucdn.com/crazysale_class_activity_icon_2.png","distance":0,"crazySalesImg":[{"crazySalesImgId":11,"imgUrl":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fto5o-5ea0sNMlW_75VgGJCv2AcJ","crazySalesId":11}]}]}
     */

    private String status;

    private String message;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class InfoEntity {
        /**
         * rowCount : 16
         * pageSize : 10
         * num : 1
         * startRow : 0
         * next : 2
         * prev : 1
         * pageCount : 2
         * begin : 1
         * end : 2
         * first : 1
         * last : 2
         * navNum : 10
         * pageData : [{"crazySalesId":9,"emobId":"895d517dce39750651b743b19d55efb9","total":520,"perLimit":5,"title":"西域美农核桃","createTime":1440073852,"endTime":1446325177,"descr":"大核桃，好吃","remain":495,"icon":"http://7d9lcl.com2.z0.glb.qiniucdn.com/crazysale_class_activity_icon_1.png","distance":0,"crazySalesImg":[{"crazySalesImgId":9,"imgUrl":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fvo5vXaD0UJyWzLoZYIgFuZi8qIM","crazySalesId":9}]},{"crazySalesId":137,"emobId":"deea8edd9aa932fd0ba796406914ba3f","total":100,"perLimit":1,"title":"跟你","createTime":1443604147,"endTime":1446196139,"descr":"哦","remain":95,"icon":"http://7d9lcl.com2.z0.glb.qiniucdn.com/crazysale_class_activity_icon_4.png","distance":24,"crazySalesImg":[{"crazySalesImgId":104,"imgUrl":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FlEn_-CqjOQgenQGflTQWSpIKluJ","crazySalesId":137}]},{"crazySalesId":1,"emobId":"38dfc72648939d5f0150f5cbdcb62a95","total":2585,"perLimit":5,"title":"天上飞的","createTime":1440070638,"endTime":1446325177,"descr":"香港韩村河村 vuvu 就不亢不卑看看你能比 i 股份土豆土豆条虽然身体需要 v 环保局崩溃可能你看看你比 uv 预测系统提醒日子让新人新动态","remain":2564,"icon":"http://7d9lcl.com2.z0.glb.qiniucdn.com/crazysale_class_activity_icon_2.png","distance":200,"crazySalesImg":[{"crazySalesImgId":1,"imgUrl":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FvaaGESoa0-VQ5wVHjASzXFO0Sob","crazySalesId":1}]},{"crazySalesId":15,"emobId":"a8ef6937e3c00b10576d0159e270fedf","total":6446,"perLimit":3,"title":"寂寞","createTime":1440075342,"endTime":1446325177,"descr":"噢情有可原","remain":6446,"icon":"http://7d9lcl.com2.z0.glb.qiniucdn.com/crazysale_class_activity_icon_2.png","distance":0,"crazySalesImg":[{"crazySalesImgId":15,"imgUrl":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fipv-peq3-iULha_nxcxDS5ijdkK","crazySalesId":15}]},{"crazySalesId":14,"emobId":"a8ef6937e3c00b10576d0159e270fedf","total":8,"perLimit":3,"title":"试试吧","createTime":1440074682,"endTime":1446325177,"descr":"责任心","remain":8,"icon":"http://7d9lcl.com2.z0.glb.qiniucdn.com/crazysale_class_activity_icon_3.png","distance":0,"crazySalesImg":[{"crazySalesImgId":14,"imgUrl":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FkJ_PNGyikcTBnwBxal6Mvlt0WGj","crazySalesId":14}]},{"crazySalesId":12,"emobId":"895d517dce39750651b743b19d55efb9","total":100,"perLimit":5,"title":"苏打水1元一瓶","createTime":1440074593,"endTime":1446325177,"descr":"哈喽","remain":98,"icon":"http://7d9lcl.com2.z0.glb.qiniucdn.com/crazysale_class_activity_icon_0.png","distance":0,"crazySalesImg":[{"crazySalesImgId":12,"imgUrl":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FpRRFlUYon4WHqHQFRbSMfXhbeKq","crazySalesId":12}]},{"crazySalesId":11,"emobId":"dd16d7a8ed0fedcc85c1ba164e847a87","total":5,"perLimit":1,"title":"同","createTime":1440074426,"endTime":1446325177,"descr":"恩","remain":3,"icon":"http://7d9lcl.com2.z0.glb.qiniucdn.com/crazysale_class_activity_icon_2.png","distance":0,"crazySalesImg":[{"crazySalesImgId":11,"imgUrl":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fto5o-5ea0sNMlW_75VgGJCv2AcJ","crazySalesId":11}]}]
         */

        private int rowCount;
        private int pageSize;
        private int num;
        private int startRow;
        private int next;
        private int prev;
        private int pageCount;
        private int begin;
        private int end;
        private int first;
        private int last;
        private int navNum;

        private List<PageDataEntity> pageData;

        public void setRowCount(int rowCount) {
            this.rowCount = rowCount;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public void setStartRow(int startRow) {
            this.startRow = startRow;
        }

        public void setNext(int next) {
            this.next = next;
        }

        public void setPrev(int prev) {
            this.prev = prev;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public void setBegin(int begin) {
            this.begin = begin;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public void setFirst(int first) {
            this.first = first;
        }

        public void setLast(int last) {
            this.last = last;
        }

        public void setNavNum(int navNum) {
            this.navNum = navNum;
        }

        public void setPageData(List<PageDataEntity> pageData) {
            this.pageData = pageData;
        }

        public int getRowCount() {
            return rowCount;
        }

        public int getPageSize() {
            return pageSize;
        }

        public int getNum() {
            return num;
        }

        public int getStartRow() {
            return startRow;
        }

        public int getNext() {
            return next;
        }

        public int getPrev() {
            return prev;
        }

        public int getPageCount() {
            return pageCount;
        }

        public int getBegin() {
            return begin;
        }

        public int getEnd() {
            return end;
        }

        public int getFirst() {
            return first;
        }

        public int getLast() {
            return last;
        }

        public int getNavNum() {
            return navNum;
        }

        public List<PageDataEntity> getPageData() {
            return pageData;
        }

        public static class PageDataEntity {
            /**
             * crazySalesId : 9
             * emobId : 895d517dce39750651b743b19d55efb9
             * total : 520
             * perLimit : 5
             * title : 西域美农核桃
             * createTime : 1440073852
             * endTime : 1446325177
             * descr : 大核桃，好吃
             * remain : 495
             * icon : http://7d9lcl.com2.z0.glb.qiniucdn.com/crazysale_class_activity_icon_1.png
             * distance : 0
             * crazySalesImg : [{"crazySalesImgId":9,"imgUrl":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fvo5vXaD0UJyWzLoZYIgFuZi8qIM","crazySalesId":9}]
             */

            private int crazySalesId;
            private String emobId;
            private int total;
            private int perLimit;
            private String title;
            private int createTime;
            private int endTime;
            private String descr;
            private int remain;
            private String icon;
            private int distance;
            private List<CrazySalesImgEntity> crazySalesImg;

            public void setCrazySalesId(int crazySalesId) {
                this.crazySalesId = crazySalesId;
            }

            public void setEmobId(String emobId) {
                this.emobId = emobId;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public void setPerLimit(int perLimit) {
                this.perLimit = perLimit;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setCreateTime(int createTime) {
                this.createTime = createTime;
            }

            public void setEndTime(int endTime) {
                this.endTime = endTime;
            }

            public void setDescr(String descr) {
                this.descr = descr;
            }

            public void setRemain(int remain) {
                this.remain = remain;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public void setDistance(int distance) {
                this.distance = distance;
            }

            public void setCrazySalesImg(List<CrazySalesImgEntity> crazySalesImg) {
                this.crazySalesImg = crazySalesImg;
            }

            public int getCrazySalesId() {
                return crazySalesId;
            }

            public String getEmobId() {
                return emobId;
            }

            public int getTotal() {
                return total;
            }

            public int getPerLimit() {
                return perLimit;
            }

            public String getTitle() {
                return title;
            }

            public int getCreateTime() {
                return createTime;
            }

            public int getEndTime() {
                return endTime;
            }

            public String getDescr() {
                return descr;
            }

            public int getRemain() {
                return remain;
            }

            public String getIcon() {
                return icon;
            }

            public int getDistance() {
                return distance;
            }

            public List<CrazySalesImgEntity> getCrazySalesImg() {
                return crazySalesImg;
            }

            public static class CrazySalesImgEntity {
                /**
                 * crazySalesImgId : 9
                 * imgUrl : http://7d9lcl.com2.z0.glb.qiniucdn.com/Fvo5vXaD0UJyWzLoZYIgFuZi8qIM
                 * crazySalesId : 9
                 */

                private int crazySalesImgId;
                private String imgUrl;
                private int crazySalesId;

                public void setCrazySalesImgId(int crazySalesImgId) {
                    this.crazySalesImgId = crazySalesImgId;
                }

                public void setImgUrl(String imgUrl) {
                    this.imgUrl = imgUrl;
                }

                public void setCrazySalesId(int crazySalesId) {
                    this.crazySalesId = crazySalesId;
                }

                public int getCrazySalesImgId() {
                    return crazySalesImgId;
                }

                public String getImgUrl() {
                    return imgUrl;
                }

                public int getCrazySalesId() {
                    return crazySalesId;
                }
            }
        }
    }
}

