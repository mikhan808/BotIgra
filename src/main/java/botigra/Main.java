package botigra;


import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        // Инициализируем апи
        TelegramBotsApi botapi = null;
        try {
            botapi = new TelegramBotsApi(DefaultBotSession.class);
            BotIgra bot;
            bot = new BotIgra();
            botapi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
