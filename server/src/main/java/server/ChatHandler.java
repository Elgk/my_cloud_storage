package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ChatHandler implements Runnable{
    private Socket socket;
    private byte[] buffer;
    private InputStream is;
    private OutputStream os;

    public ChatHandler(Socket socket) {
        this.socket = socket;
        buffer = new byte[256];
    }

    @Override
    public void run() {
        try {
            is = socket.getInputStream();
            os = socket.getOutputStream();
            while (true){
                int read = is.read(buffer);
                System.out.println("Received: " + new String(buffer, 0, read));
                os.write(buffer,0,read);
                os.flush();
            }
        }catch (Exception e){
           // e.printStackTrace();
            System.err.println("Client connection exception");
        }finally {
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
