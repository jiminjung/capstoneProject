package com.example.firsttest.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firsttest.R;
import com.example.firsttest.model.User;
import com.example.firsttest.model.Video;

import java.util.ArrayList;

public class ReplayAdatper extends RecyclerView.Adapter<ReplayAdatper.ViewHolder> {

    ArrayList<Video> videos = new ArrayList<Video>();

    public interface OnItemClickListener{
        void onItemCLicked(View view, int position);
    }

    private OnItemClickListener itemClickListener;

    //onItemlistener 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener){
        itemClickListener = listener;
    }

    @NonNull
    @Override
    public ReplayAdatper.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View videoList = inflater.inflate(R.layout.video,parent,false);

        return new ViewHolder(videoList);
    }


    @Override //viewHolder안의 내용을 position에 해당되는 데이터로 교체
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Video video = videos.get(position);
        holder.setItem(video);
    }

    public void addVideo(Video video){
        videos.add(video);
    }

    public void addVideo(int position, Video video){
        videos.add(position, video);
    }

    public void setVideos(ArrayList<Video> videos){
        this.videos = videos;
    }

    public Video getVideo(int pos){
        return videos.get(pos);
    }

    public void removeVideo(int position){
        videos.remove(position);
    }

    public void removeAllVideo(){
        videos.clear();
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView timeStamp;

        public ViewHolder(View itemView) {
            super(itemView);
            timeStamp = itemView.findViewById(R.id.video_timeStamp);

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
        public void setItem(Video video){
            timeStamp.setText(video.getTimestamp());
        }
    }
}
