package de.femrene;

import de.femrene.config.ConfigReader;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.EnumSet;
import java.util.HashMap;

public class ButtonListener extends ListenerAdapter {
    static TextChannel channel;

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        Guild guild = event.getGuild();

        if (event.getMessage().getAuthor().equals(Supportify.jda.getSelfUser())) {
            if (event.getButton().getId().equals("delete-ticket")) {
                event.deferReply(true).queue();
                event.getHook().setEphemeral(true).sendMessage("Are you sure to close this Ticket??").addActionRow(MessageCreator.confirmCloseTicketButtons()).queue();
            } else if (event.getButton().getId().equals("delete-ticket-sure")) {
                event.getInteraction().getChannel().delete().queue();
            } else {
                event.deferReply(true).queue();
                int min = 1;
                int max = 1000;
                int random = (int) Math.floor(Math.random() * (max - min + 1) + min);
                String btn = event.getButton().getLabel().replace(" Ticket", "");
                HashMap<String, String> sections = new HashMap<>(toHashMap(ConfigReader.getString(guild.getId() + ".section")));
                String admin = new HashMap<>(toHashMap(ConfigReader.getString(guild.getId() + ".admin"))).get(btn);
                String sup = new HashMap<>(toHashMap(ConfigReader.getString(guild.getId() + ".sup"))).get(btn);
                guild.createTextChannel(btn + "-" + random + "-" + event.getUser().getName(), guild.getCategoryById(sections.get(btn)))
                        .addPermissionOverride(event.getMember(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                        .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                        .addRolePermissionOverride(Long.parseLong(admin), EnumSet.of(Permission.VIEW_CHANNEL), null)
                        .addRolePermissionOverride(Long.parseLong(sup), EnumSet.of(Permission.VIEW_CHANNEL), null)
                        .queue(tempchannel -> {
                            channel = tempchannel;
                            // This block will execute once the tempchannel is successfully created
                            event.getHook().sendMessage("Channel " + tempchannel.getAsMention() + " was created")
                                    .setEphemeral(true)  // This ensures that only the user sees the message
                                    .queue();
                            tempchannel.sendMessage(guild.getRoleById(sup).getAsMention() + guild.getRoleById(admin).getAsMention()).queue();
                            tempchannel.sendMessageEmbeds(MessageCreator.ticketEmbed(btn, event.getMember())).setActionRow(MessageCreator.closeTicketButtons()).queue();
                        }, throwable -> {
                            // This block will execute if the channel creation fails
                            event.getHook().sendMessage("Failed to create channel: " + throwable.getMessage())
                                    .setEphemeral(true)  // Make this message ephemeral too
                                    .queue();
                        });

            }
        }
    }

    private static HashMap<String, String> toHashMap(String sections) {
        String str = sections.substring(1, sections.length() - 1);
        HashMap<String, String> map = new HashMap<>();
        if (str.contains(", ")) {
            String[] pairs = str.split(", ");
            for (String pair : pairs) {
                String[] keyValue = pair.split("\\=");
                map.put(keyValue[0], keyValue[1]);
            }
        } else {
            String[] keyValue = str.split("\\=");
            map.put(keyValue[0], keyValue[1]);
        }
        return map;
    }

}
