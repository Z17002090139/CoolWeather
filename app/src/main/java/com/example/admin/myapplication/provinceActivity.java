package com.example.admin.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class provinceActivity extends AppCompatActivity {

//    private int[] pids = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
//    private int[] cityids = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
//    private String[] wIds={" "," "," "," "," "," "," "," "," "," "," "," "};

    //当前层级：province，city，country
    private String currentlevel="province";
    private int pid=0;//当前选中省的id
    private int cityid=0;//当前选中城市的id
    private String Wid;
    private List<Integer> pids=new ArrayList<>();
    private List<Integer> cityids=new ArrayList<>();
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
                if(currentlevel == "province"){
                    pid=provinceActivity.this.pids.get(position);
                    currentlevel="city";
                    try {
                        getData(adapter);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (currentlevel=="city"){
                    cityid=provinceActivity.this.cityids.get(position);
                    currentlevel="country";
                    try {
                        getData(adapter);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if (currentlevel=="country"){
                    Wid=provinceActivity.this.wIds.get(position);
                    currentlevel="weather";
                    try {
                        getData(adapter);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }
        });

        try {
            getData(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void getData(final ArrayAdapter<String> adapter) throws IOException {
        String weatherUrl;
    if(currentlevel=="city"){
        weatherUrl="http://guolin.tech/api/china/"+pid;
    }
    else if(currentlevel=="country"){
        weatherUrl = "http://guolin.tech/api/china/"+pid+"/"+cityid;
    }
    else if(currentlevel=="weather"){
        weatherUrl = "http://guolin.tech/api/weather?cityid=" + Wid  + "&key=782a428345a54209bd6a3e2f746ee3d6";
        }
    else {
        weatherUrl = "http://guolin.tech/api/china/";
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
        this.cityids.clear();
        this.wIds.clear();
        try {
            jsonArray = new JSONArray(responseText);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = null;
                jsonObject = jsonArray.getJSONObject(i);
                this.data.add(jsonObject.getString("name"));
                if (currentlevel=="city"){
                    this.cityids.add(jsonObject.getInt("id"));
                }else if(currentlevel=="country"){
                    this.wIds.add(jsonObject.getString("weather_id"));
                }
                else {
                    this.pids.add(jsonObject.getInt("id"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
