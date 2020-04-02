package by.matkun.bot;

import by.matkun.service.CityServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private CityServiceImplement cityService;

    @Value("${telegramBot.name}")
    private String telegramBotName;

    @Value("${telegramBot.token}")
    private String telegramBotToken;

    public static final String HELP_INSTRUCTION = "This is telegram bot, which gives information about city" +
            "You can use it, just input name of city (This version supports only English)";

    public void sendTextMessage(Message message,String text){
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);

        try {
            setButtons(sendMessage);
            Message mainMessage = execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
    public void setButtons(SendMessage sendMessage){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("/help"));

        keyboardRows.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
    }
    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        Map<String, String> cities = cityService.findAll();
        if (message.getText().equals("/help")){
            sendTextMessage(message,HELP_INSTRUCTION);
        }else {
        if (message.hasText() && cities.get(message.getText()) != null) {
                sendTextMessage(message, cities.get(message.getText()));
            } else {
                sendTextMessage(message, "Sorry... This city is not found! Try again.");
            }
        }
    }

    @Override
    public String getBotUsername() {
        return telegramBotName;
    }

    @Override
    public String getBotToken() {
        return telegramBotToken;
    }
}
