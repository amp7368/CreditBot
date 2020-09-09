package apple.credit_bot.discord;


import apple.credit_bot.discord.commands.*;
import apple.credit_bot.discord.reactions.DoReaction;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.HashMap;

public class DiscordBot extends ListenerAdapter {
    private static final String BOT_TOKEN_FILE_PATH = "data/discordToken.data";


    private static final HashMap<String, DoCommand> commandMap = new HashMap<>();
    private static final HashMap<String, DoReaction> reactionMap = new HashMap<>();
    public static String discordToken = ""; // my bot
    public static JDA client;

    public static final String PREFIX = "f!";
    public static final String ADD_COMMAND = "add";
    public static final String SUB_COMMAND = "sub";
    public static final String PROFILE_COMMAND = "profile";
    public static final String LEADERBOARD_COMMAND = "list";
    public static final String UPDATE_COMMAND = "up";

    public DiscordBot() {
        File file = new File(BOT_TOKEN_FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
            }
            System.err.println("Please fill in the token for the discord bot in '" + BOT_TOKEN_FILE_PATH + "'");
            System.exit(1);
            return;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            discordToken = reader.readLine();
            reader.close();
        } catch (IOException e) {
            System.err.println("Please fill in the token for the discord bot in '" + BOT_TOKEN_FILE_PATH + "'");
            System.exit(1);
        }

    }

    public void enableDiscord() throws LoginException {
        JDABuilder builder = new JDABuilder(discordToken);
        builder.addEventListeners(this);
        client = builder.build();
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        commandMap.put(PREFIX+ADD_COMMAND,new CommandAdd());
        commandMap.put(PREFIX+SUB_COMMAND,new CommandSub());
        commandMap.put(PREFIX+PROFILE_COMMAND,new CommandProfile());
        commandMap.put(PREFIX+LEADERBOARD_COMMAND,new CommandLeaderBoard());
        commandMap.put(PREFIX+UPDATE_COMMAND,new CommandUpdate());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (event.getAuthor().isBot()) {
            return;
        }
        // the author is not a bot

        String messageContent = event.getMessage().getContentStripped();
        // deal with the different commands
        for (String command : commandMap.keySet()) {
            if (messageContent.startsWith(command)) {
                commandMap.get(command).dealWithCommand(event);
                break;
            }
        }
    }

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        User user = event.getUser();
        if (user == null || user.isBot()) {
            return;
        }
        String emojiName = event.getReactionEmote().getName();
        for (String reaction : reactionMap.keySet()) {
            if (emojiName.equals(reaction)) {
                reactionMap.get(emojiName).dealWithReaction(event);
                break;
            }
        }
    }
}