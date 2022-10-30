package com.example.artzoneapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemRowAdapter extends RecyclerView.Adapter<ItemRowAdapter.MyViewHolder>{

    Context context;
    ArrayList<ItemRow> itemRowTittleArray;
    private final ListRinterface listRinterface;

    public ItemRowAdapter(Context context , ArrayList<ItemRow> itemRowTittleArray,ListRinterface listRinterface){

        this.context = context;
        this.itemRowTittleArray = itemRowTittleArray;
        this.listRinterface = listRinterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Giving a look to our rows
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.itemrow , parent , false);
        return new MyViewHolder(view,listRinterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //assign values to the views we are created in the itemRow.xml layout file
        //based on the position of the recycler view

        holder.ttl.setText(itemRowTittleArray.get(position).getName());
    }

    @Override
    public int getItemCount() {
        //the recycler view just want to know the number of items you want to display
        return itemRowTittleArray.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        //grabbing the views from itemRow.xml layout file

        TextView ttl ;
        CardView cv;

        public MyViewHolder(@NonNull View itemView,ListRinterface listRinterface) {
            super(itemView);
            ttl = itemView.findViewById(R.id.textView1);
            cv = itemView.findViewById(R.id.listcard);

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listRinterface!=null){
                        int pos = getAdapterPosition();

                        if(pos!=RecyclerView.NO_POSITION){
                            listRinterface.onlistitemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
