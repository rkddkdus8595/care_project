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
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class customer_three_depth extends AppCompatActivity {

    String phoneNum, depthNameOne, depthNameTwo, storeName;
    ImageButton btnMenu, btnBack, btnCommit;
    TextView txtOneDepthName, txtTwoDepthName;
    String depthNameThree="", subjectThree="" , noSplit="";
    ArrayList<String> subjectStr, depthNameStr;
    int position;
    String depth="3";
    ArrayList<three_depth_item> list = null;
    three_depth_item bus = null;
    RecyclerView recyclerView;
    three_depth_recyclerView_adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_three_depth);

        btnMenu=(ImageButton)findViewById(R.id.imgBtnMenu);
        btnBack=(ImageButton)findViewById(R.id.imgBtnBack);
        txtOneDepthName=(TextView)findViewById(R.id.txtOneDepthName);
        txtTwoDepthName=(TextView)findViewById(R.id.txtTwoDepthName);
        btnCommit=(ImageButton)findViewById(R.id.btnCommit);

        Intent intent=getIntent();
        phoneNum=intent.getStringExtra("phoneNum");// 사용자 핸드폰 번호
        depthNameOne=intent.getStringExtra("depthName"); // 1단계 이름
        depthNameTwo=intent.getStringExtra("depthNameTwo"); // 2단계 이름
        storeName=intent.getStringExtra("storeName"); // 점 이름

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        txtOneDepthName.setText(depthNameOne);
        txtTwoDepthName.setText(depthNameTwo);

        // 시작하자마자 불러와야하니까 바로 실행
        try {
            String str = new CustomTask().execute("3", storeName, depthNameOne, depthNameTwo).get();
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
                Toast.makeText(getApplication(),"2단계로 넘어갑니다.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(customer_three_depth.this, customer_two_depth.class);
                intent.putExtra("phoneNum",phoneNum);
                intent.putExtra("depthName",depthNameOne);
                intent.putExtra("storeName",storeName);
                startActivity(intent);
                finish(); // 종료하고 넘어가
            }
        });

        // ------------ 제출 버튼 클릭 시------------
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subjectStr=new ArrayList<>();
                depthNameStr=new ArrayList<>();

                // Iterator 사용 3 - entrySet() : key / value
                Set set2 = adapter.hash.entrySet();
                Iterator iterator2 = set2.iterator();
                int i=0;
                while(iterator2.hasNext()){
                    Map.Entry<Integer,String> entry = (Map.Entry)iterator2.next();
                    int key = (Integer)entry.getKey();
                    String value = (String)entry.getValue();
                    subjectStr.add(adapter.mList.get(i++).subject.trim());
                    depthNameStr.add(value);
                    //System.out.println("제목: "+adapter.mList.get(i++).subject);
                    //System.out.println("hashMap Key : " + key);
                    //System.out.println("hashMap Value : " + value);

                }
                System.out.println(subjectStr+"/"+depthNameStr);

                Toast.makeText(getApplication(),"최종제출로 넘어갑니다.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(customer_three_depth.this, customer_final_depth.class);
                intent.putExtra("phoneNum",phoneNum); //  폰번호
                intent.putExtra("depthName",depthNameOne); // 선택한 1단계
                intent.putExtra("depthNameTwo",depthNameTwo); // 선택한 2단계
                intent.putExtra("subjectThree",subjectStr); // 선택한 3단계 타이틀
                intent.putExtra("depthNameThree", depthNameStr); // 선택한 3단계 타이틀 내용
                intent.putExtra("storeName",storeName); // 점 이름 
                startActivity(intent);
                //finish(); // 종료하고 넘어가기
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
                                Toast.makeText(getApplication(),"서비스 신청으로 넘어갑니다",Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(customer_three_depth.this, customer_one_depth.class); // 받아온 아이디 다시 전달해주기
                                intent1.putExtra("phoneNum",phoneNum);
                                startActivity(intent1);
                                finish(); // 종료하고 넘어가
                                break;
                            case R.id.m2: // 고치기
                                Toast.makeText(getApplication(),"신청 내역 조회로 넘어갑니다",Toast.LENGTH_SHORT).show();
                                Intent intent2 = new Intent(customer_three_depth.this, customer_receipt.class); // 받아온 아이디 다시 전달해주기
                                intent2.putExtra("phoneNum",phoneNum);
                                intent2.putExtra("storeName",storeName);
                                startActivity(intent2);
                                finish(); // 종료하고 넘어가
                                break;
                            case R.id.m3:
                                new AlertDialog.Builder(customer_three_depth.this).setTitle("로그아웃").setMessage("로그아웃 하시겠습니까 ?")
                                        .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(customer_three_depth.this, MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                ActivityCompat.finishAffinity(customer_three_depth.this); // 모든 액티비티 다 종료후 로그인 화면으로 돌아감
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
                java.net.URL url=new URL("http://13.125.14.62/today/oneDepth_customer.jsp"); // 간단하게 불러오는것이기 때문에 코드가 거의 유사해서 이 jsp 파일에서 모두 진행
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                System.out.println("conn: "+conn);
                conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                conn.setRequestProperty("Accept-Charset","UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestMethod("POST");
                conn.setDoOutput(true); //ENABLE POST REQUEST
                conn.setDoInput(true);

                OutputStreamWriter osw1=new OutputStreamWriter(conn.getOutputStream());
                sendMsg="depth="+strings[0]+"&store="+strings[1]+"&depthName1="+strings[2]+"&depthName2="+strings[3]; //

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
                    list = new ArrayList<three_depth_item>(); // 큰 틀 만들기
                    for (int i = 0; i < jArr.length(); i++) {
                        bus = new three_depth_item();
                        json = jArr.getJSONObject(i);
                        bus.setSubject(json.getString("subject"));
                        bus.setDepthName(json.getString("name")); //얘는 단일일수도 있고 구분자로 이루어진 문자열일수도 있음 !
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
            adapter = new three_depth_recyclerView_adapter(getApplicationContext(), list);
            recyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(new three_depth_recyclerView_adapter.OnItemClickListener() { // 리스트가 넘어가야 작동하기 때문에 여기에 넣어줌
                @Override
                public void onItemClick(int pos, String depthName, String depthName2, String subject) {
                    Toast.makeText(customer_three_depth.this, depthName+"을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                    System.out.println("넘어온 포스값과 타이틀/이름 : "+pos+" "+subject+"/"+depthName);
                    position=pos;
                    subjectThree=subject;
                    depthNameThree=depthName; // split에 의해 잘린 문자열
                    noSplit=depthName2; // 잘리지 않은 문자열
                }
            });

        }
    }

}