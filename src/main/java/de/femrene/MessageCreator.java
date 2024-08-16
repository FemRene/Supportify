package de.femrene;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageCreator {

    protected static ArrayList<Button> createTicketButtons(HashMap<String, String> sections) {
        ArrayList<Button> buttons = new ArrayList<>();
        sections.forEach((key, value) -> {
            buttons.add(Button.success(key.toLowerCase(), key+" Ticket").withEmoji(Emoji.fromUnicode("U+1F600")));
        });
        return buttons;
    }

    protected static MessageEmbed createTicketEmbed() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("FemRene");
        builder.setTitle("Supportify - TicketSystem");
        builder.setDescription("Click to create a Ticket");
        return builder.build();
    }

}
