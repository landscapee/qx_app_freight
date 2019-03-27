package qx.app.freight.qxappfreight.bean.response;

import java.util.List;

import lombok.Data;

@Data
public class NoticeBean {


        /**
         * records : [{"id":"a853de650dfdc3a6306a20b7e7ec1f65","title":"郁金香盛开通知","content":"今，郁金香已然盛开 特邀君共赴仙山赏花","createOrg":"仙山委员会","status":1,"delFlag":0,"createDate":1550492792053,"createUser":"zhangwei","updateDate":null,"updateUser":null,"readingStatus":1},{"id":"e9ca784ef149cfed9ad52da8bfdeced9","title":"绸缪","content":"绸缪束薪，三星在天。今夕何夕，见此良人。子兮子兮，如此良人何！绸缪束刍，三星在隅。今夕何夕，见此邂逅。子兮子兮，如此邂逅何！绸缪束楚，三星在户。今夕何夕，见此粲者。子兮子兮，如此粲者何！《绸缪》描写新婚之夜的缠绵与喜悦。诗借了\u201c束薪\u201d作象征，用\u201c三星\u201d作背景，描写了傍晚到入夜的过程，再借助内心的独白\u201c今夕何夕\u201d、\u201c如此良人何\u201d，真有道不完的情深意长和新婚之夜的憧憬和激动。","createOrg":"雎鸠","status":1,"delFlag":0,"createDate":1550493252952,"createUser":"zhangwei","updateDate":null,"updateUser":null,"readingStatus":0},{"id":"178321ecb71e7b58349f0aeb8fd4818b","title":"野有蔓草","content":"野有蔓草，零露漙兮。有一美人，清扬婉如。邂逅相遇，适我愿兮。野有蔓草，零露瀼瀼。有一美人，婉如清扬。邂逅相遇，与子偕臧。这是首恋歌，写在一个零露未干的清晨，男子在田野草蔓间与女子不期而遇，一见倾心。邂逅总是美好的，而邂逅美人的地点在带露的蔓草间，正合我意，让此诗浪漫而唯美","createOrg":"雎鸠","status":2,"delFlag":0,"createDate":1550493193374,"createUser":"zhangwei","updateDate":1550493377172,"updateUser":"Bigwei","readingStatus":0}]
         * total : 3
         * size : 30
         * current : 1
         * searchCount : true
         * pages : 1
         */

        private int total;
        private int size;
        private int current;
        private boolean searchCount;
        private int pages;
        private List<RecordsBean> records;



        @Data
        public static class RecordsBean {
            /**
             * id : a853de650dfdc3a6306a20b7e7ec1f65
             * title : 郁金香盛开通知
             * content : 今，郁金香已然盛开 特邀君共赴仙山赏花
             * createOrg : 仙山委员会
             * status : 1
             * delFlag : 0
             * createDate : 1550492792053
             * createUser : zhangwei
             * updateDate : null
             * updateUser : null
             * readingStatus : 1
             */

            private String id;
            private String title;
            private String content;
            private String createOrg;
            private int status;
            private int delFlag;
            private long createDate;
            private String createUser;
            private Object updateDate;
            private Object updateUser;
            private int readingStatus;


    }
}
