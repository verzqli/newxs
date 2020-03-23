package com.young.newsgathering.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gyf.immersionbar.ImmersionBar;
import com.young.newsgathering.MainActivity;
import com.young.newsgathering.R;
import com.young.newsgathering.ResetPwdActivity;
import com.young.newsgathering.ToastUtils;
import com.young.newsgathering.UserUtil;
import com.young.newsgathering.entity.User;
import com.young.newsgathering.util.BlurTransformation;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends Fragment {
    private static final int REQUEST_CODE_CHOOSE = 1002;
    private ImageView avatarImage;
    private ImageView avatarBlurImage;
    public PersonalFragment() {
        // Required empty public constructor
    }

    public static PersonalFragment newInstance() {
        return new PersonalFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        ImmersionBar.with(this).statusBarDarkFont( true).init();
        initView(view);
        initEvent();
        return view;
    }

    private void initView(View view) {
        //获取内存中的用户信息
        User user = UserUtil.getInstance().getUser();
        ((TextView) view.findViewById(R.id.name_text)).setText(user.getName());
        ((TextView) view.findViewById(R.id.job_text)).setText(user.getJob());
        ((TextView) view.findViewById(R.id.phone_text)).setText(user.getPhone());

        avatarImage = view.findViewById(R.id.avatar_image);
        avatarBlurImage=view.findViewById(R.id.avatar_blur_bg);
        //加载用户头像
        Glide.with(avatarImage.getContext())
                .load(UserUtil.getInstance().getUser().getAvatar())
                .placeholder(R.drawable.icon_avatar)
                .into(avatarImage);
        Glide.with(avatarBlurImage.getContext())
                .load(UserUtil.getInstance().getUser().getAvatar())
                .transform(new BlurTransformation())
                .placeholder(R.drawable.icon_avatar)
                .into(avatarBlurImage);
        //点击头像进入相册
        avatarImage.setOnClickListener(v -> open());
        //跳转设置密码
        view.findViewById(R.id.reset_layout).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ResetPwdActivity.class));
        });
        //退出登录
        view.findViewById(R.id.login_out_btn).setOnClickListener(v -> {
            //删除本地保存的用户信息
            UserUtil.getInstance().logout();
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        });
    }


    private void initEvent() {

    }

    private void open() {
        //相册框架
        Matisse.from(this)
                .choose(MimeType.ofImage())//只选择图片
                .showSingleMediaType(true)//只显示单一的类型，这里因为上面选择了图片，所以只显示图片
                .capture(false)//不显示拍照功能
                .maxSelectable(1)//最多显示一张
                .theme(R.style.Matisse_Zhihu)//相册UI主题
                .imageEngine(new GlideEngine())//用glide加载图片
                .forResult(REQUEST_CODE_CHOOSE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && data != null) {
            //图片路径 同样视频地址也是这个 根据requestCode
            String url = Matisse.obtainPathResult(data).get(0);
            //选择图片后保存在本地后再上传到服务器（这里上传的是本地链接）
            UserUtil.getInstance().updateAvatar(url);
            Glide.with(avatarImage.getContext())
                    .load(UserUtil.getInstance().getUser().getAvatar())
                    .placeholder(R.drawable.icon_avatar)
                    .into(avatarImage);
            Glide.with(avatarBlurImage.getContext())
                    .load(UserUtil.getInstance().getUser().getAvatar())
                    .transform(new BlurTransformation())
                    .placeholder(R.drawable.icon_avatar)
                    .into(avatarBlurImage);
            updateAvatar(url);
        }
    }

    private void updateAvatar(String fileUrl) {
        User user = UserUtil.getInstance().getUser();
        user.setAvatar(fileUrl);
        user.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ToastUtils.showShort("更新成功");
                } else {
                    Log.e("BMOB", e.toString());
                    ToastUtils.showShort("更新失败，请重试");
                }
            }
        });
    }
}
