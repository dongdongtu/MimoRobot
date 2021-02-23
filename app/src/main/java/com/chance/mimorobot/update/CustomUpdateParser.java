package com.chance.mimorobot.update;


import android.util.Log;

import com.chance.mimorobot.model.AndroidUpdate;
import com.google.gson.Gson;
import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.proxy.IUpdateParser;

import static com.chance.mimorobot.retrofit.ApiManager.ROBOT_SERVER_URL;

/**
 * 自定义更新信息解析器
 *
 * @Author
 * @Date
 * @Desc
 * @Url
 */
public class CustomUpdateParser implements IUpdateParser {

    /**
     * 当前的版本code
     */
    private int currentVersionCode = 0;

    public CustomUpdateParser(int currentVersionCode) {
        this.currentVersionCode = currentVersionCode;
    }

    @Override
    public UpdateEntity parseJson(String json) throws Exception {

        AndroidUpdate result = new Gson().fromJson(json, AndroidUpdate.class);
        if (result != null) {
            boolean hasUpdate = false;
            if (result.getCode() == 200 && result.getData().getVersionCode() > currentVersionCode) {
                hasUpdate = true;
            }
//            Log.e("URL","URL =  "+result.getData().getApkUrl());
            return new UpdateEntity()
                    .setHasUpdate(hasUpdate)
                    .setIsIgnorable(!result.getData().isIsImposed())
                    .setVersionCode(result.getData().getVersionCode())
                    .setVersionName(result.getData().getVersionName())
                    .setUpdateContent(result.getData().getDes())
                    .setIsAutoInstall(true)
                    .setDownloadUrl(result.getData().getApkUrl());
        }
        return null;
    }
}
