package com.chance.mimorobot.model;

import java.util.List;

public class SemanticDialog {

    /**
     * SessionId : sample string 1
     * QueryWord : sample string 2
     * isEnd : true
     * Intention : [{"id":1,"keywords":[{"id":1,"Des":"sample string 1","IsRequired":true,"IsMatched":true,"Word":"sample string 3","MatchWord":"sample string 4"},{"id":1,"Des":"sample string 1","IsRequired":true,"IsMatched":true,"Word":"sample string 3","MatchWord":"sample string 4"}]},{"id":1,"keywords":[{"id":1,"Des":"sample string 1","IsRequired":true,"IsMatched":true,"Word":"sample string 3","MatchWord":"sample string 4"},{"id":1,"Des":"sample string 1","IsRequired":true,"IsMatched":true,"Word":"sample string 3","MatchWord":"sample string 4"}]}]
     * type : 1
     * history : ["sample string 1","sample string 2"]
     * StartTime : 2020-09-11 21:39:07
     */

    private String SessionId;
    private String QueryWord;
    private boolean isEnd;
    private int type;
    private String StartTime;
    private List<IntentionBean> Intention;
    private List<String> history;

    public String getSessionId() {
        return SessionId;
    }

    public void setSessionId(String SessionId) {
        this.SessionId = SessionId;
    }

    public String getQueryWord() {
        return QueryWord;
    }

    public void setQueryWord(String QueryWord) {
        this.QueryWord = QueryWord;
    }

    public boolean isIsEnd() {
        return isEnd;
    }

    public void setIsEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String StartTime) {
        this.StartTime = StartTime;
    }

    public List<IntentionBean> getIntention() {
        return Intention;
    }

    public void setIntention(List<IntentionBean> Intention) {
        this.Intention = Intention;
    }

    public List<String> getHistory() {
        return history;
    }

    public void setHistory(List<String> history) {
        this.history = history;
    }

    public static class IntentionBean {
        /**
         * id : 1
         * keywords : [{"id":1,"Des":"sample string 1","IsRequired":true,"IsMatched":true,"Word":"sample string 3","MatchWord":"sample string 4"},{"id":1,"Des":"sample string 1","IsRequired":true,"IsMatched":true,"Word":"sample string 3","MatchWord":"sample string 4"}]
         */

        private int id;
        private List<KeywordsBean> keywords;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<KeywordsBean> getKeywords() {
            return keywords;
        }

        public void setKeywords(List<KeywordsBean> keywords) {
            this.keywords = keywords;
        }

        public static class KeywordsBean {
            /**
             * id : 1
             * Des : sample string 1
             * IsRequired : true
             * IsMatched : true
             * Word : sample string 3
             * MatchWord : sample string 4
             */

            private int id;
            private String Des;
            private boolean IsRequired;
            private boolean IsMatched;
            private String Word;
            private String MatchWord;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getDes() {
                return Des;
            }

            public void setDes(String Des) {
                this.Des = Des;
            }

            public boolean isIsRequired() {
                return IsRequired;
            }

            public void setIsRequired(boolean IsRequired) {
                this.IsRequired = IsRequired;
            }

            public boolean isIsMatched() {
                return IsMatched;
            }

            public void setIsMatched(boolean IsMatched) {
                this.IsMatched = IsMatched;
            }

            public String getWord() {
                return Word;
            }

            public void setWord(String Word) {
                this.Word = Word;
            }

            public String getMatchWord() {
                return MatchWord;
            }

            public void setMatchWord(String MatchWord) {
                this.MatchWord = MatchWord;
            }
        }
    }
}
