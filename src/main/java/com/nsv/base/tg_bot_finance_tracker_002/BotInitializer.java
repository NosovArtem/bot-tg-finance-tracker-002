package com.nsv.base.tg_bot_finance_tracker_002;

import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class BotInitializer {

    TelegramBot tgbot;

    public BotInitializer(TelegramBotsApi telegramBotsApi, TelegramBot tgbot) {
        this.tgbot = tgbot;
        try {
            telegramBotsApi.registerBot(tgbot);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to register bot", e);
        }
    }

    @PreDestroy
    public void shutdown() {
        System.out.println("Shutting down Telegram bot...");
        tgbot.onClosing();
        System.out.println("Telegram bot stopped.");
    }
}
