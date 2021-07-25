package client;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.Message;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    public ListView<String> listView;
    public TextField textField;
    private String root = "client/clientFiles";
    private ObjectEncoderOutputStream out;
    private ObjectDecoderInputStream in;
 //   private DataInputStream in;
 //   private DataOutputStream out;
    private NettyNetwork network;
    private byte[] buffer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // создание экземляра сети
        // приходит сообщение и оно записывается в текстовое поле на экране
        network = new NettyNetwork(s -> Platform.runLater(() -> textField.setText(s.toString())));
     /*   buffer = new byte[256];
        try {
            File dir = new File(root);
            listView.getItems().clear();
            listView.getItems().addAll(dir.list());
            Socket socket = new Socket("localhost", 8189);
            out = new ObjectEncoderOutputStream(socket.getOutputStream());
            in = new ObjectDecoderInputStream(socket.getInputStream());
        //    in = new DataInputStream(socket.getInputStream());
        //    out = new DataOutputStream(socket.getOutputStream());
            Thread readThread = new Thread(() -> {
                try {
                    while (true) {
                      //  int read = in.read(buffer);
                       // String msg = in.readUTF();
                      //  String msg = new String(buffer,0,read);
                        Message msg = (Message) in.readObject();
                        Platform.runLater(() ->
                              //  listView.getItems().add(msg)
                              textField.setText(msg.toString())
                        );
                    }
                } catch (Exception e) {
                    System.err.println("Exception while read");
                }
            });
            readThread.setDaemon(true);
            readThread.start();
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }

    public void send(ActionEvent actionEvent) throws IOException {
        String content = textField.getText();
//        out.writeObject(new Message(content));
//        out.flush();
        network.writeMessage(new Message(content));
    /*    String filename = listView.getSelectionModel().getSelectedItem();
        Path file = Path.of(root, filename);
        long size = Files.size(file);
        out.writeUTF(filename);
        out.writeLong(size);
        Files.copy(file,out);
     //   os.write(msg.getBytes(StandardCharsets.UTF_8));
        out.flush();
*/
    }
}
