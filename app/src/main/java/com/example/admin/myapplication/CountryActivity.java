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

public class CountryActivity extends AppCompatActivity {

    private List<String> data2=new ArrayList();
    private TextView textView;
    private ListView listView;
    private String weatherId;
    private String[] countydata = {"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""};
    private int[] countypids=new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        Intent intent1=getIntent();
        int cid=intent1.getIntExtra("citypids",0);
        int pid=intent1.getIntExtra("pid",0);
        String weatherUrl = "http://guolin.tech/api/china/"+pid+"/"+cid;
        this.textView=findViewById(R.id.textcounty);

        this.listView=findViewById(R.id.countrylist_view);

        ArrayAdapter<String> adapter=new ArrayAdapter<>(CountryActivity.this,android.R.layout.simple_list_item_1,countydata);
        listView.setAdapter(adapter);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(CountryActivity.this,WeatherActivity.class);
                intent.putExtra("weatherId",CountryActivity.this.weatherId);
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
        try {
            jsonArray = new JSONArray(responseText);
            String[] result = new String[jsonArray.length()];
            for (int i = 0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = null;
                jsonObject= jsonArray.getJSONObject(i);
                this.countydata[i]=jsonObject.getString("name");
                this.countypids[i]=jsonObject.getInt("id");
                this.weatherId=jsonObject.getString("weather_id");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
