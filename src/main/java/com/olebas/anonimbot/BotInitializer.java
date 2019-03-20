package com.olebas.anonimbot;

import com.olebas.anonimbot.bot.AnonymizerBot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class BotInitializer {

    private static final Logger LOG = LogManager.getLogger(BotInitializer.class);

    public static void main(String[] args) {
        try {
            LOG.info("Initializing API context...");
            ApiContextInitializer.init();

            TelegramBotsApi botsApi = new TelegramBotsApi();

            LOG.info("Registering Anonymizer...");
            botsApi.registerBot(new AnonymizerBot());

            LOG.info("Anonymizer bot is ready for work!");
        } catch (TelegramApiRequestException e) {
            LOG.error("Error while initializing bot!", e);
        }
    }
}
