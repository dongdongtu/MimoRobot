package com.chance.mimorobot.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.IBinder;
import android.os.RemoteException;

import com.chance.mimorobot.ISocketService;
import com.chance.mimorobot.utils.FileUtils;
import com.google.gson.Gson;
import com.slamtec.slamware.robot.Map;


import androidx.annotation.Nullable;

import static com.chance.mimorobot.constant.Constant.ACTION_PNG_CODE;


public class PngIntentService extends Service {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *
     *
     */
    private ISocketService.Stub iBackService = new ISocketService.Stub() {
        @Override
        public boolean sendMessage(String message, String name) throws RemoteException {
//            sendMsg(message,name);
            return sendMsg(message,name);
        }
    };



   boolean sendMsg(String name, String pngName){
       new Thread(new Runnable() {
           @Override
           public void run() {
               Map map =new Gson().fromJson(name, Map.class);
               Bitmap mapbitmap = Bitmap.createBitmap(map.getDimension().getWidth(), map.getDimension().getHeight(), Bitmap.Config.ARGB_8888);
               for (int posY = 0; posY < map.getDimension().getHeight(); ++posY) //获取mapdata数据，并转化成bmp
               {
                   for (int posX = 0; posX < map.getDimension().getWidth(); ++posX) {
                       int cellValue = 128 + map.getData()[posX + (map.getDimension().getHeight() - posY - 1) * map.getDimension().getWidth()];
                       mapbitmap.setPixel(posX, posY, Color.argb(255, cellValue, cellValue, cellValue));
                   }
               }
               FileUtils.copyBitmap2Path(mapbitmap, pngName);
               Intent intentresult = new Intent(ACTION_PNG_CODE);
               sendBroadcast(intentresult);
           }
       }).start();
       return true;
   }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBackService;
    }
}
