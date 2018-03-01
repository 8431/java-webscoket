package com.gjsg.gjchat.webscoket.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gjsg.gjchat.webscoket.entity.TokenGroup;
import com.gz.medicine.common.exception.CommonException;
import org.apache.ibatis.annotations.Select;

import javax.annotation.Resource;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author dlf
 * @since 2018-02-06
 */
public interface TokenGroupMapper extends BaseMapper<TokenGroup> {
    /**
     * 根据token查询最近的连上的ip地址
     * @param token
     * @return
     * @throws CommonException
     */
    @Select("select  * from token_group t where t.token=#{0} order by creat_data desc limit 0,1")
    TokenGroup getNowTokenGroup(String token)throws CommonException;

}