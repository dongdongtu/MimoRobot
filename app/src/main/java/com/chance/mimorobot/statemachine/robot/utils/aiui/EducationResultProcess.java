package com.chance.mimorobot.statemachine.robot.utils.aiui;

import android.util.Log;

import com.blankj.utilcode.util.StringUtils;
import com.chance.mimorobot.model.AiuiResultEntity;
import com.chance.mimorobot.statemachine.robot.Output;
import com.chance.mimorobot.statemachine.robot.RobotStateMachine;
import com.chance.mimorobot.statemachine.robot.utils.aiui.constants.AiuiConstants;
import com.chance.mimorobot.statemachine.robot.utils.aiui.constants.EducationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * created by Lynn
 * on 2019/7/30
 */
public class EducationResultProcess extends CommonResultProcess {

    @Override
    public void onProcessAiuiResult(String aiuiResult, RobotStateMachine robotStateMachine) {
//        AiuiResultEntity aiuiResultEntity = new AiuiResultEntityJsonMapper().transformAiuiResultEntity(aiuiResult);
        Type aiuiResultEntityType = new TypeToken<AiuiResultEntity>() {
        }.getType();
        AiuiResultEntity aiuiResultEntity= new Gson().fromJson(aiuiResult, aiuiResultEntityType);
        onProcessAiuiResult(aiuiResultEntity, robotStateMachine);
    }

    @Override
    public void onProcessAiuiResult(AiuiResultEntity aiuiResultEntity, RobotStateMachine robotStateMachine) {
        String normValue = "";

        if (aiuiResultEntity != null && aiuiResultEntity.getSemantic() != null && !aiuiResultEntity.getSemantic().isEmpty()
                && aiuiResultEntity.getSemantic().get(0).getSlots() != null && !aiuiResultEntity.getSemantic().get(0).getSlots().isEmpty()) {

            for (AiuiResultEntity.SemanticBean.SlotsBean slotsBean : aiuiResultEntity.getSemantic().get(0).getSlots()) {
                if (slotsBean.getName().equals(AiuiConstants.SemanticConstants.EDUCATION_SEMANTIC)) {
                    normValue = slotsBean.getNormValue();
                    break;
                }
            }

            if (StringUtils.isEmpty(normValue)) {
                return;
            }

            Log.i("EducationResultProcess", "normValue=" + normValue);

            switch (normValue) {
                case EducationConstants.primary_education:
                    navigatorEducationActivity("小学");
                    break;
                case EducationConstants.preschool_education:
                    navigatorEducationActivity("学前");
                    break;
                case EducationConstants.middleschool_education:
                    navigatorEducationActivity("中学");
                    break;
                case EducationConstants.primary_read:
                    navigatorEducationActivity("同步点读", "2");
                    break;
                case EducationConstants.primary_teacher_lesson:
                    navigatorEducationActivity("名师课堂", "2");
                    break;
                case EducationConstants.primary_book_read:
                    //英语测评
//                    Output.navigatorOralEnglishTest();
                    break;
                case EducationConstants.primary_sync_teach:
//                    navigatorEducationActivity("同步教辅");
                    break;
                case EducationConstants.primary_ancient_Chinese_dictionary:
                    navigatorEducationActivity("古汉语词典");
                    break;
                case EducationConstants.primary_Chinese_idiom_dictionary:
                    navigatorEducationActivity("中华成语词典");
                    break;
                case EducationConstants.primary_modern_Chinese_dictionary:
                    navigatorEducationActivity("现代汉语词典");
                    break;
                case EducationConstants.primary_tjf_dictionary:
                    navigatorEducationActivity("同近反义词典");
                    break;
                case EducationConstants.primary_EC_dictionary:
                    navigatorEducationActivity("英汉大词典");
                    break;
                case EducationConstants.primary_practical_EC_dictionary:
                    navigatorEducationActivity("实用英汉词典");
                    break;
                case EducationConstants.primary_practical_CE_dictionary:
                    navigatorEducationActivity("实用汉英词典");
                    break;
                case EducationConstants.primary_diagnosis:
                    navigatorEducationActivity("智能诊断", "2");
                    break;
                case EducationConstants.primary_pinyin:
                    navigatorEducationActivity("拼音学习");
                    break;
                case EducationConstants.primary_hanzi:
                    navigatorEducationActivity("汉字学习");
                    break;
                case EducationConstants.primary_fun_hanzi:
                    navigatorEducationActivity("趣味学汉字");
                    break;
                case EducationConstants.primary_poetry:
                    navigatorEducationActivity("诗词欣赏");
                    break;
                case EducationConstants.primary_idiom:
                    navigatorEducationActivity("成语故事", "2");
                    break;
                case EducationConstants.primary_outside_read:
                    navigatorEducationActivity("课外阅读");
                    break;
                case EducationConstants.primary_susuan:
                    navigatorEducationActivity("速算闯关");
                    break;
                case EducationConstants.primary_geomtry:
                    navigatorEducationActivity("几何图形");
                    break;
                case EducationConstants.primary_aoshu:
                    navigatorEducationActivity("奥数课堂");
                    break;
                case EducationConstants.primary_math_formula:
                    navigatorEducationActivity("数学公式");
                    break;
                case EducationConstants.primary_fun_math:
                    navigatorEducationActivity("趣味数学");
                    break;
                case EducationConstants.primary_ladder:
                    navigatorEducationActivity("阶梯应用题");
                    break;
                case EducationConstants.primary_fun_english:
                    navigatorEducationActivity("趣味英语");
                    break;
                case EducationConstants.primary_english_word:
                    navigatorEducationActivity("分类词汇");
                    break;
                case EducationConstants.primary_english_conversation:
                    navigatorEducationActivity("英语会话");
                    break;
                case EducationConstants.primary_bobing_english:
                    navigatorEducationActivity("薄冰语法");
                    break;
                case EducationConstants.primary_english_writting:
                    navigatorEducationActivity("英语写作");
                    break;
                case EducationConstants.primary_pet_park:
                    navigatorEducationActivity("宠物乐园");
                    break;
                case EducationConstants.primary_fun_study:
                    navigatorEducationActivity("趣学堡");
                    break;
                case EducationConstants.primary_iqiyi_cartoon:
                    navigatorEducationActivity("爱奇艺动画");
                    break;
                case EducationConstants.primary_app_store:
                    navigatorEducationActivity("应用商店");
                    break;
                case EducationConstants.primary_dictionary:
                    navigatorEducationActivity("综合词典");
                    break;
                case EducationConstants.primary_my_app:
                    navigatorEducationActivity("我的应用");
                    break;
                case EducationConstants.primary_parent_help:
                    navigatorEducationActivity("家长助手");
                    break;
                case EducationConstants.primary_system_tool:
                    navigatorEducationActivity("系统工具");
                    break;
                case EducationConstants.preschool_love_kindergarten:
                    navigatorEducationActivity("爱上幼儿园");
                    break;
                case EducationConstants.preschool_fun_together:
                    navigatorEducationActivity("大家一起玩");
                    break;
                case EducationConstants.preschool_learn_to_share:
                    navigatorEducationActivity("懂得分享");
                    break;
                case EducationConstants.preschool_hospitality:
                    navigatorEducationActivity("热情好客");
                    break;
                case EducationConstants.preschool_like_you:
                    navigatorEducationActivity("我喜欢你");
                    break;
                case EducationConstants.preschool_put_chopsticks:
                    navigatorEducationActivity("摆碗筷");
                    break;
                case EducationConstants.preschool_take_bus:
                    navigatorEducationActivity("乘车");
                    break;
                case EducationConstants.preschool_sorry:
                    navigatorEducationActivity("对不起");
                    break;
                case EducationConstants.preschool_help_each_other:
                    navigatorEducationActivity("互相帮忙");
                    break;
                case EducationConstants.preschool_please:
                    navigatorEducationActivity("请");
                    break;
                case EducationConstants.preschool_thanks:
                    navigatorEducationActivity("谢谢");
                    break;
                case EducationConstants.preschool_eat_everything:
                    navigatorEducationActivity("不挑食");
                    break;
                case EducationConstants.preschool_eat:
                    navigatorEducationActivity("吃饭");
                    break;
                case EducationConstants.preschool_garbage:
                    navigatorEducationActivity("垃圾分类");
                    break;
                case EducationConstants.preschool_pickup_toys:
                    navigatorEducationActivity("收玩具");
                    break;
                case EducationConstants.preschool_wash_face:
                    navigatorEducationActivity("刷牙洗脸");
                    break;
                case EducationConstants.preschool_eyes:
                    navigatorEducationActivity("我爱眼睛");
                    break;
                case EducationConstants.preschool_healthy_lifestyle:
                    navigatorEducationActivity("早睡早起身体好");
                    break;
                case EducationConstants.preschool_montessori_sense:
                    navigatorEducationActivity("感官");
                    break;
                case EducationConstants.preschool_montessori_science:
                    navigatorEducationActivity("科学");
                    break;
                case EducationConstants.preschool_montessori_math:
                    navigatorEducationActivity("数学");
                    break;
                case EducationConstants.preschool_baijiaxing:
                    navigatorEducationActivity("百家姓");
                    break;
                case EducationConstants.preschool_dizigui:
                    navigatorEducationActivity("弟子规");
                    break;
                case EducationConstants.preschool_lunyu:
                    navigatorEducationActivity("论语");
                    break;
                case EducationConstants.preschool_sanzijing:
                    navigatorEducationActivity("三字经");
                    break;
                case EducationConstants.preschool_qianziwen:
                    navigatorEducationActivity("千字文");
                    break;
                case EducationConstants.preschool_zengguangxianwen:
                    navigatorEducationActivity("增广贤文");
                    break;
                case EducationConstants.preschool_english_song:
                    navigatorEducationActivity("英文儿歌");
                    break;
                case EducationConstants.preschool_nature_song:
                    navigatorEducationActivity("自然儿歌");
                    break;
                case EducationConstants.preschool_life_song:
                    navigatorEducationActivity("生活儿歌");
                    break;
                case EducationConstants.preschool_plant_song:
                    navigatorEducationActivity("植物儿歌");
                    break;
                case EducationConstants.preschool_animal_song:
                    navigatorEducationActivity("动物儿歌");
                    break;
                case EducationConstants.preschool_music_battle:
                    navigatorEducationActivity("音乐保卫战");
                    break;
                case EducationConstants.preschool_drums:
                    navigatorEducationActivity("小小架子鼓");
                    break;
                case EducationConstants.preschool_fun_street:
                    navigatorEducationActivity("热闹的街道");
                    break;
                case EducationConstants.preschool_container:
                    navigatorEducationActivity("容器");
                    break;
                case EducationConstants.preschool_history_spots:
                    navigatorEducationActivity("世界名胜古迹");
                    break;
                case EducationConstants.preschool_flip_puzzle:
                    navigatorEducationActivity("翻转拼图");
                    break;
                case EducationConstants.preschool_snow:
                    navigatorEducationActivity("下雪啦");
                    break;
                case EducationConstants.preschool_toy_saler:
                    navigatorEducationActivity("小小玩具售货员");
                    break;
                case EducationConstants.preschool_far_near:
                    navigatorEducationActivity("远近");
                    break;
                case EducationConstants.preschool_find:
                    navigatorEducationActivity("找一找");
                    break;
                case EducationConstants.preschool_my_room:
                    navigatorEducationActivity("我的房间");
                    break;
                case EducationConstants.preschool_sea_baby:
                    navigatorEducationActivity("大海宝宝");
                    break;
                case EducationConstants.preschool_fun_puzzle:
                    navigatorEducationActivity("趣味拼图");
                    break;
                case EducationConstants.preschool_fun_color:
                    navigatorEducationActivity("趣味颜色");
                    break;
                case EducationConstants.preschool_identify:
                    navigatorEducationActivity("慧眼识物");
                    break;
                case EducationConstants.preschool_magic_candy:
                    navigatorEducationActivity("魔法糖果");
                    break;
                case EducationConstants.preschool_ecd:
                    navigatorEducationActivity("早教认知");
                    break;
                case EducationConstants.preschool_pinyin:
                    navigatorEducationActivity("拼音学习");
                    break;
                case EducationConstants.preschool_magic_word:
                    navigatorEducationActivity("魔法识字");
                    break;
                case EducationConstants.preschool_english:
                    navigatorEducationActivity("手绘儿童英语");
                    break;
                case EducationConstants.preschool_english_letter:
                    navigatorEducationActivity("字母学习");
                    break;
                case EducationConstants.preschool_my_abc:
                    navigatorEducationActivity("ABC");
                    break;
                case EducationConstants.preschool_oral_english:
                    navigatorEducationActivity("儿童互动口语");
                    break;
                case EducationConstants.preschool_1001_nights:
                    navigatorEducationActivity("一千零一夜");
                    break;
                case EducationConstants.preschool_chengyu_story:
                    navigatorEducationActivity("成语故事");
                    break;
                case EducationConstants.preschool_person_story:
                    navigatorEducationActivity("人物故事");
                    break;
                case EducationConstants.preschool_yanyu_story:
                    navigatorEducationActivity("谚语故事");
                    break;
                case EducationConstants.preschool_fairy_tale:
                    navigatorEducationActivity("童话故事");
                    break;
                case EducationConstants.preschool_aesop_fables:
                    navigatorEducationActivity("伊索寓言");
                    break;
                case EducationConstants.preschool_painting_story:
                    navigatorEducationActivity("绘本故事");
                    break;
                case EducationConstants.preschool_bear_eating:
                    navigatorEducationActivity("小熊吃饭");
                    break;
                case EducationConstants.preschool_rescue:
                    navigatorEducationActivity("营救行动");
                    break;
                case EducationConstants.preschool_wash_hands:
                    navigatorEducationActivity("宝宝洗手");
                    break;
                case EducationConstants.preschool_love_sports:
                    navigatorEducationActivity("我爱运动");
                    break;
                case EducationConstants.preschool_protect_tooth:
                    navigatorEducationActivity("保护牙齿");
                    break;
                case EducationConstants.preschool_pig_watching_TV:
                    navigatorEducationActivity("小猪看电视");
                    break;
                case EducationConstants.preschool_eat_everything2:
                    navigatorEducationActivity("我不挑食");
                    break;
                case EducationConstants.preschool_beautiful_clothes:
                    navigatorEducationActivity("漂亮的衣服");
                    break;
                case EducationConstants.preschool_common_symbols:
                    navigatorEducationActivity("常用标志");
                    break;
                case EducationConstants.preschool_respect:
                    navigatorEducationActivity("尊老爱幼");
                    break;
                case EducationConstants.preschool_emergency_phone:
                    navigatorEducationActivity("紧急电话");
                    break;
                case EducationConstants.preschool_play_order:
                    navigatorEducationActivity("遵守秩序");
                    break;
                case EducationConstants.preschool_polite:
                    navigatorEducationActivity("礼貌待人");
                    break;
                case EducationConstants.preschool_learn_0:
                    navigatorEducationActivity("认识0");
                    break;
                case EducationConstants.preschool_learn_1_5:
                    navigatorEducationActivity("认识1-5");
                    break;
                case EducationConstants.preschool_learn_6_10:
                    navigatorEducationActivity("认识6-10");
                    break;
                case EducationConstants.preschool_learn_10_20:
                    navigatorEducationActivity("10-20的认识");
                    break;
                case EducationConstants.preschool_learn_1_100:
                    navigatorEducationActivity("认识数字1-100");
                    break;
                case EducationConstants.preschool_learn_math_symbol:
                    navigatorEducationActivity("认识等于大于小于");
                    break;
                case EducationConstants.preschool_learn_graphics:
                    navigatorEducationActivity("认识图形");
                    break;
                case EducationConstants.preschool_learn_clock:
                    navigatorEducationActivity("认识钟表");
                    break;
                case EducationConstants.preschool_learn_week:
                    navigatorEducationActivity("认识星期");
                    break;
                case EducationConstants.preschool_learn_month:
                    navigatorEducationActivity("认识月份");
                    break;
                case EducationConstants.preschool_pig_birthday:
                    navigatorEducationActivity("小猪的生日");
                    break;
                case EducationConstants.preschool_add_sub_10:
                    navigatorEducationActivity("10以内的加减法");
                    break;
                case EducationConstants.preschool_add_20:
                    navigatorEducationActivity("20以内加法");
                    break;
                case EducationConstants.preschool_sub_20:
                    navigatorEducationActivity("20以内减法");
                    break;
                case EducationConstants.preschool_sub:
                    navigatorEducationActivity("不退位减法");
                    break;
                case EducationConstants.preschool_add_sub:
                    navigatorEducationActivity("连加减法");
                    break;
                case EducationConstants.preschool_add_11:
                    navigatorEducationActivity("整十数加一位数");
                    break;
                case EducationConstants.preschool_quard:
                    navigatorEducationActivity("四边形");
                    break;
                case EducationConstants.preschool_line:
                    navigatorEducationActivity("直线间的关系");
                    break;
                case EducationConstants.preschool_statics:
                    navigatorEducationActivity("统计");
                    break;
                case EducationConstants.preschool_fun_math:
                    navigatorEducationActivity("趣味数学");
                    break;
                case EducationConstants.preschool_mini_tank:
                    navigatorEducationActivity("迷你坦克");
                    break;
                case EducationConstants.preschool_food_locker:
                    navigatorEducationActivity("食品柜");
                    break;
                case EducationConstants.preschool_compare:
                    navigatorEducationActivity("作比较");
                    break;
                case EducationConstants.preschool_animal_go_home:
                    navigatorEducationActivity("动物回家");
                    break;
                case EducationConstants.preschool_plant_world:
                    navigatorEducationActivity("植物世界");
                    break;
                case EducationConstants.preschool_bear_puzzle:
                    navigatorEducationActivity("小熊拼图");
                    break;
                case EducationConstants.preschool_ocean_world:
                    navigatorEducationActivity("海洋世界");
                    break;
                case EducationConstants.preschool_hunt_bug:
                    navigatorEducationActivity("捕虫行动");
                    break;
                case EducationConstants.preschool_fruit_battle:
                    navigatorEducationActivity("水果保卫战");
                    break;
                case EducationConstants.preschool_day_night:
                    navigatorEducationActivity("白天和晚上");
                    break;
                case EducationConstants.preschool_learn_vegetable:
                    navigatorEducationActivity("认识蔬菜");
                    break;
                case EducationConstants.preschool_garden:
                    navigatorEducationActivity("花园日记");
                    break;
                case EducationConstants.preschool_learn_animal:
                    navigatorEducationActivity("认识动物");
                    break;
                case EducationConstants.preschool_different_clothes:
                    navigatorEducationActivity("不同的穿着");
                    break;
                case EducationConstants.preschool_big_monster:
                    navigatorEducationActivity("大怪兽");
                    break;
                case EducationConstants.preschool_who_came_to_my_house:
                    navigatorEducationActivity("谁来过我家");
                    break;
                case EducationConstants.preschool_constellation:
                    navigatorEducationActivity("星座");
                    break;
                case EducationConstants.preschool_on_the_farm:
                    navigatorEducationActivity("在农场");
                    break;
                case EducationConstants.preschool_find_orders:
                    navigatorEducationActivity("找顺序");
                    break;
                case EducationConstants.preschool_find_tail:
                    navigatorEducationActivity("找尾巴");
                    break;
                case EducationConstants.preschool_find_shadow:
                    navigatorEducationActivity("找影子");
                    break;
                case EducationConstants.preschool_parent_child:
                    navigatorEducationActivity("亲子互动");
                    break;
                case EducationConstants.preschool_montessori:
                    navigatorEducationActivity("蒙特梭利");
                    break;
                case EducationConstants.preschool_fun_guoxue:
                    navigatorEducationActivity("快乐国学");
                    break;
                case EducationConstants.preschool_story_house:
                    navigatorEducationActivity("故事小屋");
                    break;
                case EducationConstants.preschool_child_song:
                    navigatorEducationActivity("儿歌天地");
                    break;
                case EducationConstants.preschool_vision_zone:
                    navigatorEducationActivity("视觉空间");
                    break;
                case EducationConstants.preschool_language:
                    navigatorEducationActivity("语言启蒙");
                    break;
                case EducationConstants.preschool_habit:
                    navigatorEducationActivity("习惯养成");
                    break;
                case EducationConstants.preschool_math_logic:
                    navigatorEducationActivity("数理逻辑");
                    break;
                case EducationConstants.preschool_nature_knowledge:
                    navigatorEducationActivity("自然知识");
                    break;
                case EducationConstants.preschool_fun_cartoon:
                    navigatorEducationActivity("欢乐动漫");
                    break;
                case EducationConstants.middleschool_sync_read:
                    navigatorEducationActivity("同步点读", "3");
                    break;
                case EducationConstants.middleschool_teacher_lesson:
                    navigatorEducationActivity("名师课堂", "3");
                    break;
                case EducationConstants.middleschool_nine_classes:
                    navigatorEducationActivity("九门功课");
                    break;
                case EducationConstants.middleschool_exam:
                    navigatorEducationActivity("决胜中高考");
                    break;
                case EducationConstants.middleschool_diagnose:
                    navigatorEducationActivity("智能诊断", "3");
                    break;
                case EducationConstants.middleschool_dictionary:
                    navigatorEducationActivity("综合词典");
                    break;
                case EducationConstants.middleschool_development:
                    navigatorEducationActivity("综合拓展");
                    break;
                case EducationConstants.middleschool_my_application:
                    navigatorEducationActivity("我的应用");
                    break;
                case EducationConstants.middleschool_parent_help:
                    navigatorEducationActivity("家长助手");
                    break;
                case EducationConstants.middleschool_homework:
                    navigatorEducationActivity("作业帮");
                    break;
                case EducationConstants.middleschool_pyhsical_formula:
                    navigatorEducationActivity("物理公式");
                    break;
                case EducationConstants.middleschool_chemical_formula:
                    navigatorEducationActivity("化学公式");
                    break;
                case EducationConstants.middleschool_periodic_table:
                    navigatorEducationActivity("元素周期表");
                    break;
                case EducationConstants.middleschool_world_history:
                    navigatorEducationActivity("世界历史");
                    break;
                case EducationConstants.middleschool_biological_knowledge:
                    navigatorEducationActivity("生物常识");
                    break;
                case EducationConstants.middleschool_geographic_knowledge:
                    navigatorEducationActivity("地理知识");
                    break;
                case EducationConstants.middleschool_political_knowledge:
                    navigatorEducationActivity("政治知识");
                    break;
                case EducationConstants.middleschool_animal_world:
                    navigatorEducationActivity("动物世界");
                    break;
                case EducationConstants.middleschool_Chinese_history:
                    navigatorEducationActivity("中国历史");
                    break;
                case EducationConstants.middleschool_Chinese_5_thousands_years:
                    navigatorEducationActivity("上下五千年");
                    break;
                case EducationConstants.middleschool_study_log:
                    navigatorEducationActivity("学习日志");
                    break;
                case EducationConstants.middleschool_green_internet:
                    navigatorEducationActivity("绿色上网");
                    break;
                case EducationConstants.middleschool_resource_management:
                    navigatorEducationActivity("资源管理");
                    break;
                case EducationConstants.middleschool_application_management:
                    navigatorEducationActivity("应用管理");
                    break;
                case EducationConstants.middleschool_system_setting:
                    navigatorEducationActivity("系统设置");
                    break;
                case EducationConstants.middleschool_android_system:
                    navigatorEducationActivity("安卓系统");
                    break;
                case EducationConstants.middleschool_change_passwrod:
                    navigatorEducationActivity("修改密码");
                    break;
                case EducationConstants.close_education_application:
                    closeEducationApplication();
                    break;
                case EducationConstants.oral_english_test:
//                    Output.navigatorOralEnglishTest();
                    break;
            }
        }
    }

    private void navigatorEducationActivity(String type) {
        navigatorEducationActivity(type, "-1");
    }

    private void navigatorEducationActivity(String type, String grade) {
//        Output.navigatorEducationActivity(type, grade);
    }

    private void closeEducationApplication() {
//        Output.killEducationActivity();
    }
}