package com.olebas.anonimbot.command;

import com.olebas.anonimbot.logger.LogLevel;
import com.olebas.anonimbot.logger.LogTemplate;
import com.olebas.anonimbot.service.AnonymousService;
import org.apache.logging.log4j.Level;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class MyNameCommand extends AnonymizerCommand {

    private final AnonymousService mAnonymouses;

    public MyNameCommand(AnonymousService anonymouses) {
        super("my_name", "показать ваше текущее имя, которое будет отображаться с вашими сообщениями\n");
        mAnonymouses = anonymouses;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        log.info(LogTemplate.COMMAND_PROCESSING.getTemplate(), user.getId(), getCommandIdentifier());

        StringBuilder sb = new StringBuilder();

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        if (!mAnonymouses.hasAnonymous(user)) {
            log.log(Level.getLevel(LogLevel.STRANGE.getValue()), "User {} is trying to execute '{}' without starting the bot.",
                    user.getId(), getCommandIdentifier());
            sb.append("На данный момент у вас нет имени.\nУстановите его с помощью команды '/set_name <отображаемое_имя>'");

        } else {
            log.info("User {} is executing '{}'. Name is '{}'.", user.getId(), getCommandIdentifier(),
                    mAnonymouses.getDisplayedName(user));
            sb.append("Ваше текущее имя: ").append(mAnonymouses.getDisplayedName(user));
        }

        message.setText(sb.toString());
        execute(absSender, message, user);
    }
}
