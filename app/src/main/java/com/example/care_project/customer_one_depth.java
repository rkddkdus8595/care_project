package com.example.care_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
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

public class customer_one_depth extends AppCompatActivity {

    Spinner storeSelect1, storeSelect2;
    String phoneNum, store, detailStore; // 폰번호, 지점의 큰 타이틀, 지점 이름
    ImageButton btnMenu;
    String depthName1="";
    int position;
    String depth="1";
    ArrayList<one_depth_item> list = null;
    one_depth_item bus = null;
    RecyclerView recyclerView;
    one_depth_recyclerView_adapter adapter;
    ArrayList<String> seoulStorelst=new ArrayList<>(), capitalStorelst=new ArrayList<>(), provinceStorelst=new ArrayList<>();
    customer_login_Fra customer_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_one_depth);

        customer_login = new customer_login_Fra();

        btnMenu=(ImageButton)findViewById(R.id.imgBtnMenu);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        storeSelect1=(Spinner)findViewById(R.id.storeSelect1); // 서울 이런거 선택
        storeSelect2=(Spinner)findViewById(R.id.storeSelect2); // 지점 선택

        //String[] seoulStore = getResources().getStringArray(R.array.seoulStore); // 서울지점 배열
        //String[] capitalStore=getResources().getStringArray(R.array.capitalStore); // 수도권지점 배열
        //String[] provinceStore=getResources().getStringArray(R.array.provinceStore); // 지방지점 배열
        
        Intent intent=getIntent();
        phoneNum=intent.getStringExtra("phoneNum");


        storeSelect1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                store=parent.getItemAtPosition(position).toString();
                String str = null;
                try {
                    str = new CustomTask1().execute(store, "customer").get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("setOnClick: "+str);
                if(store.equals("서울")){
                    System.out.println("수도권1지역");
                    ArrayAdapter adapter=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, seoulStorelst);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    storeSelect2.setAdapter(adapter);
                }else if(store.equals("수도권")){
                    System.out.println("수도권2지역");
                    ArrayAdapter adapter=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, capitalStorelst);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    storeSelect2.setAdapter(adapter);
                }else if(store.equals("지방")){
                    ArrayAdapter adapter=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, provinceStorelst);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    storeSelect2.setAdapter(adapter);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        storeSelect2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                detailStore=parent.getItemAtPosition(position).toString();
                // 선택하면 계속 불러올 수 있게끔.
                try {
                    String str = new CustomTask().execute("1", detailStore).get(); // 몇뎁스인지랑 지점은 어디인지
                    System.out.println("성공여부: "+str);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                                Toast.makeText(getApplication(), "현재 화면입니다.", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.m2:
                                Toast.makeText(getApplication(),"신청 내역 조회로 넘어갑니다",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(customer_one_depth.this, customer_receipt.class); // 받아온 아이디 다시 전달해주기
                                intent.putExtra("phoneNum",phoneNum);
                                //intent.putExtra("storeName",detailStore);
                                startActivity(intent);
                                //finish(); // 종료하고 넘어가
                                break;
                            case R.id.m3:
                                new AlertDialog.Builder(customer_one_depth.this).setTitle("로그아웃").setMessage("로그아웃 하시겠습니까 ?")
                                        .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(customer_one_depth.this, MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                ActivityCompat.finishAffinity(customer_one_depth.this); // 모든 액티비티 다 종료후 로그인 화면으로 돌아감
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
                sendMsg="depth="+strings[0]+"&store="+strings[1]; //

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
                    list = new ArrayList<one_depth_item>(); // 큰 틀 만들기
                    for (int i = 0; i < jArr.length(); i++) {
                        bus = new one_depth_item();
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
            adapter = new one_depth_recyclerView_adapter(getApplicationContext(), list);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new one_depth_recyclerView_adapter.OnItemClickListener() { // 리스트가 넘어가야 작동하기 때문에 여기에 넣어줌
                @Override
                public void onItemClick(int pos, String depthName) {
                    Toast.makeText(customer_one_depth.this, depthName+"을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                    System.out.println("넘어온 포스값과 이름 : "+pos+" "+depthName);
                    position=pos;
                    depthName1=depthName;
                    Intent intent = new Intent(customer_one_depth.this, customer_two_depth.class); // 2단계로 넘어가기
                    intent.putExtra("phoneNum", phoneNum); // 사용자 핸드폰 번호
                    intent.putExtra("depthName", depthName1); // 1단계 이름
                    intent.putExtra("storeName", detailStore); // 점 이름
                    startActivity(intent);
                    //finish();
                }
            });

        }
    }

    class CustomTask1 extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override

        protected String doInBackground(String... strings){
            try{

                String str;
                java.net.URL url=new URL("http://13.125.14.62/today/getStoreName.jsp");
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                System.out.println("conn: "+conn);
                conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                conn.setRequestProperty("Accept-Charset","UTF-8");
                //conn.setRequestProperty("Accept", "application/json");
                conn.setRequestMethod("POST");
                conn.setDoOutput(true); //ENABLE POST REQUEST
                conn.setDoInput(true);

                OutputStreamWriter osw1=new OutputStreamWriter(conn.getOutputStream());
                sendMsg="groupName="+strings[0]+"&kind="+strings[1]; //

                //URLEncoder.encode(strings[5], "UTF-8");
                osw1.write(sendMsg);
                System.out.println("sendMsg"+sendMsg); // 출력 확인
                osw1.flush(); // 스트림 값을 목적지 파일로 보낸다.
                if(conn.getResponseCode()==conn.HTTP_OK){
                    InputStreamReader tmp=new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader=new BufferedReader(tmp);
                    StringBuffer buffer=new StringBuffer();
                    String page = "";

                    while((str=reader.readLine())!=null){
                        buffer.append(str);
                        page+=str;
                    }

                    System.out.println("doIn: "+page);
                    seoulStorelst.clear();
                    capitalStorelst.clear();
                    provinceStorelst.clear();
                    JSONObject json = new JSONObject(page);
                    JSONArray jArr = json.getJSONArray("store");

                    if(jArr.length()==0){ // 아무것도 없을때는 리사이클러뷰가 초기화되게 !!!
                        seoulStorelst.add("");
                        capitalStorelst.add("");
                        provinceStorelst.add("");
                    }
                    for (int i = 0; i < jArr.length(); i++) {
                        json = jArr.getJSONObject(i);
                        if(json.getString("groupName").equals("서울")){
                            seoulStorelst.add(json.getString("storeName"));
                        }else if(json.getString("groupName").equals("수도권")){
                            capitalStorelst.add(json.getString("storeName"));
                        }else if(json.getString("groupName").equals("지방")){
                            provinceStorelst.add(json.getString("storeName"));
                        }
                    }
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


        }
    }

}