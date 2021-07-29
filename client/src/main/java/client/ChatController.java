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
    private NettyNetwork network;
    private byte[] buffer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        network = new NettyNetwork(s -> Platform.runLater(() -> textField.setText(s.toString())));

    }

    public void send(ActionEvent actionEvent) throws IOException {
        String content = textField.getText();
//
        network.writeMessage(new Message(content));

    }
}
