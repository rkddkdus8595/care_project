package com.example.care_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class customer_two_depth extends AppCompatActivity {

    ImageButton btnMenu, btnBack;
    TextView txtOneDepthName;
    String phoneNum="",depthNameTwo=""; // 2단계에서 선택된 리사이클러뷰 값
    String depthNameOne=""; // 1단계에서 넘어온 선택된 1단계 리사이클러뷰 값
    String storeName="";
    int position;
    String depth="2";
    ArrayList<two_depth_item> list = null;
    two_depth_item bus = null;
    RecyclerView recyclerView;
    two_depth_recyclerView_adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_two_depth);

        btnMenu=(ImageButton)findViewById(R.id.imgBtnMenu);
        btnBack=(ImageButton)findViewById(R.id.imgBtnBack);
        txtOneDepthName=(TextView)findViewById(R.id.txtOneDepthName);

        Intent intent=getIntent();
        phoneNum=intent.getStringExtra("phoneNum");
        depthNameOne=intent.getStringExtra("depthName");
        storeName=intent.getStringExtra("storeName");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        txtOneDepthName=(TextView)findViewById(R.id.txtOneDepthName);
        txtOneDepthName.setText(depthNameOne);

        // 시작하자마자 불러와야하니까 바로 실행
        try {
            String str = new CustomTask().execute("2", storeName, depthNameOne).get();
            System.out.println("성공여부: "+str);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ------------ 메뉴버튼 클릭 시 ------------
        btnMenu.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                PopupMenu popup= new PopupMenu(getApplicationContext(), v);//v는 클릭된 뷰를 의미

                getMenuInflater().inflate(R.menu.customer_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        switch (item.getItemId()){
                            case R.id.m1:
                                Toast.makeText(getApplication(),"서비스 신청으로 넘어갑니다",Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(customer_two_depth.this, customer_one_depth.class); // 받아온 아이디 다시 전달해주기
                                intent1.putExtra("id",phoneNum);
                                startActivity(intent1);
                                finish(); // 종료하고 넘어가
                                break;
                            case R.id.m2: // 고치기
                                Toast.makeText(getApplication(),"신청 내역 조회로 넘어갑니다",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(customer_two_depth.this, customer_receipt.class); // 받아온 아이디 다시 전달해주기
                                intent.putExtra("phoneNum",phoneNum);
                                intent.putExtra("storeName",storeName);
                                startActivity(intent);
                                finish(); // 종료하고 넘어가
                                break;
                            case R.id.m3:
                                new AlertDialog.Builder(customer_two_depth.this).setTitle("로그아웃").setMessage("로그아웃 하시겠습니까 ?")
                                        .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(customer_two_depth.this, MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                ActivityCompat.finishAffinity(customer_two_depth.this); // 모든 액티비티 다 종료후 로그인 화면으로 돌아감
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

        // ------------ 뒤로가기버튼 클릭 시------------
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(),"서비스 신청으로 넘어갑니다.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(customer_two_depth.this, customer_one_depth.class); // 받아온 아이디 다시 전달해주기
                intent.putExtra("phoneNum",phoneNum);
                startActivity(intent);
                finish(); // 종료하고 넘어가
            }
        });
    }

    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override

        protected String doInBackground(String... strings){
            try{

                String str;
                java.net.URL url=new URL("http://13.125.14.62/today/oneDepth_customer.jsp");
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                System.out.println("conn: "+conn);
                conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                conn.setRequestProperty("Accept-Charset","UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestMethod("POST");
                conn.setDoOutput(true); //ENABLE POST REQUEST
                conn.setDoInput(true);

                OutputStreamWriter osw1=new OutputStreamWriter(conn.getOutputStream());
                sendMsg="depth="+strings[0]+"&store="+strings[1]+"&depthName1="+strings[2]; //

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
                    JSONArray jArr = json.getJSONArray("question");
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
                    Toast.makeText(customer_two_depth.this, depthName+"을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                    System.out.println("넘어온 포스값과 이름 : "+pos+" "+depthName);
                    position=pos;
                    depthNameTwo=depthName;
                    Intent intent = new Intent(customer_two_depth.this, customer_three_depth.class); // 2단계로 넘어가기
                    intent.putExtra("phoneNum", phoneNum); // 사용자 핸드폰 번호
                    intent.putExtra("depthName", depthNameOne); // 1단계 이름
                    intent.putExtra("depthNameTwo",depthNameTwo); // 2단계 이름
                    intent.putExtra("storeName", storeName); // 점 이름
                    startActivity(intent);
                    //finish();
                }
            });

        }
    }
}