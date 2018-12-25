package com.poni.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.ServletRequest;
import java.io.Serializable;

/*
 *@author:PONI_CHAN
 *@date:2018/12/24 15:22
 */
//解决多次访问redis的问题
public class CustomSessionManager extends DefaultWebSessionManager {

    //sessionKey里面存放着session对象
    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        Serializable sessionId = getSessionId(sessionKey);
        ServletRequest request = null;

        //先判断sessionKey是不是WebSessionKey
        if (sessionKey instanceof WebSessionKey) {
            //将sessionKey强转成WebSessionKey，获得request
            request = ((WebSessionKey) sessionKey).getServletRequest();
        }
        if (request != null && sessionId != null) {
            Session session = (Session) request.getAttribute(sessionId.toString());
            if (session != null) {
                return session;
            }
        }
        //如果request没有，则先从redis取出来
        Session session = super.retrieveSession(sessionKey);
        //取出来后放到request再返回
        if (request != null && sessionId != null) {
            request.setAttribute(sessionId.toString(), session);
        }
        return session;
    }
}
