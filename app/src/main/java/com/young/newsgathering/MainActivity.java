package com.young.newsgathering;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.young.newsgathering.entity.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends BaseActivity {
    private TextView loginBtn;
    private EditText accountEdit;
    private EditText pwdEdit;

    @Override
    protected void initView() {
        loginBtn = findViewById(R.id.login_btn);
        accountEdit = findViewById(R.id.edit_user_account);
        pwdEdit = findViewById(R.id.edit_user_pwd);
    }

    @Override
    protected void initEvent() {
        loginBtn.setOnClickListener(v -> {
            login();
        });
    }

    private void login() {
        String account = accountEdit.getText().toString().trim();
        String pwd = pwdEdit.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            ToastUtils.showShort("请输入账号");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showShort("请输入密码");
            return;
        }
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("account", account);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> userList, BmobException e) {
                if (e == null) {
                    if (userList.size() > 0) {
                        User user = userList.get(0);
                        if (user.getPwd().equals(pwd)) {
                            UserUtil.getInstance().save(user);
                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        } else {
                            ToastUtils.showShort("密码错误");
                        }
                    } else {
                        ToastUtils.showShort("该账号不存在");
                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}
