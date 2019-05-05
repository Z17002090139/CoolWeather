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

public class CityActivity extends AppCompatActivity {

//    private List<String> data2=new ArrayList();
    private List<String> data = new ArrayList<>();
    private int[] cityids=new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private TextView textView;
    private ListView listView;
    private int pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        Intent intent1=getIntent();
        pid=intent1.getIntExtra("pid",0);
        String weatherUrl = "http://guolin.tech/api/china/"+pid;
        this.textView=findViewById(R.id.textcity);
        this.listView=findViewById(R.id.Citylist_view);
        final ArrayAdapter<String> adapter=new ArrayAdapter<>(CityActivity.this,android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(CityActivity.this,CountryActivity.class);
                intent.putExtra("cityids",CityActivity.this.cityids[position]);
                intent.putExtra("pid",CityActivity.this.pid);
                startActivity(intent);
            }
        });
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
            for (int i = 0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = null;
                jsonObject= jsonArray.getJSONObject(i);
                this.data.add(jsonObject.getString("name"));
                this.cityids[i]=jsonObject.getInt("id");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
