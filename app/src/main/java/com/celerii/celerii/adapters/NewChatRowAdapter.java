package com.celerii.celerii.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.celerii.celerii.Activities.Inbox.ChatActivity;
import com.celerii.celerii.R;
import com.celerii.celerii.models.NewChatRowModel;
import com.bumptech.glide.Glide;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by DELL on 6/5/2018.
 */

public class NewChatRowAdapter extends RecyclerView.Adapter<NewChatRowAdapter.MyViewHolder>{

    private List<NewChatRowModel> newChatRowModelList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, relationship;
        public ImageView profilePic;
        public View clickableView;

        public MyViewHolder(final View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            relationship = (TextView) view.findViewById(R.id.relationship);
            profilePic = (ImageView) view.findViewById(R.id.pic);
            clickableView = view;
        }
    }

    public NewChatRowAdapter(List<NewChatRowModel> newChatRowModelList, Context context) {
        this.newChatRowModelList = newChatRowModelList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_chat_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final NewChatRowModel newChatRowModel = newChatRowModelList.get(position);

        holder.name.setText(newChatRowModel.getName());
        holder.relationship.setText(newChatRowModel.getRelationship());

        Glide.with(context)
                .load(newChatRowModel.getProfilePicURL())
                .centerCrop()
                .bitmapTransform(new CropCircleTransformation(context))
                .into(holder.profilePic);

        holder.clickableView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(context, ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ID", newChatRowModel.getIDofPartner());
                bundle.putString("name", newChatRowModel.getName());
                I.putExtras(bundle);
                context.startActivity(I);
                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return newChatRowModelList.size();
    }
}