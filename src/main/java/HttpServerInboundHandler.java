import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.HttpRequest;

public class HttpServerInboundHandler extends ChannelInboundHandlerAdapter {

    private HttpRequest request;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
        throws Exception {
        if (msg instanceof HttpRequest) {
            request = (HttpRequest) msg;

            String uri = request.getUri();
            System.out.println("uri:" + uri);
        } else {
            System.out.println("++++++++++++++++++++++");
            System.out.println(msg.toString());
            ctx.write(msg.toString());
            ctx.flush();
        }
        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            ByteBuf buf = content.content();
            System.out.println(buf.toString(io.netty.util.CharsetUtil.UTF_8));
            buf.release();

            //            String res = "I am OK";
            String res = "<?xml version=\"1.0\" encoding=\"GB2312\"?>\n"
                + "<CMBC header=\"100\" lang=\"chs\" security=\"none\" trnCode=\"qryDtl\"\n"
                + "\tversion=\"100\">\n" + "\t<responseHeader>\n"
                + "\t\t<status>\n" + "\t\t\t<code>0</code>\n"
                + "\t\t\t<severity>info</severity>\n"
                + "\t\t\t<message>ok</message>\n" + "\t\t</status>\n"
                + "\t\t<dtServer>2008-09-01 15:51:09</dtServer>\n"
                + "\t\t<userKey>N</userKey>\n" + "\t\t<dtDead>\n"
                + "\t\t</dtDead>\n" + "\t\t<language>chs</language>\n"
                + "\t</responseHeader>\n" + "\t<xDataBody>\n"
                + "\t\t<trnId>1111</trnId>\n" + "\t\t<dtlList>\n"
                + "\t\t\t<dtlInfo>\n" + "\t\t\t\t<svrId>900100230001</svrId>\n"
                + "\t\t\t\t<acntNo>0101014830000648</acntNo>\n"
                + "\t\t\t\t<acntName>\n" + "\t\t\t\t</acntName>\n"
                + "\t\t\t\t<type>1</type>\n"
                + "\t\t\t\t<actDate>2008-09-01</actDate>\n"
                + "\t\t\t\t<intrDate>2008-09-01</intrDate>\n"
                + "\t\t\t\t<chequeNum>9800809010553</chequeNum>\n"
                + "\t\t\t\t<amount>1.00</amount>\n"
                + "\t\t\t\t<opAcntNo>0101014130001041</opAcntNo>\n"
                + "\t\t\t\t<opAcntName>中冶集团财务公司</opAcntName>\n"
                + "\t\t\t\t<opBankName>中国民生银行总行营业部</opBankName>\n"
                + "\t\t\t\t<opBankAddr></opBankAddr>\n"
                + "\t\t\t\t<opAreaCode>100086</opAreaCode>\n"
                + "\t\t\t\t<explain>平台付款fafsd</explain>\n"
                + "\t\t\t\t<balance>63765638.60</balance>\n"
                + "\t\t\t\t<recseq>1</recseq>\n"
                + "\t\t\t\t<timestamp>1294210503</timestamp>\n"
                + "\t\t\t</dtlInfo>\n" + "\t\t</dtlList>\n"
                + "\t\t<totalNum>1</totalNum>\n" + "\t</xDataBody>\n"
                + "</CMBC>\n";
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
                OK, Unpooled.wrappedBuffer(res.getBytes("GB2312")));
            response.headers().set(CONTENT_TYPE, "application/xml");
            response.headers().set(CONTENT_LENGTH,
                response.content().readableBytes());
            if (HttpHeaders.isKeepAlive(request)) {
                response.headers().set(CONNECTION, Values.KEEP_ALIVE);
            }
            ctx.write(response);
            ctx.flush();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
        throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

    public byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }
}
