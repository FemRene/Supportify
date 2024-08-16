package de.femrene;

import de.femrene.config.ConfigReader;
import de.femrene.config.ConfigWriter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.HashMap;

public class CommandListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "addsupport":
                Role sup = event.getOption("suprole").getAsRole();
                Role admin = event.getOption("adminrole").getAsRole();
                HashMap<String, String> supSections = new HashMap<>();
                HashMap<String, String> supRole = new HashMap<>();
                HashMap<String, String> adminRole = new HashMap<>();
                if (ConfigReader.ifString(event.getGuild().getId()+".section")) {
                    supSections.putAll(toHashMap(ConfigReader.getString(event.getGuild().getId()+".section")));
                }
                if (ConfigReader.ifString(event.getGuild().getId()+".sup")) {
                    supRole.putAll(toHashMap(ConfigReader.getString(event.getGuild().getId()+".sup")));
                }
                if (ConfigReader.ifString(event.getGuild().getId()+".admin")) {
                    adminRole.putAll(toHashMap(ConfigReader.getString(event.getGuild().getId()+".admin")));
                }
                Category category = (Category) event.getOption("ticketchannel").getAsChannel();
                supSections.put(event.getOption("supcategory").getAsString(), category.getId());
                supRole.put(event.getOption("supcategory").getAsString(), sup.getId());
                adminRole.put(event.getOption("supcategory").getAsString(), admin.getId());
                System.out.println(supSections);
                Member member = event.getMember();
                if (member.hasPermission(Permission.ADMINISTRATOR)) {
                    new ConfigWriter(event.getGuild().getId()+".section", supSections.toString());
                    new ConfigWriter(event.getGuild().getId()+".sup", supRole.toString());
                    new ConfigWriter(event.getGuild().getId()+".admin", adminRole.toString());
                    event.getChannel().sendMessageEmbeds(MessageCreator.createTicketEmbed()).setActionRow(MessageCreator.createTicketButtons(toHashMap(ConfigReader.getString(event.getGuild().getId()+".section")))).queue();
                    event.getHook().sendMessage("Channel created").setEphemeral(true).queue();
                } else {
                    event.getHook().sendMessage("You do not have permission to use this command!").setEphemeral(true).queue();
                }
                //serverID
                //  SupportName
                //      SectionID
                //      SupRoleID
                //      AdminRoleID
        }
        super.onSlashCommandInteraction(event);
    }

    private static HashMap<String, String> toHashMap(String sections) {
        String str = sections.substring(1, sections.length() - 1);
        HashMap<String, String> map = new HashMap<>();
        if (str.contains(", ")) {
            String[] pairs = str.split(", ");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                map.put(keyValue[0], keyValue[1]);
            }
        } else {
            String[] keyValue = str.split("=");
            map.put(keyValue[0], keyValue[1]);
        }
        return map;
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        event.getGuild().upsertCommand("addsupport","Setup a new Support Section")
                .addOption(OptionType.ROLE, "suprole", "Supporter Role", true)
                .addOption(OptionType.ROLE, "adminrole", "Administrator Role", true)
                .addOption(OptionType.STRING,"supcategory","Name of the Support Tisckets",true)
                .addOption(OptionType.CHANNEL, "ticketchannel", "Channel where the Ticket threads from", true).queue();
        event.getGuild().updateCommands().queue();
    }
}
