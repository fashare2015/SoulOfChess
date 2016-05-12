package com.example.fashare.soulofchess.model;

import android.util.Log;

import java.util.List;

/**
 * Created by apple on 2016/3/4.
 */
public class UserCenter {
    private User user;
    private static UserCenter instance;
    private UserCenter (){}
    public static synchronized UserCenter getInstance() {
        if (instance == null)
            instance = new UserCenter();
        return instance;
    }

    public User getUser() {
//        Log.d("getUser", user.toString());
        return user;
    }

    public void setUser(User user) {
//        Log.d("setUser", user.toString());
        this.user = user;
    }
}
