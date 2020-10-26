package com.chance.mimorobot.statemachine.robot;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;


import com.chance.mimorobot.manager.MediaPlayerManager;
import com.chance.mimorobot.manager.SerialControlManager;
import com.chance.mimorobot.manager.VocalSpeakManager;

import cn.chuangze.robot.aiuilibrary.AIUIWrapper;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.blankj.utilcode.util.ActivityUtils.getTopActivity;


/**
 * Created by Administrator on 2018/5/12
 * 状态机的输出
 */

public class Output {
    private static final String ERROR = "播放音频失败";
    private static boolean bCanCallback;


    public static void send() {

    }

//    private static VocalSpeakInterface vocalSpeakInterface = VocalSpeakManager.getInstance();

    /**
     * 控制机器人说话
     *
     * @param text 机器人要说的内容
     */
    public static void speak(String text) {
        speak(text, true);
    }

    public static void speak(String text, boolean changeFace) {
        VocalSpeakManager.getInstance().activateSpeak(text);
//        if (MediaPlayerManager.getInstance().isPlaying()) {
//            MediaPlayerManager.getInstance().stopPlay();
//        }
//        vocalSpeakInterface.activateSpeak(text, changeFace);

    }

    /**
     * 让机器人停止说话
     */
    public static void stopSpeak() {
        VocalSpeakManager.getInstance().stopSpeaking();
//        vocalSpeakInterface.stopSpeaking();
    }


    public static void goSleep(){
        Log.e("goSleep","goSleep");
//        SerialControlManager.newInstance().sleep();
        AIUIWrapper.getInstance(getTopActivity().getApplicationContext()).sleep();
    }

    /**
     * 机器人说完话后播放url
     *
     * @param text 机器人要说的话
     * @param url  机器人需要播放的资源
     */
    public static void playUrlAfterSpeak(String text, final String url) {
        bCanCallback = true;
//        vocalSpeakInterface.activateSpeakCallback(text, new VocalSpeakManager.OnSpeakListener() {
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onCompleted() {
//                if (bCanCallback) {
//                    bCanCallback = false;
//                    playUrl(url);
//                }
//                vocalSpeakInterface.clearListener();
//            }
//        });
    }

//    public static void playAfterSpeak(String text, VocalSpeakManager.OnSpeakListener onSpeakListener) {
//        vocalSpeakInterface.activateSpeakCallback(text, onSpeakListener);
//    }
//
//    public static void clearSpeakListener() {
//        vocalSpeakInterface.clearListener();
//    }

    /**
     * 导航到其他activity
     *
     * @param clz Activity的Class对象
     */
    public static void navigatorActivity(Class<? extends Activity> clz) {
        getTopActivity().startActivity(new Intent(getTopActivity().getApplicationContext(), clz));
    }

    public static void navigatorActivity(Intent intent) {
        getTopActivity().startActivity(intent);
    }

//    public static void navigatorEducationActivity(String type) {
//        navigatorEducationActivity(type, "-1");
//    }

//    private static int downloadPosition = -1;

//    private static void logDownloadList() {
//        for (DownloadItemModel model : DownloadManagerUtil.getInstance().getRealDownloadList()) {
//            Log.i("real_getDownloadAppList",
//                    "appUrl = " + model.getDownloadUrl()
//                            + ", appVersionCode = " + model.getVersionCode()
//                            + ", appPackageName = " + model.getPackageName());
//        }
//    }

//    private static boolean isNeedDownload(boolean isStudyApp) {
//        Context context = ActivityUtils.getTopActivity().getApplicationContext();
//        //判断是否正在下载
//        if (RobotStatus.getInstance().isDownloading()) {
//            Toast.makeText(context, "主人，我正在努力下载，请耐心等待", Toast.LENGTH_SHORT).show();
//            speak("主人，我正在努力下载，请耐心等待");
//            return true;
//        }
//
//        String downloadTips = "使用该功能需要更新相应的软件，是否现在更新？";
//        downloadPosition = -1;
//
//        if (isStudyApp) {
//            //处理真正需要下载的列表
//            DownloadManagerUtil.getInstance().getRealDownloadList().clear();
//            for (DownloadItemModel model : DownloadManagerUtil.getInstance().getDownloadList()) {
//                if (DownloadManagerUtil.getInstance().isAppInstalled(context, model.getPackageName())) {
//                    if (DownloadManagerUtil.getInstance().getAppVersionCode(context, model.getPackageName()) < Long.valueOf(model.getVersionCode())) {
//                        model.setInstalled(true);
//                        DownloadManagerUtil.getInstance().getRealDownloadList().add(model);
//                    }
//                } else {
//                    model.setInstalled(false);
//                    DownloadManagerUtil.getInstance().getRealDownloadList().add(model);
//                }
//            }
//
//            //打印列表
//            logDownloadList();
//
//            //判断教育资源是否需要下载或更新
//            if (DownloadManagerUtil.getInstance().getRealDownloadList() != null && DownloadManagerUtil.getInstance().getRealDownloadList().size() != 0) {
//                for (int i = 0; i < DownloadManagerUtil.getInstance().getRealDownloadList().size(); i++) {
//                    DownloadItemModel item = DownloadManagerUtil.getInstance().getRealDownloadList().get(i);
//                    if (item.isInstalled()) {
//                        if (DownloadManagerUtil.getInstance().getAppVersionCode(context, item.getPackageName()) < Long.valueOf(item.getVersionCode())) {
//                            downloadPosition = i;
//                            break;
//                        } else {
//                            downloadPosition = -1;
//                        }
//                    } else {
//                        downloadPosition = 0;
//                    }
//                }
//            } else if (!DownloadManagerUtil.getInstance().isAppInstalled(context, "com.wyt.launcher.yzb")) {
//                speak("现在有点问题，请稍候重试");
//                return true;
//            }
//        } else {
//            if (!DownloadManagerUtil.getInstance().isAppInstalled(context, "com.baidu.BaiduMap")) {
//                downloadTips = "使用该功能需要下载相应的软件，是否现在下载？";
//                downloadPosition = 0;
//            }
//        }
//        if (downloadPosition != -1) {
//            speak(downloadTips);
//            MaterialDialog dialog = new MaterialDialog.Builder(ActivityUtils.getTopActivity())
//                    .title(downloadTips)
//                    .cancelable(true)
//                    .positiveText("是")
//                    .negativeText("不，稍后再说")
//                    .onPositive(new MaterialDialog.SingleButtonCallback() {
//                        @Override
//                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                            stopSpeak();
//                            RobotStatus.getInstance().setDownloading(true);
//                            Toast.makeText(context, "开始下载，请稍候……", Toast.LENGTH_SHORT).show();
//                            if (isStudyApp) {
//                                DownloadManagerUtil.getInstance().download(downloadPosition);
//                            } else {
//                                //只下载百度地图
//                                for (int i = 0; i < DownloadManagerUtil.getInstance().getDownloadList().size(); i++) {
//                                    if (DownloadManagerUtil.getInstance().getDownloadList().get(i).getPackageName().equals("com.baidu.BaiduMap")) {
//                                        DownloadManagerUtil.getInstance().download(i);
//                                    }
//                                }
//                            }
//                        }
//                    })
//                    .onNegative(new MaterialDialog.SingleButtonCallback() {
//                        @Override
//                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                            stopSpeak();
//                            dialog.dismiss();
//                        }
//                    })
//                    .autoDismiss(true)
//                    .show();
//        }
//        return downloadPosition != -1;
//    }

//    public static void navigatorEducationActivity(String type, String grade) {
//        if (isNeedDownload(true)) return;
//        Intent intent = new Intent();
//        ComponentName cn = new ComponentName("com.wyt.launcher.yzb", "com.wyt.launcher.yzb.TransitActivity");
//        intent.setComponent(cn);
//        if (!StringUtils.isEmpty(type)) {
//            String module = type;
//            if (StringUtils.equals("3", grade)) {
//                module = "中学" + type;
//            }
//            intent.putExtra("type", module);
//        }
//        getTopActivity().startActivity(intent);
//
//        Intent intentKill = new Intent("android.intent.action.CZ_STOP_APP");
//        intentKill.putExtra("package_name", "air.wyt.modloader");
//        ActivityUtils.getTopActivity().sendBroadcast(intentKill);
//
//        //aiui sleep
////        AiuiManager.getInstance().sleep();
//    }

//    public static void killEducationActivity() {
//        Intent intent = new Intent("android.intent.action.CZ_STOP_APP");
//        intent.putExtra("package_name", "com.wyt.launcher.yzb");
//        intent.putExtra("package_name", "com.qiyi.video.child");
//        ActivityUtils.getTopActivity().sendBroadcast(intent);
//    }
//
//    public static void navigatorOralEnglishTest() {
//        if (isNeedDownload(true)) return;
//        Intent intent = new Intent();
//        ComponentName componentName = new ComponentName("com.ocwvar.robotevaluatingfinal", "com.ocwvar.robotevaluatingfinal.AuthenticationActivity");
//        intent.setComponent(componentName);
//        getTopActivity().startActivity(intent);
//
//        //aiui sleep
//        stopSpeak();
////        AiuiManager.getInstance().sleep();
//    }
//
//    public static void navigatorToBaiduMap(String queryWord) {
//        if (isNeedDownload(false)) return;
//        Intent intent = new Intent();
//        intent.setData(Uri.parse("baidumap://map/place/search?query=" + queryWord + "&src=andr.chuangze.robot"));
//        getTopActivity().startActivity(intent);
//
//        //aiui sleep
//        AiuiManager.getInstance().sleep();
//    }

    /**
     * 通过包名运行其他应用
     *
     * @param packagename 包名
     */
//    public static void doStartApplicationWithPackageName(Context context, String packagename) {
//        if (StringUtils.equals(packagename, "com.wyt.hth.appstore") && isNeedDownload(true)) return;
//        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
//        PackageInfo packageinfo = null;
//        try {
//            packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        if (packageinfo == null) {
//            return;
//        }
//
//        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
//        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
//        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//        resolveIntent.setPackage(packageinfo.packageName);
//
//        // 通过getPackageManager()的queryIntentActivities方法遍历
//        List<ResolveInfo> resolveinfoList = context.getPackageManager()
//                .queryIntentActivities(resolveIntent, 0);
//
//        if (resolveinfoList.size() == 0) return;
//        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
//        if (resolveinfo != null) {
//            // packagename = 参数packname
//            String packageName = resolveinfo.activityInfo.packageName;
//            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
//            String className = resolveinfo.activityInfo.name;
//            // LAUNCHER Intent
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_LAUNCHER);
//
//            // 设置ComponentName参数1:packagename参数2:MainActivity路径
//            ComponentName cn = new ComponentName(packageName, className);
//
//            intent.setComponent(cn);
//            getTopActivity().startActivity(intent);
//        }
//    }

    /**
     * 播放url资源
     *
     * @param url url资源
     */
    public static void playUrl(final String url) {
        if (VocalSpeakManager.getInstance().isSpeaking()) {
            VocalSpeakManager.getInstance().stopSpeaking();
        }
        MediaPlayerManager.getInstance().playUrlFeedback(url)
                .subscribeOn(Schedulers.computation())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            VocalSpeakManager.getInstance().activateSpeak(ERROR);
                        }
                    }
                });
    }

//    public static void playUrl(final String url, MediaPlayerManager.OnCompleteListener onCompleteListener) {
//        if (vocalSpeakInterface.isSpeaking()) {
//            vocalSpeakInterface.stopSpeaking();
//        }
//        MediaPlayerManager.getInstance().playUrlFeedback(url, onCompleteListener)
//                .subscribeOn(Schedulers.computation())
//                .subscribe(new Consumer<Boolean>() {
//                    @Override
//                    public void accept(Boolean aBoolean) throws Exception {
//                        if (!aBoolean) {
//                            vocalSpeakInterface.activateSpeak(ERROR);
//                        }
//                    }
//                });
//    }

    /**
     * 停止播放url资源
     */
    public static void stopPlayUrl() {
//        MediaPlayerManager.getInstance().stopPlay();
//        MediaPlayerManager.getInstance().setOnCompleteListener(null);
    }

    /**
     * 禁止接受说话完成情况的回调
     */
    public static void disableCanCallback() {
        Output.bCanCallback = false;
    }


//    /**
//     * 切换表情
//     *
//     * @param expressionType 表情种类
//     */
//    public static void changeExpression(ExpressionType expressionType) {
//        expressionInterface.changeExpression(expressionType);
//    }
//
//    public static void changeExpression(ExpressionType expressionType, boolean force) {
//        if (!force) {
//            expressionInterface.changeExpression(expressionType);
//        } else {
//            expressionInterface.changeExpression(expressionType, force);
//        }
//    }

//    /**
//     * 强制机器人说话，不能打断
//     *
//     * @param text 机器人要说的内容
//     */
//    public static void forceSpeak(String text) {
//        if (MediaPlayerManager.getInstance().isPlaying()) {
//            MediaPlayerManager.getInstance().stopPlay();
//        }
//        vocalSpeakInterface.forceSpeak(text);
//    }
//
//    public static void forceSpeak(String text, boolean changeFace) {
//        if (MediaPlayerManager.getInstance().isPlaying()) {
//            MediaPlayerManager.getInstance().stopPlay();
//        }
//        vocalSpeakInterface.forceSpeak(text, changeFace);
//    }
}
