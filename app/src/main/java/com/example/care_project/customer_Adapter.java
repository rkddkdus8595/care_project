package com.example.care_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class customer_Adapter extends RecyclerView.Adapter<customer_Adapter.MyViewHolder> {

    private ArrayList<customer_item> mList;
    private LayoutInflater mInflate;
    private Context mContext;
    public customer_Adapter(Context context, ArrayList<customer_item> items) {
        this.mList = items;
        this.mInflate = LayoutInflater.from(context);
        this.mContext = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // 뷰홀더를 생성 (레이아웃 생성)
        View view = mInflate.inflate(R.layout.manage_item1, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) { //뷰홀더가 재활용될 때 실행되는 메서드
        //binding
        // System.out.println("내용출력중: "+mList.get(position).txtRecept); 2개 나오는데
        holder.txtDate.setText(mList.get(position).txtDate);
        holder.txtContent.setText(mList.get(position).storeName+"/"+mList.get(position).txtOneContent+"/"+mList.get(position).txtTwoContent+"/"+mList.get(position).txtThreeContent);
        //System.out.println(mList.get(position).txtOneContent+"/"+mList.get(position).txtTwoContent+"/"+mList.get(position).txtThreeContent);
        if(mList.get(position).txtProcess.equals("Y")){
            holder.txtProcess.setText("처리 완료");
        }else if(mList.get(position).txtProcess.equals("N")){
            holder.txtProcess.setText("미처리");
        }
        //holder.txtProcess.setText(mList.get(position).txtProcess);
        if(mList.get(position).txtProcessT.equals("null") || mList.get(position).txtProcessT.isEmpty()){
            holder.txtProcessT.setText("");
        }else{
            holder.txtProcessT.setText(mList.get(position).txtProcessT); // 처리 된경우에만 이거 뿌려주기 !
        }

        //holder.txtProcessT.setText(mList.get(position).txtProcessT);

        //Click event
    }

    @Override
    public int getItemCount() { //아이템 개수를 조회
        //System.out.print("mList 사이즈라고 ! "+mList.size());
        return mList.size();
    }


    //ViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDate;
        public TextView txtContent;
        public TextView txtProcess;
        public TextView txtProcessT;
        public MyViewHolder(View itemView) {
            super(itemView);

            txtDate = itemView.findViewById(R.id.txtDate);
            txtContent = itemView.findViewById(R.id.txtContent);
            txtProcess = itemView.findViewById(R.id.txtProcess);
            txtProcessT = itemView.findViewById(R.id.txtProcessT);

            itemView.setClickable(true);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { // 아이템 클릭 시 !
                    int pos = getAdapterPosition() ; // 사용자가 클릭한 아이템의 position을 준다.
                    //System.out.println(Integer.toString(pos));
                    if (pos != RecyclerView.NO_POSITION) { //그 포지션이 recyclerView의 item을 클릭한 것인지 item이아닌 다른 클릭인지 여부를 확인한다.
                        Intent intent=new Intent(mContext, customer_detail_select.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("storeName", mList.get(pos).getStoreName());
                        intent.putExtra("date",mList.get(pos).getDate());
                        intent.putExtra("oneContent", mList.get(pos).getOneContent());
                        intent.putExtra("twoContent", mList.get(pos).getTwoContent());
                        intent.putExtra("threeContent", mList.get(pos).getThreeContent());
                        intent.putExtra("process", mList.get(pos).getProcess());
                        intent.putExtra("processT", mList.get(pos).getProcessT());
                        intent.putExtra("phone",mList.get(pos).getPhoneNum()); // 폰번호는 화면에 표기되지는 않지만 처리 완료를 위해 다음 액티비티로 넘김
                        mContext.startActivity(intent);
                    }

                }
            });

        }
    }

}
