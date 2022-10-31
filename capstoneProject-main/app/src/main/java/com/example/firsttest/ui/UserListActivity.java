package com.example.firsttest.ui;

import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.firsttest.R;
import com.example.firsttest.adapter.UserAdapter;
import com.example.firsttest.databinding.ActivityUserListBinding;
import com.example.firsttest.model.User;
import com.example.firsttest.request.DeleteRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private RequestQueue requestQueue;
    private List<User> users;
    private ActivityUserListBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        requestQueue = Volley.newRequestQueue(UserListActivity.this);

        Intent intent = getIntent();

        recyclerView = findViewById(R.id.recycler_view);

        userAdapter = new UserAdapter();

        recyclerView.setAdapter(userAdapter);

        users = new ArrayList<User>();

        String id = intent.getStringExtra("id");
        binding.userListTextView.setText(id + "님");

        try{
            //intent로 값을 가져옵니다 이때 JSONObject타입으로 가져옵니다
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String result = sharedPreferences.getString("userList", " ");
            JSONObject jsonObject = new JSONObject(result);
            //List.php 웹페이지에서 response라는 변수명으로 JSON 배열을 만들었음..
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            int count = 0;

            String  userID, userName, userAge, userPhonenumber, userIP;
            //userID = object.getString("userID");
            //JSON 배열 길이만큼 반복문을 실행
            //if(userID.equals(id)) {
            while (count < jsonArray.length()) {
                //count는 배열의 인덱스를 의미
                JSONObject object = jsonArray.getJSONObject(count);
                userID = object.getString("userID");

                if(id.equals(userID)) {
                    userName = object.getString("userName");
                    userAge = object.getString("userAge");
                    userPhonenumber = object.getString("userPhonenumber");
                    userIP = object.getString("userIP");

                    //값들을 User클래스에 묶어줍니다
                    User user = new User(userName, userAge, userPhonenumber, userIP);
                    userAdapter.addUser(user);
                    userAdapter.notifyDataSetChanged();
                    recyclerView.startLayoutAnimation();
                    count++;
                }
                else count++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        userAdapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemCLicked(View v, int pos) {
                User user = userAdapter.getUser(pos);
                Toast.makeText(UserListActivity.this, user.getUserIP(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserListActivity.this, EmergencyLiveActivity.class);
                String userIP = user.getUserIP();
                intent.putExtra("userIP",userIP);
                startActivity(intent);
            }
        });

        binding.btnBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        //비디오 버튼 클릭 시
        binding.btnReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //메인 액티비티로 이동
                Intent intent = new Intent(getApplicationContext(), ReplayListActivity.class);
                startActivity(intent);
            }
        });

        binding.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserListActivity.this, MySettingActivity.class);
                startActivity(i);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            //수정 해야함
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "응답:" + response, Toast.LENGTH_SHORT).show();
                        Log.d("유저리스트" , response);
                    }
                };

                int position = viewHolder.getAdapterPosition();

                switch (direction){
                    case ItemTouchHelper.LEFT:
                        String userIP = userAdapter.getUser(position).getUserIP();
                        userAdapter.removeUser(position); //제거
                        userAdapter.notifyItemRemoved(position); //리싸이클러뷰에게 알림
                        DeleteRequest deleteRequest = new DeleteRequest(userIP,listener);
                        requestQueue.add(deleteRequest);
                        break;
                }
            }
        }).attachToRecyclerView(recyclerView);

        //fragment로 전달해야 하기 때문에, sharedPreference로 아이디 전달
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", id);
        Log.d("userList", id + "유저리스트에서 아이디");
        editor.commit();
    }
}
