package com.example.malihakhizer.chatapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.malihakhizer.chatapp.Model.Message;
import com.example.malihakhizer.chatapp.Model.User;
import com.example.malihakhizer.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    private static final int MESSAGE_TYPE_THEIR = 0;
    private static final int MESSAGE_TYPE_MY = 1;

    String image_url;
    ArrayList<Message> mChats;
    Context context;

    FirebaseUser fUser;


    public MessageAdapter(ArrayList<Message> chats, Context c,String url) {
        this.context = c;
        this.mChats = chats;
        this.image_url = url;
    }

    @NonNull
    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == MESSAGE_TYPE_MY) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_message, parent, false);
            return (new MessageAdapter.MyViewHolder(rootView));
        } else {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.their_message, parent, false);
            return (new MessageAdapter.MyViewHolder(rootView));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyViewHolder holder, int position) {
        Message message = mChats.get(position);
        holder.messageBody.setText(message.getMessage());

        if(image_url.equals("default")){
            holder.profileImg.setImageResource(R.mipmap.ic_launcher);
        } else {
            if (!image_url.equals("")){
                Picasso.with(context).load(image_url).into(holder.profileImg);
            }
        }

    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    @Override
    public int getItemViewType(int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChats.get(position).getSender().equals(fUser.getUid())){
            return MESSAGE_TYPE_MY;
        } else {
            return MESSAGE_TYPE_THEIR;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView messageBody;
        CircleImageView profileImg;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            messageBody = itemView.findViewById(R.id.show_message);
            profileImg = itemView.findViewById(R.id.profile_image);

        }

    }


}