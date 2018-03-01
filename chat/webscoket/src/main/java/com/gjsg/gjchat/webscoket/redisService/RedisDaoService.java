package com.gjsg.gjchat.webscoket.redisService;

import java.util.Collection;
import java.util.Set;

/**
 * Created by Administrator on 2017/5/11 0011.
 */
public interface RedisDaoService {
    /**
     * 设置key
     * @param key
     * @param value
     * @param time key生存时间
     * @return
     */
    Object set(String key, Object value, Long time) throws Exception;
    /**
     * 设置key
     * @param key
     * @param value
     * @return
     */
    Object set(String key, Object value) throws Exception;

    /**
     * 通过key获取value
     * @param key
     * @return
     * @throws Exception
     */
    Object get(String key) throws Exception;

    /**
     * 队列
     * @param key
     * @param value
     * @return
     * @throws Exception
     */
    Object setLpush(String key, Object value) throws Exception;

    /**
     * 获取队列指定范围数据
     * @param key
     * @param srart
     * @param end
     * @return
     * @throws Exception
     */
    Object lrange(String key, Long srart, Long end) throws Exception;

    /**
     * 删除key
     * @param c
     */
    void del(Collection c) throws Exception;


    /**
     * set 添加
     * @param key
     * @param value
     * @throws Exception
     */
    void sadd(String key,Object... value )throws Exception;

    /**
     * set 集合元素移除
     * @param set1
     * @param set2
     * @param value
     * @throws Exception
     */
    boolean smove(String set1,String set2 ,Object value )throws Exception;

    /**
     * 判断value是否在集合中
     * @param set1
     * @param value
     * @return
     * @throws Exception
     */
    boolean  sismember(String set1,Object value )throws Exception;

    /**
     * 获取set成员
     * @param set1
     * @return
     * @throws Exception
     */
    Set smembers(String set1 )throws Exception;
}
