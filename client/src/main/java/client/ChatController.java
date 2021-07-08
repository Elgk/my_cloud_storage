package client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

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
    private DataInputStream in;
    private DataOutputStream out;
    private byte[] buffer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buffer = new byte[256];
        try {
            File dir = new File(root);
            listView.getItems().clear();
            listView.getItems().addAll(dir.list());
            Socket socket = new Socket("localhost", 8189);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            Thread readThread = new Thread(() -> {
                try {
                    while (true) {
                      //  int read = in.read(buffer);
                        String msg = in.readUTF();
                      //  String msg = new String(buffer,0,read);
                        Platform.runLater(() ->
                              //  listView.getItems().add(msg)
                              textField.setText(msg)
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
        }
    }

    public void send(ActionEvent actionEvent) throws IOException {
       // String msg = textField.getText();
        String filename = listView.getSelectionModel().getSelectedItem();
        Path file = Path.of(root, filename);
        long size = Files.size(file);
        out.writeUTF(filename);
        out.writeLong(size);
        Files.copy(file,out);
     //   os.write(msg.getBytes(StandardCharsets.UTF_8));
        out.flush();

    }
}
