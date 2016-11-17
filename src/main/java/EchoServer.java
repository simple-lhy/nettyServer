import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class EchoServer {
    private static final int port = 8844;

    public void start() throws InterruptedException {
        ServerBootstrap b = new ServerBootstrap();// 引导辅助程序
        EventLoopGroup group = new NioEventLoopGroup();// 通过nio方式来接收连接和处理连接
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            b.group(group, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 128);
//            b.channel(NioServerSocketChannel.class);// 设置nio类型的channel
//            b.localAddress(new InetSocketAddress(port));// 设置监听端口
            b.childHandler(new ChannelInitializer<SocketChannel>() {//有连接到达时会创建一个channel
                protected void initChannel(SocketChannel ch) throws Exception {
                    // pipeline管理channel中的Handler，在channel队列中添加一个handler来处理业务
                    ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                    // server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码
                    ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
                    ch.pipeline().addLast("myHandler", new EchoServerHandler());

                }
            });
            b.option(ChannelOption.SO_BACKLOG, 128);
//                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = b.bind(port).sync();// 配置完成，开始绑定server，通过调用sync同步方法阻塞直到绑定成功
            System.out.println(EchoServer.class.getName()
                    + " started and listen on " + f.channel().localAddress());
            f.channel().closeFuture().sync();// 应用程序会一直等待，直到channel关闭
            System.out.println("channel has closed");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully().sync();//关闭EventLoopGroup，释放掉所有资源包括创建的线程
        }
    }

    public static void main(String[] args) {
        try {
            new EchoServer().start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
