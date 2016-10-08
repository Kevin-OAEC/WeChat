package com.oaec.wechat.entity;

/**
 * Created by Kevin on 2016/10/4.
 * Description：
 */
public class WeChat {
    private String firstImg;
    private String source;
    private String id;
    private String title;
    private String url;
    private String mark;

    public WeChat() {
    }

    public WeChat(String firstImg, String id, String source, String title, String url, String mark) {
        this.firstImg = firstImg;
        this.id = id;
        this.source = source;
        this.title = title;
        this.url = url;
        this.mark = mark;
    }

    public String getFirstImg() {
        return firstImg;
    }

    public void setFirstImg(String firstImg) {
        this.firstImg = firstImg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
