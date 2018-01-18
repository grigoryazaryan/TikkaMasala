package net.rahar.tikkamasala.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import net.rahar.tikkamasala.R;

import helpers.C;

/**
 * Created by grigory on 16/01/18.
 */

public class LoginActivity extends AppCompatActivity {
    private final String TAG = "LoginActivity";

    private TextInputLayout usernameWrapper;
    private EditText editTextUsername;
    private CheckBox checkBoxRememberUsername;
    private Button buttonLogin;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_login);


        sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        usernameWrapper = (TextInputLayout) findViewById(R.id.login_wrapper);
        editTextUsername = (EditText) findViewById(R.id.text_username);
        checkBoxRememberUsername = (CheckBox) findViewById(R.id.remember_login);
        buttonLogin = ((Button) findViewById(R.id.login_button));

        String userName = sharedPreferences.getString(C.KEY_USERNAME, "");
        editTextUsername.setText(userName);
        checkBoxRememberUsername.setChecked(!userName.equals(""));

        buttonLogin.setOnClickListener(v -> {
            usernameWrapper.setError(null);

            String username = editTextUsername.getText().toString();

            if (checkBoxRememberUsername.isChecked())
                sharedPreferences.edit().putString(C.KEY_USERNAME, username).apply();
            else
                sharedPreferences.edit().remove(C.KEY_USERNAME).apply();


            if(TextUtils.isEmpty(username)) {
                usernameWrapper.setError(getString(R.string.username_cant_be_empty));
                return;
            }

            Intent intent = new Intent(new Intent(LoginActivity.this, GroupChatActivity.class));
            intent.putExtra(C.KEY_USERNAME, username);
            startActivity(intent);
        });
    }
}
