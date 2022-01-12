package com.example.addverb.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.addverb.R;
import com.example.addverb.entities.entity;
import com.example.addverb.entities.entityForRecyclerview;
import com.squareup.picasso.Picasso;

import java.util.List;

public class adapter extends RecyclerView.Adapter<adapter.myViewHolder> {
    Context context;
    List<entityForRecyclerview> list;
    List<entity> list2;

    public adapter(Context context, List<entityForRecyclerview> list,List<entity> list2) {
        this.context = context;
        this.list = list;
        this.list2 = list2;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.country_details,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        if(list==null){
            entity entity=list2.get(position);
            holder.county.setText(entity.getName());
            holder.capital.setText(entity.getCapital());
            holder.languages.setText(entity.getLanguages());
            holder.borders.setText(entity.getBorders());
            holder.region.setText(entity.getRegion());
            holder.population.setText(entity.getPopulation());
            holder.subregion.setText(entity.getSubregion());
            if(!(entity.getFlag()==null)) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(entity.getFlag(), 0, entity.getFlag().length);
                holder.flag.setImageBitmap(bitmap);
            }
        }

        else{
            entityForRecyclerview entity=list.get(position);
            holder.county.setText(entity.getName());
            holder.capital.setText(entity.getCapital());
            holder.languages.setText(entity.getLanguages());
            holder.borders.setText(entity.getBorders());
            holder.region.setText(entity.getRegion());
            holder.population.setText(entity.getPopulation());
            holder.subregion.setText(entity.getSubregion());
            Picasso.get().load(entity.getFlag()).into(holder.flag);
        }





    }

    @Override
    public int getItemCount() {
        return list==null?list2.size():list.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView county,capital,region,subregion,borders,languages,population;
        ImageView flag;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            county=itemView.findViewById(R.id.country);
            capital=itemView.findViewById(R.id.capital);
            languages=itemView.findViewById(R.id.languages);
            region=itemView.findViewById(R.id.region);
            borders=itemView.findViewById(R.id.borders);
            subregion=itemView.findViewById(R.id.subregion);
            population=itemView.findViewById(R.id.population);
            flag=itemView.findViewById(R.id.flag);
        }
    }
}
