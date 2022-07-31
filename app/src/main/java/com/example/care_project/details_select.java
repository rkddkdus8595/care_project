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

public class details_select extends AppCompatActivity {
    TextView txtDate, txtContent, txtProcess, txtProcessT;
    String date, oneContent, twoContent, threeContent, process, processT, phone, id;
    ImageButton btnBack, btnCommit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_select);

        txtDate=(TextView)findViewById(R.id.txtDate);
        txtContent=(TextView)findViewById(R.id.txtContent);
        txtProcess=(TextView)findViewById(R.id.txtProcess);
        txtProcessT=(TextView)findViewById(R.id.txtProcessT);

        btnBack=(ImageButton)findViewById(R.id.btnBack);
        btnCommit=(ImageButton)findViewById(R.id.btnCommit);

        Intent intent=getIntent();
        date=intent.getStringExtra("date"); // 접수 일자

        oneContent=intent.getStringExtra("oneContent"); // 접수 내용
        twoContent=intent.getStringExtra("twoContent"); // 접수 내용
        threeContent=intent.getStringExtra("threeContent"); // 접수 내용
        process=intent.getStringExtra("process"); // 처리 현황
        processT=intent.getStringExtra("processT"); // 처리 시간
        phone=intent.getStringExtra("phone"); // 핸드폰 번호
        id=intent.getStringExtra("id");
        txtDate.setText(date);
        txtContent.setText("1단계 : "+oneContent+"\n\n"+"2단계 : "+twoContent+"\n\n"+"3단계 : "+threeContent);
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

        btnCommit.setOnClickListener(new View.OnClickListener() { // 처리완료 버튼 누를때
            @Override
            public void onClick(View v) {
                try {
                    if(txtProcess.getText().equals("처리 완료")){
                        Toast.makeText(details_select.this, "이미 처리된 항목입니다.",Toast.LENGTH_SHORT).show();
                    }else if(txtProcess.getText().equals("미처리")) {
                        String str = new CustomTask().execute(phone, date, oneContent,twoContent, threeContent, "None").get(); //
                        if(!str.isEmpty()){
                            txtProcess.setText("처리 완료");
                            txtProcessT.setText(str);
                            Toast.makeText(details_select.this, "처리되었습니다.",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(details_select.this, "실패하였습니다.",Toast.LENGTH_SHORT).show();
                        }
                        System.out.println("commit 버튼: "+str); // 조회화면에 자동으로 갱신되지는 않지만 조회버튼 한번 더 누르면 됨 !
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }



    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://13.125.14.62/today/process_complete.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                System.out.println("conn: "+conn);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept-Charset","UTF-8");
                conn.setDoOutput(true); //ENABLE POST REQUEST
                conn.setDoInput(true);

                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "phone="+strings[0]+"&date="+strings[1]+"&oneContent="+strings[2]+"&twoContent="+strings[3]+
                        "&threeContent="+strings[4]+"&kind="+strings[5];
                //phone=155&recept=2020-12-28 16:01:05&content=155155515515551551155151
                System.out.println(sendMsg);
                osw.write(sendMsg);
                osw.flush();
                if(conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();

                } else {
                    Log.i("통신 결과", conn.getResponseCode()+"에러");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg;
        }
    }


}