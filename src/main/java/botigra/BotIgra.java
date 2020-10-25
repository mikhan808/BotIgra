package botigra;

import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BotIgra extends TelegramLongPollingBot {

    private Properties props;
    private String username;
    private String token;

    public BotIgra()
    {
        props = new Properties();
        try {
            props.load(new FileInputStream("bot.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        token=props.getProperty("token");
        username = props.getProperty("username");
    }

    @Override
    public void onUpdateReceived(Update update) {

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
