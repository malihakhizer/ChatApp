package com.example.malihakhizer.chatapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.malihakhizer.chatapp.Model.User;
import com.example.malihakhizer.chatapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    ArrayList<User> users;
    Context context;
    OnItemListener onItemListener;

    public UserAdapter(ArrayList<User> users,Context c,OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
        this.context = c;
        this.users = users;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return (new MyViewHolder(rootView,onItemListener));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.username.setText(users.get(position).getName());
        String url = users.get(position).getUrl();
        if (!url.equals("")){
            Picasso.with(context).load(url).into(holder.profilePic);
        }


    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CircleImageView profilePic;
        TextView username;
        OnItemListener onItemListener;

        public MyViewHolder(@NonNull View itemView,OnItemListener onItemListener) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.username);
            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemListener.onClick(v, getAdapterPosition());
        }
    }

    public interface OnItemListener{
        public void onClick(View view, int position);
    }
}
