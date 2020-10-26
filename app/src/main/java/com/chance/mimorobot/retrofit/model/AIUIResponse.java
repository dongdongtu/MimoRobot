package com.chance.mimorobot.retrofit.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * AIUI webAPI 返回语义结果
 *
 * @Author
 * @Date 2018/9/11
 * @Desc
 * @Url
 */

public class AIUIResponse {
    /**
     * 结果码(具体见错误码)
     */
    public String code;
    /**
     * 描述
     */
    public String desc;
    /**
     * 会话ID
     */
    public String sid;
    /**
     * 结果数据
     */
    public List<Data> data;

    public static class Data {
        /**
         * 业务类型：iat（识别），nlp（语义），tpp（后处理），itrans（翻译）
         */
        public String sub = "";
        /**
         * 识别结果：详细结果（object），精简结果（string)
         */
        public String text = "";
        /**
         * 语义结果
         */
        public AIUIIntent intent;
        /**
         * 后处理 (string)，翻译 (object)等结果
         */
        public String content = "";
        /**
         * 结果序号，同一业务逐渐递增
         */
        public int result_id;
        /**
         * 个性化参数,json字符串，目前支持用户级（auth_id）、应用级（appid）和用户自定义级，不支持透传其他参数
         */
        public String auth_id;

    }

    public static class AIUIIntent {
        /**
         * 应答码(response code)
         */
        public int rc;
        /**
         * 错误信息
         */
        public Error error;
        /**
         * 用户的输入，可能和请求中的原始text不完全一致，因服务器可能会对text进行语言纠错
         */
        public String text;
        /**
         * 技能提供者，不存在时默认表示为IFLYTEK提供的开放技能
         */
        public String vendor;
        /**
         * 技能的全局唯一名称，一般为vendor.name，vendor不存在时默认为IFLYTEK提供的开放技能。
         */
        public String service;
        /**
         * 本次语义（包括历史继承过来的语义）结构化表示，各技能自定义
         */
        public JsonArray semantic;
        /**
         * 数据结构化表示，各技能自定义
         */
        public JsonObject data;
        /**
         * 对结果内容的最简化文本/图片描述，各技能自定义
         */
        public Answer answer;
        /**
         * 用于客户端判断是否使用信源返回数据
         */
        public String dialog_stat;
        /**
         * 技能的类别，部分技能用来区分不同的来源
         */
        public String serviceCategory;
    }

    /**
     * 语义结果内容
     */
    public static class Answer {
        /**
         * 语义结果的简单文本
         */
        public String text;
    }

    /**
     * 错误信息对象
     */
    public static class Error {
        public int code;
        public String message;
    }

}
