package com.chance.mimorobot.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.InputFilter;
import android.text.Spanned;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhangkai on 2018/6/1.
 * 工具类验证
 */

public class ObjectUtils {
    /**
     * 判断 list 是否为空 如果是空返回true
     * @param list
     * @return
     */
    public static boolean isEmpty(List<? extends Object> list) {
//        if (list == null || list.size() <= 0) return true;
//        return false;
        return  list == null || list.size() <= 0;
    }

    /**
     * 判断对象是否为空 如果是空返回true
     * @param o
     * @return
     */
    public static boolean isEmpty(Object o) {
//        if (o == null) return true;
//        return false;
        return o == null;
    }

    /**
     * 判断数组是不是空 如果是空返回true
     * @param o
     * @return
     */
    public static boolean isEmpty(Object[] o){
//        if (o == null || o.length<=0) return true;
//        return false;
        return o == null || o.length<=0;
    }

    /**
     * 1) 密码控制只能输入字母、数字、特殊符号(~!@#$%^&*()_+[]{}|\;:'",./<>?)
     * 2) 长度 6-16 位，必须包括字母、数字、特殊符号中的2种
     * 3) 密码不能包含用户名信息
     * @param newPwd 密码
     * @param userId 用户名
     */
    public static boolean isPwd(String newPwd,String userId){
       // 判断密码是否包含数字：包含返回1，不包含返回0
        int i = newPwd.matches(".*\\d+.*") ? 1 : 0;

        //判断密码是否包含字母：包含返回1，不包含返回0
        int j = newPwd.matches(".*[a-zA-Z]+.*") ? 1 : 0;

      //  判断密码是否包含特殊符号(~!@#$%^&*()_+|<>,.?/:;'[]{}\)：包含返回1，不包含返回0
        int k = newPwd.matches(".*[~!@#$%^&*()_+|<>,.?/:;'\\[\\]{}\"]+.*") ? 1 : 0;

        //判断密码长度是否在6-16位
        int l = newPwd.length();

        //判断密码中是否包含用户名
        boolean contains = newPwd.contains(userId);

        if (i + j + k < 2 || l < 6 || l > 16 || contains) {
           return false;
        }
        return true;
    }

    /**
     * editText 限制输入特殊字符
     * @return
     */
    public static InputFilter[] getEditTextInputSpeChat() {
        return  new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？ ]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find()) {
                    return "";
                } else {
                    return null;
                }
            }
        }};
    }
    /**
     * 判断手机是否安装某个应用
     *
     * @param context
     * @param appPackageName 应用包名
     * @return true：安装，false：未安装
     */
    public static boolean isApplicationAvailable(Context context, String appPackageName) {
        try {
            // 获取packageManager
            PackageManager packageManager = context.getPackageManager();
            // 获取所有已安装程序的包信息
            List<PackageInfo> pInfo = packageManager.getInstalledPackages(0);
            if (pInfo != null) {
                for (int i = 0; i < pInfo.size(); i++) {
                    String pn = pInfo.get(i).packageName;
                    if (appPackageName.equals(pn)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception ignored) {
            return false;
        }
    }
}
