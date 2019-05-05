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

    private int[] cityids = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private String currentlevel="province";
    private int pid=0;

    private List<String> data2=new ArrayList();
    private int[] pids = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private List<String> data = new ArrayList<>();
    private TextView textView;
    private ListView listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        this.textView = findViewById(R.id.textView);
        this.listview = (ListView) findViewById(R.id.list_view);

        String weatherUrl =currentlevel=="city"?"http://guolin.tech/api/china/"+pid:"http://guolin.tech/api/china/";
//        String weatherUrl = "http://guolin.tech/api/china";

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(provinceActivity.this, android.R.layout.simple_list_item_1, data);
        listview.setAdapter(adapter);
        this.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pid=provinceActivity.this.pids[position];
                currentlevel="city";
                String weatherUrl =currentlevel=="city"?"http://guolin.tech/api/china/"+pid:"http://guolin.tech/api/china/";
                try {
                    getData(weatherUrl, adapter);
                } catch (IOException e) {
                    e.printStackTrace();
                }


//                Intent intent = new Intent(provinceActivity.this, CityActivity.class);
//                intent.putExtra("pid", provinceActivity.this.pids[position]);
//                if (currentlevel=="cite"){
//                    intent.putExtra("cityids",cityids[position]);
//                }
//                startActivity(intent);
            }
        });


    }

    private void getData(String weatherUrl, final ArrayAdapter<String> adapter) throws IOException {
        HttpUtil.sendOkHttpRequest(weatherUrl, new okhttp3.Callback() {
            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                parseJSONObject(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(responseText);
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
        try {
            jsonArray = new JSONArray(responseText);
            String[] result = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = null;
                jsonObject = jsonArray.getJSONObject(i);
                this.data.add(jsonObject.getString("name"));
                if (currentlevel=="city"){
                    this.cityids[i]=jsonObject.getInt("id");
                }else {
                    this.pids[i] = jsonObject.getInt("id");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
