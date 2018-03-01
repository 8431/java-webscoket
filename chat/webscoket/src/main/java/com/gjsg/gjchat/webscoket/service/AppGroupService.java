package com.gjsg.gjchat.webscoket.service;

import com.gjsg.gjchat.webscoket.entity.AppGroup;
import com.baomidou.mybatisplus.service.IService;
import com.gjsg.gjchat.webscoket.vo.AppGroupVo;
import com.gz.medicine.common.exception.CommonException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dlf
 * @since 2018-02-08
 */
public interface AppGroupService extends IService<AppGroup> {
    /**
     * 获取token
     * @param ap
     * @return
     * @throws CommonException
     */
    String getToken(AppGroupVo ap)throws CommonException;

    /**
     * 获取token
     * @param ap
     * @param time
     * @return
     * @throws CommonException
     */
    String refshToken(AppGroupVo ap,Long time)throws CommonException;
	
}
