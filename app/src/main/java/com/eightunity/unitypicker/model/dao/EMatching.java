package com.eightunity.unitypicker.model.dao;

import android.content.Intent;

import java.util.Date;

/**
 * Created by chokechaic on 9/6/2016.
 */
public class EMatching {

    private Integer id;
    private String user_id;
    private Integer seacrh_word_id;
    private String search_word_desc;
    private Integer content_id;
    private String title_content;
    private String web_name;
    private String url;
    private Date matching_date;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSeacrh_word_id() {
        return seacrh_word_id;
    }

    public void setSeacrh_word_id(Integer seacrh_word_id) {
        this.seacrh_word_id = seacrh_word_id;
    }

    public String getSearch_word_desc() {
        return search_word_desc;
    }

    public void setSearch_word_desc(String search_word_desc) {
        this.search_word_desc = search_word_desc;
    }

    public Integer getContent_id() {
        return content_id;
    }

    public void setContent_id(Integer content_id) {
        this.content_id = content_id;
    }

    public String getTitle_content() {
        return title_content;
    }

    public void setTitle_content(String title_content) {
        this.title_content = title_content;
    }

    public String getWeb_name() {
        return web_name;
    }

    public void setWeb_name(String web_name) {
        this.web_name = web_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getMatching_date() {
        return matching_date;
    }

    public void setMatching_date(Date matching_date) {
        this.matching_date = matching_date;
    }
}
