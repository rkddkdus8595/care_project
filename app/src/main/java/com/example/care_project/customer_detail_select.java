package com.example.care_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class customer_detail_select extends AppCompatActivity {
    TextView txtDate, txtContent, txtProcess, txtProcessT;
    String date, oneContent, twoContent, threeContent, process, processT, phone, id, storeName;
    ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail_select);

        txtDate=(TextView)findViewById(R.id.txtDate);
        txtContent=(TextView)findViewById(R.id.txtContent);
        txtProcess=(TextView)findViewById(R.id.txtProcess);
        txtProcessT=(TextView)findViewById(R.id.txtProcessT);

        btnBack=(ImageButton)findViewById(R.id.btnBack);

        Intent intent=getIntent();
        date=intent.getStringExtra("date"); // 접수 일자
        storeName=intent.getStringExtra("storeName"); // 지점 이름
        oneContent=intent.getStringExtra("oneContent"); // 접수 내용
        twoContent=intent.getStringExtra("twoContent"); // 접수 내용
        threeContent=intent.getStringExtra("threeContent"); // 접수 내용
        process=intent.getStringExtra("process"); // 처리 현황
        processT=intent.getStringExtra("processT"); // 처리 시간
        phone=intent.getStringExtra("phone"); // 핸드폰 번호
        id=intent.getStringExtra("id");
        txtDate.setText(date);
        oneContent=oneContent.trim();
        twoContent=twoContent.trim();
        threeContent=threeContent.trim();
        txtContent.setText("지점 : "+storeName+"\n\n1단계 : "+oneContent+"\n\n"+"2단계 : "+twoContent+"\n\n"+"3단계 : "+threeContent);
        if(process.equals("N")){
            txtProcess.setText("미처리");
        }else{
            txtProcess.setText("처리 완료");
        }

        if(processT.equals("null")  || processT.isEmpty()){
            txtProcessT.setText("");
        }else{
            txtProcessT.setText(processT); // 처리 된경우에만 이거 뿌려주기 !
        }

        btnBack.setOnClickListener(new View.OnClickListener() { // 뒤로가기 버튼 누를때
            @Override
            public void onClick(View v) { //확인할 것은 뒤로가기 후 변경된 데이터베이스가 자동으로 불려오는지 아닌지 !!!!!
                finish();
                //Intent intent = new Intent(v.getContext(), manager_select_main.class);
                //intent.putExtra("id",id);
                //startActivity(intent);
            }
        });

    }
}