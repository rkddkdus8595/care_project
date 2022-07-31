package com.example.care_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class three_depth_add extends AppCompatActivity {

    ImageButton btnMenu, btnBack, btnCommit;
    String getID="",depthNameTwo=""; // 2단계에서 선택된 리사이클러뷰 값
    String depthNameOne=""; // 1단계에서 넘어온 선택된 1단계 리사이클러뷰 값
    EditText edtOne, edtSubject; //타이틀과 그에 대한 내용
    String depth="3", getStoreName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_depth_add);

        btnBack=(ImageButton)findViewById(R.id.btnBack);
        btnCommit=(ImageButton)findViewById(R.id.btnCommit);
        btnMenu=(ImageButton)findViewById(R.id.imgBtnMenu);
        edtOne=(EditText)findViewById(R.id.edtOne);
        edtSubject=(EditText)findViewById(R.id.edtSubject);

        Intent intent=getIntent();
        getID=intent.getStringExtra("id");
        depthNameOne=intent.getStringExtra("depthName");
        depthNameTwo=intent.getStringExtra("depthNameTwo");
        getStoreName=intent.getStringExtra("storeName");
        // ------------ 메뉴버튼 클릭 시 ------------
        btnMenu.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                PopupMenu popup= new PopupMenu(getApplicationContext(), v);//v는 클릭된 뷰를 의미

                getMenuInflater().inflate(R.menu.mymenu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        switch (item.getItemId()){
                            case R.id.m1:
                                Toast.makeText(getApplication(),"서비스 관리로 넘어갑니다.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(three_depth_add.this, manager_serivce.class); // 받아온 아이디 다시 전달해주기
                                intent.putExtra("id",getID);
                                intent.putExtra("storeName",getStoreName);
                                startActivity(intent);
                                finish(); // 종료하고 넘어가
                                break;
                            case R.id.m2:
                                Toast.makeText(getApplication(),"접수 내역 조회로 넘어갑니다",Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(three_depth_add.this, manager_select_main.class); // 받아온 아이디 다시 전달해주기
                                intent1.putExtra("id",getID);
                                intent1.putExtra("storeName",getStoreName);
                                startActivity(intent1);
                                finish(); // 종료하고 넘어가
                                break;
                            case R.id.m3:
                                new AlertDialog.Builder(three_depth_add.this).setTitle("로그아웃").setMessage("로그아웃 하시겠습니까 ?")
                                        .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(three_depth_add.this, MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                ActivityCompat.finishAffinity(three_depth_add.this); // 모든 액티비티 다 종료후 로그인 화면으로 돌아감
                                                startActivity(intent);
                                            }
                                        })
                                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).show();
                            default:
                                break;
                        }
                        return false;
                    }

                });

                popup.show();//Popup Menu 보이기
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "이전화면으로 넘어갑니다.", Toast.LENGTH_SHORT).show();
                finish();
                Intent intent=new Intent(three_depth_add.this, three_depth.class);
                intent.putExtra("id",getID);
                intent.putExtra("depthName",depthNameOne);
                intent.putExtra("depthNameTwo",depthNameTwo);

                intent.putExtra("storeName",getStoreName);
                startActivity(intent);
            }
        });

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtOne.getText().toString().trim().isEmpty() || edtSubject.getText().toString().trim().isEmpty()){
                    edtOne.setError("내용을 입력해주세요");
                    edtSubject.setError("내용을 입력해주세요");
                }else{

                    new AlertDialog.Builder(three_depth_add.this).setTitle("추가").setMessage("추가하시겠습니까 ?")
                            .setPositiveButton("추가", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        String content=edtOne.getText().toString();
                                        content=content.trim(); // 공백제거
                                        String subject=edtSubject.getText().toString();
                                        subject=subject.trim();
                                        String str = new CustomTask().execute(depth, getID, subject, content, depthNameOne,depthNameTwo, "insert").get(); // 몇뎁스인지, 어떤지점인지 알기위해 해당 매니저의 아이디를 같이 전송

                                        System.out.println("setOnClick: "+str);
                                        if(str.equals("성공")){
                                            Toast.makeText(three_depth_add.this, "처리되었습니다.",Toast.LENGTH_SHORT).show();
                                            finish();
                                            Intent intent=new Intent(three_depth_add.this, three_depth.class);
                                            intent.putExtra("id",getID);
                                            intent.putExtra("depthName",depthNameOne);
                                            intent.putExtra("depthNameTwo",depthNameTwo);
                                            intent.putExtra("storeName",getStoreName);
                                            startActivity(intent);
                                        }else{
                                            Toast.makeText(three_depth_add.this, "중복된 값입니다.",Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();

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
                URL url = new URL("http://13.125.14.62/today/threeDepthAdd.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                System.out.println("conn: "+conn);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept-Charset","UTF-8");
                conn.setDoOutput(true); //ENABLE POST REQUEST
                conn.setDoInput(true);

                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "depth="+strings[0]+"&getID="+strings[1]+"&subject="+strings[2]+"&content="+strings[3]+"&depthName="+strings[4]
                        +"&depthNameTwo="+strings[5]+"&kind="+strings[6];
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