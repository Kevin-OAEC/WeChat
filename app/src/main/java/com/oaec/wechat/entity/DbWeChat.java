package com.oaec.wechat.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Kevin on 2016/10/4.
 * Description：
 */
@Table(name = "wechat")
public class DbWeChat {

    @Column(name = "id",isId = true)
    private int id;
    @Column(name = "firstImg")
    private String firstImg;
    @Column(name = "source")
    private String source;
    @Column(name = "title")
    private String title;
    @Column(name = "url")
    private String url;
    @Column(name = "mark")
    private String mark;

    public DbWeChat() {
    }

    public DbWeChat(String firstImg, String source, String title, String url, String mark) {
        this.firstImg = firstImg;
        this.source = source;
        this.title = title;
        this.url = url;
        this.mark = mark;
    }

    public DbWeChat(WeChat weChat){
        this.firstImg = weChat.getFirstImg();
        this.source = weChat.getSource();
        this.title = weChat.getTitle();
        this.url = weChat.getUrl();
        this.mark = weChat.getMark();
    }

    public String getFirstImg() {
        return firstImg;
    }

    public void setFirstImg(String firstImg) {
        this.firstImg = firstImg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    @Override
    public String toString() {
        return "WeChat{" +
                "firstImg='" + firstImg + '\'' +
                ", id='" + id + '\'' +
                ", source='" + source + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", mark='" + mark + '\'' +
                '}';
    }
}
