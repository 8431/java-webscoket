package com.gjsg.gjchat.webscoket.dao;

import com.gz.medicine.common.exception.CommonException;

import java.util.List;
import java.util.Map;

/**
 * Created by dlf on 2018/2/6 0006.
 * Email 1429264916@qq.com
 */
public interface ChatBaseMapper {
    /**
     * 执行sql
     * @return
     * @throws CommonException
     */
    List<Map<String,Object>> querySql(Map<String, Object> param)throws CommonException;

    /**
     * 插入sql
     * @param param
     * @throws CommonException
     */
    void insertSql(Map<String, Object> param)throws CommonException;

    /**
     * 更新
     * @param param
     * @throws CommonException
     */
    void updateSql(Map<String, Object> param)throws CommonException;
    /**
     * 删除
     * @param param
     * @throws CommonException
     */
    void deleteSql(Map<String, Object> param)throws CommonException;

}
