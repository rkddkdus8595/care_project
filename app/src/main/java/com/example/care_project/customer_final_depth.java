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
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class customer_final_depth extends AppCompatActivity {

    ImageButton btnMenu, btnBack, btnCommit;
    String phoneNum, depthNameOne, depthNameTwo, storeName;
    TextView txtStore, txtOneDepthName, txtTwoDepthName;
    ArrayList<String> subjectStr, depthNameStr;

    ArrayList<three_depth_item> list = null;
    three_depth_item bus = null;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_final_depth);

        btnBack=(ImageButton) findViewById(R.id.imgBtnBack);
        btnCommit=(ImageButton)findViewById(R.id.btnCommit);
        btnMenu=(ImageButton)findViewById(R.id.imgBtnMenu);

        txtStore=(TextView)findViewById(R.id.txtStore);
        txtOneDepthName=(TextView)findViewById(R.id.txtOneDepthName);
        txtTwoDepthName=(TextView)findViewById(R.id.txtTwoDepthName);

        Intent intent=getIntent();
        phoneNum=intent.getStringExtra("phoneNum"); //  폰번호
        depthNameOne=intent.getStringExtra("depthName"); // 선택한 1단계
        depthNameTwo=intent.getStringExtra("depthNameTwo"); // 선택한 2단계
        subjectStr = (ArrayList<String>) intent.getSerializableExtra("subjectThree");
        depthNameStr=(ArrayList<String>) intent.getSerializableExtra("depthNameThree");// 선택한 3단계 타이틀 내용
        storeName=intent.getStringExtra("storeName"); // 점 이름

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        CustomTask myAsyncTask = new CustomTask();
        myAsyncTask.execute();

        System.out.println("파이널 화면/ "+subjectStr+"/"+depthNameStr);

        txtStore.setText(storeName);
        txtOneDepthName.setText(depthNameOne);
        txtTwoDepthName.setText(depthNameTwo);

        // ------------ 뒤로가기버튼 클릭 시------------
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(),"2단계로 넘어갑니다.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(customer_final_depth.this, customer_three_depth.class);
                intent.putExtra("phoneNum",phoneNum);
                intent.putExtra("depthName",depthNameOne);
                intent.putExtra("depthNameTwo",depthNameTwo);
                intent.putExtra("storeName",storeName);
                startActivity(intent);
                finish(); // 종료하고 넘어가
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
                                Intent intent1 = new Intent(customer_final_depth.this, customer_one_depth.class); // 받아온 아이디 다시 전달해주기
                                intent1.putExtra("phoneNum", phoneNum);
                                startActivity(intent1);
                                finish(); // 종료하고 넘어가
                                break;
                            case R.id.m2: // 고치기
                                Toast.makeText(getApplication(),"신청 내역 조회로 넘어갑니다",Toast.LENGTH_SHORT).show();
                                Intent intent2 = new Intent(customer_final_depth.this, customer_receipt.class); // 받아온 아이디 다시 전달해주기
                                intent2.putExtra("phoneNum",phoneNum);
                                intent2.putExtra("storeName",storeName);
                                startActivity(intent2);
                                finish(); // 종료하고 넘어가
                                break;
                            case R.id.m3:
                                new AlertDialog.Builder(customer_final_depth.this).setTitle("로그아웃").setMessage("로그아웃 하시겠습니까 ?")
                                        .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(customer_final_depth.this, MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                ActivityCompat.finishAffinity(customer_final_depth.this); // 모든 액티비티 다 종료후 로그인 화면으로 돌아감
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

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(customer_final_depth.this).setTitle("접수").setMessage("접수하시겠습니까 ?")
                        .setPositiveButton("접수", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String str = null; // 몇뎁스인지, 어떤지점인지 알기위해 해당 매니저의 아이디를 같이 전송
                                try {
                                    String subjectName="", depthName3="";
                                    subjectName= StringUtils.join(subjectStr.toArray(),','); // jsp로 보내기 위해 arraylist를 문자열로 합쳐주었다.
                                    depthName3=StringUtils.join(depthNameStr.toArray(),',');


                                    str = new CustomTask1().execute(phoneNum, depthNameOne, depthNameTwo, subjectName, depthName3, storeName, "final").get();
                                    System.out.println("setOnClick: "+str);
                                    if(str.equals("성공")){
                                        Toast.makeText(customer_final_depth.this, "접수되었습니다.",Toast.LENGTH_SHORT).show();
                                        finish();
                                        Intent intent=new Intent(customer_final_depth.this, customer_one_depth.class);
                                        intent.putExtra("phoneNum", phoneNum);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(customer_final_depth.this, "접수에 실패하였습니다.",Toast.LENGTH_SHORT).show();
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
        });
    }
    class CustomTask1 extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://13.125.14.62/today/customer_request.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                System.out.println("conn: "+conn);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept-Charset","UTF-8");
                conn.setDoOutput(true); //ENABLE POST REQUEST
                conn.setDoInput(true);

                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "phoneNum="+strings[0]+"&depthNameOne="+strings[1]+"&depthNameTwo="+strings[2]+"&subjectName="+strings[3]+"&depthName3="+strings[4]
                        +"&storeName="+strings[5]+"&kind="+strings[6];
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
            list = new ArrayList<three_depth_item>(); // 큰 틀 만들기
            for (int i = 0; i < subjectStr.size(); i++) {
                bus = new three_depth_item();
                bus.setSubject(subjectStr.get(i));
                bus.setDepthName(depthNameStr.get(i));
                list.add(bus);
            }
            return receiveMsg;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //어답터 연결
            customer_final_depth_adapter adapter = new customer_final_depth_adapter(getApplicationContext(), list);
            recyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(new customer_final_depth_adapter.OnItemClickListener() { // 리스트가 넘어가야 작동하기 때문에 여기에 넣어줌
                @Override
                public void onItemClick(int pos, String depthName, String subject) {
                    Toast.makeText(customer_final_depth.this, depthName+"을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                    System.out.println("넘어온 포스값과 타이틀/이름 : "+pos+" "+subject+"/"+depthName);
                }
            });

        }
    }
}