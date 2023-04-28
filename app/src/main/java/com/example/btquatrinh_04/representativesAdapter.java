package com.example.btquatrinh_04;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btquatrinh_04.model.OnItemClickListener;
import com.example.btquatrinh_04.model.representatives;

import java.util.List;

public class representativesAdapter extends RecyclerView.Adapter<representativesAdapter.representativesViewHolder>{

    private List<representatives> replist;
    private OnItemClickListener onListener;

    public representativesAdapter(List<representatives> replist, OnItemClickListener lis) {
        this.replist = replist;
        this.onListener = lis;
    }

    @NonNull
    @Override
    public representativesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vie= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview,parent,false);
        return new representativesViewHolder(vie);
    }

    @Override
    public void onBindViewHolder(@NonNull representativesViewHolder holder, int position) {
        holder.binData(replist.get(position));
    }

    @Override
    public int getItemCount() {
        if (replist!=null){
            return replist.size();
        }
        return 0;
    }

    class representativesViewHolder extends RecyclerView.ViewHolder{
        private TextView name, office;
        private representatives repre;

        public representativesViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onListener.OnItemClick(repre);
                }
            });
            name= itemView.findViewById(R.id.txtName);
            office= itemView.findViewById(R.id.txtOffice);
        }

        private void binData(representatives repbin){
            this.repre= repbin;
            name.setText(repbin.getName());
            office.setText(repbin.getOffice());
        }
    }
}
