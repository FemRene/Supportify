package de.femrene;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Supportify {
    private static JDABuilder builder = null;
    protected static JDA jda = null;
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Usage: java -jar supportify.jar <discord token>");
            System.exit(0);
        } else {
            startBot(args[0]);
        }
    }

    private static void startBot(String token) throws IOException {
        builder = JDABuilder.createDefault(token);
        builder.setAutoReconnect(true);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.customStatus("Supporting Users"));
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES);
        builder.addEventListeners(new CommandListener(), new ButtonListener());
        jda = builder.build();
        System.out.println("[BOT] " + jda.getSelfUser().getAsTag() + " is now online");
        setCommands();
        stopBot();
    }

    private static void setCommands() {
        jda.upsertCommand("addsupport","Setup a new Support Section")
                .addOption(OptionType.ROLE, "suprole", "Supporter Role", true)
                .addOption(OptionType.ROLE, "adminrole", "Administrator Role", true)
                .addOption(OptionType.STRING,"supcategory","Name of the Support Tisckets",true)
                .addOption(OptionType.CHANNEL, "ticketchannel", "Channel where the Ticket threads from", true).queue();
        jda.updateCommands().queue();
    }

    private static void stopBot() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            if (reader.readLine().equalsIgnoreCase("stop")) {
                System.out.println("[BOT] Shutting down...");
                builder.setStatus(OnlineStatus.OFFLINE);
                jda.shutdown();
                reader.close();
                System.out.println("[BOT] Stopped");
                System.exit(0);
            }
        }
    }
}