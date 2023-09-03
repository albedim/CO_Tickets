package it.craftopoly.co_tickets.utils;

import com.google.gson.Gson;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Utils
{
    public static boolean isSuccess(String message)
    {
        return message.trim().startsWith("§a");
    }

    public static String convertObjectToJson(Object obj) {
        try {
            Gson gson = new Gson();
            return gson.toJson(obj); // Convert object to JSON string
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isUsernamePremium(String username) throws IOException, MalformedURLException {
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/"+username);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = in.readLine())!=null){
            result.append(line);
        }
        return !result.toString().equals("");
    }

    public static void sendMessage(Player player, String message)
    {
        String[] subMessages = message.split("_");

        for(String msg : subMessages)
            player.sendMessage(msg.replace("_", ""));
    }

    public static TextComponent createInteractiveMessage(String hoverText, ClickEvent.Action action, String message, String command)
    {
        TextComponent button = new TextComponent(message);
        if(hoverText != null){
            BaseComponent[] hoverTextComponents = TextComponent.fromLegacyText(hoverText);
            button.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverTextComponents));
        }

        button.setClickEvent(new ClickEvent(action, command));
        return button;
    }

    public static TextComponent createEmptyInteractiveMessage(String message)
    {
        return new TextComponent(message);
    }
}
