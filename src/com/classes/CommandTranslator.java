package com.classes;

/**
 @author Roclh

 @version 1.00

 Класс реализует перевод строк команд в нужный формат

 Метод translateCommand() превращает строку в отдельные команду и аргументы. Так как в программе может быть максимум
 3 аргумета, то строчка конвертируется в три отдельных слова и преобразуется в объект класса Command

 @see com.wrappers.Command



 */

import com.enums.Country;
import com.enums.EyeColor;
import com.enums.HairColor;
import com.exceptions.SavePeopleException;
import com.wrappers.Person;
import com.wrappers.UserCommand;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CommandTranslator {


    /**
     * Переводчик команд. Подразумевает, что в программе не должно быть лишних пробелов, но на работу программы это не влияет
     * @param command
     * На вход попадает строка, в которой могут содержаться команда и 2 аргумента
     * @return
     * Возвращает объект класса Command
     */
    public static UserCommand translateCommand(String command) {
        UserCommand translatedCommand;
        if (!command.contains(" ")) {
            translatedCommand = new UserCommand(command, "", "");
        } else if (command.indexOf(" ") == command.lastIndexOf(" ")) {
            translatedCommand = new UserCommand(command.substring(0, command.indexOf(" ")), command.substring(command.indexOf(" ") + 1));
        } else {
            translatedCommand = new UserCommand(command.substring(0, command.indexOf(" ")),
                    command.substring(command.indexOf(" ") + 1, command.indexOf(" ", command.indexOf(" ") + 1)),
                    command.substring(command.indexOf(" ", command.indexOf(" ") + 1) + 1));
        }

        return translatedCommand;
    }

    /**
     * Метод translateArg() преобразует аргумент строки в формате json в объект класса Person
     *     Параметры, которых не было внутри аргумента, создаются автоматически случайным образом
     *     Также метод может реализовать копирование параметров переведенного аргумента в другой объект Person,
     *     если передать нужного Person в метод.
     * @param arg1
     * Аргумент командной строки, который мы преобразуем
     * @return
     * Возвращает объект класса Person, собранный из аргумента командной строки
     * @throws SavePeopleException
     * Может выбрасывать исключение в случае невозможности перевести параметр строки
     */
    public static Person translateArg(String arg1) throws SavePeopleException {
        String buf = arg1;
        Person person = new Person();
        if (!buf.contains("\"name\":") || (buf.indexOf("\"name\":\"") + 9 == buf.indexOf("\";"))) throw new SavePeopleException();;
        if (buf.contains("\"name\":\"")) try {
            person.setName(buf.substring(buf.indexOf("\"name\":\"") + 8, buf.indexOf("\";", buf.indexOf("\"name\":\"") + 8)));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new SavePeopleException();
        }
        if (buf.contains("\"coordinates\"{\"X\":")) {
            try {
                person.getCoordinates().setX(Long.valueOf(buf.substring(buf.indexOf("\"X\":\"") + 5,
                        buf.indexOf("\";", buf.indexOf("\"X\":\"") + 5))));
                person.getCoordinates().setY(Float.parseFloat(buf.substring(buf.indexOf("\"Y\":\"") + 5,
                        buf.indexOf("\";", buf.indexOf("\"Y\":\"") + 5))));
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                throw new SavePeopleException();
            }
        } else {
            person.getCoordinates().setX((long) (Math.random() * 1000 - 500));
            person.getCoordinates().setY((float) Math.random() * 1000f - 500f);
        }
        if (buf.contains("\"height\":\"")) try {
            person.setHeight(Float.parseFloat(buf.substring(
                    buf.indexOf("\"height\":\"") + 10, buf.indexOf("\";",
                            buf.indexOf("\"height\":\"") + 10))));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new SavePeopleException();
        }
        else person.setHeight((float) Math.random() * 70f + 130f);
        if (buf.contains("\"eyeColor\":\"")) try {
            person.setEyeColor(EyeColor.valueOf(
                    buf.substring(buf.indexOf("\"eyeColor\":\"") + 12, buf.indexOf("\";",
                            buf.indexOf("\"eyeColor\":\"") + 12)).toUpperCase()));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new SavePeopleException();
        }
        else {
            person.setEyeColor(EyeColor.getRandomEyeColor());
        }
        if (buf.contains("\"hairColor\":\"")) try {
            person.setHairColor(HairColor.valueOf(
                    buf.substring(buf.indexOf("\"hairColor\":\"") + 13, buf.indexOf("\";", buf.indexOf("\"hairColor\":\"") + 13)).toUpperCase()));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new SavePeopleException();
        }
        else {
            person.setHairColor(HairColor.getRandomHairColor());
        }
        if (buf.contains("\"nationality\":\"")) try {
            person.setNationality(Country.valueOf(
                    buf.substring(buf.indexOf("\"nationality\":\"") + 15, buf.indexOf("\";", buf.indexOf("\"nationality\":\"") + 15)).toUpperCase()));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new SavePeopleException();
        }
        else {
            person.setNationality(Country.getRandomCountry());
        }
        if (buf.contains("\"location\"{\"X\":")) {
            try {
                person.getLocation().setX(Integer.valueOf(buf.substring(buf.lastIndexOf("{\"X\":\"") + 6, buf.indexOf("\";", buf.lastIndexOf("{\"X\":\"") + 6))));
                person.getLocation().setY(Float.valueOf(buf.substring(buf.lastIndexOf("\"Y\":\"") + 5, buf.indexOf("\";", buf.lastIndexOf("\"Y\":\"") + 5))));
                person.getLocation().setZ(Float.valueOf(buf.substring(buf.lastIndexOf("\"Z\":\"") + 5, buf.indexOf("\"}", buf.lastIndexOf("\"Z\":\"") + 5))));
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                throw new SavePeopleException();
            }
        } else {
            person.getLocation().setX((int) (Math.random() * 100 - 50));
            person.getLocation().setY((float) (Math.random() * 100 - 50));
            person.getLocation().setZ((float) (Math.random() * 100 - 50));
        }
        return person;
    }

    /**
     *  Такой-же метод, как и предыдущий, только запись идет в конкретного Person, который уже имеет какие-то значения
     *
     * @param person
     * Параметр, который содержит в себе объект типа Person, параметры которого нужно изменить
     */
    public static Person translateArg(String arg1, Person person) throws SavePeopleException {
        String buf = arg1;
        if (buf.contains("\"name\":\"")) try {
            person.setName(buf.substring(buf.indexOf("\"name\":\"") + 8, buf.indexOf("\";", buf.indexOf("\"name\":\"") + 8)));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new SavePeopleException();
        }
        if (buf.contains("\"coordinates\"{\"X\":")) {
            try {
                person.getCoordinates().setX(Long.valueOf(buf.substring(buf.indexOf("\"X\":\"") + 5, buf.indexOf("\";", buf.indexOf("\"X\":\"") + 5))));
                person.getCoordinates().setY(Float.parseFloat(buf.substring(buf.indexOf("\"Y\":\"") + 5, buf.indexOf("\";", buf.indexOf("\"Y\":\"") + 5))));
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                throw new SavePeopleException();
            }
        }
        if (buf.contains("\"height\":\"")) try {
            person.setHeight(Float.parseFloat(buf.substring(
                    buf.indexOf("\"height\":\"") + 10, buf.indexOf("\";", buf.indexOf("\"height\":\"") + 10))));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new SavePeopleException();
        }
        if (buf.contains("\"eyeColor\":\"")) try {
            person.setEyeColor(EyeColor.valueOf(
                    buf.substring(buf.indexOf("\"eyeColor\":\"") + 12, buf.indexOf("\";", buf.indexOf("\"eyeColor\":\"") + 12)).toUpperCase()));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new SavePeopleException();
        }
        if (buf.contains("\"hairColor\":\"")) try {
            person.setHairColor(HairColor.valueOf(
                    buf.substring(buf.indexOf("\"hairColor\":\"") + 13, buf.indexOf("\";", buf.indexOf("\"hairColor\":\"") + 13)).toUpperCase()));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new SavePeopleException();
        }
        if (buf.contains("\"nationality\":\"")) try {
            person.setNationality(Country.valueOf(
                    buf.substring(buf.indexOf("\"nationality\":\"") + 15, buf.indexOf("\";", buf.indexOf("\"nationality\":\"") + 15)).toUpperCase()));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new SavePeopleException();
        }
        if (buf.contains("\"location\"{\"X\":")) {
            try {
                person.getLocation().setX(Integer.valueOf(buf.substring(buf.lastIndexOf("{\"X\":\"") + 6, buf.indexOf("\";", buf.lastIndexOf("{\"X\":\"") + 6))));
                person.getLocation().setY(Float.valueOf(buf.substring(buf.lastIndexOf("\"Y\":\"") + 5, buf.indexOf("\";", buf.lastIndexOf("\"Y\":\"") + 5))));
                person.getLocation().setZ(Float.valueOf(buf.substring(buf.lastIndexOf("\"Z\":\"") + 5, buf.indexOf("\"}", buf.lastIndexOf("\"Z\":\"") + 5))));
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                throw new SavePeopleException();
            }
        }
        return person;
    }

    public static ConcurrentLinkedQueue<Person> getData(){
        ConcurrentLinkedQueue<Person> allPerson = new ConcurrentLinkedQueue<>();
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/PersonInfo";
            try (java.sql.Connection con = DriverManager.getConnection(url, "postgres", "Nikita444super")) {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM persons\n ORDER BY id");
                while (rs.next()) {
                    Person person = new Person();
                    person.setOwner(rs.getString("owner"));
                    person.setId(rs.getLong("id"));
                    person.setName(rs.getString("name"));
                    person.getCoordinates().setX(rs.getLong("coordinatesx"));
                    person.getCoordinates().setY(rs.getLong("coordinatesy"));
                    person.setCreationDate(LocalDateTime.parse(rs.getString("datetime")));
                    person.setHeight(rs.getFloat("height"));
                    person.setEyeColor(EyeColor.valueOf(rs.getString("eyecolor")));
                    person.setHairColor(HairColor.valueOf(rs.getString("haircolor")));
                    person.setNationality(Country.valueOf(rs.getString("country")));
                    person.getLocation().setX(rs.getInt("locationx"));
                    person.getLocation().setY(rs.getFloat("locationy"));
                    person.getLocation().setZ(rs.getFloat("locationz"));
                    if (allPerson.offer(person)) {
                        System.out.println("Человек с id " + person.getId() + " был добавлен в коллекцию.");
                    } else System.out.println("Произошла ошибка");
                }
                rs.close();
                stmt.close();
                return allPerson;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static String translatePerson(Person person){
        String ans = "{";
        ans = ans + "\"name\":\""+person.getName()+"\";";
        ans = ans + "\"coordinates\"{\"X\":\""+person.getCoordinates().getX()+"\";\"Y\":\""+person.getCoordinates().getY()+"\";};";
        ans = ans + "\"height\":\""+person.getHeight()+"\";";
        ans = ans + "\"eyeColor\":\""+person.getEyeColor().toString()+"\";";
        ans = ans + "\"hairColor\":\""+person.getHairColor().toString()+"\";";
        ans = ans + "\"nationality\":\""+person.getNationality().toString()+"\";";
        ans = ans + "\"location\"{\"X\":\""+person.getLocation().getX()+"\";\"Y\":\""+ person.getLocation().getY()+"\";\"Z\":\""+person.getLocation().getZ()+"\"};";
        return ans;
    }



}