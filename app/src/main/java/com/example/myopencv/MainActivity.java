package com.example.myopencv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    String TAG = "TuanNA";
    RecyclerView recyclerView;
    UserManualAdapter adapter;
    List<User> mListUsers;

    SwipeRefreshLayout swipeRefreshLayout;


    private int currentPage = 1;
    private int pageSize = 10;
    private int totalPages = 1;

    private Spinner pageSizeSpinner;
    private Button pageIndicator, btnFirst, btnNext, btnPre, btnLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //gen list
        mListUsers = getListUsers();

        //recycle
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //adapter
        adapter = new UserManualAdapter();
        adapter.setOnItemClickListener(new UserManualAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(User user) {
                Toast.makeText(getApplicationContext(), "Detail: user = " + user.name, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);


        //Reload data
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.swipe_color_1, R.color.swipe_color_2,
                R.color.swipe_color_3, R.color.swipe_color_4);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "onRefresh called from SwipeRefreshLayout");
                initiateRefresh();
            }
        });

        //ui
        pageSizeSpinner = findViewById(R.id.page_size_spinner);
        pageIndicator = findViewById(R.id.btnPageIndicator);
        btnFirst = findViewById(R.id.btnFirst);
        btnLast = findViewById(R.id.btnLast);
        btnPre = findViewById(R.id.btnPre);
        btnNext = findViewById(R.id.btnNext);

        setupSpinner();
        setupButtons();
        updatePagination();

    }

    private void setupSpinner() {
        pageSizeSpinner.setSelection(0);
        pageSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pageSize = Integer.parseInt(parent.getItemAtPosition(position).toString());
                currentPage = 1;
                updatePagination();
            }

            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupButtons() {
        findViewById(R.id.btnFirst).setOnClickListener(v -> {
            currentPage = 1;
            updatePagination();
        });

        findViewById(R.id.btnPre).setOnClickListener(v -> {
            if (currentPage > 1) {
                currentPage--;
                updatePagination();
            }
        });

        findViewById(R.id.btnNext).setOnClickListener(v -> {
            if (currentPage < totalPages) {
                currentPage++;
                updatePagination();
            }
        });

        findViewById(R.id.btnLast).setOnClickListener(v -> {
            currentPage = totalPages;
            updatePagination();
        });
    }


    private void updatePagination() {
        if (mListUsers == null || mListUsers.isEmpty()) return;

        totalPages = (int) Math.ceil((double) mListUsers.size() / pageSize);
        currentPage = Math.max(1, Math.min(currentPage, totalPages));  // Clamp

        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, mListUsers.size());

        List<User> sublist = mListUsers.subList(start, end);
        adapter.submitList(sublist);  // adapter handles copy and notify

        pageIndicator.setText("Page " + currentPage + " / " + totalPages);
    }




    private void initiateRefresh() {
        Log.i(TAG, "initiateRefresh");

        /**
         * Execute the background task, which uses {@link android.os.AsyncTask} to load the data.
         */
        new DummyBackgroundTask().execute();
    }

    private class DummyBackgroundTask extends AsyncTask<Void, Void, List<String>> {

        static final int TASK_DURATION = 2 * 1000; // 3 seconds

        @Override
        protected List<String> doInBackground(Void... params) {
            // Sleep for a small amount of time to simulate a background-task
            try {
                Thread.sleep(TASK_DURATION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Return a new random list of cheeses
            return null;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            super.onPostExecute(result);

            // Tell the Fragment that the refresh has completed
            onRefreshComplete(result);
        }
    }

    private void onRefreshComplete(List<String> result) {
        Log.i(TAG, "onRefreshComplete");

        //guestAdapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(), "Updated!", Toast.LENGTH_SHORT).show();

        // Stop the refreshing indicator
        swipeRefreshLayout.setRefreshing(false);
    }


    List<User> getListUsers(){
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 300000; i++) {
            int id = i + 1;
            users.add(new User(id, "User " + id));
        }
        return  users;
    }


}