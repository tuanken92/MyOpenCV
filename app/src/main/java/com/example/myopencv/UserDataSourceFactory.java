package com.example.myopencv;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import java.util.ArrayList;
import java.util.List;

public class UserDataSourceFactory extends DataSource.Factory<Integer, User>{
    private String currentQuery = "";
    private List<User> fullUserList;
    private MutableLiveData<UserDataSource> userDataSourceLiveData = new MutableLiveData<>();

    public UserDataSourceFactory(List<User> fullUserList) {
        this.fullUserList = fullUserList;
    }

    public void updateListData(List<User> listUpdated)
    {
        this.fullUserList = listUpdated;
    }
    public void setSearchQuery(String query) {
        currentQuery = query.toLowerCase();
    }

    private List<User> filterUsers(List<User> users, String query) {
        if (query == null || query.isEmpty()) return new ArrayList<>(users);
        List<User> filtered = new ArrayList<>();
        for (User user : users) {
            if (user.name.toLowerCase().contains(query)) {
                filtered.add(user);
            }
        }
        return filtered;
    }

    public MutableLiveData<UserDataSource> getDataSourceLiveData() {
        return userDataSourceLiveData;
    }

    @NonNull
    @Override
    public DataSource<Integer, User> create() {
        List<User> filteredList = filterUsers(fullUserList, currentQuery);
        UserDataSource dataSource = new UserDataSource(filteredList);
        userDataSourceLiveData.postValue(dataSource);
        return dataSource;
    }
}
