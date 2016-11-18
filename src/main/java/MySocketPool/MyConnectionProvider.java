/**
 * @(#)MyConnectionProvider.java, 16/11/10.
 * <p/>
 * Copyright 2016 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package MySocketPool;

import java.io.IOException;

/**
 * 连接池的简单实现
 * 
 */
public class MyConnectionProvider implements ConnectionProvider {

    /**
     * 连接池提供
     */
    private static ConnectionProvider provider = null;

    /**
     * 生成一个新连接池时需要获取此对象锁
     */
    private static Object lock = new Object();

    /**
     * 连接池最大容量
     */
    private static int max_size = 4;

    /**
     * 默认连接池最小连接数,在初始化的时候就会生成3个一直保持的链接
     */
    private static int min_size = 3;

    private String ip;

    private String port;

    /**
     * socket 连接池
     */
    private SocketAdapter[] socketPool = null;

    /**
     * 连接池加锁对象列表
     */
    private static Object[] object_lock_list = new Object[max_size];

    public MyConnectionProvider() {
        ip = "127.0.0.1";
        port = "8844";
        //初始化连接池
        init();
    }

    public boolean isPooled() {
        if (socketPool != null) {
            return true;
        }
        return false;
    }

    /**
     * 获取一个Socket连接
     * 
     * @return
     */
    public SocketAdapter getConnection() {
        SocketAdapter socketAdapter = null;
        for (int i = 0; i < socketPool.length; i++) {
            if (socketPool[i] != null) {
                //查找是否为空闲连接,如果空闲,则返回一个空闲连接,若没有,则继续循环寻找
                if (!socketPool[i].isFree()) {
                    continue;
                } else {
                    synchronized (object_lock_list[i]) {

                        if (socketPool[i].isFree()) {
                            socketAdapter = socketPool[i];
                            //                            try {
                            //                                socketAdapter = new SocketAdapter(ip,
                            //                                    Integer.parseInt(port));
                            //                            } catch (IOException e) {
                            //                                e.printStackTrace();
                            //                                System.out.println(e.getMessage());
                            //                            }
                            socketAdapter.setFree(false);
                            System.out.println("reuse the socket " + i);
                            return socketAdapter;
                        } else {
                            continue;
                        }

                    }
                }
            } else {
                System.out.println("create a new Socket" + i);
                try {
                    socketAdapter = socketPool[i] = new SocketAdapter(ip,
                        Integer.parseInt(port));
                    socketAdapter.setFree(false);
                    return socketAdapter;
                } catch (IOException e) {
                    System.out.println("create new Socket error");
                    e.printStackTrace();
                }
            }
        }
        //如果连接依旧为空,则生成可以关闭的socket连接,使用完毕后会关闭,不放到连接池中
        if (socketAdapter == null) {
            try {
                socketAdapter = new SocketAdapter(ip, Integer.parseInt(port),
                    true);
                System.out.println("create new  normal Socket");

            } catch (IOException e) {
                System.out.println("create new  normal Socket error");
                e.printStackTrace();
            }
        }
        return socketAdapter;
    }

    public void init() {
        socketPool = new SocketAdapter[max_size];
        for (int i = 0; i < min_size; i++) {
            //            object_lock_list[i] = new Object();
            try {
                //默认创建三个连接,长久保持连接
                object_lock_list[i] = new SocketAdapter(ip,
                    Integer.parseInt(port));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("SocketPool init success……");

    }

    /**
     * 重启连接池
     */
    public void restart() {
        destroy();
        System.out.println("SocketPool is restarting ……");
        init();
    }

    /**
     * 注销连接池
     */
    public void destroy() {
        for (int i = 0; i < socketPool.length; i++) {
            if (socketPool[i] != null) {
                SocketAdapter socketAdapter = socketPool[i];
                socketAdapter.setCanClosed(true);
                try {
                    socketAdapter.destroy();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static ConnectionProvider newInstance() {
        if (provider == null) {
            synchronized (lock) {
                if (provider == null) {
                    provider = new MyConnectionProvider();
                }
            }
        }
        return provider;
    }
}
