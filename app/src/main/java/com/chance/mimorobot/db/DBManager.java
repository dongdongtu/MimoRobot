package com.chance.mimorobot.db;

import android.database.sqlite.SQLiteDatabase;

import com.chance.mimorobot.MyApplication;
import com.chance.mimorobot.db.dao.DaoMaster;
import com.chance.mimorobot.db.dao.DaoSession;


/**
 * Created by Administrator on 2016/11/30 0030.
 */

public class DBManager {

    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private static DBManager greenDaoUtils;

    private DBManager() {
    }

    public static DBManager getInstance() {
        if (greenDaoUtils == null) {
            greenDaoUtils = new DBManager();
        }
        return greenDaoUtils;
    }

    private void initGreenDao() {
        mHelper = new DaoMaster.DevOpenHelper(MyApplication.getInstance(), "mimorobot", null);
        db = mHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getmDaoSession() {
        if (mDaoMaster == null) {
            initGreenDao();
        }
        return mDaoSession;
    }
}
