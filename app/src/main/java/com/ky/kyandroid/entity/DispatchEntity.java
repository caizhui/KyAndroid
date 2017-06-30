package com.ky.kyandroid.entity;

import java.io.Serializable;

/**
 * Created by Caizhui on 2017/6/30.
 */

public class DispatchEntity implements Serializable{

    private String userId;

    private String sjId;

    private OrgsEntity orgs;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSjId() {
        return sjId;
    }

    public void setSjId(String sjId) {
        this.sjId = sjId;
    }

    public OrgsEntity getOrgs() {
        return orgs;
    }

    public void setOrgs(OrgsEntity orgs) {
        this.orgs = orgs;
    }
}
