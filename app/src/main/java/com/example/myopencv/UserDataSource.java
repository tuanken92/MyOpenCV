package com.example.myopencv;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserDataSource extends PageKeyedDataSource<Integer, User> {

    String TAG = "TuanNA-UserDataSource";

    public static final int PAGE_SIZE = 20;
    public static final int FIRST_PAGE = 1;

    private final List<User> fullUserList;

    public UserDataSource(List<User> fullUserList) {
        this.fullUserList = fullUserList;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, User> callback) {
        Log.d(TAG, "loadInitial => get first page!");
        List<User> page = getPage(FIRST_PAGE);
        callback.onResult(page, null, FIRST_PAGE + 1);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, User> callback) {
        Log.d(TAG, "loadBefore => Not needed unless backward paging!");
        // Optional: Not needed unless backward paging
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, User> callback) {
        Log.d(TAG, "loadAfter! params key = " + params.key);
        List<User> page = getPage(params.key);
        if (!page.isEmpty()) {
            callback.onResult(page, params.key + 1);
        }

    }

    private List<User> getPage(int page) {
        int fromIndex = (page - 1) * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, fullUserList.size());
        if (fromIndex >= fullUserList.size()) return Collections.emptyList();
        return fullUserList.subList(fromIndex, toIndex);
    }
}
