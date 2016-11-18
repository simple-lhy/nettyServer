/**
 * @(#)SocketInfo.java, 16/11/9.
 * <p/>
 * Copyright 2016 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package MySocketPool;

import java.io.IOException;
import java.net.Socket;

/**
 */
public class SocketAdapter extends Socket {
    /**
     * 默认构造方法
     */
    public SocketAdapter() {
        super();
    }

    /**
     * @param host
     * @param port
     * @throws IOException
     */
    public SocketAdapter(String host, int port) throws IOException {
        super(host, port);
    }

    /**
     * 构造连接一次就断开的Socket
     * 
     * @param host
     * @param port
     * @param canClose
     * @throws IOException
     */
    public SocketAdapter(String host, int port, boolean canClose)
        throws IOException {
        super(host, port);
        this.canClosed = canClose;
    }

    /**
     * socket是否空闲 是:true 否:false
     */
    private boolean isFree = true;

    /**
     * socket 池中Id
     */
    private Integer socketId;

    /**
     * 是否为可关闭连接 是:true 否 false
     */
    private boolean canClosed = false;

    /**
     * @return 当前socket是否空闲可用
     */
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
        return canClosed;
    }

    public void setCanClosed(boolean canClosed) {
        this.canClosed = canClosed;
    }

    public void destroy() throws IOException {
        close();
    }

    @Override
    public void close() throws IOException {
        isFree = true;
        if (canClosed) {
            super.close();
        }
    }
}
