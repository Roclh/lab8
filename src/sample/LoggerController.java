package sample;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URL;
import java.nio.channels.DatagramChannel;
import java.util.Locale;
import java.util.ResourceBundle;

import com.classes.Listener;
import com.classes.Sender;
import com.classes.Terminal;
import com.classes.serverSide.answers.Request;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class LoggerController {



    public static String status = "Nothing Yet";
    public static Sender sender;
    public static Listener listener;

    public static SocketAddress socketAddress;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Tab auth;

    @FXML
    private TextField login_auth;

    @FXML
    private TextField login_pass;

    @FXML
    private Button login_entry;

    @FXML
    private Tab registr;

    @FXML
    private TextField login_registr;

    @FXML
    private TextField pass_registr;

    @FXML
    private Button entry_registr;

    @FXML
    private TextField portReg;

    @FXML
    private TextField portAuth;

    @FXML
    void initialize() {

        login_entry.setOnAction(event -> {
            if(!portAuth.getText().isEmpty()){
                try {
                    DatagramChannel datagramChannel = DatagramChannel.open();
                    SocketAddress socketAddress = null;
                        try {
                            socketAddress = new InetSocketAddress("localhost", Integer.parseInt(portAuth.getText().trim()));
                        } catch (IllegalArgumentException e) {
                            Main.setError("Вы не ввели порт!", "Введите порт и повторите попытку.");
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Error.fxml"));
                            Parent root;
                            try {
                                root = fxmlLoader.load();
                                Stage stage = new Stage();
                                stage.initModality(Modality.APPLICATION_MODAL);
                                stage.setOpacity(1);
                                stage.setTitle("Ошибка!");
                                stage.setScene(new Scene(root));
                                stage.showAndWait();
                            } catch (IOException er) {
                                er.printStackTrace();
                            }
                        }
                    Listener listener = new Listener(datagramChannel, socketAddress);
                    listener.setDaemon(true);
                    listener.start();
                    sender = new Sender(datagramChannel, socketAddress);
                    logIn(sender);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                    Main.setError("Произошла ошибка создания отправителя!", "Попробуйте ввести другой порт и повторите попытку.");
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Error.fxml"));
                    Parent root;
                    try {
                        root = fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setOpacity(1);
                        stage.setTitle("Ошибка!");
                        stage.setScene(new Scene(root));
                        stage.showAndWait();
                    } catch (IOException er) {
                        er.printStackTrace();
                    }
                }
            }else{
                Main.setError("Вы не ввели порт!", "Введите порт и повторите попытку.");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Error.fxml"));
                Parent root;
                try {
                    root = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setOpacity(1);
                    stage.setTitle("Ошибка!");
                    stage.setScene(new Scene(root));
                    stage.showAndWait();
                } catch (IOException er) {
                    er.printStackTrace();
                }
            }

        });
        entry_registr.setOnAction(event -> {
            if(!portReg.getText().isEmpty()){
                try {
                    DatagramChannel datagramChannel = DatagramChannel.open();
                    SocketAddress socketAddress = null;
                    try {
                        socketAddress = new InetSocketAddress("localhost", Integer.parseInt(portReg.getText().trim()));
                    } catch (IllegalArgumentException e) {
                        Main.setError("Вы не ввели порт!", "Введите порт и повторите попытку.");
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Error.fxml"));
                        Parent root;
                        try {
                            root = fxmlLoader.load();
                            Stage stage = new Stage();
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.setOpacity(1);
                            stage.setTitle("Ошибка!");
                            stage.setScene(new Scene(root));
                            stage.showAndWait();
                        } catch (IOException er) {
                            er.printStackTrace();
                        }
                    }
                    Listener listener = new Listener(datagramChannel, socketAddress);
                    listener.setDaemon(true);
                    listener.start();
                    sender = new Sender(datagramChannel, socketAddress);
                    auth(sender);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                    Main.setError("Произошла ошибка создания отправителя!", "Попробуйте ввести другой порт и повторите попытку.");
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Error.fxml"));
                    Parent root;
                    try {
                        root = fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setOpacity(1);
                        stage.setTitle("Ошибка!");
                        stage.setScene(new Scene(root));
                        stage.showAndWait();
                    } catch (IOException er) {
                        er.printStackTrace();
                    }
                }
            }else{
                Main.setError("Вы не ввели порт!", "Введите порт и повторите попытку.");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Error.fxml"));
                Parent root;
                try {
                    root = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setOpacity(1);
                    stage.setTitle("Ошибка!");
                    stage.setScene(new Scene(root));
                    stage.showAndWait();
                } catch (IOException er) {
                    er.printStackTrace();
                }
            }
        });
    }

    private void logIn(Sender sender) {
        Request request = new Request(login_auth.getText().trim(), "login", login_auth.getText().trim(), login_pass.getText().trim());
        if (!login_auth.getText().trim().equals("") && !login_pass.getText().trim().equals("")) {
            sender.send(request);
            while (true) {
                System.out.print("");
                if (status.equals("AllFine")) {
                    Main.Username = request.getUserName();
                    login_auth.getScene().getWindow().hide();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"), ResourceBundle.getBundle("Locale_ru", Locale.forLanguageTag("ru")));
                    Parent root;
                    try{
                        root = loader.load();
                        Stage stage = new Stage();
                        stage.setTitle("Мурмурмур");
                        stage.setResizable(true);
                        stage.sizeToScene();
                        stage.setOnCloseRequest(t -> {
                            sender.send(new Request(login_auth.getText().trim(), "exit", login_auth.getText().trim(), login_pass.getText().trim()));
                            System.exit(0);
                        });

                        stage.setScene(new Scene(root));
                        MainController.sender = sender;
                        stage.showAndWait();

                    }catch (IOException e) {
                        e.printStackTrace();
                        Main.setError("Произошла ошибка запуска основного окна.", "Перезапустите приложение и повторите попытку. \n"+e.getMessage() + "\n" + e.getStackTrace());
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Error.fxml"));
                        try {
                            root = fxmlLoader.load();
                            Stage stage = new Stage();
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.setOpacity(1);
                            stage.setTitle("Ошибка!");
                            stage.setScene(new Scene(root));
                            stage.showAndWait();
                        } catch (IOException er) {
                            er.printStackTrace();
                        }
                    }
                    break;
                } else if (status.equals("AlreadyInUse")) {
                    Main.setError("Вашим аккаунтом кто-то \nуже пользуется", "Если вы об этом не знаете, \nобратитесь в поддержку");
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Error.fxml"));
                    try {
                        Parent root = fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setOpacity(1);
                        stage.setTitle("Ошибка!");
                        stage.setScene(new Scene(root));
                        stage.showAndWait();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    status = "Nothing Yet";
                    break;
                } else if (status.equals("WrongPassword")) {
                    Main.setError("Вы ввели неверный пароль", "Попробуйте ввести его снова");
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Error.fxml"));
                    try {
                        Parent root = fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setOpacity(1);
                        stage.setTitle("Ошибка!");
                        stage.setScene(new Scene(root));
                        MainController.sender = sender;
                        stage.showAndWait();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    status = "Nothing Yet";
                    break;
                }
            }
        }
    }

    private void auth(Sender sender) {
        Request request = new Request(login_registr.getText().trim(), "auth", login_registr.getText().trim(), pass_registr.getText().trim());
        if (login_registr.getText().length() < 5) {
            Main.setError("Ваш логин слишком короткий", "Попробуйте ввести другой логин");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Error.fxml"));
            try {
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setOpacity(1);
                stage.setTitle("Ошибка!");
                stage.setScene(new Scene(root));
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (pass_registr.getText().length() < 5) {
                Main.setError("Ваш пароль слишком короткий", "Попробуйте ввести другой пароль");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Error.fxml"));
                try {
                    Parent root = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setOpacity(1);
                    stage.setTitle("Ошибка!");
                    stage.setScene(new Scene(root));
                    stage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                sender.send(request);
                while (true) {
                    System.out.print("");
                    if (status.equals("AllFine")) {
                        Main.Username = request.getUserName();
                        login_auth.getScene().getWindow().hide();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"), ResourceBundle.getBundle("Locale_ru", Locale.forLanguageTag("ru")));
                        Parent root;
                        try{
                            root = loader.load();
                            Stage stage = new Stage();
                            stage.setTitle("Мурмурмур");
                            stage.setResizable(true);
                            stage.sizeToScene();
                            stage.setOnCloseRequest(t -> {
                                sender.send(new Request(login_registr.getText().trim(), "exit", login_registr.getText().trim(), pass_registr.getText().trim()));
                                System.exit(0);
                            });
                            stage.setScene(new Scene(root));
                            MainController.sender =sender;

                            stage.showAndWait();

                        }catch (IOException e) {
                            System.out.println(e.getMessage());
                            Main.setError("Произошла ошибка запуска основного окна.", "Перезапустите приложение и повторите попытку.");
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Error.fxml"));
                            try {
                                root = fxmlLoader.load();
                                Stage stage = new Stage();
                                stage.initModality(Modality.APPLICATION_MODAL);
                                stage.setOpacity(1);
                                stage.setTitle("Ошибка!");
                                stage.setScene(new Scene(root));
                                stage.showAndWait();
                            } catch (IOException er) {
                                er.printStackTrace();
                            }
                        }
                        break;
                    } else if (status.equals("AlreadyInUse")) {
                        Main.setError("Данный логин уже занят", "Попробуйте ввести другой логин");
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Error.fxml"));
                        try {
                            Parent root = fxmlLoader.load();
                            Stage stage = new Stage();
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.setOpacity(1);
                            stage.setTitle("Ошибка!");
                            stage.setScene(new Scene(root));
                            stage.showAndWait();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        status = "Nothing Yet";
                        break;
                    }
                }
            }
        }
    }
}
