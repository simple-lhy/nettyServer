/**
 * @(#)ConnectionProvider.java, 16/11/9.
 * <p/>
 * Copyright 2016 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package MySocketPool;

/**
 *  * @author hzlvhaiyan@corp.netease.com on 16/11/9.  
 */
public interface ConnectionProvider {
    /**
     * 判断池内是否可用
     * 
     * @return true or false
     */
    boolean isPooled();

    /**
     * 提供一个可用的Socket
     * 
     * @return
     */
    SocketAdapter getConnection();

    /**
     * 连接池初始化
     */
    void init();

    /**
     * 连接池重新启动
     */
    void restart();

    /**
     * 销毁连接池
     */
    void destroy();
}
