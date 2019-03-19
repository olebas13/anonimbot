package com.olebas.anonimbot.command;

import com.olebas.anonimbot.logger.LogLevel;
import com.olebas.anonimbot.logger.LogTemplate;
import com.olebas.anonimbot.model.Anonymous;
import com.olebas.anonimbot.service.AnonymousService;
import org.apache.logging.log4j.Level;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class StartCommand extends AnonymizerCommand {

    private final AnonymousService mAnonymouses;

    public StartCommand(AnonymousService anonymouses) {
        super("start", "Начать использовать бот\n");
        mAnonymouses = anonymouses;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        log.info(LogTemplate.COMMAND_PROCESSING.getTemplate(), user.getId(), getCommandIdentifier());

        StringBuilder sb = new StringBuilder();
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        if (mAnonymouses.addAnonymous(new Anonymous(user, chat))) {
            log.info("User {} is trying to execute '{}' the first time. Added to users' list.", user.getId(), getCommandIdentifier());
            sb.append("Привет, ").append(user.getUserName()).append("! Вы был добавлены в список пользователей бота!\n")
                    .append("Пожалуйста, выполните команду:\n'/set_name <отображаемое_имя>'\nгде &lt;отображаемое_имя&gt; " +
                            "это имя, которое вы хотите использовать, чтобы скрыть свое настоящее имя.");
        } else {
            log.log(Level.getLevel(LogLevel.STRANGE.getValue()),
                    "User {} has already executed '{}'. Is he trying to do it one more time?",
                    user.getId(), getCommandIdentifier());
            sb.append("Вы уже пользуетесь ботом! Вы можете отправлять сообщения, если вы установили свое имя (/set_name)");
        }

        message.setText(sb.toString());
        execute(absSender, message, user);
    }
}
