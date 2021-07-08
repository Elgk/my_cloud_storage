package server;

import java.io.*;
import java.net.Socket;

public class ChatHandler implements Runnable{
    private static String ROOT = "server/serverFiles";
    private Socket socket;
    private byte[] buffer;
//    private InputStream is;
//    private OutputStream os;
    private DataInputStream in;
    private DataOutputStream out;

    public ChatHandler(Socket socket) {
        this.socket = socket;
        buffer = new byte[256];
    }

    @Override
    public void run() {
        try {
//            is = socket.getInputStream();
//            os = socket.getOutputStream();
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            while (true){
                 fileUpload();
//                int read = in.read(buffer);
//                System.out.println("Received: " + new String(buffer, 0, read));
//                out.write(buffer,0,read);
//                out.flush();
            }
        }catch (Exception e){
           // e.printStackTrace();
            System.err.println("Client connection exception");
        }finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void fileUpload() throws IOException {
        String filename = in.readUTF();
        File file = new File(ROOT + "/"+ filename);
//        if (!file.exists()) {
//            file.createNewFile();

        long size = in.readLong();
        System.out.println("File recieved: "+ filename + ", " + size);
        try(FileOutputStream fos = new FileOutputStream(file)){

            int read;
            for (int i = 0; i < (size + 255)/256; i++) {
                read = in.read(buffer);
                fos.write(buffer,0,read);
            }
        }
        out.writeUTF("File recieved: " + filename + ", " + size);
    }
}
