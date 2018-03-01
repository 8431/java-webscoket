package com.gjsg.gjchat.webscoket.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gjsg.gjchat.webscoket.dao.TokenGroupMapper;
import com.gjsg.gjchat.webscoket.entity.TokenGroup;
import com.gjsg.gjchat.webscoket.service.TokenGroupService;
import com.gz.medicine.common.exception.CommonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dlf
 * @since 2018-02-06
 */
@Service
public class TokenGroupServiceImpl extends ServiceImpl<TokenGroupMapper, TokenGroup> implements TokenGroupService {
    @Autowired
    TokenGroupMapper tokengroupmapper;
    @Override
    public TokenGroup getNowTokenGroup(String token) throws CommonException {
        return tokengroupmapper.getNowTokenGroup(token);
    }
}
