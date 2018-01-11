package com.example.eqvol.eqvola.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eqvol.eqvola.Classes.Api;
import com.example.eqvol.eqvola.Classes.Leverage;
import com.example.eqvol.eqvola.Classes.Message;
import com.example.eqvol.eqvola.Classes.Ticket;
import com.example.eqvol.eqvola.Classes.User;
import com.example.eqvol.eqvola.R;
import com.example.eqvol.eqvola.fragments.SupportChat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by eqvol on 31.10.2017.
 */

public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.TicketsViewHolder> implements Comparator<Ticket>{

    Context ctx;
    LayoutInflater lInflater;
    List<Ticket> tickets;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnCLickListener(OnItemClickListener onItemClickListener)
    {
        this.mOnItemClickListener = onItemClickListener;
    }

    public TicketsAdapter(@NonNull Context context, List<Ticket> tickets) {
        this.ctx = context;
        this.tickets = tickets;
        Collections.sort(this.tickets, this);
        this.lInflater = LayoutInflater.from(this.ctx);
    }


    public static class TicketsViewHolder extends RecyclerView.ViewHolder{
        RecyclerView rv;
        TextView tvTitle;
        TextView tvMessage;
        TextView tvTime;
        ImageView img ;

        public View container;

        TicketsViewHolder(View itemView) {
            super(itemView);
            container = itemView;
            rv = (RecyclerView)itemView.findViewById(R.id.support_chat_list);
            tvTitle = ((TextView) itemView.findViewById(R.id.support_chat_title));
            tvMessage = ((TextView) itemView.findViewById(R.id.support_chat_last_message));
            tvTime = ((TextView) itemView.findViewById(R.id.support_chat_time));
            img = (ImageView)itemView.findViewById(R.id.support_chat_ticket_image);
        }

    }

    @Override
    public TicketsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ticket_item, parent, false);
        TicketsViewHolder pvh = new TicketsViewHolder(v);
        return pvh;
    }

    private long timestampToDays(long timestamp)
    {
        long seconds = timestamp / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        return days;
    }

    @Override
    public void onBindViewHolder(TicketsViewHolder holder, final int position) {

        //set title
        String title = tickets.get(position).getTitle();
        if(title.length() > 20)
        {
            holder.tvTitle.setText(title.substring(0,20) + "...");
        }
        else
        {
            holder.tvTitle.setText(title);
        }

        //set message
        String message = tickets.get(position).getMessage().getMessage();
        if(message.contentEquals(""))
        {
            holder.tvMessage.setText("Attachment..");
        }
        else {
            holder.tvMessage.setText(message);
        }

        //set time
        String time = "";
        SimpleDateFormat formatter = new SimpleDateFormat(ctx.getString(R.string.date_format));
        Date date = null;
        DateFormat dateFormat = null;
        try {
            date = formatter.parse(tickets.get(position).getMessage().getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date != null) {
            long messageDays = timestampToDays(date.getTime());
            long currentDays = timestampToDays(new Date().getTime());
            if (currentDays - messageDays == 0) {
                dateFormat = new SimpleDateFormat("h:mm a");
            } else if (currentDays - messageDays < 365) {
                dateFormat = new SimpleDateFormat("MMM dd");
            } else {
                dateFormat = new SimpleDateFormat("yyyy MMM");
            }
            time = dateFormat.format(date.getTime());
        }
        holder.tvTime.setText(time);
        Bitmap bmp = null;
        for(Map.Entry<Integer, Bitmap> entry: SupportChat.images.entrySet())
        {
            if(entry.getKey().equals(tickets.get(position).getMessage().getUser().getId())){
                bmp = entry.getValue();
            }
        }
        holder.img.setImageBitmap(bmp);

        holder.container.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return tickets.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    @Override
    public int compare(Ticket o1, Ticket o2) {
        DateFormat format = new SimpleDateFormat(ctx.getString(R.string.date_format));
        try {
            Date date1 = format.parse(o1.getMessage().getDate());
            Date date2 = format.parse(o2.getMessage().getDate());
            return date1.compareTo(date2)*(-1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void UpdateTicketById(Ticket ticket){
        for(Ticket t: tickets){
            if(t.getId() == ticket.getId())
                t = ticket;
        }
        Collections.sort(this.tickets, this);
    }

    public Ticket getTicketById(long id){
        for(Ticket t: tickets){
            if(t.getId() == id)
                return t;
        }
        return null;
    }



}
