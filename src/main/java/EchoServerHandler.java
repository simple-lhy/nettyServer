import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Sharable表示此对象在channel间共享 handler类是我们的具体业务类
 */
@Sharable //注解@Sharable可以让它在channels间共享
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        System.out.println("server received data :" + msg);
        String ret = "000000#111111111111#20100505#2#1|RMB|123454|0|100.00|2222222222|张|华夏行|20000.00|sdfsd|qwer|1|sdf|sdfs|#2|RMB|123455|0|100.00|2222222222|李|北京华夏|20000.00|sdfsd|qwer|1|sdf|sdfs|#@@@@\r\n";
        //        System.out
        //            .println(((ByteBuf) ret).toString(io.netty.util.CharsetUtil.UTF_8));

        ctx.write(ret);//写回数据，
        //        ctx.write(-1);
        ctx.flush();

    }

    public void channelReadComplete(ChannelHandlerContext ctx) {
        System.out.println("ready to flush:");
        //        ChannelFuture future = ctx.writeAndFlush(Unpooled.EMPTY_BUFFER);
        //        System.out.println(future.channel().close());
        //        System.out.println(future.toString());
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)//flush掉所有写回的数据
            .addListener(ChannelFutureListener.CLOSE); //当flush完成后关闭channel
        System.out.println("flush done");
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();//捕捉异常信息
        ctx.close();//出现异常时关闭channel
    }
}
