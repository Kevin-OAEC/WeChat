package com.oaec.wechat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import com.alibaba.fastjson.JSON;
import com.oaec.wechat.R;
import com.oaec.wechat.activity.ContentActivity;
import com.oaec.wechat.entity.DbWeChat;
import com.oaec.wechat.entity.WeChat;
import com.oaec.wechat.util.CommonAdapter;
import com.oaec.wechat.util.ViewHolder;
import com.oaec.wechat.widget.LoadListView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 2016/10/4.
 * Description：
 */
@ContentView(R.layout.fragment_wechat)
public class WeChatFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, LoadListView.OnLoadListener, AdapterView.OnItemClickListener {

    private static final String TAG = "WeChatFragment";
    private final String URL = "http://v.juhe.cn/weixin/query";
    private final String KEY = "bb8dd3952c72f1eca3e86e66fc5234e0";
    private List<WeChat> list;
    private CommonAdapter<WeChat> mAdapter;
    private int pno = 1;

    @ViewInject(R.id.refreshlayout)
    private SwipeRefreshLayout refreshLayout;

    @ViewInject(R.id.lv_wechat)
    private LoadListView listView;

    private DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            .setDbName("wechat.db")
            .setDbVersion(1)
            .setDbOpenListener(new DbManager.DbOpenListener() {
                @Override
                public void onDbOpened(DbManager db) {
                    db.getDatabase().enableWriteAheadLogging();
                }
            });
    private DbManager db;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshLayout.setOnRefreshListener(this);
        list = new ArrayList<>();
        listView.setOnLoadListener(this);
        getWeChat(pno);
        db = x.getDb(daoConfig);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pno = 1;
                getWeChat(pno);
                refreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

    private void getWeChat(int pno) {
        RequestParams params = new RequestParams(URL);
        params.addBodyParameter("key", KEY);
        params.addBodyParameter("pno", pno + "");
        params.addBodyParameter("ps", "10");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, "onSuccess() called with: " + "result = [" + result + "]");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String reason = jsonObject.getString("reason");
                    if (reason.equals("success")) {
                        jsonObject = jsonObject.getJSONObject("result");
                        String jsonArray = jsonObject.getString("list");
                        List<WeChat> weChats = JSON.parseArray(jsonArray, WeChat.class);
                        if (refreshLayout.isRefreshing()) {
                            list = weChats;
                        } else {
                            list.addAll(weChats);
                        }
                        if (mAdapter == null) {
                            mAdapter = new CommonAdapter<WeChat>(x.app(), list, R.layout.wechat_item) {
                                @Override
                                public void convert(ViewHolder holder, final WeChat weChat) {
                                    boolean checked = false;
                                    try {
                                        DbWeChat first = db.selector(DbWeChat.class).where("title", "=", weChat.getTitle()).findFirst();
                                        checked = first == null ? false : true;
                                    } catch (DbException e) {
                                        e.printStackTrace();
                                    }
                                    holder.setText(R.id.tv_title, weChat.getTitle())
                                            .setText(R.id.tv_source, weChat.getSource())
                                            .setImageUrl(R.id.iv_firstImg, weChat.getFirstImg())
                                            .setChecked(R.id.cb_favorites,checked)
                                            .setOnCheckedChangeListener(R.id.cb_favorites, new CompoundButton.OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                    Log.d(TAG, "onCheckedChanged() called with: " + "buttonView = [" + buttonView + "], isChecked = [" + isChecked + "], WeChat = [" + weChat + "]");
                                                    DbWeChat dbWeChat = new DbWeChat(weChat);
                                                    try {
                                                        if (isChecked){
                                                            //保存
                                                            db.save(dbWeChat);
                                                            Log.d(TAG, "onCheckedChanged: 保存成功");
                                                        }else{
                                                            //删除
                                                            db.delete(DbWeChat.class,WhereBuilder.b("title","==",dbWeChat.getTitle()));
                                                            Log.d(TAG, "onCheckedChanged: 删除成功");
                                                        }
                                                    } catch (DbException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                }
                            };
                            listView.setAdapter(mAdapter);
                        } else {
                            mAdapter.onDataSetChanged(list);
                        }
                        refreshLayout.setRefreshing(false);
                        listView.loadComplete();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d(TAG, "onError() called with: " + "ex = [" + ex + "], isOnCallback = [" + isOnCallback + "]");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.d(TAG, "onCancelled() called with: " + "cex = [" + cex + "]");
            }

            @Override
            public void onFinished() {
                Log.d(TAG, "onFinished() called with: " + "");

            }
        });
    }

    @Override
    public void onLoad() {
        pno += 1;
        getWeChat(pno);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem) {
        if (view.getChildAt(0) == null) {
            return;
        }
        if (view.getChildAt(0).getY() == 0 && firstVisibleItem == 0) {
            refreshLayout.setEnabled(true);
        } else {
            refreshLayout.setEnabled(false);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemClick() called with: " + "parent = [" + parent + "], view = [" + view + "], position = [" + position + "], id = [" + id + "]");
        WeChat weChat = list.get(position);
        Intent intent = new Intent(x.app(), ContentActivity.class);
        intent.putExtra("title",weChat.getTitle());
        intent.putExtra("url",weChat.getUrl());
        startActivity(intent);
    }
}
