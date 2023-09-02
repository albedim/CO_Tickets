package it.craftopoly.co_tickets.utils;

import com.google.gson.*;
import it.craftopoly.co_tickets.CO_Tickets;
import it.craftopoly.co_tickets.schema.TicketCreationSchema;

public class HttpCall
{
    public static String create(String uuid, String message)
    {
        JsonObject res = HttpUtils.post(
                "/tickets/?platform=mcserver",
                uuid,
                new TicketCreationSchema(message),
                JsonObject.class
        ).getAsJsonObject();

        if(res.get("code").getAsInt() != 200 && res.get("code").getAsInt() != 409)
            return CO_Tickets.getInstance().getConfig().getString("messages.ticket_creation_failed");

        if(res.get("code").getAsInt() == 409)
            return CO_Tickets.getInstance().getConfig().getString("messages.ticket_already_open");

        return CO_Tickets.getInstance().getConfig().getString("messages.ticket_creation_success");
    }

    public static JsonArray getTicketList(String page, String username)
    {
        return HttpUtils.get(
                "/tickets/user/"+username + "?page="+page,
                null,
                JsonObject.class
        ).get("param").getAsJsonArray();
    }

    public static String close(String uuid, String ticketId)
    {
        JsonObject res = HttpUtils.put(
                "/tickets/"+ticketId+"?platform=mcserver",
                uuid,
                null,
                JsonObject.class
        ).getAsJsonObject();

        if(res.get("code").getAsInt() == 403)
            return CO_Tickets.getInstance().getConfig().getString("messages.no_enough_permissions");
        else if(res.get("code").getAsInt() == 404)
            return CO_Tickets.getInstance().getConfig().getString("messages.ticket_not_found");
        else if(res.get("code").getAsInt() == 409)
            return CO_Tickets.getInstance().getConfig().getString("messages.ticket_already_closed");
        else return CO_Tickets.getInstance().getConfig().getString("messages.ticket_successfully_closed");
    }


    public static Object getAllTickets(String page, String uuid)
    {
        JsonObject res = HttpUtils.get(
                "/tickets/?page=" +
                        page +
                        "&platform=mcserver",
                uuid,
                JsonObject.class
        ).getAsJsonObject();

        if(res.get("code").getAsInt() == 403)
            return CO_Tickets.getInstance().getConfig().getString("messages.no_enough_permissions");

        if(res.get("code").getAsInt() != 200)
            return CO_Tickets.getInstance().getConfig().getString("messages.error");

        return res;
    }

    public static Object getTicket(String page, String uuid, String ticketId)
    {
        JsonObject res = HttpUtils.get(
                "/tickets/"+ticketId +
                        "?page="+page+
                        "&platform=mcserver",
                uuid,
                JsonObject.class
        ).getAsJsonObject();

        if(res.get("code").getAsInt() == 403)
            return CO_Tickets.getInstance().getConfig().getString("messages.no_enough_permissions");

        if(res.get("code").getAsInt() == 404)
            return CO_Tickets.getInstance().getConfig().getString("messages.ticket_not_found");

        if(res.get("code").getAsInt() != 200)
            return CO_Tickets.getInstance().getConfig().getString("messages.error");

        return res;
    }

    public static JsonArray getStaffers()
    {
        return HttpUtils.get(
                "/users/staffers",
                null,
                JsonObject.class
        ).get("param").getAsJsonArray();
    }
}

