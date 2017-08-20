package com.ky.kyandroid.entity;

import java.util.List;

/**
 * Created by Caizhui on 2017/8/20.
 */

public class OrgAndUserEntity {

    private List<AclOrgEntity> orgList;

    private List<UserEntity> userList;

    public List<AclOrgEntity> getOrgList() {
        return orgList;
    }

    public void setOrgList(List<AclOrgEntity> orgList) {
        this.orgList = orgList;
    }

    public List<UserEntity> getUserList() {
        return userList;
    }

    public void setUserList(List<UserEntity> userList) {
        this.userList = userList;
    }
}
