package com.example.care_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class manager_login extends AppCompatActivity {
    EditText edtID;
    EditText edtPasswd;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_login);
        edtID=(EditText)findViewById(R.id.edtID);
        edtPasswd=(EditText)findViewById(R.id.edtPasswd);
        btnLogin=(Button)findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                try {
                    String id=edtID.getText().toString();
                    id=id.trim();
                    String passwd=edtPasswd.getText().toString();
                    passwd=passwd.trim();
                    String result  = new CustomTask().execute(id, passwd, "login").get();
                    System.out.println("result: "+result);
                    //System.out.println("id: "+id+" passwd: "+passwd);
                    if(result.equals("N")) {
                        System.out.println("로그인 true");
                        Toast.makeText(manager_login.this,"로그인",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(manager_login.this, manager_select_main.class); // 여기서 아이디 전달해주기
                        intent.putExtra("id",id); // 아이디 전달
                        startActivity(intent);
                        finish();
                    }else if(result.equals("S")) {
                        System.out.println("로그인 true");
                        Toast.makeText(manager_login.this,"로그인",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(manager_login.this, manager_super_select_main.class); // 슈퍼관리자 !!!!
                        intent.putExtra("id",id); // 아이디 전달
                        startActivity(intent);
                        finish();
                    }else if(result.equals("false")) {
                        System.out.println("로그인 false");
                        Toast.makeText(manager_login.this,"아이디 또는 비밀번호가 틀렸음",Toast.LENGTH_SHORT).show();
                        edtID.setText("");
                        edtPasswd.setText("");
                    } else if(result.equals("noId")) {
                        System.out.println("로그인 noId");
                        Toast.makeText(manager_login.this,"존재하지 않는 아이디",Toast.LENGTH_SHORT).show();
                        edtID.setText("");
                        edtPasswd.setText("");
                    }
                }catch (Exception e) {}

            }
        });
    }

    class CustomTask extends AsyncTask<String, Void, String>{
        String sendMsg, receiveMsg;
        @Override

        protected String doInBackground(String... strings){
            try{

                String str;
                java.net.URL url=new URL("http://13.125.14.62/today/data.jsp");
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                System.out.println("conn: "+conn);
                conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoOutput(true); //ENABLE POST REQUEST
                conn.setDoInput(true);
                OutputStreamWriter osw1=new OutputStreamWriter(conn.getOutputStream());
                sendMsg="id="+strings[0]+"&passwd="+strings[1]+"&kind="+strings[2];

                osw1.write(sendMsg);
                System.out.println("sendMsg"+sendMsg); // 출력 확인
                osw1.flush(); // 스트림 값을 목적지 파일로 보낸다.
                if(conn.getResponseCode()==conn.HTTP_OK){
                    InputStreamReader tmp=new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader=new BufferedReader(tmp);
                    StringBuffer buffer=new StringBuffer();
                    while((str=reader.readLine())!=null){
                        buffer.append(str);
                    }

                    receiveMsg=buffer.toString();
                    conn.disconnect();
                }else{
                    Log.i("통신결과",conn.getResponseCode()+"에러");
                }
            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }

            return receiveMsg;

        }
    }


}