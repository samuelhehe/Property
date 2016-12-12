package xj.property.beans;

import java.util.List;

/**
 * Created by che on 2015/9/16.
 */
public class WelfareHistoryBean {

    /**
     * status : yes
     * info : {"pageCount":1,"num":1,"last":1,"pageData":[{"startTime":1442299992,"total":22,"content":"http://www.tu.com/1,http://www.tu.com/2,http://www.tu.com/32","summary":"优质 好吃 真品 只要九块九 手快有 手慢无2","phone":"158774496632","title":"五常大米 9.9元2","status":"success","address":"十号楼101室，物业管理中心2","charactervalues":7,"rule":"此福利需至少50人才可生效，10月24日由物业处统一发放领取，需排队发放，妥善保管自己的福利码， 排队发放需注意安全2","poster":"http://xxxx.com2","welfareId":4,"endTime":1442299992},{"startTime":1442299992,"total":21,"content":"http://www.tu.com/1,http://www.tu.com/2,http://www.tu.com/31","summary":"优质 好吃 真品 只要九块九 手快有 手慢无1","phone":"158774496631","title":"五常大米 9.9元1","status":"success","address":"十号楼101室，物业管理中心1","charactervalues":6,"rule":"此福利需至少50人才可生效，10月24日由物业处统一发放领取，需排队发放，妥善保管自己的福利码， 排队发放需注意安全1","poster":"http://xxxx.com1","welfareId":3,"endTime":1442299992},{"startTime":1442299992,"total":20,"content":"http://www.tu.com/1,http://www.tu.com/2,http://www.tu.com/30","summary":"优质 好吃 真品 只要九块九 手快有 手慢无0","phone":"158774496630","title":"五常大米 9.9元0","status":"success","address":"十号楼101室，物业管理中心0","charactervalues":5,"rule":"此福利需至少50人才可生效，10月24日由物业处统一发放领取，需排队发放，妥善保管自己的福利码， 排队发放需注意安全0","poster":"http://xxxx.com0","welfareId":2,"endTime":1442299992},{"startTime":1442299992,"total":20,"content":"http://www.tu.com/1,http://www.tu.com/2,http://www.tu.com/3","summary":"优质 好吃 真品 只要九块九 手快有 手慢无","phone":"15877449663","title":"五常大米 9.9元","status":"success","address":"十号楼101室，物业管理中心","charactervalues":5,"rule":"此福利需至少50人才可生效，10月24日由物业处统一发放领取，需排队发放，妥善保管自己的福利码， 排队发放需注意安全","poster":"http://xxxx.com","welfareId":1,"endTime":1442299992}],"startRow":0,"next":1,"pageSize":10,"navNum":10,"rowCount":4,"first":1,"end":1,"begin":1,"prev":1}
     */
    private String status;
    private InfoEntity info;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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
         * pageCount : 1
         * num : 1
         * last : 1
         * pageData : [{"startTime":1442299992,"total":22,"content":"http://www.tu.com/1,http://www.tu.com/2,http://www.tu.com/32","summary":"优质 好吃 真品 只要九块九 手快有 手慢无2","phone":"158774496632","title":"五常大米 9.9元2","status":"success","address":"十号楼101室，物业管理中心2","charactervalues":7,"rule":"此福利需至少50人才可生效，10月24日由物业处统一发放领取，需排队发放，妥善保管自己的福利码， 排队发放需注意安全2","poster":"http://xxxx.com2","welfareId":4,"endTime":1442299992},{"startTime":1442299992,"total":21,"content":"http://www.tu.com/1,http://www.tu.com/2,http://www.tu.com/31","summary":"优质 好吃 真品 只要九块九 手快有 手慢无1","phone":"158774496631","title":"五常大米 9.9元1","status":"success","address":"十号楼101室，物业管理中心1","charactervalues":6,"rule":"此福利需至少50人才可生效，10月24日由物业处统一发放领取，需排队发放，妥善保管自己的福利码， 排队发放需注意安全1","poster":"http://xxxx.com1","welfareId":3,"endTime":1442299992},{"startTime":1442299992,"total":20,"content":"http://www.tu.com/1,http://www.tu.com/2,http://www.tu.com/30","summary":"优质 好吃 真品 只要九块九 手快有 手慢无0","phone":"158774496630","title":"五常大米 9.9元0","status":"success","address":"十号楼101室，物业管理中心0","charactervalues":5,"rule":"此福利需至少50人才可生效，10月24日由物业处统一发放领取，需排队发放，妥善保管自己的福利码， 排队发放需注意安全0","poster":"http://xxxx.com0","welfareId":2,"endTime":1442299992},{"startTime":1442299992,"total":20,"content":"http://www.tu.com/1,http://www.tu.com/2,http://www.tu.com/3","summary":"优质 好吃 真品 只要九块九 手快有 手慢无","phone":"15877449663","title":"五常大米 9.9元","status":"success","address":"十号楼101室，物业管理中心","charactervalues":5,"rule":"此福利需至少50人才可生效，10月24日由物业处统一发放领取，需排队发放，妥善保管自己的福利码， 排队发放需注意安全","poster":"http://xxxx.com","welfareId":1,"endTime":1442299992}]
         * startRow : 0
         * next : 1
         * pageSize : 10
         * navNum : 10
         * rowCount : 4
         * first : 1
         * end : 1
         * begin : 1
         * prev : 1
         */
        private int pageCount;
        private int num;
        private int last;
        private List<PageDataEntity> pageData;
        private int startRow;
        private int next;
        private int pageSize;
        private int navNum;
        private int rowCount;
        private int first;
        private int end;
        private int begin;
        private int prev;

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public void setLast(int last) {
            this.last = last;
        }

        public void setPageData(List<PageDataEntity> pageData) {
            this.pageData = pageData;
        }

        public void setStartRow(int startRow) {
            this.startRow = startRow;
        }

        public void setNext(int next) {
            this.next = next;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public void setNavNum(int navNum) {
            this.navNum = navNum;
        }

        public void setRowCount(int rowCount) {
            this.rowCount = rowCount;
        }

        public void setFirst(int first) {
            this.first = first;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public void setBegin(int begin) {
            this.begin = begin;
        }

        public void setPrev(int prev) {
            this.prev = prev;
        }

        public int getPageCount() {
            return pageCount;
        }

        public int getNum() {
            return num;
        }

        public int getLast() {
            return last;
        }

        public List<PageDataEntity> getPageData() {
            return pageData;
        }

        public int getStartRow() {
            return startRow;
        }

        public int getNext() {
            return next;
        }

        public int getPageSize() {
            return pageSize;
        }

        public int getNavNum() {
            return navNum;
        }

        public int getRowCount() {
            return rowCount;
        }

        public int getFirst() {
            return first;
        }

        public int getEnd() {
            return end;
        }

        public int getBegin() {
            return begin;
        }

        public int getPrev() {
            return prev;
        }

        public class PageDataEntity {
            /**
             * startTime : 1442299992
             * total : 22
             * content : http://www.tu.com/1,http://www.tu.com/2,http://www.tu.com/32
             * summary : 优质 好吃 真品 只要九块九 手快有 手慢无2
             * phone : 158774496632
             * title : 五常大米 9.9元2
             * status : success
             * address : 十号楼101室，物业管理中心2
             * charactervalues : 7
             * rule : 此福利需至少50人才可生效，10月24日由物业处统一发放领取，需排队发放，妥善保管自己的福利码， 排队发放需注意安全2
             * poster : http://xxxx.com2
             * welfareId : 4
             * endTime : 1442299992
             */
            private int startTime;
            private int total;
            private String content;
            private String summary;
            private String phone;
            private String title;
            private String status;
            private String address;
            private int charactervalues;
            private String rule;
            private String poster;
            private int welfareId;
            private int endTime;

            public void setStartTime(int startTime) {
                this.startTime = startTime;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public void setSummary(String summary) {
                this.summary = summary;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public void setCharactervalues(int charactervalues) {
                this.charactervalues = charactervalues;
            }

            public void setRule(String rule) {
                this.rule = rule;
            }

            public void setPoster(String poster) {
                this.poster = poster;
            }

            public void setWelfareId(int welfareId) {
                this.welfareId = welfareId;
            }

            public void setEndTime(int endTime) {
                this.endTime = endTime;
            }

            public int getStartTime() {
                return startTime;
            }

            public int getTotal() {
                return total;
            }

            public String getContent() {
                return content;
            }

            public String getSummary() {
                return summary;
            }

            public String getPhone() {
                return phone;
            }

            public String getTitle() {
                return title;
            }

            public String getStatus() {
                return status;
            }

            public String getAddress() {
                return address;
            }

            public int getCharactervalues() {
                return charactervalues;
            }

            public String getRule() {
                return rule;
            }

            public String getPoster() {
                return poster;
            }

            public int getWelfareId() {
                return welfareId;
            }

            public int getEndTime() {
                return endTime;
            }
        }
    }
}
