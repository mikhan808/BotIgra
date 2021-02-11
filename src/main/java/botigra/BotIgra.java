package botigra;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BotIgra extends TelegramLongPollingBot {

    private static final String CHATS_FILE_NAME = "chats.csv";
    private static final String VOPROS_FILE_NAME = "vopros.csv";
    private static final String DELIMITER = ";";
    private final Properties props;
    private final String username;
    private final String token;
    private final String password;
    private final List<Vopros> voproses;
    private int currentVopros = 0;

    public BotIgra() {
        props = new Properties();
        try {
            props.load(new FileInputStream("bot.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        token = props.getProperty("token");
        username = props.getProperty("username");
        password = props.getProperty("password");
        voproses = getVoproses();
    }

    private void sendVopros() {
        if (currentVopros < voproses.size()) {
            sendAll("Вопрос:");
            sendAll(voproses.get(currentVopros).getVopros());
        } else {
            sendAll("Поздравляю! Вы выиграли! Молодцы)");
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message msg = update.getMessage();
        Long id = msg.getChatId();
        List<Long> trueChats = getTrueChats();
        if (trueChats.contains(id)) {
            if (msg.getText().toLowerCase().contentEquals(voproses.get(currentVopros).getOtvet().toLowerCase())) {
                sendAll(msg.getText());
                sendAll("Верно!!!");
                currentVopros++;
                sendVopros();
            } else {
                sendAll(msg.getText());
                sendAll("Неверно!!! Попробуйте еще раз");
                sendVopros();
            }
        } else {
            if (msg.getText().contentEquals(password)) {
                try {
                    FileWriter writer = new FileWriter(CHATS_FILE_NAME, true);
                    BufferedWriter bufferWriter = new BufferedWriter(writer);
                    bufferWriter.write("\n" + id);
                    bufferWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sendMsg(id, "Добро пожаловать");
                sendVopros();
            } else sendMsg(id, "Введите пароль");
        }


    }

    private void sendAll(String text) {
        List<Long> trueChats = getTrueChats();
        for (int i = 0; i < trueChats.size(); i++) {
            sendMsg(trueChats.get(i), text);
        }
    }

    private void sendMsg(Long chatId, String text) {
        SendMessage s = new SendMessage();
        s.setChatId(chatId.toString()); // Боту может писать не один человек, и поэтому чтобы отправить сообщение, грубо говоря нужно узнать куда его отправлять
        s.setText(text);
        try { //Чтобы не крашнулась программа при вылете Exception
            execute(s);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    List<Long> getTrueChats() {
        List<Long> result = new ArrayList<>();
        try {
            File file = new File(CHATS_FILE_NAME);
            //создаем объект FileReader для объекта File
            FileReader fr = new FileReader(file);
            //создаем BufferedReader с существующего FileReader для построчного считывания
            BufferedReader reader = new BufferedReader(fr);
            // считаем сначала первую строку
            String line = reader.readLine();
            while (line != null) {
                line = line.trim();
                if (!line.isEmpty())
                    result.add(Long.parseLong(line));
                // считываем остальные строки в цикле
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    List<Vopros> getVoproses() {
        List<Vopros> result = new ArrayList<>();
        try {
            File file = new File(VOPROS_FILE_NAME);
            //создаем объект FileReader для объекта File
            FileReader fr = new FileReader(file);
            //создаем BufferedReader с существующего FileReader для построчного считывания
            BufferedReader reader = new BufferedReader(fr);
            // считаем сначала первую строку
            String line = reader.readLine();
            while (line != null) {
                line = line.trim();
                if (!line.isEmpty())
                    result.add(new Vopros(line, DELIMITER, null));

                // считываем остальные строки в цикле
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

}
