package com.eightunity.unitypicker.model.dao;

import java.util.Date;

/**
 * Created by chokechaic on 9/6/2016.
 */
public class ESearchWord {

    private Integer search_id;
    private String user_id;
    private String description;
    private Integer search_type;
    private Date modified_date;

    public Integer getSearch_id() {
        return search_id;
    }

    public void setSearch_id(Integer search_id) {
        this.search_id = search_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSearch_type() {
        return search_type;
    }

    public void setSearch_type(Integer search_type) {
        this.search_type = search_type;
    }

    public Date getModified_date() {
        return modified_date;
    }

    public void setModified_date(Date modified_date) {
        this.modified_date = modified_date;
    }
}
