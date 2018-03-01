package com.gjsg.gjchat.webscoket;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gjsg.gjchat.webscoket.config.WebSocketConfig;
import com.gjsg.gjchat.webscoket.entity.MsgGroup;
import com.gjsg.gjchat.webscoket.entity.TokenGroup;
import com.gjsg.gjchat.webscoket.redisService.RedisDaoService;
import com.gjsg.gjchat.webscoket.service.MsgGroupService;
import com.gjsg.gjchat.webscoket.service.TokenGroupService;
import com.gjsg.gjchat.webscoket.util.RedisKeyTitle;
import com.gjsg.gjchat.webscoket.vo.AppGroupVo;
import com.gz.medicine.common.exception.CommonException;
import com.gz.medicine.common.util.HttpRequest;
import com.gz.medicine.common.util.UUIDTool;
import org.apache.log4j.Logger;
import org.kurento.client.KurentoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.InetAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by dlf on 2018/2/5 0005.
 * Email 1429264916@qq.com
 */
@Service("chatwebscoket")
public class ChatWebScoket extends TextWebSocketHandler {
    @Autowired
    TokenGroupService tokengroupservice;
    @Autowired
    RedisDaoService redisdaoservice;
    @Autowired
    MsgGroupService msggroupservice;
    private Logger logger = Logger.getLogger(ChatWebScoket.class);

    //    @Autowired
//    KurentoClient kurentoclient;
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        try {
            URI head = session.getUri();
            String token = head.getQuery();
            //校验toke合法性，合法的加入,改对象存到数据库中维护
            String address = session.getLocalAddress().toString();
            logger.info(address);
            address = address.substring(address.indexOf("/") + 1);
            //token应被设计成可以退出appid
            //u.setAppid();
            //以群为单位

            //////////////////////////////////////存取webscoketSession/////////////////////////////////////////
            byte[] by = (byte[]) redisdaoservice.get(token);
            if (by == null) {
                throw new CommonException("token无效");
            } else {
                AppGroupVo av = (AppGroupVo) SerializationUtils.deserialize(by);
                //小组
                String appGroup = av.getAppGroup();
                String thirdGuid = av.getThirdGuid();
                String appGuid = av.getAppGuid();
                //校验群-
                MsgGroup mgp = new MsgGroup();
                mgp.setAppGuid(appGuid);
                mgp.setGuid(appGroup);
                EntityWrapper<MsgGroup> ew = new EntityWrapper<MsgGroup>();
                ew.setEntity(mgp);
                MsgGroup mgp2 = msggroupservice.selectOne(ew);
                if (mgp2 == null) {
                    //该群不存在
                    throw new CommonException("该群不存在");
                } else {
                    /**需要检验是否已在其他集群机器建立连接，如果建立连接则通知另一台机器关闭连接，新建该链接
                     * 并通知之前的连接，在另一台设备登录，强制下线
                     * 多开的前提下应该是不同平台，pc ios  等
                     **/
                    //加入redis在线集合-------判断是否在离线中，在的话从离线转移到在线
                    //不在直接加入在线
                    boolean re = redisdaoservice.sismember(RedisKeyTitle.OFFLINE + appGroup, thirdGuid);
                    if (re) {
                        //1.判断redis在线集合中是否有该用户，有的话判断ip是否与本机相同
                        //不同就通过ip发起关闭请求，并在本机新建连接
                        TokenGroup sendTk = tokengroupservice.getNowTokenGroup(token);
                        //判断ip是否与本机相同
                        if (sendTk != null && !address.equals(sendTk.getIp())) {
                            Map<String, String> param = new HashMap<>();
                            param.put("token", token);
                            logger.info("转发到：" + "http://" + sendTk.getIp() + "/v1/close");
                            String response = HttpRequest.sendPost("http://" + sendTk.getIp() + "/v1/close", param);
                            logger.info(response);
                        } else {
                            //////////////////////////////////////////////判断是否在本机建立连接在的话先关闭在开启//////////////////////////////////////////////////////////////////
                            WebSocketSession sessionLast = WebSocketConfig.WEBSCOKETSESSION.get(thirdGuid);
                            if (sessionLast != null) {
                                sessionLast.sendMessage(new TextMessage("{'code':'001','message':'已在另一台设备登录，强制下线！'}"));
                                sessionLast.close();
                            }
                        }
                        redisdaoservice.smove(RedisKeyTitle.OFFLINE + appGroup, RedisKeyTitle.ONLINE + appGroup, thirdGuid);
                    } else {
                        redisdaoservice.sadd(RedisKeyTitle.ONLINE + appGroup, thirdGuid);
                    }
                    //群组必须存在redis里面，session存在服务器中
                    WebSocketConfig.WEBSCOKETSESSION.put(thirdGuid, session);
                    //sessionId-thirdGuid
                    WebSocketConfig.SESSIONID.put(session.getId(), av);
                }
                //通过token获取群的Map,然后key(用户guid),value(session)
                TokenGroup tg = new TokenGroup();
                tg.setToken(token);
                tg.setGuid(UUIDTool.getUUID());
                tg.setIp(address);
                tg.setAppId(av.getAppGuid());
                tokengroupservice.insert(tg);


            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            session.close();
            //...
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        AppGroupVo av = WebSocketConfig.SESSIONID.get(session.getId());
        if (av != null) {
            WebSocketConfig.WEBSCOKETSESSION.remove(av.getThirdGuid());
            //加入redis离线集合-------判断是否在在线中，在的话从在线转移到离线
            boolean re = redisdaoservice.sismember(RedisKeyTitle.ONLINE + av.getAppGroup(), av.getThirdGuid());
            if (re) {
                redisdaoservice.smove(RedisKeyTitle.ONLINE + av.getAppGroup(), av.getThirdGuid(), RedisKeyTitle.OFFLINE + av.getAppGroup());
            } else {
                redisdaoservice.sadd(RedisKeyTitle.OFFLINE + av.getAppGroup(), av.getThirdGuid());
            }
            WebSocketConfig.SESSIONID.remove(session.getId());

////////////////////////////////////////////test////////////////////////////////////////////
            Set s2 = redisdaoservice.smembers(RedisKeyTitle.ONLINE + av.getAppGroup());
            Iterator it2 = s2.iterator();
            logger.info("在线用户");
            while (it2.hasNext()) {
                logger.info(it2.next());
            }
            Set s = redisdaoservice.smembers(RedisKeyTitle.OFFLINE + av.getAppGroup());
            Iterator it = s.iterator();
            logger.info("离线用户");
            while (it.hasNext()) {
                logger.info(it.next());
            }
        }
    }
}
