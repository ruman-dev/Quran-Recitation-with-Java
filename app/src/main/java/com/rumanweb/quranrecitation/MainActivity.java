package com.rumanweb.quranrecitation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ProgressBar progressBar;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.progressBar);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.statusBarColor));

        String url = "https://ruman-test.000webhostapp.com/apps/video.json";

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                    try {
                        progressBar.setVisibility(View.GONE);
                        for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String title = jsonObject.getString("title");
                        String video_id = jsonObject.getString("video_id");

                        hashMap = new HashMap<>();
                        hashMap.put("title", title);
                        hashMap.put("video_id", video_id);
                        arrayList.add(hashMap);
                    }
                } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                CustomAdapter customAdapter = new CustomAdapter();
                listView.setAdapter(customAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Log.d("Volley Error", String.valueOf(error));
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(arrayRequest);
    }

    private class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {

            LayoutInflater layoutInflater = getLayoutInflater();

            View myView = layoutInflater.inflate(R.layout.layout_item, null, false);

            ImageView imageThumb = myView.findViewById(R.id.imageThumb);
            TextView video_title = myView.findViewById(R.id.video_title);
            LinearLayout videoLayout = myView.findViewById(R.id.videoLayout);

            HashMap<String, String> hashMap = arrayList.get(position);
            String videoTitle = hashMap.get("title");
            String videoId = hashMap.get("video_id");
            String videoThumbLink = "https://img.youtube.com/vi/"+videoId+"/0.jpg";

            VideoActivity.videoTitle = videoTitle;
            VideoActivity.videoId = videoId;

            videoLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(),VideoActivity.class));
                }
            });
            video_title.setText(videoTitle);
            Picasso.get().load(videoThumbLink).placeholder(R.drawable.image_placeholder).into(imageThumb);

            return myView;
        }
    }
}