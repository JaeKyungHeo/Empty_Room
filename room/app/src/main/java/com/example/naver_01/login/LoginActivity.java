package com.example.naver_01.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.naver_01.MainActivity;
import com.example.naver_01.R;
import com.example.naver_01.ServerRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {
    private EditText et_email, et_password;
    private Button btn_login, btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email = findViewById(R.id.et_loginmail);
        et_password = findViewById(R.id.et_loginpass);
        btn_register = findViewById(R.id.btn_logintoregister);
        btn_login = findViewById(R.id.btn_login);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString();
                final String password = et_password.getText().toString();

                JSONObject body = new JSONObject();
                try {
                    body.put("email", email);
                    body.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ServerRequest req = new ServerRequest("auth", "POST");
                try {
                    req.execute(body.toString());
                    Pair<String, Integer> response = req.get();
                    JSONObject jsonObject = new JSONObject(response.first);
                    if (response.second == 200) {
                        // ??????
                        Toast.makeText(getApplicationContext(), "???????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("userName", jsonObject.getString("username"));
                        intent.putExtra("userEmail", email);
                        startActivity(intent);
                    }
                    else {
                        // ??????
                        if (jsonObject.getString("Error").equals("data incorrect")) {
                            Toast.makeText(getApplicationContext(), "????????? ?????? ??????????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "??? ??? ?????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException | InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}