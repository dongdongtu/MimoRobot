package com.chance.mimorobot.statemachine.robot.utils.aiui.constants;

/**
 * Created by Administrator on 2018/5/14.
 */

public class AiuiConstants {

    public static class IntentTypeConstants {
        public static final String CUSTOM_INTENT_TYPE = "custom";
    }

    public static class CategoryConstants {
        public static final String CUSTOM_CATEGORY = "OS7970178988.mimo";
        public static final String CUSTOM_CATEGORYCONTROL = "OS7970178988.MIMOcontrol";
        public static final String CUSTOM_EDU_CATEGORY = "JOYRRY.edu_action";
        public static final String CUSTOM_MAP_CATEGORY = "JOYRRY.map_search";
        public static final String IFLY_TELEPHONE = "IFLYTEK.telephone";
    }

    public static class IntentConstants {

        public static final String DEFAULT_INTENT  = "default_intent";
        public static final String ACTION_INTENT = "action_intent";
        public static final String COMMAND_INTENT = "command_intent";
        public static final String OTHER_PAGE_INTENT = "other_page_intent";
    }

    public static class SemanticConstants {
        public static final String MOVE_ACTION_SEMANTIC = "move_action";
        public static final String COMMAND_SEMANTIC = "command";
        public static final String EDUCATION_SEMANTIC = "education";
        public static final String OTHER_PAGE_SEMANTIC = "other_page";
    }

    public static class ServiceConstants {
        public static final String WEATHER_SERVICE = "weather";
        public static final String NEWS_SERVICE = "news";
        public static final String TRAIN = "train";
        public static final String SCHEDULE_SERVICE = "scheduleX";
        public static final String MUSIC_SERVICE = "musicX";
        public static final String CMD_SERVICE = "cmd";
        public static final String FLIGHT = "flight";
    }
}
