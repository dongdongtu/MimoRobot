package com.chance.mimorobot.model;

import java.util.List;

/**
 * Created by Administrator on 2018/5/12.
 */

public class AiuiResultEntity {

    private DataBean data;
    /**
     * data : {"result":[{"airData":35,"airQuality":"优","city":"北京","date":"2018-02-07","dateLong":1517932800,"exp":{"ct":{"expName":"穿衣指数","level":"冷","prompt":"天气冷，建议着棉服、羽绒服、皮夹克加羊毛衫等冬季服装。年老体弱者宜着厚棉衣、冬大衣或厚羽绒服。"}},"humidity":"16%","lastUpdateTime":"2018-02-07 11:00","pm25":"9","temp":1,"tempRange":"-9℃ ~ 2℃","weather":"多云转晴","weatherType":1,"wind":"北风4-5级","windLevel":2}]}
     * rc : 0
     * semantic : [{"intent":"QUERY","slots":[{"name":"datetime","value":"今天","normValue":"{\"datetime\":\"2018-02-07\",\"suggestDatetime\":\"2018-02-07\"}"},{"name":"location.city","value":"北京市","normValue":"北京市"},{"name":"subfocus","value":"天气状态"}]}]
     * service : weather
     * state : {"fg::weather::default::default":{"state":"default"}}
     * text : 查看北京今天的天气
     * uuid : atn00d35cc3@ch13c70dd605786f1d01
     * used_state : {"state_key":"fg::weather::default::default","state":"default"}
     * answer : {"text":"\"北京今天多云转晴\"，\"-9℃ ~ 2℃\"，\"北风4-5级\""}
     * dialog_stat : DataValid
     * save_history : true
     * sid : atn00d35cc3@ch13c70dd605786f1d01
     */

    private String category;//分类

    private int rc;
    private String intentType;
    private String service;
    /**
     * fg::weather::default::default : {"state":"default"}
     */
    /**
     * semantic : [{"intent":"QUERY","slots":[{"name":"datetime","value":"今天","normValue":"{\"datetime\":\"2018-02-07\",\"suggestDatetime\":\"2018-02-07\"}"},{"name":"location.city","value":"北京市","normValue":"北京市"},{"name":"subfocus","value":"天气状态"}]}]
     * text : 查看北京今天的天气
     * uuid : atn00d35cc3@ch13c70dd605786f1d01
     * used_state : {"state_key":"fg::weather::default::default","state":"default"}
     * answer : {"text":"\"北京今天多云转晴\"，\"-9℃ ~ 2℃\"，\"北风4-5级\""}
     * dialog_stat : DataValid
     * save_history : true
     * sid : atn00d35cc3@ch13c70dd605786f1d01
     */

    private String text;
    private String uuid;
    /**
     * state_key : fg::weather::default::default
     * state : default
     */

    private UsedStateBean used_state;
    /**
     * text : "北京今天多云转晴"，"-9℃ ~ 2℃"，"北风4-5级"
     */

    private AnswerBean answer;
    private String dialog_stat;
    private boolean save_history;
    private String sid;
    /**
     * intent : QUERY
     * slots : [{"name":"datetime","value":"今天","normValue":"{\"datetime\":\"2018-02-07\",\"suggestDatetime\":\"2018-02-07\"}"},{"name":"location.city","value":"北京市","normValue":"北京市"},{"name":"subfocus","value":"天气状态"}]
     */

    private List<SemanticBean> semantic;


    public void setText(String text) {
        this.text = text;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setUsed_state(UsedStateBean used_state) {
        this.used_state = used_state;
    }

    public void setAnswer(AnswerBean answer) {
        this.answer = answer;
    }

    public void setDialog_stat(String dialog_stat) {
        this.dialog_stat = dialog_stat;
    }

    public void setSave_history(boolean save_history) {
        this.save_history = save_history;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setSemantic(List<SemanticBean> semantic) {
        this.semantic = semantic;
    }

    public String getText() {
        return text;
    }

    public String getUuid() {
        return uuid;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public UsedStateBean getUsed_state() {
        return used_state;
    }

    public AnswerBean getAnswer() {
        return answer;
    }

    public String getDialog_stat() {
        return dialog_stat;
    }

    public boolean isSave_history() {
        return save_history;
    }

    public String getSid() {
        return sid;
    }

    public List<SemanticBean> getSemantic() {
        return semantic;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public int getRc() {
        return rc;
    }

    public void setRc(int rc) {
        this.rc = rc;
    }

    public String getIntentType() {
        return intentType;
    }

    public void setIntentType(String intentType) {
        this.intentType = intentType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<MoreResultBean> getMoreResults() {
        return moreResults;
    }

    public void setMoreResults(List<MoreResultBean> moreResults) {
        this.moreResults = moreResults;
    }

    public static class DataBean {
        /**
         * airData : 35
         * airQuality : 优
         * city : 北京
         * date : 2018-02-07
         * dateLong : 1517932800
         * exp : {"ct":{"expName":"穿衣指数","level":"冷","prompt":"天气冷，建议着棉服、羽绒服、皮夹克加羊毛衫等冬季服装。年老体弱者宜着厚棉衣、冬大衣或厚羽绒服。"}}
         * humidity : 16%
         * lastUpdateTime : 2018-02-07 11:00
         * pm25 : 9
         * temp : 1
         * tempRange : -9℃ ~ 2℃
         * weather : 多云转晴
         * weatherType : 1
         * wind : 北风4-5级
         * windLevel : 2
         */

        private List<ResultBean> result;

        public void setResult(List<ResultBean> result) {
            this.result = result;
        }

        public List<ResultBean> getResult() {
            return result;
        }

        public static class ResultBean {
            private String city;  //城市
            private String tempReal; //当前温度
            private String tempRange; //温度范围
            private String weather; //天气状况（晴  多云）
            private String airQuality; //空气质量
            private String wind; //风（西风3~4级）

            private String itemid;
            private List<String> singernames;
            private String songname;
            private String source;

            private int airData;
            private String date;
            private int dateLong;

            private String tempHigh;
            private String tempLow;
            /**
             * ct : {"expName":"穿衣指数","level":"冷","prompt":"天气冷，建议着棉服、羽绒服、皮夹克加羊毛衫等冬季服装。年老体弱者宜着厚棉衣、冬大衣或厚羽绒服。"}
             */

            private ExpBean exp;
            private String humidity;
            private String lastUpdateTime;
            private String pm25;
            private int temp;
            private int weatherType;
            private int windLevel;
            private String url;
            private String playUrl;
            private String title;

            private String name;//电话服务

            public void setAirData(int airData) {
                this.airData = airData;
            }

            public void setAirQuality(String airQuality) {
                this.airQuality = airQuality;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public void setDateLong(int dateLong) {
                this.dateLong = dateLong;
            }

            public void setExp(ExpBean exp) {
                this.exp = exp;
            }

            public void setHumidity(String humidity) {
                this.humidity = humidity;
            }

            public void setLastUpdateTime(String lastUpdateTime) {
                this.lastUpdateTime = lastUpdateTime;
            }

            public void setPm25(String pm25) {
                this.pm25 = pm25;
            }

            public void setTemp(int temp) {
                this.temp = temp;
            }

            public void setTempRange(String tempRange) {
                this.tempRange = tempRange;
            }

            public void setWeather(String weather) {
                this.weather = weather;
            }

            public void setWeatherType(int weatherType) {
                this.weatherType = weatherType;
            }

            public void setWind(String wind) {
                this.wind = wind;
            }

            public void setWindLevel(int windLevel) {
                this.windLevel = windLevel;
            }

            public int getAirData() {
                return airData;
            }

            public String getAirQuality() {
                return airQuality;
            }

            public String getCity() {
                return city;
            }

            public String getDate() {
                return date;
            }

            public int getDateLong() {
                return dateLong;
            }

            public ExpBean getExp() {
                return exp;
            }

            public String getHumidity() {
                return humidity;
            }

            public String getLastUpdateTime() {
                return lastUpdateTime;
            }

            public String getPm25() {
                return pm25;
            }

            public int getTemp() {
                return temp;
            }

            public String getTempRange() {
                return tempRange;
            }

            public String getWeather() {
                return weather;
            }

            public int getWeatherType() {
                return weatherType;
            }

            public String getWind() {
                return wind;
            }

            public int getWindLevel() {
                return windLevel;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getPlayUrl() {
                return playUrl;
            }

            public void setPlayUrl(String playUrl) {
                this.playUrl = playUrl;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public String getSongname() {
                return songname;
            }

            public void setSongname(String songname) {
                this.songname = songname;
            }

            public List<String> getSingernames() {
                return singernames;
            }

            public void setSingernames(List<String> singernames) {
                this.singernames = singernames;
            }

            public String getItemid() {
                return itemid;
            }

            public void setItemid(String itemid) {
                this.itemid = itemid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTempReal() {
                return tempReal;
            }

            public void setTempReal(String tempReal) {
                this.tempReal = tempReal;
            }

            public String getTempHigh() {
                return tempHigh;
            }

            public void setTempHigh(String tempHigh) {
                this.tempHigh = tempHigh;
            }

            public String getTempLow() {
                return tempLow;
            }

            public void setTempLow(String tempLow) {
                this.tempLow = tempLow;
            }

            public static class ExpBean {
                /**
                 * expName : 穿衣指数
                 * level : 冷
                 * prompt : 天气冷，建议着棉服、羽绒服、皮夹克加羊毛衫等冬季服装。年老体弱者宜着厚棉衣、冬大衣或厚羽绒服。
                 */

                private CtBean ct;

                public void setCt(CtBean ct) {
                    this.ct = ct;
                }

                public CtBean getCt() {
                    return ct;
                }

                public static class CtBean {
                    private String expName;
                    private String level;
                    private String prompt;

                    public void setExpName(String expName) {
                        this.expName = expName;
                    }

                    public void setLevel(String level) {
                        this.level = level;
                    }

                    public void setPrompt(String prompt) {
                        this.prompt = prompt;
                    }

                    public String getExpName() {
                        return expName;
                    }

                    public String getLevel() {
                        return level;
                    }

                    public String getPrompt() {
                        return prompt;
                    }
                }
            }
        }
    }

    public static class UsedStateBean {
        private String state_key;
        private String state;

        public void setState_key(String state_key) {
            this.state_key = state_key;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getState_key() {
            return state_key;
        }

        public String getState() {
            return state;
        }
    }

    public static class AnswerBean {
        private String text;

        public void setText(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

    public static class SemanticBean {
        private String intent;
        /**
         * name : datetime
         * value : 今天
         * normValue : {"datetime":"2018-02-07","suggestDatetime":"2018-02-07"}
         */

        private List<SlotsBean> slots;

        private String template;

        public void setIntent(String intent) {
            this.intent = intent;
        }

        public void setSlots(List<SlotsBean> slots) {
            this.slots = slots;
        }

        public String getIntent() {
            return intent;
        }

        public List<SlotsBean> getSlots() {
            return slots;
        }

        public void setTemplate(String template) {
            this.template = template;
        }

        public String getTemplate() {
            return template;
        }

        public static class SlotsBean {
            private String name;
            private String value;
            private String normValue;

            public void setName(String name) {
                this.name = name;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public void setNormValue(String normValue) {
                this.normValue = normValue;
            }

            public String getName() {
                return name;
            }

            public String getValue() {
                return value;
            }

            public String getNormValue() {
                return normValue;
            }
        }
    }

    private List<MoreResultBean> moreResults;

    public class MoreResultBean {
        private String service;
        private String text;

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
