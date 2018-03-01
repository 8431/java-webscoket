package com.gjsg.gjchat.webscoket.service.impl;

import com.gjsg.gjchat.webscoket.config.WebSocketConfig;
import com.gjsg.gjchat.webscoket.dao.TokenGroupMapper;
import com.gjsg.gjchat.webscoket.entity.TokenGroup;
import com.gjsg.gjchat.webscoket.redisService.RedisDaoService;
import com.gjsg.gjchat.webscoket.service.SendService;
import com.gjsg.gjchat.webscoket.util.RedisKeyTitle;
import com.gjsg.gjchat.webscoket.vo.AppGroupVo;
import com.gjsg.gjchat.webscoket.vo.SendVo;
import com.google.gson.Gson;
import com.gz.medicine.common.exception.CommonException;
import com.gz.medicine.common.util.HttpRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by dlf on 2018/2/8 0008.
 * Email 1429264916@qq.com
 */
@Service
public class SendServiceImpl implements SendService {
    @Autowired
    RedisDaoService redisdaoservice;
    @Autowired
    TokenGroupMapper tokengroupmapper;
    private Logger logger = Logger.getLogger(SendServiceImpl.class);

    @Override
    public void send(SendVo sv) throws CommonException {
        try {
            //根据token判断session在本机还是在其他集群机器上
            String sendGuid = sv.getSendGuid();
            String reciveGuid = sv.getReciveGuid();
            String sendMsg = sv.getSendMsg();
            String token = sv.getToken();
            //校验token
            AppGroupVo av = checkToken(token);
            if (av == null) {
                throw new CommonException("token无效");
            }
            TokenGroup sendTk = tokengroupmapper.getNowTokenGroup(token);
            if (sendTk == null) {
                throw new CommonException("发送方离线中。。。");
            }
            //接收方token
            String reciveToken = (String) redisdaoservice.get(RedisKeyTitle.THIRDTITLE + reciveGuid);
            TokenGroup reciveTk = tokengroupmapper.getNowTokenGroup(reciveToken);
            if (reciveTk == null) {
                throw new CommonException("接收方离线中。。。");
            }

            String ip = reciveTk.getIp();
            String localIp = InetAddress.getLocalHost().getHostAddress();
            if (ip.indexOf(localIp) < 0) {
                //转发到其他机器
                Gson gn = new Gson();
                Map param = gn.fromJson(gn.toJson(sv), Map.class);
                logger.info("转发到：" + "http://" + ip + "/v1/send");
                String response = HttpRequest.sendPost("http://" + ip + "/v1/send", param);
                logger.info(response);
                Map reMp = gn.fromJson(response, Map.class);
                if (!"000".equals(reMp.get("code"))) {
                    throw new CommonException((String) reMp.get("message"));
                }
            } else {
                logger.info("本机发送");
                //本机
                if ("0".equals(sv.getSendStatus())) {
                    //发送给个人，离线是否需要发送，待定！！！
                    WebSocketSession sendSession = WebSocketConfig.WEBSCOKETSESSION.get(sendGuid);
                    if (sendSession == null) {
                        throw new CommonException("sendGuid无效");
                    }
                    WebSocketSession recivedSession = WebSocketConfig.WEBSCOKETSESSION.get(reciveGuid);
                    if (recivedSession != null) {
                        recivedSession.sendMessage(new TextMessage(sendMsg));
                    } else {
                        throw new CommonException(reciveGuid + "离线或者不存在！");
                    }

                } else {
                    //发送给群里所有人
                    String appGroup = av.getAppGroup();
                    Set set = redisdaoservice.smembers(RedisKeyTitle.ONLINE + appGroup);
                    Iterator<String> it = set.iterator();
                    while (it.hasNext()) {
                        String thirdGuid = it.next();
                        WebSocketSession thirdGuidSession = WebSocketConfig.WEBSCOKETSESSION.get(thirdGuid);
                        if (thirdGuidSession != null)
                            thirdGuidSession.sendMessage(new TextMessage(sendMsg));
                    }
                }

            }


        } catch (Exception e) {
            throw new CommonException("CHAT002", "getToken:" + e.getMessage());
        }

    }

    @Override
    public void close(String token) throws CommonException {
        try {
            byte[] by2 = (byte[]) redisdaoservice.get(token);
            if (by2 != null) {
                AppGroupVo av = (AppGroupVo) SerializationUtils.deserialize(by2);
                String thirdGuid = av.getThirdGuid();
                WebSocketSession session = WebSocketConfig.WEBSCOKETSESSION.get(thirdGuid);
                if (session != null) {
                    session.sendMessage(new TextMessage("{'code':'001','message':'已在另一台设备登录，强制下线！'}"));
                    session.close();
                } else {
                    throw new CommonException("请求关闭的session为空！");
                }
            } else {
                throw new CommonException("token无效");
            }
        } catch (Exception e) {
            logger.error("关闭失败：" + e.getMessage());
            throw new CommonException("关闭失败：" + e.getMessage());
        }
    }

    @Override
    public AppGroupVo checkToken(String token) throws CommonException {
        AppGroupVo av = null;
        try {
            byte[] by = (byte[]) redisdaoservice.get(token);
            av = (AppGroupVo) SerializationUtils.deserialize(by);
        } catch (Exception e) {
            //..
        }
        return av;
    }

    public static void main(String[] args) {
//        System.out.println("192.168.253.1:8080".c("192.168.253.1"));
    }
}
