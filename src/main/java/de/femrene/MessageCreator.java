package de.femrene;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class MessageCreator {

    protected static ArrayList<Button> createTicketButtons(HashMap<String, String> sections) {
        ArrayList<Button> buttons = new ArrayList<>();
        sections.forEach((key, value) -> {
            buttons.add(Button.success(key.toLowerCase(), key+" Ticket").withEmoji(Emoji.fromUnicode("U+1F39F")));
        });
        return buttons;
    }
    protected static ArrayList<Button> closeTicketButtons() {
        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(Button.danger("delete-ticket", "Close this Ticket").withEmoji(Emoji.fromUnicode("U+1F5D1")));
        return buttons;
    }
    protected static ArrayList<Button> confirmCloseTicketButtons() {
        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(Button.danger("delete-ticket-sure", "Yes").withEmoji(Emoji.fromUnicode("U+1F5D1")));
        return buttons;
    }

    protected static MessageEmbed createTicketEmbed() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("FemRene");
        builder.setColor(Color.GREEN);
        builder.setDescription("Click to create a Ticket");
        builder.setAuthor("Supportify by FemRene","https://femrene.dev");
        return builder.build();
    }

    protected static MessageEmbed ticketEmbed(String cat, Member member) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.GREEN);
        builder.setDescription("Support will help you straight away "+member.getAsMention());
        builder.addField("Category", cat, true);
        builder.setThumbnail(member.getUser().getAvatarUrl());
        builder.setAuthor("Supportify by FemRene","https://femrene.dev");
        return builder.build();
    }

}
