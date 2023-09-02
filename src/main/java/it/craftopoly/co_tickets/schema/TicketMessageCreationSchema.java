package it.craftopoly.co_tickets.schema;

public class TicketMessageCreationSchema
{
    private String message;

    private Integer ticket_id;

    public TicketMessageCreationSchema(Integer ticketId, String message)
    {
        this.message = message;
        this.ticket_id = ticketId;
    }
}
