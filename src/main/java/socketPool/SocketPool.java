///**
// * @(#)SocketPool.java, 16/11/8.
// * <p/>
// * Copyright 2016 Netease, Inc. All rights reserved.
// * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
// */
//
//package socketPool;
//
//import java.io.IOException;
//import java.net.Socket;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// *  * @author hzlvhaiyan@corp.netease.com on 16/11/8.  
// */
//public class SocketPool {
//
//    /**
//     * socketMap
//     */
//    public static ConcurrentHashMap<Integer, SocketInfo> socketMap = new ConcurrentHashMap<Integer, SocketInfo>();
//
//    private static SocketPool instance = new SocketPool();
//
//    private SocketPool() {}
//
//    public static SocketPool getInstance() {
//        if (instance == null) {
//            synchronized (SocketPool.class) {
//                if (instance == null) {
//                    instance = new SocketPool();
//                }
//            }
//        }
//        return instance;
//    }
//
//    static {
//        instance.initSocket(true);
//    }
//
//    /**
//     * @DateTime 2014-8-25 下午3:18:52
//     * @User liuxingmi
//     * @Desc 初始化链接池
//     * @param isAllReInit
//     *            是否全部重新初始化
//     * @return void
//     */
//    public void initSocket(boolean isAllReInit) {
//        int defaultCount = Integer.parseInt("1");
//        //        logger.info("nameserver:initSocket", DMLogger.RESULT_OK,
//        //            "开始初始化分布式名字服务器连接数：" + defaultCount);
//
//            if (isAllReInit) {
//                socketMap.put(i, setSocketInfo(i, true, false));
//            } else {
//                if (socketMap.get(i) == null || socketMap.get(i).isClosed()) {
//                    socketMap.put(i, setSocketInfo(i, true, false));
//                }
//            }
//        }
//
//        //        logger.info("nameserver:initSocket", DMLogger.RESULT_OK,
//        //            "完成初始化分布式名字服务器连接数");
//        new CheckSocketThread().start();
//
//    }
//
//    /**
//     * @DateTime 2014-8-26 上午10:06:13
//     * @User liuxingmi
//     * @Desc 设置socketInfo值
//     * @param socket
//     * @param key
//     * @param isFree
//     * @param isClosed
//     * @return SocketInfo
//     */
//    private static SocketInfo setSocketInfo(Integer key, boolean isFree,
//        boolean isClosed) {
//        SocketInfo socketInfo = new SocketInfo();
//        Socket socket = createSocket();
//        socketInfo.setFree(isFree);
//        socketInfo.setSocket(socket);
//        socketInfo.setSocketId(key);
//        socketInfo.setCanClosed(isClosed);
//        return socketInfo;
//    }
//
//    /**
//     * @DateTime 2014-8-25 下午3:19:06
//     * @User liuxingmi
//     * @Desc 获取名字服务器链接
//     * @return SocketInfo
//     */
//    public SocketInfo getSocketInfo() {
//
//        SocketInfo socketInfo = null;
//
//        if (socketMap.size() < Integer.parseInt("1")) {
//            initSocket(false);
//        }
//
//        if (socketMap.size() > 0) {
//            for (Map.Entry<Integer, SocketInfo> entry: socketMap.entrySet()) {
//                socketInfo = entry.getValue();
//                if (socketInfo.isFree() && !socketInfo.getSocket().isClosed()) {
//                    socketInfo.setFree(false);
//                    return socketInfo;
//                }
//            }
//        } else {
//            //            logger.info("nameserver:socketInfo", DMLogger.RESULT_FAIL,
//            //                "名字服务器socket连接池数量为零。");
//            return null;
//        }
//
//        //        logger.info("nameserver:socketInfo", DMLogger.RESULT_OK,
//        //            "所有名字服务器socket链接都忙，创建临时链接。");
//
//        socketInfo = setSocketInfo(-1, true, true);
//        //        logger.info("nameserver:socketInfo", DMLogger.RESULT_OK,
//        //            "成功创建服务器socket临时链接。");
//        return socketInfo;
//
//    }
//
//    /**
//     * 释放socket
//     *
//     * @param socketId
//     */
//    public static void distorySocket(Integer socketId) {
//
//        //        logger.debug("nameserver:distorySocket", DMLogger.RESULT_OK,
//        //            "释放名字服务器socket链接。");
//        SocketInfo socketInfo = socketMap.get(socketId);
//        socketInfo.setFree(true);
//
//    }
//
//    /**
//     * @DateTime 2014-8-25 下午3:19:42
//     * @User liuxingmi
//     * @Desc 释放socket
//     * @param socketInfo
//     *            void
//     */
//    public static void distorySocket(SocketInfo socketInfo) {
//
//        if (socketInfo == null)
//            return;
//
//        if (!socketInfo.isClosed()) {
//            //            logger.debug("nameserver:distorySocket", DMLogger.RESULT_OK,
//            //                "链接池socket，释放资源。");
//            distorySocket(socketInfo.getSocketId());
//            return;
//        }
//
//        //        logger.debug("nameserver:distorySocket", DMLogger.RESULT_OK,
//        //            "可关闭临时链接，关闭socket");
//        try {
//            if (socketInfo.getSocket() != null) {
//                socketInfo.getSocket().close();
//            }
//        } catch (IOException e) {
//            //            logger.error("nameserver:distorySocket", DMLogger.RESULT_FAIL,
//            //                "关闭名字服务器socket链接失败", e);
//        }
//        socketInfo = null;
//    }
//
//    /**
//     * @DateTime 2014-8-25 下午3:19:51
//     * @User liuxingmi
//     * @Desc 创建socket
//     * @return Socket
//     */
//    public static Socket createSocket() {
//
//        String nameServerip1 = CloudpConfigUtil.DM_CONFIG.getNameSerIP1();
//        int namServerport1 = CloudpConfigUtil.DM_CONFIG.getNameSerPort1();
//        String nameServerip2 = CloudpConfigUtil.DM_CONFIG.getNameSerIP2();
//        int namServerport2 = CloudpConfigUtil.DM_CONFIG.getNameSerPort2();
//        Socket socket = null;
//
//        try {// 尝试通过ip1第一次建立连接
//            socket = new Socket(nameServerip1, namServerport1);
//            //            logger.info("nameserver:login", DMLogger.RESULT_OK, "nameServerip1:"
//            //                + nameServerip1 + ", namServerport1:" + namServerport1);
//        } catch (IOException e) {
//            //            logger
//            //                .error("nameserver:login",
//            //                    DMLogger.RESULT_FAIL, "first link fail nameServerip1:"
//            //                        + nameServerip1 + ", namServerport1:" + namServerport1,
//            //                    e);
//            try {
//                // 如果第一次通过ip1建立连接失败，则进行第二次连接
//                socket = new Socket(nameServerip2, namServerport2);
//                //                logger.info("nameserver:login", DMLogger.RESULT_OK,
//                //                    "nameServerip2:" + nameServerip2 + ", namServerport2:"
//                //                        + namServerport2);
//            } catch (IOException e1) {
//                //                logger.error("nameserver:login",
//                //                    DMLogger.RESULT_FAIL, "second link fail nameServerip2:"
//                //                        + nameServerip2 + ", namServerport2:" + namServerport2,
//                //                    e);
//                return null;
//            }
//        }
//        return socket;
//    }
//
//    class CheckSocketThread extends Thread {
//        @Override
//        public void run() {
//            while (true) {
//                //                logger.debug("nameserver:checkSocket", DMLogger.RESULT_OK,
//                //                    "开始检测分布式链接状态。");
//                if (socketMap.size() < Integer
//                    .parseInt(ConfigConst.SOCKET_DEFAULT_COUNT)) {
//                    //                    logger.info("nameserver:checkSocket", DMLogger.RESULT_OK,
//                    //                        "分布式名字服务器socket链接小于默认链接数，增加socket链接。");
//                    initSocket(false);
//                }
//
//                for (Map.Entry<Integer, SocketInfo> entry: socketMap
//                    .entrySet()) {
//                    SocketInfo socketInfo = entry.getValue();
//                    if (socketInfo.getSocket() == null
//                        || socketInfo.isClosed()) {
//                        //                        logger.error("nameserver:checkSocket",
//                        //                            DMLogger.RESULT_FAIL,
//                        //                            "第" + entry.getKey() + "个socket链接已关闭，重新连接分布式。",
//                        //                            null);
//                        socketInfo.setSocket(createSocket());
//                    } else {
//                        if (socketInfo.isFree()) {
//                            boolean flag = NameServerUtils.getInstance()
//                                .checkHeartbeat(socketInfo);
//                            if (!flag) {
//                                //                                logger.error("nameserver:checkSocket",
//                                //                                    DMLogger.RESULT_FAIL, "第" + entry.getKey()
//                                //                                        + "个socket链接失败，重新连接分布式。",
//                                //                                    null);
//                                socketInfo.setSocket(createSocket());
//                                continue;
//                            }
//                        }
//                        //                        logger.debug("nameserver:checkSocket",
//                        //                            DMLogger.RESULT_OK,
//                        //                            "第" + entry.getKey() + "个socket链接正常。");
//                    }
//                }
//
//                try {
//                    sleep(Long.valueOf(ConfigConst.SOCKET_CHECK_TIME));
//                } catch (Exception e) {}
//            }
//        }
//    }
//
//}
