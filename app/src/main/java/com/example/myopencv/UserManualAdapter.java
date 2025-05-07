package com.example.myopencv;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserManualAdapter extends RecyclerView.Adapter<UserManualAdapter.UserViewHolder> {
    private OnItemClickListener listener;
    private List<User> users = new ArrayList<>();

    @NonNull
    @Override
    public UserManualAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserManualAdapter.UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.bind(user, listener);
    }

    public void submitList(List<User> list) {
        users = new ArrayList<>(list);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return users.size();
    }

    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }

        public void bind(final User user, final UserManualAdapter.OnItemClickListener listener) {
            textView.setText("[" + user.id + "] " + user.name);
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(user);
                }
            });
        }

    }
}
