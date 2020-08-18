package sample;

import com.classes.Listener;
import com.classes.Sender;
import com.classes.Terminal;
import com.wrappers.AllCommands;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Main extends Application {
    public static ResourceBundle resourceBundle;
    public static String ErrorMessage = "";
    public static String ErrorTrace = "";
    public static String Username = "default";


    @Override
    public void start(Stage primaryStage) throws Exception{
        Main.resourceBundle = ResourceBundle.getBundle("Locale_ru", Locale.forLanguageTag("ru"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Logger.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Вход");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static void setError(String errMes, String errTrace){
        ErrorMessage = errMes;
        ErrorTrace = errTrace;
    }
}
