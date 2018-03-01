package com.gjsg.gjchat.webscoket.controller;

import com.gjsg.gjchat.webscoket.config.WebSocketConfig;
import com.gjsg.gjchat.webscoket.entity.AppGroup;
import com.gjsg.gjchat.webscoket.redisService.RedisDaoService;
import com.gjsg.gjchat.webscoket.service.AppGroupService;
import com.gjsg.gjchat.webscoket.service.SendService;
import com.gjsg.gjchat.webscoket.util.RedisKeyTitle;
import com.gjsg.gjchat.webscoket.vo.AppGroupVo;
import com.gjsg.gjchat.webscoket.vo.SendVo;
import com.gz.medicine.common.exception.CommonException;
import com.gz.medicine.common.util.SimpleCode;
import com.gz.medicine.common.util.SimpleResult;
import com.gz.medicine.common.util.UUIDTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;
import java.io.IOException;
import java.util.*;

import static com.gz.medicine.common.util.ValidateWithException.validates;

/**
 * Created by dlf on 2018/2/5 0005.
 * Email 1429264916@qq.com
 */
@RestController
@RequestMapping("/v1")
public class ChatController extends AbstractController {
    @Autowired
    AppGroupService appgroupservice;
    @Autowired
    SendService sendservice;
    @Autowired
    Validator validator;
    @Autowired
    RedisDaoService redisdaoservice;

    @RequestMapping("/send")
    public SimpleResult send(SendVo send) {
        SimpleResult sr = null;
        if (validates(validator, send) != null) {
            return SimpleResult.error(SimpleCode.ERROR.getCode(), validates(validator, send));
        }
        try {
            sendservice.send(send);
        } catch (CommonException e) {
            return SimpleResult.error(e.getCode(), e.getDesc());
        }
        sr = SimpleResult.success();
        return sr;
    }
    @RequestMapping("/close")
    public SimpleResult close(@RequestParam(value = "token", required = true) String token) {
        SimpleResult sr = null;
        try {
            sendservice.close(token);
        } catch (CommonException e) {
            return SimpleResult.error("001", e.getMessage());
        }
        sr = SimpleResult.success();
        return sr;
    }

    //获取token
    @GetMapping("/getToken")
    public SimpleResult getToekn(AppGroupVo ap) {
        SimpleResult sr = null;
        String token = "";
        if (validates(validator, ap) != null) {
            return SimpleResult.error(SimpleCode.ERROR.getCode(), validates(validator, ap));
        }
        try {
            token = appgroupservice.getToken(ap);
        } catch (CommonException e) {
            return SimpleResult.error(e.getCode(), e.getDesc());
        }
        sr = SimpleResult.success();
        sr.put("data", token);
        return sr;
    }

    //获取在线用户
    @GetMapping("/getOnline")
    public SimpleResult getOnline(@RequestParam(value = "groupToken", required = true) String groupToken) {
        SimpleResult sr = null;
        Set s2 = null;
        try {
            s2 = redisdaoservice.smembers(RedisKeyTitle.ONLINE + groupToken);
        } catch (Exception e) {
            return SimpleResult.error("001", e.getMessage());
        }
        sr = SimpleResult.success();
        sr.put("data", s2);
        return sr;
    }
    //获取离线用户
    @GetMapping("/getOffline")
    public SimpleResult getOffline(@RequestParam(value = "groupToken", required = true) String groupToken) {
        SimpleResult sr = null;
        Set s2 = null;
        try {
            s2 = redisdaoservice.smembers(RedisKeyTitle.OFFLINE + groupToken);
        } catch (Exception e) {
            return SimpleResult.error("001", e.getMessage());
        }
        sr = SimpleResult.success();
        sr.put("data", s2);
        return sr;
    }


    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        return null;
    }
}
