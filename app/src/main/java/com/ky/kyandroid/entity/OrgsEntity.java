package com.ky.kyandroid.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Caizhui on 2017/6/30.
 * 街道派遣实体
 */

public class OrgsEntity implements Serializable{

    private List<KpqbmEntity> kpqbmList;

    private List<YpqbmEntity> ypqbmList;

    public List<KpqbmEntity> getKpqbmList() {
        return kpqbmList;
    }

    public void setKpqbmList(List<KpqbmEntity> kpqbmList) {
        this.kpqbmList = kpqbmList;
    }

    public List<YpqbmEntity> getYpqbmList() {
        return ypqbmList;
    }

    public void setYpqbmList(List<YpqbmEntity> ypqbmList) {
        this.ypqbmList = ypqbmList;
    }
}
