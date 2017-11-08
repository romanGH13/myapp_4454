package com.example.eqvol.eqvola.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
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

public class TicketsAdapter extends BaseAdapter implements Comparator<Ticket> {

    Context ctx;
    LayoutInflater lInflater;
    List<Ticket> tickets;


    public TicketsAdapter(@NonNull Context context, List<Ticket> tickets) {
        //super(context, R.layout.fragment_support_chat, tickets);
        this.ctx = context;
        this.tickets = tickets;
        Collections.sort(this.tickets, this);
        this.lInflater = LayoutInflater.from(this.ctx);// ctx.getSystemService(Context.);
        //this.images = new HashMap<Integer, Bitmap>();
    }


    @Override
    public int getCount() {
        return tickets.size();
    }

    @Override
    public Ticket getItem(int position) {
        return tickets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tickets.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Ticket ticket = getItem(position);
        Message last_message = ticket.getMessage();

        if (convertView == null) {
            convertView = lInflater.inflate(R.layout.list_ticket_item, null);
        }
        TextView tvTitle = ((TextView) convertView.findViewById(R.id.support_chat_title));
        TextView tvMessage = ((TextView) convertView.findViewById(R.id.support_chat_last_message));
        ImageView img = (ImageView)convertView.findViewById(R.id.support_chat_ticket_image);

        String title = ticket.getTitle();
        int userId = last_message.getUser().getId();
        User user = last_message.getUser();

        /*for(User u: Api.users)
        {
            if(u.getId() == userId)
            {
                user = u;
                break;
            }
        }

        //String userName = user.getName();
        byte[] avatar = user.getAvatar();
        if(avatar != null) {
            Bitmap bitmap;
            if(this.images.containsKey(user.getId())) {
                bitmap = this.images.get(user.getId());

            } else{
                byte[] image = avatar;
                bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                bitmap = bitmap.createScaledBitmap(bitmap, 70, 70, false);
                this.images.put(user.getId(), bitmap);
            }
            img.setImageBitmap(bitmap);
        }*/
        tvMessage.setText(last_message.getMessage());
        tvTitle.setText(title);

        //tv.setText("1:"+name);
        /*if(Api.user.getCountry() == country.getName())
        {
            parent.dispatchSetSelected(true);
        }*/
        return convertView;
    }


    @Override
    public void notifyDataSetChanged() {
        //this.n(false);
        Collections.sort(this.tickets, this);
        /*this.sort(new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        });*/

        //this.setNotifyOnChange(true);
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
}
