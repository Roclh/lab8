package sample;

import com.classes.CommandTranslator;
import com.classes.Listener;
import com.classes.Sender;
import com.classes.Terminal;
import com.classes.serverSide.answers.Request;
import com.enums.Country;
import com.enums.EyeColor;
import com.enums.HairColor;
import com.wrappers.Coordinates;
import com.wrappers.Location;
import com.wrappers.Person;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MainController {
    private ObservableList<Person> masterData = FXCollections.observableArrayList();
    public static String PS = "default@мурмурмур: ";
    public static Sender sender;
    public static List<Color> colors = new ArrayList<>();
    private static HashMap<Person, Integer> userId = new HashMap<>();

    private double x_center = 0;
    private double y_center = 0;
    private double xOffset = 0;
    private double yOffset = 0;

    private ArrayList<Float> distinct = new ArrayList<>();

    ContextMenu contextMenu = new ContextMenu();

    MenuItem deleteItem = new MenuItem(Main.resourceBundle.getString("delete"));
    MenuItem updateItem = new MenuItem(Main.resourceBundle.getString("update"));

    @FXML
    private TextField filter;

    @FXML
    private TableView<Person> MainTable;

    @FXML
    private TableColumn<Person, String> ownerColumn;

    @FXML
    private TableColumn<Person, Long> idColumn;

    @FXML
    private TableColumn<Person, String> nameColumn;

    @FXML
    private TableColumn<Person, LocalDateTime> createColumn;

    @FXML
    private TableColumn<Person, Coordinates> coordColumn;

    @FXML
    private TableColumn<Person, Float> heightColumn;

    @FXML
    private TableColumn<Person, EyeColor> eyeColumn;

    @FXML
    private TableColumn<Person, HairColor> haircolorColumn;

    @FXML
    private TableColumn<Person, Country> nationalityColumn;

    @FXML
    private TableColumn<Person, Location> locationColumn;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Pane TablePane;

    @FXML
    private Button Add;


    @FXML
    private Button Info;


    @FXML
    private Button Clear;


    @FXML
    private Button AddIfMin;

    @FXML
    private Canvas canvas;


    @FXML
    private Button History;

    @FXML
    private Button CountByHairColor;


    @FXML
    private Tab ConsoleTab;

    @FXML
    private TextArea Console;

    @FXML
    private Tab TableTab;

    @FXML
    private Tab ViewTab;

    @FXML
    private SplitMenuButton settings;

    @FXML
    private MenuItem rus;

    @FXML
    private MenuItem fin;

    @FXML
    private MenuItem itl;

    @FXML
    private MenuItem eng;

    @FXML
    private MenuItem exit;

    @FXML
    private MenuItem help;

    @FXML
    private CheckBox print_unique_height;

    @FXML
    private Button center;

    public static ConcurrentLinkedQueue<Person> persons = new ConcurrentLinkedQueue<>();
    private FilteredList<Person> filteredList;
    private SimpleDoubleProperty opacity;
    private SimpleDoubleProperty koef;

    private AnimationTimer timer;
    private Timeline timeline;

    @FXML
    void initialize() {

        initiateTable();
        initiateConsole();
        initiateButtons();
        initiateView();
        initiateSettings();

    }


    private void initiateConsole() {
        PS = Main.Username + "@мурмурмур: ";
        Console.setText(PS);
        Console.setEditable(false);

        Console.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                if (Terminal.checkKey(event.getCharacter())) {
                    Console.setText(Console.getText() + event.getCharacter());
                }
            }
        });
        Console.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.BACK_SPACE)) {
                    Console.setText(Console.getText(0, Console.getLength() - 1));
                }
                if (event.getCode().equals(KeyCode.ENTER)) {
                    Console.setText(Console.getText() + "\n" + PS);
                }
            }
        });




    }

    private void initiateButtons() {
        Add.setOnAction(event -> {
            AddMenuController.isEditable(true);
            AddMenuController.command = "add";
            AddMenuController.sender = sender;
            AddMenuController.emptyValues();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddMenu.fxml"),Main.resourceBundle);
            try {
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setOpacity(1);
                stage.setTitle("Add");
                stage.setScene(new Scene(root));
                stage.setOnHiding(we -> {
                    Terminal.sleep(300);
                    buildData();
                });
                stage.showAndWait();


            } catch (IOException e) {
                Main.setError(Main.resourceBundle.getString("addnewpersonerror"), Main.resourceBundle.getString("tryagain")+"\n\n" + Arrays.toString(e.getStackTrace()));
                FXMLLoader fxmlLoaderer = new FXMLLoader(getClass().getResource("Error.fxml"),Main.resourceBundle);
                try {
                    Parent root = fxmlLoaderer.load();
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setOpacity(1);
                    stage.setTitle(Main.resourceBundle.getString("error"));
                    stage.setScene(new Scene(root));
                    stage.showAndWait();
                } catch (IOException er) {
                    er.printStackTrace();
                }
            }
        });

        AddIfMin.setOnAction(event -> {
            AddMenuController.isEditable(true);
            AddMenuController.command = "min";
            AddMenuController.sender = sender;
            AddMenuController.emptyValues();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddMenu.fxml"),Main.resourceBundle);
            try {
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setOpacity(1);
                stage.setTitle("AddIfMin");
                stage.setScene(new Scene(root));
                stage.setOnHiding(we -> {
                    Terminal.sleep(300);
                    buildData();
                });
                stage.showAndWait();


            } catch (IOException e) {
                Main.setError(Main.resourceBundle.getString("addnewpersonerror"), Main.resourceBundle.getString("tryagain")+"\n\n" + Arrays.toString(e.getStackTrace()));
                FXMLLoader fxmlLoaderer = new FXMLLoader(getClass().getResource("Error.fxml"),Main.resourceBundle);
                try {
                    Parent root = fxmlLoaderer.load();
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setOpacity(1);
                    stage.setTitle(Main.resourceBundle.getString("error"));
                    stage.setScene(new Scene(root));
                    stage.showAndWait();
                } catch (IOException er) {
                    er.printStackTrace();
                }
            }
        });

        Info.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sender.send(new Request(Main.Username, "info", "", ""));
                summonAlert(Main.resourceBundle.getString("collectioncontain") + MainTable.getItems().size() + Main.resourceBundle.getString("objects"), Alert.AlertType.INFORMATION);
            }
        });

        Clear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setTitle(Main.resourceBundle.getString("alert"));
                alert.setContentText((Main.resourceBundle.getString("clearquestion")));
                alert.setHeaderText(Main.resourceBundle.getString("alert"));
                Optional<ButtonType> option = alert.showAndWait();
                if(option.get() == ButtonType.OK){
                    sender.send(new Request(Main.Username, "clear", "", ""));
                    Terminal.sleep(300);
                    buildData();
                } else if(option.get() == ButtonType.CANCEL){
                    alert.close();
                }
            }
        });

        canvas.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(Listener.lastMessage.equals("Данный элемент не является минимальным\n")){
                    summonAlert(Main.resourceBundle.getString("addifminneg"), Alert.AlertType.INFORMATION);
                    Listener.lastMessage = "не является минимальным";
                } else if (Listener.lastMessage.contains("Последние команды:\n")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.setTitle(Main.resourceBundle.getString("history"));

                    TextArea textArea = new TextArea();
                    try {
                        textArea.setText(Listener.lastMessage.substring(Listener.lastMessage.indexOf("\n")+1));
                    }catch (StringIndexOutOfBoundsException e){
                        textArea.setText("Никаких команд пока-что не вводилось.");
                    }

                    alert.getDialogPane().setContent(textArea);

                    alert.setHeaderText(Main.resourceBundle.getString("lastcommands"));
                    alert.showAndWait();
                    Listener.lastMessage = "история";
                }
            }
        });

        Console.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(Listener.lastMessage.equals("Данный элемент не является минимальным\n")){
                    summonAlert(Main.resourceBundle.getString("addifminneg"), Alert.AlertType.INFORMATION);
                    Listener.lastMessage = "не является минимальным";
                } else if (Listener.lastMessage.contains("Последние команды:\n")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.setTitle(Main.resourceBundle.getString("history"));

                    TextArea textArea = new TextArea();
                    try {
                        textArea.setText(Listener.lastMessage.substring(Listener.lastMessage.indexOf("\n")+1));
                    }catch (StringIndexOutOfBoundsException e){
                        textArea.setText("Никаких команд пока-что не вводилось.");
                    }

                    alert.getDialogPane().setContent(textArea);

                    alert.setHeaderText(Main.resourceBundle.getString("lastcommands"));
                    alert.showAndWait();
                    Listener.lastMessage = "история";
                }
            }
        });

        MainTable.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(Listener.lastMessage.equals("Данный элемент не является минимальным\n")){
                    summonAlert(Main.resourceBundle.getString("addifminneg"), Alert.AlertType.INFORMATION);
                    Listener.lastMessage = "не является минимальным";
                } else if (Listener.lastMessage.contains("Последние команды:\n")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.setTitle(Main.resourceBundle.getString("history"));

                    TextArea textArea = new TextArea();
                    try {
                        textArea.setText(Listener.lastMessage.substring(Listener.lastMessage.indexOf("\n")+1));
                    }catch (StringIndexOutOfBoundsException e){
                        textArea.setText("Никаких команд пока-что не вводилось.");
                    }

                    alert.getDialogPane().setContent(textArea);

                    alert.setHeaderText(Main.resourceBundle.getString("lastcommands"));
                    alert.showAndWait();
                    Listener.lastMessage = "история";
                }
            }
        });

        CountByHairColor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sender.send(new Request(Main.Username, "count_by_hair_color", "BLACK", ""));

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setTitle("Цвета волос");

                List<Person> list = MainTable.getItems();

                TextArea textArea = new TextArea();
                textArea.setText("GREEN:" + list.stream().filter(person -> person.getHairColor().equals(HairColor.GREEN)).count() +"\n"+
                        "BLACK:"+list.stream().filter(person -> person.getHairColor().equals(HairColor.BLACK)).count()+"\n" +
                        "PINK:"+list.stream().filter(person -> person.getHairColor().equals(HairColor.PINK)).count()+"\n" +
                        "YELLOW:"+list.stream().filter(person -> person.getHairColor().equals(HairColor.YELLOW)).count()+"\n" +
                        "ORANGE:"+list.stream().filter(person -> person.getHairColor().equals(HairColor.ORANGE)).count()+"\n" +
                        "WHITE:"+list.stream().filter(person -> person.getHairColor().equals(HairColor.WHITE)).count());

                alert.getDialogPane().setContent(textArea);

                alert.setHeaderText("Количество людей с цветами волос:");
                alert.showAndWait();
            }
        });

        History.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sender.send(new Request(Main.Username, "history", "", ""));
            }
        });


    }

    private void initiateTable() {

        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sender.send(new Request(Main.Username, "remove_by_id", String.valueOf(MainTable.getSelectionModel().getSelectedItem().getId()), ""));
                Terminal.sleep(300);
                buildData();
            }
        });

        updateItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AddMenuController.isEditable(true);
                AddMenuController.sender = sender;
                AddMenuController.id = MainTable.getSelectionModel().getSelectedItem().getId();
                AddMenuController.command = "update";
                AddMenuController.updateValues(MainTable.getSelectionModel().getSelectedItem());
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddMenu.fxml"),Main.resourceBundle);
                try {
                    Parent root = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setOpacity(1);
                    stage.setTitle("Update");
                    stage.setScene(new Scene(root));
                    stage.setOnCloseRequest(we -> buildData());
                    stage.setOnHiding(we -> {
                        Terminal.sleep(600);
                        buildData();
                    });
                    stage.showAndWait();


                } catch (IOException e) {
                    Main.setError(Main.resourceBundle.getString("addnewpersonerror"), Main.resourceBundle.getString("tryagain") +"\n\n" + e.getMessage());
                    FXMLLoader fxmlLoaderer = new FXMLLoader(getClass().getResource("Error.fxml"),Main.resourceBundle);
                    try {
                        Parent root = fxmlLoaderer.load();
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
            }
        });

        contextMenu.getItems().addAll(updateItem, deleteItem);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("owner"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        createColumn.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        coordColumn.setCellValueFactory(new PropertyValueFactory<>("coordinates"));
        heightColumn.setCellValueFactory(new PropertyValueFactory<>("height"));
        eyeColumn.setCellValueFactory(new PropertyValueFactory<>("eyeColor"));
        haircolorColumn.setCellValueFactory(new PropertyValueFactory<>("hairColor"));
        nationalityColumn.setCellValueFactory(new PropertyValueFactory<>("nationality"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        setFactories();
        filteredList = new FilteredList<>(masterData, p -> true);

        filter.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredList.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                return String.valueOf(person.getName()).toLowerCase().contains(lowerCaseFilter);
            });
        }));


        print_unique_height.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(print_unique_height.isSelected()){
                    sender.send(new Request(Main.Username, "print_unique_height", "", ""));
                }
            }
        });

        print_unique_height.selectedProperty().addListener((observable) -> {
            filteredList.setPredicate(person -> {
                if (print_unique_height.isSelected()) {
                    if (!distinct.contains(person.getHeight())) {
                        distinct.add(person.getHeight());
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    distinct.clear();
                    return true;
                }
            });
        });

        buildData();


    }

    private void initiateView() {
        colors.add(new Color(Color.GREEN.getRed(), Color.GREEN.getGreen(), Color.GREEN.getBlue(), 0.5f));
        colors.add(new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), 0.5f));
        colors.add(new Color(Color.ORANGE.getRed(), Color.ORANGE.getGreen(), Color.ORANGE.getBlue(), 0.5f));
        colors.add(new Color(Color.HOTPINK.getRed(), Color.HOTPINK.getGreen(), Color.HOTPINK.getBlue(), 0.5f));
        colors.add(new Color(Color.AQUA.getRed(), Color.AQUA.getGreen(), Color.AQUA.getBlue(), 0.5f));
        colors.add(new Color(Color.SILVER.getRed(), Color.SILVER.getGreen(), Color.SILVER.getBlue(), 0.5f));
        colors.add(new Color(Color.CHOCOLATE.getRed(), Color.CHOCOLATE.getGreen(), Color.CHOCOLATE.getBlue(), 0.5f));

        canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = x_center - event.getX(); //
                yOffset = y_center - event.getY(); //

            }
        });

        canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                x_center = event.getX() + xOffset;
                y_center = event.getY() + yOffset;
                draw();
            }
        });

        center.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                x_center = 0;
                y_center = 0;
                draw();
            }
        });

        opacity  = new SimpleDoubleProperty();
        koef  = new SimpleDoubleProperty();

        timeline = new Timeline(

                new KeyFrame(Duration.seconds(0),
                        new KeyValue(opacity, 0),
                        new KeyValue(koef, 0.3)
                ),
                new KeyFrame(Duration.seconds(2),
                        new KeyValue(opacity, 0.5),
                        new KeyValue(koef, 1.0)
                )

        );

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                //buildDataWithProp();
                draw(opacity, koef);
            }
        };

    }

    public void startDraw(Event event) {
        timer.start();
        timeline.play();

    }

    public void drawPerson(int x, int y, int size, int color) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(colors.get(color));
        gc.fillOval(x + x_center, y + y_center, size, size);


    }

    public void drawPerson(int x, int y, int size, int color, DoubleProperty op, DoubleProperty koef){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(colors.get(color).deriveColor(0, 1, 1, op.get()));
        gc.fillOval(x + x_center, y + y_center, koef.doubleValue() * size, koef.doubleValue() * size);
    }

    public void draw(SimpleDoubleProperty op,SimpleDoubleProperty ko){
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        userId.forEach((person, integer) -> {
            int color_owner_id = integer;
            int x = person.getCoordinates().getX().intValue();
            int y = (int) person.getCoordinates().getY();
            int size = (int) (person.getHeight() / 2);
            drawPerson(x, y, size, color_owner_id, op, ko);
        });
    }

    public void draw() {
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        userId.forEach((person, integer) -> {
            int color_owner_id = integer;
            int x = person.getCoordinates().getX().intValue();
            int y = (int) person.getCoordinates().getY();
            int size = (int) (person.getHeight() / 2);
            drawPerson(x, y, size, color_owner_id);
        });

    }

    public void buildData() {
        masterData.clear();
        userId.clear();
        persons = CommandTranslator.getData();
        masterData = FXCollections.observableArrayList();
        masterData.addAll(persons);
        for (Person person : masterData) {
            userId.put(person, person.getOwner().hashCode() % 7);
        }
        filteredList = new FilteredList<>(masterData, p -> true);
        SortedList<Person> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(MainTable.comparatorProperty());
        MainTable.setItems(sortedList);
    }

    private void setFactories() {
        createColumn.setCellFactory(column -> {
            TableCell cell = new TableCell<Person, LocalDateTime>() {
                private final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd/MM/yy");


                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        Date out = Date.from(item.atZone(ZoneId.systemDefault()).toInstant());
                        setText(format.format(out));
                    }
                }
            };
            cell.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                @Override
                public void handle(ContextMenuEvent event) {
                    if (MainTable.getSelectionModel().getSelectedItem().getOwner().equals(Main.Username)) {
                        deleteItem.setDisable(false);
                        updateItem.setDisable(false);
                        contextMenu.show(cell, event.getScreenX(), event.getScreenY());
                    } else {
                        deleteItem.setDisable(true);
                        updateItem.setDisable(true);
                        contextMenu.show(cell, event.getScreenX(), event.getScreenY());
                    }


                }
            });
            return cell;
        });

        idColumn.setCellFactory(column -> {
            TableCell cell = new TableCell<Person, Long>() {
                @Override
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
            cell.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                @Override
                public void handle(ContextMenuEvent event) {
                    if (MainTable.getSelectionModel().getSelectedItem().getOwner().equals(Main.Username)) {
                        deleteItem.setDisable(false);
                        updateItem.setDisable(false);
                        contextMenu.show(cell, event.getScreenX(), event.getScreenY());
                    } else {
                        deleteItem.setDisable(true);
                        updateItem.setDisable(true);
                        contextMenu.show(cell, event.getScreenX(), event.getScreenY());
                    }
                }
            });
            return cell;
        });
        ownerColumn.setCellFactory(column -> {
            TableCell cell = new TableCell<Person, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item);
                    }
                }
            };
            cell.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                @Override
                public void handle(ContextMenuEvent event) {
                    if (MainTable.getSelectionModel().getSelectedItem().getOwner().equals(Main.Username)) {
                        deleteItem.setDisable(false);
                        updateItem.setDisable(false);
                        contextMenu.show(cell, event.getScreenX(), event.getScreenY());
                    } else {
                        deleteItem.setDisable(true);
                        updateItem.setDisable(true);
                        contextMenu.show(cell, event.getScreenX(), event.getScreenY());
                    }
                }
            });
            return cell;
        });
        nameColumn.setCellFactory(column -> {
            TableCell cell = new TableCell<Person, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item);
                    }
                }
            };
            cell.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                @Override
                public void handle(ContextMenuEvent event) {
                    if (MainTable.getSelectionModel().getSelectedItem().getOwner().equals(Main.Username)) {
                        deleteItem.setDisable(false);
                        updateItem.setDisable(false);
                        contextMenu.show(cell, event.getScreenX(), event.getScreenY());
                    } else {
                        deleteItem.setDisable(true);
                        updateItem.setDisable(true);
                        contextMenu.show(cell, event.getScreenX(), event.getScreenY());
                    }
                }
            });
            return cell;
        });
        coordColumn.setCellFactory(column -> {
            TableCell cell = new TableCell<Person, Coordinates>() {
                @Override
                protected void updateItem(Coordinates item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
            cell.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                @Override
                public void handle(ContextMenuEvent event) {
                    if (MainTable.getSelectionModel().getSelectedItem().getOwner().equals(Main.Username)) {
                        deleteItem.setDisable(false);
                        updateItem.setDisable(false);
                        contextMenu.show(cell, event.getScreenX(), event.getScreenY());
                    } else {
                        deleteItem.setDisable(true);
                        updateItem.setDisable(true);
                        contextMenu.show(cell, event.getScreenX(), event.getScreenY());
                    }
                }
            });
            return cell;
        });
        heightColumn.setCellFactory(column -> {
            TableCell cell = new TableCell<Person, Float>() {
                @Override
                protected void updateItem(Float item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
            cell.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                @Override
                public void handle(ContextMenuEvent event) {
                    if (MainTable.getSelectionModel().getSelectedItem().getOwner().equals(Main.Username)) {
                        deleteItem.setDisable(false);
                        updateItem.setDisable(false);
                        contextMenu.show(cell, event.getScreenX(), event.getScreenY());
                    } else {
                        deleteItem.setDisable(true);
                        updateItem.setDisable(true);
                        contextMenu.show(cell, event.getScreenX(), event.getScreenY());
                    }
                }
            });
            return cell;
        });
        eyeColumn.setCellFactory(column -> {
            TableCell cell = new TableCell<Person, EyeColor>() {
                @Override
                protected void updateItem(EyeColor item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
            cell.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                @Override
                public void handle(ContextMenuEvent event) {
                    if (MainTable.getSelectionModel().getSelectedItem().getOwner().equals(Main.Username)) {
                        deleteItem.setDisable(false);
                        updateItem.setDisable(false);
                        contextMenu.show(cell, event.getScreenX(), event.getScreenY());
                    } else {
                        deleteItem.setDisable(true);
                        updateItem.setDisable(true);
                        contextMenu.show(cell, event.getScreenX(), event.getScreenY());
                    }

                }
            });
            return cell;
        });
        haircolorColumn.setCellFactory(column -> {
            TableCell cell = new TableCell<Person, HairColor>() {
                @Override
                protected void updateItem(HairColor item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
            cell.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                @Override
                public void handle(ContextMenuEvent event) {
                    if (MainTable.getSelectionModel().getSelectedItem().getOwner().equals(Main.Username)) {
                        deleteItem.setDisable(false);
                        updateItem.setDisable(false);
                        contextMenu.show(cell, event.getScreenX(), event.getScreenY());
                    } else {
                        deleteItem.setDisable(true);
                        updateItem.setDisable(true);
                        contextMenu.show(cell, event.getScreenX(), event.getScreenY());
                    }
                }
            });
            return cell;
        });
        nationalityColumn.setCellFactory(column -> {
            TableCell cell = new TableCell<Person, Country>() {
                @Override
                protected void updateItem(Country item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
            cell.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                @Override
                public void handle(ContextMenuEvent event) {
                    if (MainTable.getSelectionModel().getSelectedItem().getOwner().equals(Main.Username)) {
                        deleteItem.setDisable(false);
                        updateItem.setDisable(false);
                        contextMenu.show(cell, event.getScreenX(), event.getScreenY());
                    } else {
                        deleteItem.setDisable(true);
                        updateItem.setDisable(true);
                        contextMenu.show(cell, event.getScreenX(), event.getScreenY());
                    }
                }
            });
            return cell;
        });
        locationColumn.setCellFactory(column -> {
            TableCell cell = new TableCell<Person, Location>() {
                @Override
                protected void updateItem(Location item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item.toString());
                    }
                }
            };
            cell.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                @Override
                public void handle(ContextMenuEvent event) {
                    if (MainTable.getSelectionModel().getSelectedItem().getOwner().equals(Main.Username)) {
                        deleteItem.setDisable(false);
                        updateItem.setDisable(false);
                        contextMenu.show(cell, event.getScreenX(), event.getScreenY());
                    } else {
                        deleteItem.setDisable(true);
                        updateItem.setDisable(true);
                        contextMenu.show(cell, event.getScreenX(), event.getScreenY());
                    }
                }
            });
            return cell;
        });

    }

    private void initiateSettings() {

        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sender.send(new Request(Main.Username, "exit", Main.Username, ""));
                System.exit(0);
            }
        });


        help.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sender.send(new Request(Main.Username, "help", "", ""));
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Help.fxml"),Main.resourceBundle);
                Parent root;
                try{
                    root = loader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Помощь");
                    stage.setResizable(true);
                    stage.sizeToScene();
                    stage.setOnCloseRequest(t -> {
                        stage.close();
                    });
                    stage.setScene(new Scene(root));
                    stage.showAndWait();

                }catch (IOException e) {
                    Main.setError("Произошла ошибка запуска основного окна.", "Перезапустите приложение и повторите попытку.");
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Error.fxml"),Main.resourceBundle);
                    try {
                        root = fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setOpacity(1);
                        stage.setTitle(Main.resourceBundle.getString("error"));
                        stage.setScene(new Scene(root));
                        stage.showAndWait();
                    } catch (IOException er) {
                        er.printStackTrace();
                    }
                }
            }
        });
    }

    public void showPerson(MouseEvent mouseEvent) {
        double mouse_x = mouseEvent.getX();
        double mouse_y = mouseEvent.getY();
        for (Person person : persons) {
            int x = person.getCoordinates().getX().intValue();
            int y = (int) person.getCoordinates().getY();
            int size = (int) (person.getHeight() / 2);
            double offset = System.currentTimeMillis() / 300.;

            if (mouse_x > x + x_center - size && mouse_x < x + x_center + size && mouse_y > y + y_center - Math.sin(size - (mouse_x - x)) * size && mouse_y < y + y_center + Math.sin(offset) * size) {
                if (person.getOwner().equals(Main.Username)) {
                    try {
                        AddMenuController.isEditable(true);
                        AddMenuController.sender = sender;
                        AddMenuController.command = "update";
                        AddMenuController.id = person.getId();
                        AddMenuController.updateValues(person);
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddMenu.fxml"),Main.resourceBundle);
                        Parent root = fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setOpacity(1);
                        stage.setTitle("Update");
                        stage.setScene(new Scene(root));
                        stage.setOnCloseRequest(we -> buildData());
                        stage.setOnHiding(we -> {
                            Terminal.sleep(600);
                            buildData();
                        });
                        stage.showAndWait();
                        break;
                    } catch (IOException e) {
                        Main.setError(Main.resourceBundle.getString("addnewpersonerror"), Main.resourceBundle.getString("tryagain")+"\n\n" + e.getMessage());
                        FXMLLoader fxmlLoaderer = new FXMLLoader(getClass().getResource("Error.fxml"),Main.resourceBundle);
                        try {
                            Parent root = fxmlLoaderer.load();
                            Stage stage = new Stage();
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.setOpacity(1);
                            stage.setTitle(Main.resourceBundle.getString("error"));
                            stage.setScene(new Scene(root));
                            stage.showAndWait();
                        } catch (IOException er) {
                            er.printStackTrace();
                        }
                    }
                } else {
                    try {
                        AddMenuController.isEditable(false);
                        AddMenuController.sender = sender;
                        AddMenuController.command = "update";
                        AddMenuController.id = person.getId();
                        AddMenuController.updateValues(person);
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddMenu.fxml"),Main.resourceBundle);
                        Parent root = fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setOpacity(1);
                        stage.setTitle("Update");
                        stage.setScene(new Scene(root));
                        stage.setOnCloseRequest(we -> buildData());
                        stage.setOnHiding(we -> {
                            Terminal.sleep(600);
                            buildData();
                        });
                        stage.showAndWait();
                        break;
                    } catch (IOException e) {
                        Main.setError(Main.resourceBundle.getString("addnewpersonerror"),  Main.resourceBundle.getString("tryagain")+"\n\n" + e.getMessage());
                        FXMLLoader fxmlLoaderer = new FXMLLoader(getClass().getResource("Error.fxml"),Main.resourceBundle);
                        try {
                            Parent root = fxmlLoaderer.load();
                            Stage stage = new Stage();
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.setOpacity(1);
                            stage.setTitle(Main.resourceBundle.getString("error"));
                            stage.setScene(new Scene(root));
                            stage.showAndWait();
                        } catch (IOException er) {
                            er.printStackTrace();
                        }
                    }
                }


            }
        }
    }

    public static void summonAlert(String text, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle(Main.resourceBundle.getString("alert"));
        alert.setContentText(text);
        alert.setHeaderText(Main.resourceBundle.getString("alert"));
        alert.showAndWait();
    }

    public void setRus(Event event){
        Main.resourceBundle = ResourceBundle.getBundle("Locale_ru", Locale.forLanguageTag("ru"));
        Scene scene = Add.getScene();
        try{
            scene.setRoot(FXMLLoader.load(getClass().getResource("main.fxml"), Main.resourceBundle));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setEng(Event event){
        Main.resourceBundle = ResourceBundle.getBundle("Locale_en", Locale.forLanguageTag("en"));
        Scene scene = Add.getScene();
        try{
            scene.setRoot(FXMLLoader.load(getClass().getResource("main.fxml"), Main.resourceBundle));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFin(Event event){
        Main.resourceBundle = ResourceBundle.getBundle("Locale_fin", Locale.forLanguageTag("fin"));
        Scene scene = Add.getScene();
        try{
            scene.setRoot(FXMLLoader.load(getClass().getResource("main.fxml"), Main.resourceBundle));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setItl(Event event){
        Main.resourceBundle = ResourceBundle.getBundle("Locale_itl", Locale.forLanguageTag("itl"));
        Scene scene = Add.getScene();
        try{
            scene.setRoot(FXMLLoader.load(getClass().getResource("main.fxml"), Main.resourceBundle));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




