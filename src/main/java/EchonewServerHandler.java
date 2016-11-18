
/**
 * @(#)EchonewServerHandler.java, 16/11/11.
 * <p/>
 * Copyright 2016 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 */
public class EchonewServerHandler extends SimpleChannelInboundHandler {
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
        Object o) throws Exception {
        System.out.println("ready to flush" + o.toString());
        channelHandlerContext.writeAndFlush(o);
    }

}
