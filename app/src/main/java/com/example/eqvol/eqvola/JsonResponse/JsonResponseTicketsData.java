package com.example.eqvol.eqvola.JsonResponse;

import com.example.eqvol.eqvola.Classes.Ticket;

import java.util.List;

/**
 * Created by eqvol on 31.10.2017.
 */

public class JsonResponseTicketsData{

    private List<Ticket> tickets;
    private int last_messages;

    /*public JsonResponseTicketsData()
    {
        this.tickets = new ArrayList<Ticket>();
        this.last_messages = 0;
    }
    public JsonResponseTicketsData(ArrayList<Ticket> tickets, int last_messages)
    {
        this.tickets = tickets;
        this.last_messages = last_messages;
    }*/

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public int getLast_messages() {
        return last_messages;
    }

    public void setLast_messages(int last_messages) {
        this.last_messages = last_messages;
    }

   /* public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public int getLast_messages() {
        return last_messages;
    }

    public void setLast_messages(int last_messages) {
        this.last_messages = last_messages;
    }

    public JsonResponseTicketsData()
    {
        this.last_messages = 0;
        this.tickets = new ArrayList<Ticket>();
    }

    public JsonResponseTicketsData(JsonElement element)
    {
        this.last_messages = element.getAsInt();
        this.tickets = new ArrayList<Ticket>();
        Gson gson = new Gson();
        for(JsonElement e: element.getAsJsonArray()){
            this.tickets.add(gson.fromJson(e, Ticket.class));
        }
    }*/
}
