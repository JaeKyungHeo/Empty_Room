package com.example.naver_01.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.naver_01.R;
import com.example.naver_01.ServerRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class RegisterActivity extends AppCompatActivity {
    private EditText et_email, et_username, et_password, et_passwrodcheck;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_email = findViewById(R.id.et_email);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        et_passwrodcheck = findViewById(R.id.et_passwordcheck);

        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString();
                String username = et_username.getText().toString();
                final String password = et_password.getText().toString();
                final String passwordcheck = et_passwrodcheck.getText().toString();

                if (password.length() < 8) {
                    Toast.makeText(getApplicationContext(), "비밀번호는 8글자 이상이어야합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(passwordcheck)) {
                    Toast.makeText(getApplicationContext(), "비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();;
                    return;
                }

                JSONObject body = new JSONObject();
                try {
                    body.put("email", email);
                    body.put("username", username);
                    body.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ServerRequest req = new ServerRequest("user", "POST");
                try {
                    req.execute(body.toString());
                    Pair<String, Integer> response = req.get();
                    if (response.second == 200) {
                        // 성공
                        Toast.makeText(getApplicationContext(), "가입에 성공했습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    else {
                        // 실패
                        JSONObject jsonObject = new JSONObject(response.first);
                        if (jsonObject.getString("Error").equals("email is invalid")) {
                            Toast.makeText(getApplicationContext(), "이메일 형식을 맞추어주세요.", Toast.LENGTH_SHORT).show();
                        } else if (jsonObject.getString("Error").equals("email already exists")) {
                            Toast.makeText(getApplicationContext(), "이미 가입된 이메일입니다.", Toast.LENGTH_SHORT).show();
                        } else if (jsonObject.getString("Error").equals("username already exists")) {
                            Toast.makeText(getApplicationContext(), "이미 존재하는 닉네임입니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "가입 요청 혹은 통신에 문제가 있습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException | InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}