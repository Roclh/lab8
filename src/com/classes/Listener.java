package com.classes;

import com.classes.serverSide.answers.Answer;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import sample.LoggerController;
import sample.Main;
import sample.MainController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Listener extends Thread{
    public static String lastMessage = "";
    private DatagramChannel datagramChannel;
    private SocketAddress serverAddress;
    private ByteBuffer buffer;

    public Listener(DatagramChannel datagramChannel, SocketAddress socketAddress){
        this.datagramChannel = datagramChannel;
        this.serverAddress = socketAddress;
        this.buffer = ByteBuffer.allocate(16384);
    }

    @Override
    public void run() {
        while(!isInterrupted()){
            try {
                buffer.clear();
                datagramChannel.connect(serverAddress);
                datagramChannel.receive(buffer);
                buffer.flip();

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer.array());
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                Answer answer = (Answer) objectInputStream.readObject();

                if (answer.getValue().equals("BigData")){
                    System.out.println("Слишком большой объем данных. Ожидаемое количество пакетов:");
                    buffer.clear();
                    datagramChannel.receive(buffer);
                    buffer.flip();
                    byteArrayInputStream = new ByteArrayInputStream(buffer.array());
                    objectInputStream = new ObjectInputStream(byteArrayInputStream);
                    Answer countAnswer = (Answer) objectInputStream.readObject();
                    System.out.println(countAnswer.getValue());
                    for( int i=0;i<Integer.parseInt(countAnswer.getValue()); i++){
                        buffer.clear();
                        datagramChannel.receive(buffer);
                        buffer.flip();
                        byteArrayInputStream = new ByteArrayInputStream(buffer.array());
                        objectInputStream = new ObjectInputStream(byteArrayInputStream);
                        Answer newAnswer = (Answer) objectInputStream.readObject();
                        lastMessage = lastMessage + newAnswer.getValue();
                        System.out.print(newAnswer.getValue());
                    }
                }
                else if(answer.getValue().equals("Данный элемент не является минимальным\n")){
                    lastMessage = answer.getValue();
                }
                else if(answer.getValue().equals("AllFine")){
                    LoggerController.status = "AllFine";
                } else if(answer.getValue().equals("AlreadyInUse")){
                    LoggerController.status = "AlreadyInUse";
                }
                else if(answer.getValue().equals("WrongPassword")){
                    LoggerController.status = "WrongPassword";
                    System.out.println("WrongPassword");
                }
                else{
                    lastMessage = answer.getValue();
                    System.out.println(answer.getValue());
                }

                objectInputStream.close();
                byteArrayInputStream.close();
                buffer.clear();
                datagramChannel.disconnect();
            } catch (PortUnreachableException | IllegalStateException e) {
                System.out.println("Сервер не доступен:\n"+ e.getMessage());
                try {
                    datagramChannel.disconnect();
                } catch (IOException ignored) {
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
