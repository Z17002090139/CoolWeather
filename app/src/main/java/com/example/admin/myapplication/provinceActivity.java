package com.example.admin.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class provinceActivity extends AppCompatActivity {
    public static final String PROVINCE = "province";
    public static final String CITY = "city";
    public static final String COUNTRY = "country";

//    private int[] pids = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
//    private int[] cityids = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
//    private String[] wIds={" "," "," "," "," "," "," "," "," "," "," "," "};

    //当前层级：province，city，country
    private String currentlevel= PROVINCE;
    private int pid=0;//当前选中省的id
    private int cityid=0;//当前选中城市的id
    private String Wid;
    private List<Integer> pids=new ArrayList<>();
    private List<String> data = new ArrayList<>();
    private List<String> wIds = new ArrayList<>();
    private ListView listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        this.listview = (ListView) findViewById(R.id.list_view);


        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(provinceActivity.this, android.R.layout.simple_list_item_1, data);
        listview.setAdapter(adapter);
        this.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentlevel == PROVINCE){
                    pid=provinceActivity.this.pids.get(position);
                    currentlevel= CITY;
                    try {
                        getData(adapter);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (currentlevel== CITY){
                    cityid=provinceActivity.this.pids.get(position);
                    currentlevel= COUNTRY;

                    try {
                        getData(adapter);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else if (currentlevel== COUNTRY){
                    Wid=provinceActivity.this.wIds.get(position);
                    try {
                        getData(adapter);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    currentlevel="weather";
                    Intent intent=new Intent(provinceActivity.this,WeatherActivity.class);
                    intent.putExtra("weatherId",provinceActivity.this.wIds.get(position));
                    startActivity(intent);
                }
//                else if(currentlevel=="weather") {
//                    Intent intent=new Intent(provinceActivity.this,WeatherActivity.class);
//                    intent.putExtra("weatherId",provinceActivity.this.wIds.get(position));
//                    startActivity(intent);
//                }


            }
        });

        try {
            getData(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void getData(final ArrayAdapter<String> adapter) throws IOException {
        String weatherUrl = null;
    if(currentlevel== PROVINCE) {
            weatherUrl = "http://guolin.tech/api/china/";
        }
    else if(currentlevel== CITY){
        weatherUrl="http://guolin.tech/api/china/"+pid;
    }
    else if(currentlevel== COUNTRY){
        weatherUrl = "http://guolin.tech/api/china/"+pid+"/"+cityid;
    }else if(currentlevel=="weather"){

    }


        HttpUtil.sendOkHttpRequest(weatherUrl, new okhttp3.Callback() {
            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                final String responseText = response.body().string();

                parseJSONObject(responseText);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }


            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }
        });
    }

    private void parseJSONObject(String responseText) {
        JSONArray jsonArray = null;
        this.data.clear();
        this.pids.clear();
        this.wIds.clear();
        try {
            jsonArray = new JSONArray(responseText);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = null;
                jsonObject = jsonArray.getJSONObject(i);
                this.data.add(jsonObject.getString("name"));
                this.pids.add(jsonObject.getInt("id"));
                 if(currentlevel== COUNTRY){
                    this.wIds.add(jsonObject.getString("weather_id"));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
