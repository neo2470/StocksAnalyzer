package com.alex.develop.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alex.develop.entity.CircleDrawable;
import com.alex.develop.stockanalyzer.R;

/**
 * Created by alex on 15-11-16.
 * 登录界面
 */
public class SignInFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_in_fragment, container, false);

        ImageView avaterView = (ImageView) view.findViewById(R.id.avaterView);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avater);
        CircleDrawable drawable = new CircleDrawable(bitmap);
        avaterView.setImageDrawable(drawable);

        final SharedPreferences mPref = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
        String username = mPref.getString(getString(R.string.sign_in_key_username), "");
        String userpwd = mPref.getString(getString(R.string.sign_in_key_userpwd), "");

        final EditText userId = (EditText) view.findViewById(R.id.userId);
        final EditText userPwd = (EditText) view.findViewById(R.id.userPwd);
        if (!username.equals("")) {
            userId.setText(username);
        }

        if (!userpwd.equals("")) {
            userPwd.setText(userpwd);
        }

        Button loginBtn = (Button) view.findViewById(R.id.signInBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userId.getText().toString().trim();
                String userpwd = userPwd.getText().toString().trim();

                if(check(username, userpwd)) {

                    SharedPreferences.Editor editor = mPref.edit();
                    editor.putString(getString(R.string.sign_in_key_username), username);
                    editor.putString(getString(R.string.sign_in_key_userpwd), userpwd);
                    editor.apply();

                    Toast.makeText(getActivity(), R.string.toast_remember_pwd, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private boolean check(String username, String userpwd) {
        if(username.equals("") || userpwd.equals("")) {
            Toast.makeText(getActivity(), R.string.toast_not_input_data, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
