package com.oaec.wechat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.AbsListView;
import android.widget.Toast;

import com.oaec.wechat.R;
import com.oaec.wechat.entity.DbWeChat;
import com.oaec.wechat.util.CommonAdapter;
import com.oaec.wechat.util.ViewHolder;
import com.oaec.wechat.widget.LoadListView;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 2016/10/4.
 * Description：
 */
@ContentView(R.layout.fragment_favorites)
public class FavoritesFragment extends BaseFragment implements LoadListView.OnLoadListener {

    @ViewInject(R.id.refreshlayout)
    private SwipeRefreshLayout refreshLayout;

    @ViewInject(R.id.lv_favorites)
    private LoadListView listView;

    private List<DbWeChat> list;

    private CommonAdapter<DbWeChat> mAdapter;

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
        listView.setOnLoadListener(this);
        list = new ArrayList<>();
        db = x.getDb(daoConfig);
        getFavorites();
    }

    @Override
    public void onLoad() {

    }

    public void getFavorites(){
        try {
            list = db.selector(DbWeChat.class).findAll();
            if(list == null){
                Toast.makeText(x.app(), "没有收藏的内容", Toast.LENGTH_SHORT).show();
                return;
            }
            if(mAdapter == null){
                mAdapter = new CommonAdapter<DbWeChat>(x.app(),list,R.layout.favorite_item) {
                    @Override
                    public void convert(ViewHolder holder, DbWeChat dbWeChat) {
                        holder.setText(R.id.tv_title,dbWeChat.getTitle())
                                .setText(R.id.tv_source,dbWeChat.getSource())
                                .setImageUrl(R.id.iv_firstImg,dbWeChat.getFirstImg());
                    }
                };
                listView.setAdapter(mAdapter);
            }else{
                mAdapter.onDataSetChanged(list);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem) {
        if (view.getChildAt(0) == null){
            return;
        }
        if(view.getChildAt(0).getY() == 0 && firstVisibleItem == 0){
            refreshLayout.setEnabled(true);
        }else{
            refreshLayout.setEnabled(false);
        }
    }
}
