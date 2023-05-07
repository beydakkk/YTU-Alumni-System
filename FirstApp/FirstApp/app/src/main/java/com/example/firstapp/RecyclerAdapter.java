package com.example.firstapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    Context context;
    ArrayList<RecyclerModel> modelArrayList;

    public RecyclerAdapter(Context context, ArrayList<RecyclerModel> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.rc_item,parent,false);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        RecyclerModel model = modelArrayList.get(position);
        holder.title.setText(model.title);
        holder.announce.setText(model.announce);
        holder.date.setText(model.date);
        if(model.getImage() != null)
            Glide.with(context).load(model.getImage()).into(holder.announceImage);
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        ImageView announceImage;
        TextView title, announce, date;
        public RecyclerViewHolder(@NonNull View itemView){
            super(itemView);

            announceImage = itemView.findViewById(R.id.pp_image);
            title = itemView.findViewById(R.id.text_name);
            announce = itemView.findViewById(R.id.rc_announce);
            date = itemView.findViewById(R.id.rc_date);
        }
    }

}
