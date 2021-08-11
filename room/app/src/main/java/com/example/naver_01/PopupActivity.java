package com.example.naver_01;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.naver.maps.map.overlay.InfoWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;


public class PopupActivity extends Activity {
    EditText address, contact, introduction;
    private TextView txtAddr, txtTel, txtInform;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_marker);

        address = findViewById(R.id.add_address);
        contact = findViewById(R.id.add_contact);
        introduction = findViewById(R.id.add_introduction);
        txtAddr = (TextView) findViewById(R.id.txtaddr);
        txtTel = (TextView) findViewById(R.id.txttel);
        txtInform = (TextView) findViewById(R.id.txtinform);
    }

    //확인 버튼 클릭
    public void mOnClose(View v) {
        //데이터 전달하기
        // TODO: 규격에 맞을 때만 등록하도록 해주세요 (주소 50자, 연락 50자, 설명 200자)
        // TODO: 규격에 맞지 않았을 때 안내가 필요
        // TODO: 다시 입력하여 시도하도록 안내하기 + 취소버튼 만들기 vs 아예 등록하지 않은 채 팝업을 꺼버리기
        // TODO: 주소가 유효한 주소인지도 확인해주세요
        JSONObject jsonObject = new JSONObject();
        try {
            Log.v("TEST", getIntent().getStringExtra("userName"));
            jsonObject.put("uploader", getIntent().getStringExtra("userName"));
            jsonObject.put("address", address.getText());
            jsonObject.put("contact", contact.getText());
            jsonObject.put("introduction", introduction.getText());
            //jsonObject.put("latitude",((MainActivity)MainActivity.context_main).maker_latitude);
            //jsonObject.put("longitude",((MainActivity)MainActivity.context_main).maker_longitude);

            ServerRequest req = new ServerRequest("room", "POST");
            req.execute(jsonObject.toString());
            Pair<String, Integer> response = req.get();
            if (response.second == 200) {
                // 성공
                // TODO: 일단 성공할 때만 창이 꺼지게 함, 어떻게 해야 좋을까?
            } else {
                // 실패
                // TODO: 실패한다면 규격 안맞는것처럼 다시 시도하도록 처리하자
            }
        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        return event.getAction() != MotionEvent.ACTION_OUTSIDE;
    }
}
