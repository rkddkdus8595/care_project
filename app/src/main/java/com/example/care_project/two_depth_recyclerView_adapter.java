package com.example.care_project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class two_depth_recyclerView_adapter extends RecyclerView.Adapter<two_depth_recyclerView_adapter.MyViewHolder> {

    private ArrayList<two_depth_item> mList;
    private LayoutInflater mInflate;
    private Context mContext;
    public two_depth_recyclerView_adapter(Context context, ArrayList<two_depth_item> itmes) {
        this.mList = itmes;
        this.mInflate = LayoutInflater.from(context);
        this.mContext = context;
    }
    public interface OnItemClickListener {
        void onItemClick(int pos, String depthName);
    }

    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null ;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.two_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //binding
        holder.depthName.setText(mList.get(position).depthName);
        System.out.println("내용출력중: "+mList.get(position).depthName); //
        //Click event
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //ViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView depthName;

        public MyViewHolder(View itemView) {
            super(itemView);

            depthName = itemView.findViewById(R.id.depthName);

            itemView.setClickable(true);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { // 아이템 클릭 시 !
                    int pos = getAdapterPosition() ; // 사용자가 클릭한 아이템의 position을 준다.
                    if (pos != RecyclerView.NO_POSITION) { //그 포지션이 recyclerView의 item을 클릭한 것인지 item이아닌 다른 클릭인지 여부를 확인한다.
                        //Intent intent=new Intent(mContext, manager_serivce.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 어디액티비티에 리사이클러뷰 있는지
                        System.out.println("선택한 포지션과 이름 : "+pos+" "+mList.get(pos).depthName);
                        if (mListener != null) {
                            mListener.onItemClick(pos, mList.get(pos).depthName); // 선택한 위치와 선택한 위치의 이름 넘겨주기
                        }
                    }


                }
            });

        }
    }

}