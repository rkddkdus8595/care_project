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
        phoneNum=intent.getStringExtra("phoneNum"); //  ?????????
        depthNameOne=intent.getStringExtra("depthName"); // ????????? 1??????
        depthNameTwo=intent.getStringExtra("depthNameTwo"); // ????????? 2??????
        subjectStr = (ArrayList<String>) intent.getSerializableExtra("subjectThree");
        depthNameStr=(ArrayList<String>) intent.getSerializableExtra("depthNameThree");// ????????? 3?????? ????????? ??????
        storeName=intent.getStringExtra("storeName"); // ??? ??????

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        CustomTask myAsyncTask = new CustomTask();
        myAsyncTask.execute();

        System.out.println("????????? ??????/ "+subjectStr+"/"+depthNameStr);

        txtStore.setText(storeName);
        txtOneDepthName.setText(depthNameOne);
        txtTwoDepthName.setText(depthNameTwo);

        // ------------ ?????????????????? ?????? ???------------
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(),"2????????? ???????????????.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(customer_final_depth.this, customer_three_depth.class);
                intent.putExtra("phoneNum",phoneNum);
                intent.putExtra("depthName",depthNameOne);
                intent.putExtra("depthNameTwo",depthNameTwo);
                intent.putExtra("storeName",storeName);
                startActivity(intent);
                finish(); // ???????????? ?????????
            }
        });

        // ------------ ???????????? ?????? ??? ------------
        btnMenu.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                PopupMenu popup= new PopupMenu(getApplicationContext(), v);//v??? ????????? ?????? ??????

                getMenuInflater().inflate(R.menu.customer_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        switch (item.getItemId()){
                            case R.id.m1:
                                Toast.makeText(getApplication(),"????????? ???????????? ???????????????",Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(customer_final_depth.this, customer_one_depth.class); // ????????? ????????? ?????? ???????????????
                                intent1.putExtra("phoneNum", phoneNum);
                                startActivity(intent1);
                                finish(); // ???????????? ?????????
                                break;
                            case R.id.m2: // ?????????
                                Toast.makeText(getApplication(),"?????? ?????? ????????? ???????????????",Toast.LENGTH_SHORT).show();
                                Intent intent2 = new Intent(customer_final_depth.this, customer_receipt.class); // ????????? ????????? ?????? ???????????????
                                intent2.putExtra("phoneNum",phoneNum);
                                intent2.putExtra("storeName",storeName);
                                startActivity(intent2);
                                finish(); // ???????????? ?????????
                                break;
                            case R.id.m3:
                                new AlertDialog.Builder(customer_final_depth.this).setTitle("????????????").setMessage("???????????? ?????????????????? ?")
                                        .setPositiveButton("????????????", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(customer_final_depth.this, MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                ActivityCompat.finishAffinity(customer_final_depth.this); // ?????? ???????????? ??? ????????? ????????? ???????????? ?????????
                                                startActivity(intent);
                                            }
                                        })
                                        .setNegativeButton("??????", new DialogInterface.OnClickListener() {
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

                popup.show();//Popup Menu ?????????
            }
        });

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(customer_final_depth.this).setTitle("??????").setMessage("???????????????????????? ?")
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String str = null; // ???????????????, ?????????????????? ???????????? ?????? ???????????? ???????????? ?????? ??????
                                try {
                                    String subjectName="", depthName3="";
                                    subjectName= StringUtils.join(subjectStr.toArray(),','); // jsp??? ????????? ?????? arraylist??? ???????????? ???????????????.
                                    depthName3=StringUtils.join(depthNameStr.toArray(),',');


                                    str = new CustomTask1().execute(phoneNum, depthNameOne, depthNameTwo, subjectName, depthName3, storeName, "final").get();
                                    System.out.println("setOnClick: "+str);
                                    if(str.equals("??????")){
                                        Toast.makeText(customer_final_depth.this, "?????????????????????.",Toast.LENGTH_SHORT).show();
                                        finish();
                                        Intent intent=new Intent(customer_final_depth.this, customer_one_depth.class);
                                        intent.putExtra("phoneNum", phoneNum);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(customer_final_depth.this, "????????? ?????????????????????.",Toast.LENGTH_SHORT).show();
                                    }
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }


                            }
                        })
                        .setNegativeButton("??????", new DialogInterface.OnClickListener() {
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
                    Log.i("?????? ??????", conn.getResponseCode()+"??????");
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
            list = new ArrayList<three_depth_item>(); // ??? ??? ?????????
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

            //????????? ??????
            customer_final_depth_adapter adapter = new customer_final_depth_adapter(getApplicationContext(), list);
            recyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(new customer_final_depth_adapter.OnItemClickListener() { // ???????????? ???????????? ???????????? ????????? ????????? ?????????
                @Override
                public void onItemClick(int pos, String depthName, String subject) {
                    Toast.makeText(customer_final_depth.this, depthName+"??? ?????????????????????.", Toast.LENGTH_SHORT).show();
                    System.out.println("????????? ???????????? ?????????/?????? : "+pos+" "+subject+"/"+depthName);
                }
            });

        }
    }
}