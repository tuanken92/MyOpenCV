package com.example.myopencv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.icu.text.LocaleDisplayNames;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    String TAG = "TuanNA";
    RecyclerView recyclerView;
    UserAdapter adapter;
    List<User> mListUsers = new ArrayList<>();



    UserDataSourceFactory factory;
    //LiveData<PagedList<User>> pagedListLiveData;

    //swipte to refresh
    SwipeRefreshLayout swipeRefreshLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        //gen list
//        mListUsers = getListUsers();

        //recycle
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //adapter
        adapter = new UserAdapter();
        adapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(User user) {
                Toast.makeText(getApplicationContext(), "Detail: user = " + user.name, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);

        factory = new UserDataSourceFactory(mListUsers);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(UserDataSource.PAGE_SIZE)
                .build();

        new LivePagedListBuilder<>(factory, config).build()
            .observe(this, pagedList -> adapter.submitList(pagedList));

        setupSearch();

        //Reload data
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.swipe_color_1, R.color.swipe_color_2,
                R.color.swipe_color_3, R.color.swipe_color_4);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "onRefresh called from SwipeRefreshLayout");
                /**
                 * Execute the background task, which uses {@link android.os.AsyncTask} to load the data.
                 */
                new DummyBackgroundTask().execute();
            }
        });



    }





    private class DummyBackgroundTask extends AsyncTask<Void, Void, List<String>> {

        static final int TASK_DURATION = 2 * 1000; // 3 seconds

        @Override
        protected List<String> doInBackground(Void... params) {
            // Sleep for a small amount of time to simulate a background-task
            try {
                //Thread.sleep(TASK_DURATION);
                //gen list
                mListUsers = getListUsers();


            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }

            // Return a new random list of cheeses
            return null;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            super.onPostExecute(result);

            // Tell the Fragment that the refresh has completed
            factory.updateListData(mListUsers); // refresh the factory’s internal list

            //Rebuild the LivePagedListBuilder
            PagedList.Config config = new PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setPageSize(UserDataSource.PAGE_SIZE)
                    .build();

            //Submit it again to the adapter.
            new LivePagedListBuilder<>(factory, config)
                    .build()
                    .observe(MainActivity.this, users -> adapter.submitList(users));

            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getApplicationContext(), "Updated!", Toast.LENGTH_SHORT).show();
        }
    }


    private void setupSearch() {
        EditText searchInput = findViewById(R.id.edtSearch);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                search(s.toString());
            }
        });
    }

    private void search(String query) {
        factory.setSearchQuery(query);

        // Rebuild the PagedList with filtered data
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(UserDataSource.PAGE_SIZE)
                .build();

        new LivePagedListBuilder<>(factory, config)
                .build()
                .observe(this, users -> adapter.submitList(users));
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