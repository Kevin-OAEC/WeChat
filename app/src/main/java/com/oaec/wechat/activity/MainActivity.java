package com.oaec.wechat.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.oaec.wechat.R;
import com.oaec.wechat.adapter.FragmentTabAdapter;
import com.oaec.wechat.fragment.FavoritesFragment;
import com.oaec.wechat.fragment.WeChatFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";
    private RadioButton rb_wechat,rb_favorites;
    private RadioGroup rg_tab;
    private List<Fragment> list ;
    private FragmentTabAdapter adapter;
    private WeChatFragment tab1;
    private FavoritesFragment tab2;
    private TextView tv_title;
    private List<String> tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_title = (TextView) findViewById(R.id.tv_title);
        list = new ArrayList<Fragment>();
        tags = new ArrayList<>();
        tags.add("tab1");
        tags.add("tab2");
        tab1 = new WeChatFragment();
        tab2 = new FavoritesFragment();
        list.add(tab1);
        list.add(tab2);
        rb_wechat = (RadioButton) findViewById(R.id.rb_wechat);
        rb_favorites = (RadioButton) findViewById(R.id.rb_favorites);
        rg_tab = (RadioGroup) findViewById(R.id.rg_tab);
        rg_tab.check(R.id.rb_wechat);
        adapter = new FragmentTabAdapter(this,list,tags,R.id.frame,rg_tab);
        adapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener(){
            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
                switch (index){
                    case 0:
                        tv_title.setText("微信精选");
                        break;
                    case 1:
                        tv_title.setText("我的收藏");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                FragmentManager manager = getSupportFragmentManager();
                                FavoritesFragment tab2 = (FavoritesFragment) manager.findFragmentByTag("tab2");
                                Log.d(TAG, "OnRgsExtraCheckedChanged: "+tab1+"-"+tab2);
                        tab2.getFavorites();
                            }
                        },100);
                        break;
                }
            }
        });
    }
    @Override
    // 设置回退
    // 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if(adapter.getCurrentTab()!=0){
                rb_wechat.setChecked(true);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
