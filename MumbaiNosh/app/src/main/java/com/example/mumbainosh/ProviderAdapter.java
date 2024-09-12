package com.example.mumbainosh;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProviderAdapter extends RecyclerView.Adapter<ProviderAdapter.ProviderViewHolder> {

    private List<Provider> providerList;

    public ProviderAdapter(List<Provider> providerList) {
        this.providerList = providerList;
    }

    @NonNull
    @Override
    public ProviderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.provider_item, parent, false);
        return new ProviderViewHolder(view);
    }

    // Inside ProviderAdapter class, update onBindViewHolder method
    @Override
    public void onBindViewHolder(@NonNull ProviderViewHolder holder, int position) {
        Provider provider = providerList.get(position);
        holder.nameTextView.setText(provider.name);
        holder.addressTextView.setText(provider.address);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), RestroDashboard.class);
            intent.putExtra("TIMING", provider.timing);
            holder.itemView.getContext().startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return providerList.size();
    }

    public static class ProviderViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView addressTextView;

        public ProviderViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.providerName);
            addressTextView = itemView.findViewById(R.id.providerAddress);
        }
    }
}
