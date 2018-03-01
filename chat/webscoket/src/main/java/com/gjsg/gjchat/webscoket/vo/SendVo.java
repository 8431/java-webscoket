package com.gjsg.gjchat.webscoket.vo;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by dlf on 2018/2/5 0005.
 * Email 1429264916@qq.com
 */
public class SendVo {
    @NotEmpty(message = "token不能为空")
    private String token;
    @NotEmpty(message = "发送方guid不能为空")
    private String sendGuid;
    @NotEmpty(message = "接收方guid不能为空")
    private String reciveGuid;
    @NotEmpty(message = "发送类容不能为空")
    private String sendMsg;
    @NotEmpty(message = "发送类型不能为空")
    private String sendType;
   //0，发送给个人 1发送给群里所有人
    @NotEmpty(message = "发送方式不能为空")
    private String sendStatus;
    @Override
    public String toString() {


        return "{'token':"+token+",'sendGuid':"+sendGuid+",'reciveGuid':"+reciveGuid+",'sendMsg':"+sendMsg+",'sendType':"+sendType+"}";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSendGuid() {
        return sendGuid;
    }

    public void setSendGuid(String sendGuid) {
        this.sendGuid = sendGuid;
    }

    public String getReciveGuid() {
        return reciveGuid;
    }

    public void setReciveGuid(String reciveGuid) {
        this.reciveGuid = reciveGuid;
    }

    public String getSendMsg() {
        return sendMsg;
    }

    public void setSendMsg(String sendMsg) {
        this.sendMsg = sendMsg;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }
}
