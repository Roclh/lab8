package sample;

import com.classes.CommandTranslator;
import com.classes.Sender;
import com.classes.serverSide.answers.Request;
import com.enums.Country;
import com.enums.EyeColor;
import com.enums.HairColor;
import com.wrappers.Coordinates;
import com.wrappers.Location;
import com.wrappers.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.naming.Name;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class AddMenuController {

    public static Sender sender;
    public static String command;
    public static Long id;
    private static boolean editable = true;

    public static String NameFieldText = "";
    public static String HeightFieldText = "";
    public static String ChoiceEyeColorText = "";
    public static String ChoiceHairColorText = "";
    public static String ChoiceNationalityText = "";
    public static Coordinates ChoiceCoordinatesText = new Coordinates(0L, 0);
    public static Location ChoiceLocationText = new Location(0, 0f, 0f);

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField NameField;

    @FXML
    private TextField HeightField;

    @FXML
    private ComboBox<String> ChoiceEyeColor;

    @FXML
    private ComboBox<String> ChoiceHairColor;

    @FXML
    private ComboBox<String> ChoiceNationality;

    @FXML
    private TextField CoordinateX;

    @FXML
    private TextField CoordinateY;

    @FXML
    private TextField LocationX;

    @FXML
    private TextField LocationY;

    @FXML
    private TextField LocationZ;

    @FXML
    private Button AddButton;

    @FXML
    void initialize() {
        initiateChoiceBoxes();
        initiateButton();
    }

    private void initiateChoiceBoxes(){
        ObservableList<String> hairColors;
        if(editable) {
            hairColors = FXCollections.observableArrayList("GREEN", "BLACK", "PINK", "YELLOW", "ORANGE", "WHITE");
            ChoiceHairColor.setItems(hairColors);
        }

        if(ChoiceHairColorText.isEmpty()){
            ChoiceHairColor.setValue(Main.resourceBundle.getString("haircolor"));
        }else{
            ChoiceHairColor.setValue(ChoiceHairColorText);
        }
        if(editable) {
            ObservableList<String> eyeColors = FXCollections.observableArrayList("RED", "BLUE", "YELLOW");
            ChoiceEyeColor.setItems(eyeColors);
        }
        if(ChoiceEyeColorText.isEmpty()){
            ChoiceEyeColor.setValue(Main.resourceBundle.getString("eyecolor"));
        }else{
            ChoiceEyeColor.setValue(ChoiceEyeColorText);
        }

        if(editable){
            ObservableList<String> nationality = FXCollections.observableArrayList("INDIA", "VATICAN", "NORTH_AMERICA", "JAPAN");
            ChoiceNationality.setItems(nationality);
        }
        if(ChoiceNationalityText.isEmpty()){
            ChoiceNationality.setValue(Main.resourceBundle.getString("country"));
        }else{
            ChoiceNationality.setValue(ChoiceNationalityText);
        }

        NameField.setText(NameFieldText);
        HeightField.setText(HeightFieldText);
        CoordinateX.setText(ChoiceCoordinatesText.getX().toString());
        CoordinateY.setText(String.valueOf(ChoiceCoordinatesText.getY()));
        LocationX.setText(ChoiceLocationText.getX().toString());
        LocationY.setText(ChoiceLocationText.getY().toString());
        LocationZ.setText(ChoiceLocationText.getZ().toString());

        NameField.setEditable(editable);
        HeightField.setEditable(editable);
        CoordinateX.setEditable(editable);
        CoordinateY.setEditable(editable);
        LocationX.setEditable(editable);
        LocationY.setEditable(editable);
        LocationZ.setEditable(editable);

    }

    private void initiateButton() {
        if (command.equals("add")) {
            AddButton.setOnAction(event -> {
                if (ChoiceHairColor.getValue().equals(Main.resourceBundle.getString("haircolor")) || ChoiceEyeColor.getValue().equals(Main.resourceBundle.getString("eyecolor")) || ChoiceNationality.getValue().equals(Main.resourceBundle.getString("country")) ||
                        NameField.getText().isEmpty() || HeightField.getText().isEmpty() || CoordinateX.getText().isEmpty() || CoordinateY.getText().isEmpty() ||
                        LocationX.getText().isEmpty() || LocationY.getText().isEmpty() || LocationZ.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initStyle(StageStyle.UNDECORATED);
                    alert.setTitle("Внимание!");
                    alert.setContentText("Вы не выбрали некоторые параметры");
                    alert.setHeaderText("Внимение!");
                    alert.showAndWait();
                } else
                    try {
                        sender.send(new Request(Main.Username, "add", CommandTranslator.translatePerson(new Person(Main.Username, NameField.getText().trim(),
                                new Coordinates(Long.parseLong(CoordinateX.getText().trim()), Float.parseFloat(CoordinateY.getText().trim())),
                                Float.parseFloat(HeightField.getText().trim()), EyeColor.valueOf(ChoiceEyeColor.getValue()), HairColor.valueOf(ChoiceHairColor.getValue()),
                                Country.valueOf(ChoiceNationality.getValue()), new Location(Integer.parseInt(LocationX.getText().trim()), Float.parseFloat(LocationY.getText().trim()),
                                Float.parseFloat(LocationZ.getText().trim())))), ""));
                        Stage stage = (Stage) AddButton.getScene().getWindow();
                        stage.close();
                    } catch (NumberFormatException e) {
                        Main.setError("Вы ввели неверные данные. Проверьте значения полей", e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Error.fxml"));
                        try {
                            Parent root = fxmlLoader.load();
                            Stage stage = new Stage();
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.setOpacity(1);
                            stage.setTitle("Ошибка!");
                            stage.setScene(new Scene(root));
                            stage.showAndWait();
                        } catch (IOException er) {
                            e.printStackTrace();
                        }
                    }

            });

        }else if(command.equals("min")){
            AddButton.setOnAction(event -> {
                if (ChoiceHairColor.getValue().equals(Main.resourceBundle.getString("haircolor")) || ChoiceEyeColor.getValue().equals(Main.resourceBundle.getString("eyecolor")) || ChoiceNationality.getValue().equals(Main.resourceBundle.getString("country")) ||
                        NameField.getText().isEmpty() || HeightField.getText().isEmpty() || CoordinateX.getText().isEmpty() || CoordinateY.getText().isEmpty() ||
                        LocationX.getText().isEmpty() || LocationY.getText().isEmpty() || LocationZ.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initStyle(StageStyle.UNDECORATED);
                    alert.setTitle("Внимание!");
                    alert.setContentText("Вы не выбрали некоторые параметры");
                    alert.setHeaderText("Внимение!");
                    alert.showAndWait();
                } else
                    try {
                        sender.send(new Request(Main.Username, "add_if_min", CommandTranslator.translatePerson(new Person(Main.Username, NameField.getText().trim(),
                                new Coordinates(Long.parseLong(CoordinateX.getText().trim()), Float.parseFloat(CoordinateY.getText().trim())),
                                Float.parseFloat(HeightField.getText().trim()), EyeColor.valueOf(ChoiceEyeColor.getValue()), HairColor.valueOf(ChoiceHairColor.getValue()),
                                Country.valueOf(ChoiceNationality.getValue()), new Location(Integer.parseInt(LocationX.getText().trim()), Float.parseFloat(LocationY.getText().trim()),
                                Float.parseFloat(LocationZ.getText().trim())))), ""));
                        Stage stage = (Stage) AddButton.getScene().getWindow();
                        stage.close();
                    } catch (NumberFormatException e) {
                        Main.setError("Вы ввели неверные данные. Проверьте значения полей", e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Error.fxml"));
                        try {
                            Parent root = fxmlLoader.load();
                            Stage stage = new Stage();
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.setOpacity(1);
                            stage.setTitle("Ошибка!");
                            stage.setScene(new Scene(root));
                            stage.showAndWait();
                        } catch (IOException er) {
                            e.printStackTrace();
                        }
                    }

            });
        }
        else{
            AddButton.setOnAction(event -> {
                if (ChoiceHairColor.getValue().equals(Main.resourceBundle.getString("haircolor")) || ChoiceEyeColor.getValue().equals(Main.resourceBundle.getString("eyecolor")) || ChoiceNationality.getValue().equals(Main.resourceBundle.getString("country")) ||
                        NameField.getText().isEmpty() || HeightField.getText().isEmpty() || CoordinateX.getText().isEmpty() || CoordinateY.getText().isEmpty() ||
                        LocationX.getText().isEmpty() || LocationY.getText().isEmpty() || LocationZ.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initStyle(StageStyle.UNDECORATED);
                    alert.setTitle("Внимание!");
                    alert.setContentText("Вы не выбрали некоторые параметры");
                    alert.setHeaderText("Внимение!");
                    alert.showAndWait();
                } else
                    try {
                        sender.send(new Request(Main.Username, "update_by_id", id.toString() ,CommandTranslator.translatePerson(new Person(Main.Username, NameField.getText().trim(),
                                new Coordinates(Long.parseLong(CoordinateX.getText().trim()), Float.parseFloat(CoordinateY.getText().trim())),
                                Float.parseFloat(HeightField.getText().trim()), EyeColor.valueOf(ChoiceEyeColor.getValue()), HairColor.valueOf(ChoiceHairColor.getValue()),
                                Country.valueOf(ChoiceNationality.getValue()), new Location(Integer.parseInt(LocationX.getText().trim()), Float.parseFloat(LocationY.getText().trim()),
                                Float.parseFloat(LocationZ.getText().trim()))))));
                        Stage stage = (Stage) AddButton.getScene().getWindow();
                        stage.close();
                    } catch (NumberFormatException e) {
                        Main.setError("Вы ввели неверные данные. Проверьте значения полей", e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Error.fxml"));
                        try {
                            Parent root = fxmlLoader.load();
                            Stage stage = new Stage();
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.setOpacity(1);
                            stage.setTitle("Ошибка!");
                            stage.setScene(new Scene(root));
                            stage.showAndWait();
                        } catch (IOException er) {
                            e.printStackTrace();
                        }
                    }

            });
        }
    }

    public static void updateValues(Person person){
        NameFieldText = person.getName();
        HeightFieldText = person.getHeight().toString();
        ChoiceEyeColorText = person.getEyeColor().toString();
        ChoiceHairColorText = person.getHairColor().toString();
        ChoiceNationalityText = person.getNationality().toString();
        ChoiceCoordinatesText = person.getCoordinates();
        ChoiceLocationText = person.getLocation();
    }

    public static void emptyValues(){
        NameFieldText = "";
        HeightFieldText = "";
        ChoiceEyeColorText = "";
        ChoiceHairColorText = "";
        ChoiceNationalityText = "";
        ChoiceCoordinatesText = new Coordinates(0L, 0);
        ChoiceLocationText = new Location(0, 0f ,0f);
    }

    public static void isEditable(boolean val){
        editable = val;
    }


}
