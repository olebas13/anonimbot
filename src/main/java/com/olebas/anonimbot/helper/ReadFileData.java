package com.olebas.anonimbot.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ReadFileData {

    private Properties properties = new Properties();

    public ReadFileData() {
        File file = new File((System.getProperty("user.dir") + "/src/main/resources/app.properties"));

        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            properties.load(fileInput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getBotToken() {
        return properties.getProperty("BOT_TOKEN");
    }

    public String getBotUsername() {
        return properties.getProperty("BOT_USERNAME");
    }
}
