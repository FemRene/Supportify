package de.femrene;

import de.femrene.config.ConfigReader;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;

public class ButtonListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        Guild guild = event.getGuild();

        if (event.getMessage().getAuthor().equals(Supportify.jda.getSelfUser())) {
            int min = 1000;
            int max = 99999;
            int random = (int) Math.floor(Math.random() * (max - min + 1) + min);
            String btn = event.getButton().getLabel().replace(" Ticket","");
            HashMap<String, String> sections = new HashMap<>(toHashMap(ConfigReader.getString(guild.getId()+".section")));

            guild.createTextChannel(btn+"-"+random, guild.getCategoryById(sections.get(btn))).queue();
        }
    }

    private static HashMap<String, String> toHashMap(String sections) {
        String str = sections.substring(1, sections.length() - 1);
        HashMap<String, String> map = new HashMap<>();
        System.out.println(sections);
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
