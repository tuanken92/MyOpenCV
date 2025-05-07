package com.example.myopencv;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class UserDetailBottomSheet extends BottomSheetDialogFragment {

        private static final String ARG_ID = "user_id";
        private static final String ARG_NAME = "user_name";

        public static UserDetailBottomSheet newInstance ( int id, String name){
            UserDetailBottomSheet fragment = new UserDetailBottomSheet();
            Bundle args = new Bundle();
            args.putInt(ARG_ID, id);
            args.putString(ARG_NAME, name);
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView (
                @NonNull LayoutInflater inflater,
                @Nullable ViewGroup container,
                @Nullable Bundle savedInstanceState
    ){
            View view = inflater.inflate(R.layout.bottom_sheet_user_detail, container, false);

            TextView detailText = view.findViewById(R.id.detail_text);

            if (getArguments() != null) {
                int userId = getArguments().getInt(ARG_ID);
                String userName = getArguments().getString(ARG_NAME);
                detailText.setText("ID: " + userId + "\nName: " + userName);
            }

            return view;
        }

    }
