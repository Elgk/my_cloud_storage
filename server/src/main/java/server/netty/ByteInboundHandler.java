package server.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ByteInboundHandler extends ChannelInboundHandlerAdapter {
    private final ScriptEngine engine;

    public ByteInboundHandler() {
        ScriptEngineManager factory = new ScriptEngineManager();
        engine = factory.getEngineByName("nashorn");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("recieved {}", msg);
        ByteBuf buf = (ByteBuf) msg;
        StringBuilder sb = new StringBuilder();
        while (buf.isReadable()) {
            char b = (char) buf.readByte();
            sb.append(b);
        }
        sb.append("\r\n");

        String expression = sb.toString();
        String result = engine.eval(expression).toString() + "\r\n";

        log.debug("evaluated: {}", result);

        ByteBuf res = ctx.alloc().buffer();
        res.writeBytes(result.getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(res);

//        log.debug("msg {} ", sb);
//        // buf.retain();
//        ByteBuf buffer = ctx.alloc().buffer();
//        buffer.writeBytes(sb.toString().getBytes(StandardCharsets.UTF_8));
//        //  ctx.writeAndFlush(buf);
//        ctx.writeAndFlush(buffer);

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Client connected...");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Client disconnected...");
    }
}
