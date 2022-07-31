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

public class one_depth_modify extends AppCompatActivity {
    String depthName="",id="";
    TextView txtOldContent;
    EditText edtNewContent;
    String depth="1", getStoreName="";
    ImageButton btnMenu, btnBack, btnCommit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_depth_modify);

        txtOldContent=(TextView)findViewById(R.id.txtOldContent);
        edtNewContent=(EditText)findViewById(R.id.edtNewContent);
        btnBack=(ImageButton)findViewById(R.id.btnBack);
        btnCommit=(ImageButton)findViewById(R.id.btnCommit);
        btnMenu=(ImageButton)findViewById(R.id.imgBtnMenu);

        Intent intent = getIntent(); /*데이터 수신*/
        depthName = intent.getStringExtra("depthName"); // 전 인텐트에서 아이디를 받아오기
        id=intent.getStringExtra("id");
        getStoreName=intent.getStringExtra("storeName");
        txtOldContent.setText(depthName);

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
                                Toast.makeText(getApplication(), "서비스 관리로 넘어갑니다.", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(one_depth_modify.this, manager_serivce.class); // 받아온 아이디 다시 전달해주기
                                intent1.putExtra("id",id);
                                intent1.putExtra("storeName",getStoreName);
                                startActivity(intent1);
                                break;
                            case R.id.m2:
                                Toast.makeText(getApplication(),"접수 내역 조회로 넘어갑니다",Toast.LENGTH_SHORT).show();
                                Intent intent2 = new Intent(one_depth_modify.this, manager_select_main.class); // 받아온 아이디 다시 전달해주기
                                intent2.putExtra("id",id);
                                intent2.putExtra("storeName",getStoreName);
                                startActivity(intent2);
                                finish(); // 종료하고 넘어가
                                break;
                            case R.id.m3:
                                new AlertDialog.Builder(one_depth_modify.this).setTitle("로그아웃").setMessage("로그아웃 하시겠습니까 ?")
                                        .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(one_depth_modify.this, MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                ActivityCompat.finishAffinity(one_depth_modify.this); // 모든 액티비티 다 종료후 로그인 화면으로 돌아감
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
                Intent intent=new Intent(one_depth_modify.this, manager_serivce.class);
                intent.putExtra("id",id);
                intent.putExtra("storeName",getStoreName);

                startActivity(intent);
            }
        });

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtNewContent.getText().toString().isEmpty()){
                    edtNewContent.setError("내용을 입력해주세요");
                }else{
                    String oldContent=txtOldContent.getText().toString();
                    String newContent=edtNewContent.getText().toString();
                    new AlertDialog.Builder(one_depth_modify.this).setTitle("수정").setMessage("수정하시겠습니까 ?")
                            .setPositiveButton("수정", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {

                                        String str = new CustomTask().execute(depth, id, depthName, newContent, "update").get(); // 몇뎁스인지, 어떤지점인지 알기위해 해당 매니저의 아이디를 같이 전송
                                        System.out.println("setOnClick: "+str);
                                        if(str.equals("성공")){
                                            Toast.makeText(one_depth_modify.this, "처리되었습니다.",Toast.LENGTH_SHORT).show();
                                            finish();
                                            Intent intent=new Intent(one_depth_modify.this, manager_serivce.class);
                                            intent.putExtra("id",id);
                                            intent.putExtra("storeName",getStoreName);
                                            startActivity(intent);
                                        }else{
                                            Toast.makeText(one_depth_modify.this, "중복된 값입니다.",Toast.LENGTH_SHORT).show();
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
                URL url = new URL("http://13.125.14.62/today/oneDepthAdd.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                System.out.println("conn: "+conn);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept-Charset","UTF-8");
                conn.setDoOutput(true); //ENABLE POST REQUEST
                conn.setDoInput(true);

                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "depth="+strings[0]+"&getID="+strings[1]+"&oldContent="+strings[2]+"&newContent="+strings[3]+"&kind="+strings[4];
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