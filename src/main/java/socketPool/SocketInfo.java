/**
 * @(#)SocketInfo.java, 16/11/8.
 * <p/>
 * Copyright 2016 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package socketPool;

import java.net.Socket;

/**
 *  * @author hzlvhaiyan@corp.netease.com on 16/11/8.  
 */

/**
 * @Crop 深圳市xxx科技有限公司
 * @author liuxingmi
 * @QQ 63972012
 * @DateTime 2014-8-25 下午3:21:19
 * @Desc 名字服务器连接信息
 */
public class SocketInfo {

    /**
     * socket
     */
    private Socket socket;

    /**
     * 是否空闲 （是：true 否：false）
     */
    private boolean isFree;

    /**
     * socket id
     */
    private Integer socketId;

    /**
     * 是否为可关闭链接 （是：true 否：false）
     */
    private boolean isClosed;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean isFree) {
        this.isFree = isFree;
    }

    public Integer getSocketId() {
        return socketId;
    }

    public void setSocketId(Integer socketId) {
        this.socketId = socketId;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }

}
