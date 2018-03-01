package com.gjsg.gjchat.webscoket.service;

import com.baomidou.mybatisplus.service.IService;
import com.gjsg.gjchat.webscoket.entity.TokenGroup;
import com.gz.medicine.common.exception.CommonException;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dlf
 * @since 2018-02-06
 */

public interface TokenGroupService extends IService<TokenGroup> {
    TokenGroup getNowTokenGroup(String token)throws CommonException;


}
