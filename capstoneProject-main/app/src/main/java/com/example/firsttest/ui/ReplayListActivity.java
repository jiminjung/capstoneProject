package com.example.firsttest.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.firsttest.R;
import com.example.firsttest.TimeStampRequest;
import com.example.firsttest.adapter.ReplayAdatper;
import com.example.firsttest.databinding.ActivityReplayListBinding;
import com.example.firsttest.model.Video;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReplayListActivity extends AppCompatActivity {
    private ActivityReplayListBinding binding;
    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private ReplayAdatper replayAdatper;
    private List<Video> videos;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReplayListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        requestQueue = Volley.newRequestQueue(ReplayListActivity.this);

        videos = new ArrayList<Video>();

        recyclerView = findViewById(R.id.recycler_view_replayList);

        replayAdatper = new ReplayAdatper();
        recyclerView.setAdapter(replayAdatper);

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String id = prefs.getString("id", " ");

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    int count = 0;
                    String timeStamp,url;

                    while (count < jsonArray.length()) {
                        JSONObject object = jsonArray.getJSONObject(count);
                        timeStamp = object.getString("timestamp");
                        url = object.getString("url");

                        Video video = new Video(timeStamp,url);
                        replayAdatper.addVideo(video);
                        replayAdatper.notifyDataSetChanged();
                        recyclerView.startLayoutAnimation();
                        count++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        TimeStampRequest timeStampRequest = new TimeStampRequest(id, responseListener);
        requestQueue.add(timeStampRequest);

        replayAdatper.setOnItemClickListener(new ReplayAdatper.OnItemClickListener() {
            @Override
            public void onItemCLicked(View view, int position) {
                Video video = replayAdatper.getVideo(position);
                String url = video.getUrl();

                Intent i = new Intent(getApplicationContext(), ReplayVideoActivity.class);
                i.putExtra("url", url);
                startActivity(i);
            }
        });

       binding.btnBack.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i = new Intent(getApplicationContext(), UserListActivity.class);
               i.putExtra("id",id);
               startActivity(i);
           }
       });
    }
}
