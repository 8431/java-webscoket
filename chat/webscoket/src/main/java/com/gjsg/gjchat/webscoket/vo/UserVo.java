package com.gjsg.gjchat.webscoket.vo;

import java.io.Serializable;

/**
 * Created by dlf on 2018/2/5 0005.
 * Email 1429264916@qq.com
 */
public class UserVo implements Serializable {
    //token
    private String token;

    private String guid;

    private String ip;
    // 项目区分字段
    private String appid;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
}
