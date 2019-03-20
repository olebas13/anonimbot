package com.olebas.anonimbot.command;

import com.olebas.anonimbot.logger.LogLevel;
import com.olebas.anonimbot.logger.LogTemplate;
import com.olebas.anonimbot.service.AnonymousService;
import org.apache.logging.log4j.Level;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class StopCommand extends AnonymizerCommand {

    private final AnonymousService mAnonymouses;

    public StopCommand(AnonymousService anonymouses) {
        super("stop", "удалить себя из списка пользователей бота\n");
        mAnonymouses = anonymouses;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        log.info(LogTemplate.COMMAND_PROCESSING.getTemplate(), user.getId(), getCommandIdentifier());

        StringBuilder sb = new StringBuilder();

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        if (mAnonymouses.removeAnonymous(user)) {
            log.info("User {} has been removed from bot's users list!", user.getId());
            sb.append("Вы были удалены из списка пользователей бота! До свидания!");
        } else {
            log.log(Level.getLevel(LogLevel.STRANGE.getValue()), "User {} is trying to execute '{}' without having executed 'start' before!",
                    user.getId(), getCommandIdentifier());
            sb.append("Вы не были в списке пользователей бота. До свидания!");
        }

        message.setText(sb.toString());
        execute(absSender, message, user);
    }
}
