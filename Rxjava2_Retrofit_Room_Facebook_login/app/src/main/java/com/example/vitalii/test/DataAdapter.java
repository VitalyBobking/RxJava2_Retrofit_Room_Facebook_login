package com.example.vitalii.test;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vitalii.test.model.Person;
import com.example.vitalii.test.service.RecyclerViewClickListener;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder>{
    private List<Person> people;
    private RecyclerViewClickListener listener;

    public DataAdapter(List<Person> people, RecyclerViewClickListener listener) {
        this.people = people;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DataAdapter.ViewHolder viewHolder, int i) {

        Person person = people.get(i);

        viewHolder.tv_name.setText("Name: " + person.getName());
        viewHolder.tv_userName.setText("User Name: " + person.getUsername());
        viewHolder.tv_email.setText("Email: " + person.getEmail());
        viewHolder.tv_phone.setText("Phone: " + person.getPhone());

        viewHolder.itemView.setOnClickListener((View v) -> {
            listener.onItemClick(v, person);
        });
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name,tv_email, tv_phone, tv_userName;

            ViewHolder(View view) {
            super(view);

            tv_name = view.findViewById(R.id.tv_name);
            tv_email = view.findViewById(R.id.tv_email);
            tv_phone = view.findViewById(R.id.tv_phone);
            tv_userName = view.findViewById(R.id.tv_userName);

        }
    }


}
