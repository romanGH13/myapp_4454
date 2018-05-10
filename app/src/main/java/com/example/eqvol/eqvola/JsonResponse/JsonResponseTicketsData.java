package com.example.eqvol.eqvola.JsonResponse;

import com.example.eqvol.eqvola.Classes.Ticket;

import java.util.List;

/**
 * Created by eqvol on 31.10.2017.
 */

public class JsonResponseTicketsData{

    private List<Ticket> tickets;
    private int last_messages;

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


}
