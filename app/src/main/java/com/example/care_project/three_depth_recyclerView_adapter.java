package com.example.care_project;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class three_depth_recyclerView_adapter extends RecyclerView.Adapter<three_depth_recyclerView_adapter.MyViewHolder> {

    public ArrayList<three_depth_item> mList;
    private LayoutInflater mInflate;
    private Context mContext;
    public int pos;
    public String text, str;
    public Spinner depthName1;
    public TextView subject1;
    public HashMap<Integer, String> hash =new HashMap<Integer, String>();
    public three_depth_recyclerView_adapter(Context context, ArrayList<three_depth_item> itmes) {
        this.mList = itmes;
        this.mInflate = LayoutInflater.from(context);
        this.mContext = context;
    }
    public interface OnItemClickListener {
        void onItemClick(int pos, String depthName, String depthName2, String subject);
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
        View view = mInflate.inflate(R.layout.three_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //binding
        holder.subject.setText(mList.get(position).subject);
        String[] results = mList.get(position).depthName.split(";");
        
        ArrayAdapter adapter=new ArrayAdapter(mContext, android.R.layout.simple_spinner_dropdown_item, results);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.depthName.setAdapter(adapter);

        if(hash.containsKey(position)){
            System.out.println("포지션:"+position);
        }else{
            hash.put(position, ""); // 여기서 미리 포지션에 따른 해시맵을 만들어줌
            System.out.println("포지션 :"+position);
        }
        //holder.depthName.setText(mList.get(position).depthName);
        //System.out.println("내용출력중: "+mList.get(position).depthName); //
        //Click event
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //ViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public Spinner depthName;
        public TextView subject;

        public MyViewHolder(View itemView) {
            super(itemView);

            depthName = itemView.findViewById(R.id.depthName);
            depthName1=itemView.findViewById(R.id.depthName);
            subject=itemView.findViewById(R.id.subject);
            subject1=itemView.findViewById(R.id.subject);

            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { // 아이템 클릭 시 !
                    pos = getAdapterPosition() ; // 사용자가 클릭한 아이템의 position을 준다.
                    if (pos != RecyclerView.NO_POSITION) { //그 포지션이 recyclerView의 item을 클릭한 것인지 item이아닌 다른 클릭인지 여부를 확인한다.
                        //Intent intent=new Intent(mContext, manager_serivce.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 어디액티비티에 리사이클러뷰 있는지
                        text = depthName.getSelectedItem().toString();
                        System.out.println("선택한 포지션과 이름 : "+pos+" "+text);
                        if (mListener != null) {
                            mListener.onItemClick(pos, text, mList.get(pos).depthName, mList.get(pos).subject); // 선택한 위치와 선택한 위치의 이름 넘겨주기
                        }
                    }


                }
            });

            depthName1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                      @RequiresApi(api = Build.VERSION_CODES.N)
                      @Override
                      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                          // On selecting a spinner item


                          //for(int i=0; i<mList.size();i++){
                          //    hash.put(mList.get(i).subject, mList.get(i).depthName);
                           //   System.out.println(mList.get(i).subject+"/"+parent.getItemAtPosition(position).toString()+"/"+getAdapterPosition());
                          //}
                          if(hash.containsKey(getAdapterPosition())){ // 이미 존재하는 포지션이라면 값만 바꿔주기
                                hash.replace(getAdapterPosition(), parent.getItemAtPosition(position).toString());
                          }else{
                              //hash.put(getAdapterPosition(), "");
                          }

                          // Iterator 사용 3 - entrySet() : key / value
                            /*Set set2 = hash.entrySet();
                            Iterator iterator2 = set2.iterator();
                            while(iterator2.hasNext()){
                                Map.Entry<Integer,String> entry = (Map.Entry)iterator2.next();
                                int key = (Integer)entry.getKey();
                                String value = (String)entry.getValue();
                                System.out.println("hashMap Key : " + key);
                                System.out.println("hashMap Value : " + value);
                            }*/

                          //String item = parent.getItemAtPosition(position).toString();
                          //str+=item+";";
                      }

                      @Override
                      public void onNothingSelected(AdapterView<?> parent) {
                          // todo for nothing selected
                      }
                  });


        }
    }

}