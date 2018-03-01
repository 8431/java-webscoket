package com.gjsg.gjchat.webscoket.redisService.impl;

import com.gjsg.gjchat.webscoket.redisService.RedisDaoService;
import com.gz.medicine.common.exception.CommonException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/5/11 0011.
 *
 * 需要整改，异常部分--后续加上
 */
@Service
public class RedisDaoServiceImpl implements RedisDaoService {
    private Logger logger = Logger.getLogger(RedisDaoServiceImpl.class);
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public Object set(final String key,final  Object value, final Long time) throws Exception{

            SessionCallback<Object> sc=new SessionCallback<Object>(){
                @Override
                public Object execute(RedisOperations kvRedisOperations) throws DataAccessException {
                    kvRedisOperations.opsForValue().set(key,value);
                    kvRedisOperations.expire(key,time,TimeUnit.MILLISECONDS);
                    return null;
                }
            };
            redisTemplate.execute(sc);

        return null;
    }

    @Override
    public Object set(String key, Object value) throws Exception {
            redisTemplate.opsForValue().set(key,value);

        return null;
    }

    @Override
    public Object get(String key) throws Exception {


            return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Object setLpush(String key, Object value) throws Exception {
            ListOperations listOperations = redisTemplate.opsForList();
            listOperations.leftPush(key, value);



        return null;
    }

    @Override
    public Object lrange(String key, Long start, Long end) throws Exception {
        Object ob=null;

            ListOperations listOperations = redisTemplate.opsForList();
            ob=listOperations.range(key, start,end);

        return ob;
    }

    @Override
    public void del(Collection c) throws Exception {
        redisTemplate.delete(c);


    }

    @Override
    public void sadd(String key, Object ...value) throws Exception {
        redisTemplate.opsForSet().add(key,value);
    }

    @Override
    public boolean smove(String set1, String set2, Object value) throws Exception {
        return redisTemplate.opsForSet().move(set1,set2,value);
    }

    @Override
    public boolean sismember(String set1, Object value) throws Exception {
        boolean re=false;

            re=redisTemplate.opsForSet().isMember(set1,value);

        return re;
    }

    @Override
    public Set smembers(String set1) throws Exception {
        Set st=null;
        st=redisTemplate.opsForSet().members(set1);
        return st;
    }
}
