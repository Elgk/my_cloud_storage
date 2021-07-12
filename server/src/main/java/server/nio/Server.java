package server.nio;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Server {

    private static int cnt = 1;
    private ServerSocketChannel sc;
    private Selector selector;
    private String name = "user";

    public Server() throws IOException {
        sc = ServerSocketChannel.open();
        selector = Selector.open();
        sc.bind(new InetSocketAddress(8189));
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_ACCEPT);

        while (sc.isOpen()) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectedKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    handleAccept(key);
                }
                if (key.isReadable()) {
                    handleRead(key);
                }
                iterator.remove();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Server();
    }

    private void handleRead(SelectionKey key) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(256);
        SocketChannel channel = (SocketChannel) key.channel();
        String name = (String) key.attachment();
        int read;
        StringBuilder sb = new StringBuilder();
        while (true) {
            read = channel.read(buffer);
            buffer.flip();
            if (read == -1) {
                channel.close();
                break;
            }
            if (read > 0) {
                while (buffer.hasRemaining()) {
                    sb.append((char) buffer.get());
                }
                buffer.clear();
            } else {
                break;
            }
        }
        System.out.println("received: " + sb);

        String command = sb.toString().replace("\n","").replace("\r","");
        if ("ls".equals(command)){
            getFiles();
        }
        if (command.startsWith("cat")){
            getFileContent(command);
        }
        if ("exit".equals(command)){
            System.out.println("Client is log out");
            channel.close();
            return;

        }
        // массовая рассылка
/*        for (SelectionKey selectionKey : selector.keys()) {
            if (selectionKey.isValid() && selectionKey.channel() instanceof SocketChannel) {
                SocketChannel ch = (SocketChannel) selectionKey.channel();
                ch.write(ByteBuffer.wrap((name + ": " + sb).getBytes(StandardCharsets.UTF_8)));
            }
        }*/
    }

    private void getFileContent(String command) throws IOException {
        String fileName = command.split(" ")[1];
        if (!Files.exists(Path.of("server/serverFiles", fileName))){
            sendMessage("No such file exists\n");
        }else {
            List<String> list = Files.readAllLines(Path.of("server/serverFiles", fileName));
            for (String s : list) {
                sendMessage(s.concat("\n"));
            }
        }
    }

    private void sendMessage(String message) throws IOException {
        for (SelectionKey selectionKey : selector.keys()) {
            if (selectionKey.isValid() && selectionKey.channel() instanceof SocketChannel) {
                SocketChannel ch = (SocketChannel) selectionKey.channel();
                ch.write(ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8)));
            }
        }
    }
    private void getFiles() throws IOException {
        File file = new File("server/serverFiles");
        String msg = Arrays.toString(file.list()).concat("\n");
        sendMessage(msg);
    }

    private void handleAccept(SelectionKey key) throws IOException {
        SocketChannel channel = sc.accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ, name + cnt);
        cnt++;
    }

}
