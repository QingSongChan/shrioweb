package com.poni.session;


import com.poni.util.JedisUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/*
 *@author:PONI_CHAN
 *@date:2018/12/22 16:46
 */
public class RedisSessionDao extends AbstractSessionDAO {

    @Resource
    private JedisUtil jedisUtil;

    private final String shiro_session_prefix = "poni-session";

    //redis中通过存储二进制的形式来存储
    private byte[] getKey(String key) {
        return (shiro_session_prefix + key).getBytes();
    }

    //创建一个create跟update共有的办法
    private void saveSession(Session session) {
        if (session != null && session.getId() != null) {
            byte[] key = getKey(session.getId().toString());
            byte[] value = SerializationUtils.serialize(session);
            jedisUtil.set(key, value);
            jedisUtil.expire(key, 500);   //设置超时时间
        }
    }

    //创建session，先拿到sessionid,将value序列化
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session,sessionId);    //生成的sessionId 要与session捆绑
        saveSession(session);

        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            return null;
        }
        byte[] key = getKey(sessionId.toString());
        byte[] value = jedisUtil.get(key);

        //获得value数组后反序列化成session对象
        return (Session) SerializationUtils.deserialize(value);
    }

    //由于更新和创建方法类似，所以提取一个共同的保存方法save
    @Override
    public void update(Session session) throws UnknownSessionException {
        saveSession(session);
    }

    @Override
    public void delete(Session session) {
        if (session == null && session.getId() == null) {
            return;
        }
        byte[] key = getKey(session.getId().toString());
        jedisUtil.del(key);
    }

    //获得指定的(前缀)存活session
    @Override
    public Collection<Session> getActiveSessions() {
        Set<byte[]> keys = jedisUtil.keys(shiro_session_prefix);
        Set<Session> sessions = new HashSet<>();
        if (CollectionUtils.isEmpty(keys)) {
            return sessions;
        }
        for (byte[] key : keys) {
            Session session = (Session) SerializationUtils.deserialize(jedisUtil.get(key));
            sessions.add(session);
        }
        return sessions;

    }
}
