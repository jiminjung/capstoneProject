package com.example.firsttest.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firsttest.R;
import com.example.firsttest.model.User;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    ArrayList<User> users = new ArrayList<User>();

    public interface OnItemClickListener{
        void onItemCLicked(View view, int position);
    }

    private OnItemClickListener itemClickListener;

    //onItemlistener 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener){
        itemClickListener = listener;
    }

    @NonNull
    @Override //viewHolder 객체를 생성하여 리턴
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View userView = inflater.inflate(R.layout.user,parent,false);

        return new ViewHolder(userView);
    }

    @Override //viewHolder안의 내용을 position에 해당되는 데이터로 교체
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.setItem(user);
    }

    public void addUser(User user){
        users.add(user);
    }

    //유저 등록, 메소드 오버로딩
    public void addUser(int position, User user){
        users.add(position, user);
    }

    public void setUsers(ArrayList<User> users){
        this.users = users;
    }

    public User getUser(int pos){
        return users.get(pos);
    }

    public void removeUser(int position){
        users.remove(position);
    }

    public void removeAllUser(){
        users.clear();
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView userAge;
        private TextView userName;
        private TextView userPhoneNumber;
        private TextView userIP;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userAge = itemView.findViewById(R.id.userAge);
            userName = itemView.findViewById(R.id.userName);
            userPhoneNumber = itemView.findViewById(R.id.userPhonenumber);
            userIP = itemView.findViewById(R.id.userIP);

            itemView.setOnClickListener(new View.OnClickListener() {
                //item click 처리 이벤트
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        itemClickListener.onItemCLicked(v,pos);
                    }
                }
            });
        }
        //뷰에 데이터를 bind
        public void setItem(User user){
            userAge.setText(user.getUserAge());
            userName.setText(user.getUserName() + " /");
            userPhoneNumber.setText("0" + user.getUserPhoneNumber());
            userIP.setText(user.getUserIP());
        }


    }
}
