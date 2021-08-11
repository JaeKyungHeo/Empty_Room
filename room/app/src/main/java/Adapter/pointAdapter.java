package Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.naver_01.MainActivity;
import com.example.naver_01.R;
import com.example.naver_01.ServerRequest;
import com.google.gson.JsonObject;
import com.naver.maps.map.overlay.InfoWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class pointAdapter extends InfoWindow.DefaultViewAdapter
{
    private final Context mContext;
    private final ViewGroup mParent;
    private TextView txtAddr;
    private TextView txtTel;
    private TextView txtInform;
    private boolean checked;
    private String id;

    protected void onCreate(Bundle savedInstanceState){
    }
    public pointAdapter(@NonNull Context context, ViewGroup parent)
    {
        super(context);
        mContext = context;
        mParent = parent;
    }

    @NonNull
    @Override
    protected View getContentView(@NonNull InfoWindow infoWindow)
    {

        View view = (View) LayoutInflater.from(mContext).inflate(R.layout.item_point, mParent, false);

        ImageView imagePoint = (ImageView) view.findViewById(R.id.imagepoint);
        txtAddr = (TextView) view.findViewById(R.id.txtaddr);
        txtTel = (TextView) view.findViewById(R.id.txttel);
        txtInform = (TextView) view.findViewById(R.id.txtinform);

        try {
            ServerRequest req = new ServerRequest("room/all", "GET");
            req.execute("");
            Pair<String, Integer> response = req.get();
            if (response.second == 200) {
                // 200일 떄만 성공임
                JSONObject jsonObject = new JSONObject(response.first);
                JSONArray rooms = jsonObject.getJSONArray("rooms");
                for (int i=0; i<rooms.length(); i+=1) {
                    JSONObject room = rooms.getJSONObject(i);
                    id = room.getString("id"); // id 반환
                }
            }
        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        JSONObject body = new JSONObject();
        try {
            body.put("id", id); // 아까 얻는 목록에서 id를 뽑아서 넣으면 됨
            ServerRequest req = new ServerRequest("room", "GET");
            req.execute(body.toString());
            Pair<String, Integer> response = req.get();
            if (response.second == 200) {
                // 성공
                JSONObject jsonObject = new JSONObject(response.first);
                txtAddr.setText(jsonObject.getString("address")); // 주소 반환
                txtTel.setText(jsonObject.getString("contact")); // 연락처 반환
                txtInform.setText(jsonObject.getString("introduction")); // 소개 반환
            }
        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //  imagePoint.setImageResource();
        //txtAddr.setText(((MainActivity)MainActivity.context_main).editLocation.getText());
        //txtTel.setText(((MainActivity)MainActivity.context_main).editCalling.getText());
        //txtInform.setText(((MainActivity)MainActivity.context_main).editInformation.getText());

        return view;
    }
}