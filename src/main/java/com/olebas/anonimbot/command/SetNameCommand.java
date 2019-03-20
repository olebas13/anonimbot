package com.olebas.anonimbot.command;

import com.olebas.anonimbot.logger.LogLevel;
import com.olebas.anonimbot.logger.LogTemplate;
import com.olebas.anonimbot.service.AnonymousService;
import org.apache.logging.log4j.Level;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class SetNameCommand extends AnonymizerCommand {

    private final AnonymousService mAnonymouses;

    public SetNameCommand(AnonymousService anonymouses) {
        super(
                "set_name",
                "установить или изменить имя, которое будет отображаться в ваших сообщениях");
        mAnonymouses = anonymouses;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        log.info(LogTemplate.COMMAND_PROCESSING.getTemplate(), user.getId(), getCommandIdentifier());

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        if (!mAnonymouses.hasAnonymous(user)) {
            log.log(Level.getLevel(LogLevel.STRANGE.getValue()), "User {} is trying to execute '{}' without starting the bot!",
                    user.getId(), getCommandIdentifier());
            message.setText("Сначала вы должны запустить бот! Используйте команду '/start'");
            execute(absSender, message, user);
            return;
        }

        String displayedName = getName(strings);

        if (displayedName == null) {
            log.log(Level.getLevel(LogLevel.STRANGE.getValue()), "User {} is trying to set empty name",
                    user.getId());
            message.setText("Вы должны использовать непустое имя");
            execute(absSender, message, user);
            return;
        }

        StringBuilder sb = new StringBuilder();

        if (mAnonymouses.setUserDisplayedName(user, displayedName)) {
            if (mAnonymouses.getDisplayedName(user) == null) {
                log.info("User {} set a name '{}'", user.getId(), displayedName);
                sb.append("Ваше отображаемое имя: '").append(displayedName)
                        .append("'. Теперь Вы можете отправлять сообщения боту!");
            } else {
                log.info("User {} has changed name to '{}'", user.getId(), displayedName);
                sb.append("Ваше новое отображаемое имя: '").append(displayedName).append("'.");
            }
        } else {
            log.log(Level.getLevel(LogLevel.STRANGE.getValue()), "User {} is trying to set taken name '{}'",
                    user.getId(), displayedName);
            sb.append("Имя ").append(displayedName).append(" уже используется! Выберите другое имя!");
        }
        message.setText(sb.toString());
        execute(absSender, message, user);
    }

    private String getName(String[] strings) {
        if (strings == null || strings.length == 0) {
            return null;
        }
        String name = String.join(" ", strings);
        return name.replaceAll(" ", "").isEmpty() ? null : name;
    }
}
