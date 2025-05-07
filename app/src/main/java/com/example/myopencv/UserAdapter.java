package com.example.myopencv;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends PagedListAdapter<User, UserAdapter.UserViewHolder> {
    private OnItemClickListener listener;
//    private List<User> users = new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

//    public void submitList(List<User> userList) {
//        users = new ArrayList<>(userList); // ensure immutable copy
//        notifyDataSetChanged();
//    }


    public UserAdapter() {
        super(DIFF_CALLBACK);
    }

    private static DiffUtil.ItemCallback<User> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<User>() {
                @Override
                public boolean areItemsTheSame(User oldItem, User newItem) {
                    return oldItem.id == newItem.id;
                }

                @Override
                public boolean areContentsTheSame(User oldItem, User newItem) {
                    return oldItem.name.equals(newItem.name);
                }
            };

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = getItem(position);
        if (user != null) {
            holder.bind(user, listener);
        }
    }


    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }

        public void bind(final User user, final OnItemClickListener listener) {
            textView.setText("[" + user.id + "] " + user.name);
            itemView.setOnClickListener(v -> listener.onItemClick(user));
        }

    }
}
