package com.gjsg.gjchat.webscoket.service;

import com.baomidou.mybatisplus.service.IService;
import com.gjsg.gjchat.webscoket.entity.TokenGroup;
import com.gjsg.gjchat.webscoket.vo.AppGroupVo;
import com.gjsg.gjchat.webscoket.vo.SendVo;
import com.gz.medicine.common.exception.CommonException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dlf
 * @since 2018-02-06
 */
public interface SendService  {
    /**
     * 发送信息
     * @param sv
     * @throws CommonException
     */
    void send(SendVo sv)throws CommonException;

    /**
     * 关闭WebscoketSession
     * @param token
     * @throws CommonException
     */
    void close(String token)throws CommonException;

    /**
     * 校验token
     * @param token
     * @return
     * @throws CommonException
     */
    AppGroupVo checkToken(String token)throws CommonException;

}
