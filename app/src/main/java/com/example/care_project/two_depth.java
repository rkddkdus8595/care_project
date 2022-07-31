package com.example.care_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class two_depth extends AppCompatActivity {

    ImageButton btnMenu, btnBack, btnAdd, btnUpdate, btnDel;
    Button btnThreeAdd;
    TextView txtOneDepthName;
    String getID="",depthNameTwo=""; // 2단계에서 선택된 리사이클러뷰 값
    String depthNameOne=""; // 1단계에서 넘어온 선택된 1단계 리사이클러뷰 값
    String getStoreName;
    int position;
    String depth="2";
    ArrayList<two_depth_item> list = null;
    two_depth_item bus = null;
    RecyclerView recyclerView;
    two_depth_recyclerView_adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_depth);
        btnMenu=(ImageButton)findViewById(R.id.imgBtnMenu);
        btnBack=(ImageButton)findViewById(R.id.imgBtnBack);

        btnAdd=(ImageButton)findViewById(R.id.btnAdd);
        btnUpdate=(ImageButton)findViewById(R.id.btnUpdate);
        btnDel=(ImageButton)findViewById(R.id.btnDel);
        btnThreeAdd=(Button)findViewById(R.id.btnThreeAdd);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent=getIntent();
        getID=intent.getStringExtra("id");
        depthNameOne=intent.getStringExtra("depthName");
        getStoreName=intent.getStringExtra("storeName");
        txtOneDepthName=(TextView)findViewById(R.id.txtOneDepthName);
        txtOneDepthName.setText(depthNameOne);

        // 시작하자마자 불러와야하니까 바로 실행
        try {
            String str = new CustomTask().execute("2", getID, depthNameOne).get(); // 몇뎁스인지랑 아이디, 1뎁스의 이름까지 전달
            System.out.println("성공여부: "+str);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // ------------ 뒤로가기버튼 클릭 시------------
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(),"서비스 관리로 넘어갑니다.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(two_depth.this, manager_serivce.class); // 받아온 아이디 다시 전달해주기
                intent.putExtra("id",getID);
                intent.putExtra("storeName",getStoreName);

                startActivity(intent);
                finish(); // 종료하고 넘어가
            }
        });
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
                                Intent intent = new Intent(two_depth.this, manager_serivce.class); // 받아온 아이디 다시 전달해주기
                                intent.putExtra("id",getID);
                                intent.putExtra("storeName",getStoreName);

                                startActivity(intent);
                                finish(); // 종료하고 넘어가
                                break;
                            case R.id.m2:
                                Toast.makeText(getApplication(),"접수 내역 조회로 넘어갑니다",Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(two_depth.this, manager_select_main.class); // 받아온 아이디 다시 전달해주기
                                intent1.putExtra("id",getID);
                                intent1.putExtra("storeName",getStoreName);

                                startActivity(intent1);
                                finish(); // 종료하고 넘어가
                                break;
                            case R.id.m3:
                                new AlertDialog.Builder(two_depth.this).setTitle("로그아웃").setMessage("로그아웃 하시겠습니까 ?")
                                        .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(two_depth.this, MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                ActivityCompat.finishAffinity(two_depth.this); // 모든 액티비티 다 종료후 로그인 화면으로 돌아감
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

        btnThreeAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pos=String.valueOf(position);
                if(pos.equals("0") && depthNameTwo.equals("")){
                    Toast.makeText(getApplication(),"원하는 값을 선택해주세요",Toast.LENGTH_SHORT).show();
                }else { //
                    Intent intent = new Intent(two_depth.this, three_depth.class);
                    intent.putExtra("id", getID);
                    intent.putExtra("depthName", depthNameOne); // 1단계 이름
                    intent.putExtra("depthNameTwo", depthNameTwo); // 2단계 이름
                    intent.putExtra("storeName",getStoreName);
                    startActivity(intent);
                    finish(); // 끊고 넘어가는 이유는 추가하고 이 화면에 돌아왔을때 추가된걸로 보이게 만들려고 !!!!!!
                }
            }
        });
        // ------------ 추가 버튼 클릭 시 ------------
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(two_depth.this, two_depth_add.class);
                intent.putExtra("id",getID);
                intent.putExtra("depthName",depthNameOne);
                intent.putExtra("storeName",getStoreName);

                startActivity(intent);
                finish(); // 끊고 넘어가는 이유는 추가하고 이 화면에 돌아왔을때 추가된걸로 보이게 만들려고 !!!!!!
            }
        });

        // ------------ 수정 버튼 클릭 시 ------------
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pos=String.valueOf(position);
                if(pos.equals("0") && depthNameTwo.equals("")){
                    Toast.makeText(getApplication(),"원하는 값을 선택해주세요",Toast.LENGTH_SHORT).show();
                }else{ // 수정 누르기 전에 리사이클러뷰를 선택한 기록이 있어야함 !
                    Intent intent=new Intent(two_depth.this, two_depth_modify.class);
                    intent.putExtra("id",getID);
                    intent.putExtra("depthNameOne", depthNameOne);
                    intent.putExtra("depthName", depthNameTwo);
                    intent.putExtra("storeName",getStoreName);

                    startActivity(intent);
                    finish(); // 끊고 넘어가는 이유는 추가하고 이 화면에 돌아왔을때 수정된걸로 보이게 만들려고 !!!!!!
                }

            }
        });

        // ------------ 삭제 버튼 클릭 시 ------------
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pos=String.valueOf(position);
                if(pos.equals("0") && depthNameTwo.equals("")){
                    Toast.makeText(getApplication(),"원하는 값을 선택해주세요",Toast.LENGTH_SHORT).show();
                }else{
                    new AlertDialog.Builder(two_depth.this).setTitle("삭제").setMessage("삭제하시겠습니까 ?")
                            .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String str = null;
                                    try {
                                        str = new CustomTask1().execute(depth, getID, depthNameTwo, depthNameOne, "delete").get();
                                        System.out.println("setOnClick: "+str);
                                        if(str.equals("성공")){
                                            Toast.makeText(two_depth.this, "처리되었습니다.",Toast.LENGTH_SHORT).show();
                                            finish(); // 재시작하려고 !
                                            Intent intent=new Intent(two_depth.this, two_depth.class);
                                            intent.putExtra("id",getID);
                                            intent.putExtra("depthName", depthNameOne);
                                            intent.putExtra("storeName",getStoreName);

                                            startActivity(intent);
                                        }else{
                                            Toast.makeText(two_depth.this, "중복된 값입니다.",Toast.LENGTH_SHORT).show();
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

    class CustomTask1 extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://13.125.14.62/today/twoDepthAdd.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                System.out.println("conn: "+conn);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept-Charset","UTF-8");
                conn.setDoOutput(true); //ENABLE POST REQUEST
                conn.setDoInput(true);

                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "depth="+strings[0]+"&getID="+strings[1]+"&oldContent="+strings[2]+"&depthName="+strings[3]+"&kind="+strings[4];
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

    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override

        protected String doInBackground(String... strings){
            try{

                String str;
                java.net.URL url=new URL("http://13.125.14.62/today/oneDepth.jsp");
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                System.out.println("conn: "+conn);
                conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                conn.setRequestProperty("Accept-Charset","UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestMethod("POST");
                conn.setDoOutput(true); //ENABLE POST REQUEST
                conn.setDoInput(true);

                OutputStreamWriter osw1=new OutputStreamWriter(conn.getOutputStream());
                sendMsg="depth="+strings[0]+"&getID="+strings[1]+"&depthName1="+strings[2]; //

                //URLEncoder.encode(strings[5], "UTF-8");
                osw1.write(sendMsg);
                System.out.println("sendMsg"+sendMsg); // 출력 확인
                osw1.flush(); // 스트림 값을 목적지 파일로 보낸다.
                if(conn.getResponseCode()==conn.HTTP_OK){
                    InputStreamReader tmp=new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader=new BufferedReader(tmp);
                    StringBuffer buffer=new StringBuffer();
                    String page = "";
                    //String line = null;
                    while((str=reader.readLine())!=null){
                        buffer.append(str);
                        page+=str;
                    }
                    System.out.println("doIn: "+page);

                    JSONObject json = new JSONObject(page);
                    JSONArray jArr = json.getJSONArray("receipt");
                    list = new ArrayList<two_depth_item>(); // 큰 틀 만들기
                    for (int i = 0; i < jArr.length(); i++) {
                        bus = new two_depth_item();
                        json = jArr.getJSONObject(i);
                        bus.setDepthName(json.getString("name"));
                        list.add(bus);
                    }
                    //System.out.print("리스트 사이즈:"+list.size());

                    receiveMsg=buffer.toString();
                    if(page.isEmpty()){
                        receiveMsg="null";
                    }
                    conn.disconnect();
                }else{
                    Log.i("통신결과",conn.getResponseCode()+"에러");
                }
            }catch (MalformedURLException e){
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return receiveMsg;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //어답터 연결
            adapter = new two_depth_recyclerView_adapter(getApplicationContext(), list);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new two_depth_recyclerView_adapter.OnItemClickListener() { // 리스트가 넘어가야 작동하기 때문에 여기에 넣어줌
                @Override
                public void onItemClick(int pos, String depthName) {
                    Toast.makeText(two_depth.this, depthName+"을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                    System.out.println("넘어온 포스값과 이름 : "+pos+" "+depthName);
                    position=pos;
                    depthNameTwo=depthName;

                    //Intent intent = new Intent(two_depth.this, three_depth.class); // 2단계로 넘어가기
                    //intent.putExtra("id", getID); // 사용자 핸드폰 번호
                    //intent.putExtra("depthName", depthNameOne); // 1단계 이름
                    //intent.putExtra("depthNameTwo", depthNameTwo); // 1단계 이름
                    //startActivity(intent);
                    //finish();

                }
            });

        }
    }
}