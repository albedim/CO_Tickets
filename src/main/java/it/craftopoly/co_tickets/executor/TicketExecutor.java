package it.craftopoly.co_tickets.executor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.craftopoly.co_tickets.CO_Tickets;
import it.craftopoly.co_tickets.utils.DateUtils;
import it.craftopoly.co_tickets.utils.HttpCall;
import it.craftopoly.co_tickets.utils.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.w3c.dom.Text;

import java.util.Arrays;

public class TicketExecutor implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = (Player) sender;

        if (cmd.getName().equals("ticket"))
        {
            if(args.length > 0)
            {
                switch (args[0]) {
                    case "create":
                        if (args.length > 1) {
                            String message = String.join(" ", args).substring(7);
                            String response = HttpCall.create(player.getUniqueId().toString(), message);

                            if(Utils.isSuccess(response))
                                sendMessageToStaff(
                                        CO_Tickets.getInstance().getConfig().getString(
                                                "messages.ticket_created")
                                        .replace("{username}", player.getName()),
                                        player.getName()
                                );

                            player.sendMessage(response);

                        } else {
                            player.sendMessage(
                                    CO_Tickets
                                            .getInstance()
                                            .getConfig()
                                            .getString("messages.wrong_command_use")
                            );
                            return false;
                        }
                        break;

                    case "list":

                        String ticketListpage = args.length == 2 ? args[1] : "1";
                        JsonArray ticketList = HttpCall.getTicketList(ticketListpage, player.getName());

                        player.sendMessage("§8-------------------------------------");

                        Utils.sendMessage(
                                player,
                                "§a§lTickets    §8(§7Pagina §a" + ticketListpage + "§8)__"
                        );

                        ticketList.forEach((e) -> {
                            JsonObject ticket = e.getAsJsonObject();
                            TextComponent[] textComponents = getTicketListTextComponent(ticket);
                            player.spigot().sendMessage(
                                    textComponents[0],
                                    textComponents[1],
                                    textComponents[2]
                            );
                        });

                        player.sendMessage("§8-------------------------------------");
                        break;

                    case "info":
                        if (args.length == 2 || args.length == 3) {

                            String ticketId = args[1];
                            String page = args.length == 3 ? args[2] : "1";

                            Object ticketResponse = HttpCall.getTicket(page, player.getUniqueId().toString(), ticketId);

                            if(ticketResponse instanceof String)
                                player.sendMessage(ticketResponse.toString());
                            else{
                                JsonObject ticket = ((JsonObject) ticketResponse).get("param").getAsJsonObject();

                                player.sendMessage("§8-------------------------------------");
                                Utils.sendMessage(player,
                                        "§a§lTicket §7#" +
                                                ticket.get("ticket_id") +
                                                "    §8(§7Pagina §a" + page + "§8)"
                                );
                                Utils.sendMessage(player, getTicketInfoTextComponent(ticket));
                                player.sendMessage(" ");

                                JsonArray messages = ticket.get("messages").getAsJsonArray();
                                messages.forEach(e -> {
                                    JsonObject message = e.getAsJsonObject();
                                    TextComponent textComponent = getMessageTextComponent(message);
                                    player.spigot().sendMessage(textComponent);
                                });

                                if(ticket.get("open").getAsBoolean()) {
                                    TextComponent[] ticketActions = getTicketActionComponent(ticket.get("ticket_id").getAsInt());
                                    player.sendMessage(" ");
                                    player.spigot().sendMessage(
                                            ticketActions[0],
                                            Utils.createEmptyInteractiveMessage("     "),
                                            ticketActions[1]
                                    );
                                }
                                player.sendMessage("§8-------------------------------------");
                            }

                        } else {
                            player.sendMessage(
                                    CO_Tickets
                                            .getInstance()
                                            .getConfig()
                                            .getString("messages.wrong_command_use")
                            );
                        }
                        break;

                    case "all":

                        String allTicketsPage = args.length == 2 ? args[1] : "1";
                        Object allTicketsResponse = HttpCall.getAllTickets(
                                allTicketsPage,
                                player.getUniqueId().toString()
                        );

                        if(allTicketsResponse instanceof String)
                            player.sendMessage(allTicketsResponse.toString());
                        else{
                            JsonArray allTickets = ((JsonObject) allTicketsResponse).get("param").getAsJsonArray();

                            player.sendMessage("§8-------------------------------------");
                            Utils.sendMessage(player, "§a§lTickets    §8(§7Pagina §a" + allTicketsPage + "§8)__");
                            allTickets.forEach(e -> {
                                JsonObject ticket = e.getAsJsonObject();
                                TextComponent[] textComponents = getAllTicketTextComponent(ticket);
                                player.spigot().sendMessage(
                                        textComponents[0],
                                        textComponents[1],
                                        textComponents[2]
                                );
                            });
                            player.sendMessage("§8-------------------------------------");
                        }
                        break;

                    case "close":

                        if(args.length == 2) {
                            String ticketId = args[1];
                            String ticketClosed = HttpCall.close(
                                    player.getUniqueId().toString(),
                                    ticketId
                            );
                            player.sendMessage(ticketClosed);
                        } else {
                            player.sendMessage(
                                    CO_Tickets
                                            .getInstance()
                                            .getConfig()
                                            .getString("messages.wrong_command_use")
                            );
                        }
                        break;

                    case "comment":
                        if(args.length >= 3) {
                            String ticketId = args[1];
                            String message = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                            String ticketCommented = HttpCall.comment(
                                    player.getUniqueId().toString(),
                                    ticketId,
                                    message
                            );

                            if(Utils.isSuccess(ticketCommented))
                                sendMessageToStaff(
                                        CO_Tickets.getInstance().getConfig().getString(
                                                        "messages.ticket_commented")
                                                .replace("{ticketId}", ticketId),
                                        player.getName()
                                );
                            player.sendMessage(ticketCommented);
                        } else {
                            player.sendMessage(
                                    CO_Tickets
                                            .getInstance()
                                            .getConfig()
                                            .getString("messages.wrong_command_use")
                            );
                        }
                        break;
                    default:
                        player.sendMessage(
                                CO_Tickets
                                        .getInstance()
                                        .getConfig()
                                        .getString("messages.wrong_command_use")
                        );
                        return false;
                }
            }else{
                player.sendMessage("§8-------------------------------------");
                Utils.sendMessage(
                        player,
                        CO_Tickets
                                .getInstance()
                                .getConfig()
                                .getString("messages.tutorial")
                );
                player.sendMessage("§8-------------------------------------");
                return false;
            }
        }
        return false;
    }

    private static void sendMessageToStaff(String message, String username)
    {
        JsonArray staffers = HttpCall.getStaffers();
        staffers.forEach(e -> {
            JsonObject staffer = e.getAsJsonObject();
            Player player = Bukkit.getPlayerExact(staffer.get("username").getAsString());
            if(player != null)
                if(player.isOnline())
                {
                    player.sendMessage(message);
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 300, 300);
                }
        });
    }

    @Deprecated
    private static void sendMessageToPlayer(String username)
    {
        Player player = Bukkit.getPlayerExact(username);

        if(player != null)
            if(player.isOnline())
            {
                player.sendMessage(
                        CO_Tickets.getInstance().getConfig().getString(
                        "messages.ticket_news_notification"));
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 300, 300);
            }
    }

    private static TextComponent[] getTicketListTextComponent(JsonObject ticket)
    {
        TextComponent status = Utils.createEmptyInteractiveMessage(
                "§8["+(ticket.get("open").getAsBoolean() ?
                        "§aAperto" :
                        "§cChiuso")+"§8] "
        );
        TextComponent ticketId = Utils.createInteractiveMessage(
                "§7Clicca per visualizzare questo ticket",
                ClickEvent.Action.RUN_COMMAND,
                "§7#"+ticket.get("ticket_id") + ": ",
                "/ticket info "+ticket.get("ticket_id")
        );
        TextComponent message = Utils.createInteractiveMessage(
                "§7" +
                        ticket.get("message_owner").getAsString() + " §8➜ §7" + ticket.get("message").getAsString() + "\n§8✉ " +
                        DateUtils.formatDate(
                            DateUtils.fixDate(
                                    ticket.get("created_on").getAsString().split(" ")[0]
                            )
                        ) + " alle " +
                        ticket.get("created_on").getAsString().split(" ")[1].split(":")[0] + ":" +
                        ticket.get("created_on").getAsString().split(" ")[1].split(":")[1],
                ClickEvent.Action.OPEN_URL,
                "§7" +
                        (
                                ticket.get("message").getAsString().length() > 34 ?
                                ticket.get("message").getAsString().substring(0,34) + "..." :
                                ticket.get("message").getAsString()
                        ),
                null
        );
        return new TextComponent[] { status, ticketId, message };
    }

    private static TextComponent[] getTicketActionComponent(Integer ticketId)
    {
        TextComponent comment = Utils.createInteractiveMessage(
                "§7Commenta questo ticket",
                ClickEvent.Action.SUGGEST_COMMAND,
                "  §a[Commenta]",
                "/ticket comment " + ticketId + " "
        );
        TextComponent close = Utils.createInteractiveMessage(
                "§7Chiudi questo ticket",
                ClickEvent.Action.RUN_COMMAND,
                "§c[Chiudi]",
                "/ticket close " + ticketId
        );
        return new TextComponent[] { comment, close };
    }

    private static TextComponent[] getAllTicketTextComponent(JsonObject ticket)
    {
        TextComponent status = Utils.createEmptyInteractiveMessage(
                "§8["+(ticket.get("open").getAsBoolean() ?
                        "§aAperto" :
                        "§cChiuso")+"§8] "
        );
        TextComponent ticketId = Utils.createInteractiveMessage(
                "§7Clicca per visualizzare questo ticket",
                ClickEvent.Action.RUN_COMMAND,
                "§7#"+ticket.get("ticket_id") + ": " +
                        ticket.get("owner").getAsJsonObject().get("username").getAsString() + " §8▪ ",
                "/ticket info "+ticket.get("ticket_id")
        );
        TextComponent message = Utils.createInteractiveMessage(
                "§7" +
                        ticket.get("message_owner").getAsString() + " §8➜ §7" + ticket.get("message").getAsString() + "\n§8✉ " +
                        DateUtils.formatDate(
                                DateUtils.fixDate(
                                        ticket.get("created_on").getAsString().split(" ")[0]
                                )
                        ) + " alle " +
                        ticket.get("created_on").getAsString().split(" ")[1].split(":")[0] + ":" +
                        ticket.get("created_on").getAsString().split(" ")[1].split(":")[1],
                ClickEvent.Action.OPEN_URL,
                "§7" +
                        (
                                ticket.get("message").getAsString().length() > 34 ?
                                        ticket.get("message").getAsString().substring(0,34) + "..." :
                                        ticket.get("message").getAsString()
                        ),
                null
        );
        return new TextComponent[] { status, ticketId, message };
    }

    private static String getTicketInfoTextComponent(JsonObject ticket) {

        String status = (ticket.get("open").getAsBoolean() ?
            "§aAperto_" :
            "§cChiuso_"
        );

        String createdOn = (DateUtils.fixDate(
            ticket.get("created_on").getAsString().split(" ")[0]
        ) + " alle " +
            (
                ticket.get("created_on")
                    .getAsString()
                    .split(" ")[1]
                    .split(":")[0] + ":" +
                    ticket.get("created_on")
                        .getAsString()
                        .split(" ")[1]
                        .split(":")[1] + "_"
            )
        );

        String closedOn = (
            !ticket.get("open").getAsBoolean() ?
                    " §8▪ §7Chiuso il: " +
                    (DateUtils.fixDate(
                        ticket.get("closed_on").getAsString().split(" ")[0]
                    ) + " alle " + (
                        ticket.get("closed_on")
                            .getAsString()
                            .split(" ")[1]
                            .split(":")[0] + ":" +
                            ticket.get("closed_on")
                                .getAsString()
                                .split(" ")[1]
                                .split(":")[1]
                    )
                )
            : ""
        );

        return (
                " §8▪ §7Stato: " + status +
                " §8▪ §7Player: " + ticket.get("owner").getAsJsonObject().get("username").getAsString() + "_" +
                " §8▪ §7Aperto il: " + createdOn + closedOn
        );
    }

    private static TextComponent getMessageTextComponent(JsonObject message)
    {
        String createdOn = DateUtils.formatDate(
            DateUtils.fixDate(
                message.get("created_on").getAsString().split(" ")[0]
            )
        ) + " alle " + (
            message.get("created_on").getAsString().split(" ")[1].split(":")[0] + ":" +
            message.get("created_on").getAsString().split(" ")[1].split(":")[1]
        );

        String styledMessage = "  §7" +
                message.get("owner").getAsJsonObject().get("username").getAsString() +
                " §8➜ §7" + message.get("content").getAsString();

        return Utils.createInteractiveMessage(
                "§8✉ " + createdOn,
                ClickEvent.Action.OPEN_URL,
                styledMessage,
                null
        );
    }
}
