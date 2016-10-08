package com.oaec.wechat.app;

import android.app.Application;

import org.xutils.BuildConfig;
import org.xutils.x;

/**
 * Created by Kevin on 2016/10/4.
 * Description：
 */
public class WeChatApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
    }
}
