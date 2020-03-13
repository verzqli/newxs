package com.young.newsgathering;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.young.newsgathering.entity.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ResetPwdActivity extends BaseActivity {
    private EditText oldPwdEdit;
    private EditText newPwdEdit;
    private EditText confirmPwdEdit;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reset_pwd;
    }

    @Override
    protected void initView() {
        setToolBar("设置密码", "完成");
        oldPwdEdit = findViewById(R.id.old_pwd);
        newPwdEdit = findViewById(R.id.new_pwd);
        confirmPwdEdit = findViewById(R.id.confirm_pwd);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void menuClick() {
        String oldPwd = oldPwdEdit.getText().toString().trim();
        String newPwd = newPwdEdit.getText().toString().trim();
        String confirmPwd = confirmPwdEdit.getText().toString().trim();
        if (TextUtils.isEmpty(oldPwd) || TextUtils.isEmpty(newPwd) || TextUtils.isEmpty(confirmPwd)) {
            ToastUtils.showShort("密码不能为空");
            return;
        }
        if (!oldPwd.equals(UserUtil.getInstance().getUser().getPwd())) {
            ToastUtils.showShort("您输入的原密码错误");
            return;
        }
        if (!newPwd.equals(confirmPwd)) {
            ToastUtils.showShort("两次输入的密码不一致");
            return;
        }
        User user = UserUtil.getInstance().getUser();
        user.setPwd(newPwd);
        user.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ToastUtils.showShort("修改成功");
                    UserUtil.getInstance().logout();
                    baseStartActivity( MainActivity.class);
                    finish();
                } else {
                    Log.e("BMOB", e.toString());
                    ToastUtils.showShort("修改失败，请重试");
                }
            }
        });
    }
}
