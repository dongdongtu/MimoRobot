package com.chance.mimorobot.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextHighLightUtil {
    /**
     * 关键字高亮显示
     *
     * @param context 上下文
     * @param text    需要显示的文字
     * @param target  需要高亮的关键字
     * @param color   高亮颜色
     * @param start   头部增加高亮文字个数
     * @param end     尾部增加高亮文字个数
     * @return 处理完后的结果
     */
    public static SpannableString highlight(Context context, String text, String target,
                                            String color, int start, int end) {
        SpannableString spannableString = new SpannableString(text);
//        Pattern pattern = Pattern.compile(target);
//        Matcher matcher = pattern.matcher(text);
//        while (matcher.find()) {
//            ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor(color));
//            spannableString.setSpan(span, matcher.start() - start, matcher.end() + end,
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
        SpannableString style=new SpannableString(text);
        style.setSpan(new BackgroundColorSpan(Color.RED),start,end,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new ForegroundColorSpan(Color.RED),7,9,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return spannableString;
    }

}
