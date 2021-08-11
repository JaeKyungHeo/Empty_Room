package com.example.naver_01;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import Adapter.pointAdapter;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static Context context_main;
    public boolean check;
    public double maker_latitude, maker_longitude;

    private double maker_latitudes[], maker_longitudes[];
    private AppBarConfiguration mAppBarConfiguration;
    private static NaverMap naverMap;
    private LatLng coord = new LatLng(37.2434038, 127.0800383);
    private int openMarker;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        maker_latitudes = new double[99];
        maker_longitudes = new double[99];

        maker_latitude = coord.latitude;
        maker_longitude = coord.longitude;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        TextView nameView = navigationView.getHeaderView(0).findViewById(R.id.nav_name);
        TextView emailView = navigationView.getHeaderView(0).findViewById(R.id.nav_email);
        nameView.setText(getIntent().getStringExtra("userName"));
        emailView.setText(getIntent().getStringExtra("userEmail"));

        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        CheckBox checkBox = (CheckBox) findViewById(R.id.btn_transit);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked())
                    naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, true);
                else
                    naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, false);
            }
        });

        CheckBox checkBox2 = (CheckBox) findViewById(R.id.btn_building);
        checkBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked())
                    naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, true);
                else
                    naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, false);
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        CameraPosition cameraPosition = new CameraPosition(coord, 16);
        naverMap.setCameraPosition(cameraPosition);

       /* Marker marker;
        for(int i=0; i<maker_latitudes.length; i++){
            if(maker_longitudes[i] !=0 && maker_latitudes[i] != 0){
                marker = new Marker();
                marker.setPosition(new LatLng(maker_latitudes[i],maker_longitudes[i]));
                marker.setMap(naverMap);
            }
        }*/

        Marker marker3 = new Marker();

        InfoWindow infoWindow = new InfoWindow();

        Button markerButton = (Button) findViewById(R.id.btn_marker);
        markerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PopupActivity.class);
                intent.putExtra("userName", getIntent().getStringExtra("userName"));
                startActivity(intent);
                coord = new LatLng(coord.latitude-0.0005,coord.longitude);
                marker3.setPosition(coord);
                marker3.setMap(naverMap);
                marker3.setCaptionText("빈 방");
            }
        });
        ViewGroup rootView = (ViewGroup)findViewById(R.id.fragment_container);
        pointAdapter adapter = new pointAdapter(MainActivity.this, rootView);
        infoWindow.setAdapter(adapter);

        naverMap.setOnMapClickListener((coord, point) -> {
            infoWindow.close();
        });

        marker3.setTag("");
        marker3.setOnClickListener(overlay -> {
            // 마커를 클릭할 때 정보창을 엶
            infoWindow.open(marker3);
            openMarker = 3;
            return true;
        });

    }
/*
    private void run() {
        getPoint(
                "월곡산정로 108"
        );
    }
    private void getPoint(String... addr) {
        GeoPointer geoPointer = new GeoPointer(MainActivity.this, listener);
        geoPointer.execute(addr);
    }
    private GeoPointer.OnGeoPointListener listener = new GeoPointer.OnGeoPointListener() {
        @Override
        public void onPoint(GeoPointer.Point[] p) {
            int sCnt = 0, fCnt = 0;
            for (GeoPointer.Point point : p) {
                if (point.havePoint) sCnt++;
                else fCnt++;
                Log.d("TEST_CODE", point.toString());
            }
            Log.d("TEST_CODE", String.format("성공 : %s, 실패 : %s", sCnt, fCnt));
        }

        @Override
        public void onProgress(int progress, int max) {
            Log.d("TEST_CODE", String.format("좌표를 얻어오는중 %s / %s", progress, max));
        }
    };
 */
}