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
import com.young.newsgathering.MainActivity;
import com.young.newsgathering.R;
import com.young.newsgathering.ToastUtils;
import com.young.newsgathering.UserUtil;
import com.young.newsgathering.entity.User;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends Fragment {
    private static final int REQUEST_CODE_CHOOSE = 1002;
    private ImageView avatarImage;

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
        initView(view);
        initEvent();
        return view;
    }

    private void initView(View view) {
        User user = UserUtil.getInstance().getUser();
        ((TextView) view.findViewById(R.id.name_text)).setText(user.getName());
        ((TextView) view.findViewById(R.id.job_text)).setText(user.getJob());
        ((TextView) view.findViewById(R.id.phone_text)).setText(user.getPhone());

        avatarImage = view.findViewById(R.id.avatar_image);
        Glide.with(avatarImage.getContext())
                .load(UserUtil.getInstance().getUser().getAvatar())
                .into(avatarImage);
        avatarImage.setOnClickListener(v -> open());
        view.findViewById(R.id.reset_layout).setOnClickListener(v -> {

        });
        view.findViewById(R.id.login_out_btn).setOnClickListener(v -> {
            UserUtil.getInstance().logout();
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        });
    }



    private void initEvent() {

    }
    private void open() {
        Matisse.from(this)
                .choose(MimeType.ofImage())
                .showSingleMediaType(true)
                .capture(false)
                .maxSelectable(1)
                .theme(R.style.Matisse_Zhihu)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && data != null) {
            //图片路径 同样视频地址也是这个 根据requestCode
            String url = Matisse.obtainPathResult(data).get(0);
            uploadAvatar(url);
        }
    }

    private void uploadAvatar(String url) {
        BmobFile bmobFile = new BmobFile(new File(url));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    updateAvatar(bmobFile.getFileUrl());
                }
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
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
