/**
 * @(#)EchoClientHandler.java, 16/10/22.
 * <p/>
 * Copyright 2016 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

@Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    /**
     * 此方法会在连接到服务器后被调用
     */
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(
            Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8))
            .addListener(new ChannelFutureListener() {

                public void operationComplete(ChannelFuture future)
                    throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("request success");
                    } else {
                        System.out.println("request fail");
                    }
                }
            });
    }

    /**
     * 此方法会在接收到服务器数据后调用
     */
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
        //        System.out.println("Client received: "
        //            + ByteBufUtil.hexDump(in.readBytes(in.readableBytes())));
        System.out.println(
            "Client received: " + in.toString(io.netty.util.CharsetUtil.UTF_8));
    }

    /**
     * 捕捉到异常
     */
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    protected void messageReceived(ChannelHandlerContext channelHandlerContext,
        ByteBuf in) throws Exception {
        //        System.out.println("Client received: "
        //            + ByteBufUtil.hexDump(in.readBytes(in.readableBytes())));
        System.out.println(
            "Client received: " + in.toString(io.netty.util.CharsetUtil.UTF_8));
    }
}
