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

public class SearchRcAdapter extends RecyclerView.Adapter<SearchRcAdapter.SearchRcViewHolder>{
    final RC_Interface rc_interface;

    Context context;
    ArrayList<SearchRecyclerModel> modelArrayList;

    public SearchRcAdapter(Context context, ArrayList<SearchRecyclerModel> modelArrayList,RC_Interface rc_interface) {
        this.context = context;
        this.modelArrayList = modelArrayList;
        this.rc_interface = rc_interface;
    }
    @NonNull
    @Override
    public SearchRcViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.shape_rc_item,parent,false);
        return new SearchRcAdapter.SearchRcViewHolder(view,rc_interface);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchRcViewHolder holder, int position) {
        SearchRecyclerModel model = modelArrayList.get(position);
        holder.name.setText(model.name);
        holder.surname.setText(model.surname);
        if(model.getPhotoUri() != null)
            Glide.with(context).load(model.getPhotoUri()).into(holder.photoUri);
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class SearchRcViewHolder extends RecyclerView.ViewHolder{

        ImageView photoUri;
        TextView name, surname;
        public SearchRcViewHolder(@NonNull View itemView,RC_Interface rc_interface){
            super(itemView);
            photoUri = itemView.findViewById(R.id.pp_image);
            name = itemView.findViewById(R.id.text_name);
            surname = itemView.findViewById(R.id.text_surname);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(rc_interface != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            rc_interface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
