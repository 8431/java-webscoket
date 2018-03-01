package com.gjsg.gjchat.webscoket.service.impl;

import com.gjsg.gjchat.webscoket.entity.AppGroup;
import com.gjsg.gjchat.webscoket.dao.AppGroupMapper;
import com.gjsg.gjchat.webscoket.redisService.RedisDaoService;
import com.gjsg.gjchat.webscoket.service.AppGroupService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gjsg.gjchat.webscoket.service.MsgGroupService;
import com.gjsg.gjchat.webscoket.util.RedisKeyTitle;
import com.gjsg.gjchat.webscoket.vo.AppGroupVo;
import com.gz.medicine.common.exception.CommonException;
import com.gz.medicine.common.util.UUIDTool;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dlf
 * @since 2018-02-08
 */
@Service
public class AppGroupServiceImpl extends ServiceImpl<AppGroupMapper, AppGroup> implements AppGroupService {
    @Autowired
    AppGroupMapper appgroupmapper;
    @Autowired
    MsgGroupService msggroupservice;
    @Autowired
    RedisDaoService redisdaoservice;
    private Logger logger = Logger.getLogger(AppGroupServiceImpl.class);

    @Override
    public String getToken(AppGroupVo ap) throws CommonException {
        String token = "";
        try {
            AppGroup ag = new AppGroup();
            BeanUtils.copyProperties(ap, ag);
            AppGroup agp = appgroupmapper.selectOne(ag);
            //校验是否有该群（待定）
            if (agp != null) {
                logger.info("默认token有效期2小时");
                //默认token有效期2小时
                token=refshToken(ap, 60 * 120 * 1000l);
            }else{
                throw new CommonException("无效的appSecret");
            }

        } catch (Exception e) {
            throw new CommonException("CHAT002", "getToken:" + e.getMessage());
        }
        return token;
    }

    @Override
    public String refshToken(AppGroupVo ap, Long time) throws CommonException {
        String token = "";
        //刷新token
        try {
            String redisToken = (String) redisdaoservice.get(RedisKeyTitle.THIRDTITLE + ap.getThirdGuid());
            token = UUIDTool.getUUID();
            if (StringUtils.isEmpty(redisToken)) {
                //存AppGroupVo
                byte[] by1 = SerializationUtils.serialize(ap);
                redisdaoservice.set(token, by1, time);
            } else {
                byte[] by2 = (byte[]) redisdaoservice.get(redisToken);
                redisdaoservice.set(token, by2, time);
            }
            if(redisToken!=null){
                //删除老的token，需要移除老的webscoketSession
                redisdaoservice.set(redisToken, "", 100L);
            }
            //设置第三方id----token
            redisdaoservice.set(RedisKeyTitle.THIRDTITLE + ap.getThirdGuid(), token, time);
        } catch (Exception e) {
            throw new CommonException("CHAT001", "refshToken:" + e.getMessage());
        }
        return token;
    }

}
