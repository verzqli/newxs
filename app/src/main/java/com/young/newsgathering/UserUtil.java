package com.young.newsgathering;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.young.newsgathering.entity.User;

import static android.content.Context.MODE_PRIVATE;

/**
 * 用户信息简单工具类
 */
public class UserUtil {
    //工具类里面保存了一个用户信息类
    private User user;
    private static UserUtil userUtil = new UserUtil();

    public static UserUtil getInstance() {
        return userUtil;
    }

    /**
     * 将从服务器获取到的用户信息保存在本地，方便下次不用重新登录
     * @param user
     */
    public void save(User user) {
        this.user = user;
        SharedPreferences spf = Utils.getApp().getSharedPreferences("news", 0);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("user", new Gson().toJson(user));
        editor.apply();
    }

    /**
     * 如果用户信息类不为null。就直接返回，如果为null，就从本地SharedPreferences拿
     * @return
     */
    public User getUser() {
        if (user != null) {
            return user;
        }
        SharedPreferences sp = Utils.getApp().getSharedPreferences("news", 0);
        String userJson = sp.getString("user", "");
        Log.i("aaa", "userJosn" + userJson);
        if (TextUtils.isEmpty(userJson)) {
            return null;
        } else {
            user = new Gson().fromJson(userJson, User.class);
            return user;
        }
    }

    public void updateAvatar(String url) {
        this.user.setAvatar(url);
        save(this.user);
    }

    /**
     * 退出登录时要清空本地数据。
     */
    public void logout() {
        this.user = null;
        SharedPreferences spf = Utils.getApp().getSharedPreferences("news", 0);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("user", "");
        editor.apply();
    }

    public boolean isAdmin() {
        return getUser().getType().equals("管理员");
    }
}
