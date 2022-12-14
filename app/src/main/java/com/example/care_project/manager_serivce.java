package com.example.care_project;

import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class manager_serivce extends AppCompatActivity {

    ImageButton btnMenu, btnDel, btnAdd, btnUpdate;
    Button btnTwoAdd;
    String getID="",depthName1="", getStoreName="";
    int position;
    String depth="1";
    ArrayList<one_depth_item> list = null;
    one_depth_item bus = null;
    RecyclerView recyclerView;
    one_depth_recyclerView_adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_serivce);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(this).getOrientation());
        //recyclerView.addItemDecoration(dividerItemDecoration);

        btnMenu=(ImageButton)findViewById(R.id.imgBtnMenu);
        btnAdd=(ImageButton)findViewById(R.id.btnAdd);
        btnUpdate=(ImageButton)findViewById(R.id.btnUpdate);
        btnDel=(ImageButton)findViewById(R.id.btnDel);
        btnTwoAdd=(Button)findViewById(R.id.btnTwoAdd);

        Intent intent = getIntent(); /*????????? ??????*/
        getID = intent.getExtras().getString("id"); // ??? ??????????????? ???????????? ????????????
        getStoreName=intent.getStringExtra("storeName");



        // ?????????????????? ????????????????????? ?????? ??????
        try {
            String str = new CustomTask().execute("1", getID).get(); // ?????????????????? ???????????? ?????? ??????
            System.out.println("????????????: "+str);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ------------ 2?????? ?????? ?????? ?????? ??? ------------
        btnTwoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pos=String.valueOf(position);
                if(pos.equals("0") && depthName1.equals("")){
                    Toast.makeText(getApplication(),"????????? ?????? ??????????????????",Toast.LENGTH_SHORT).show();
                }else { //
                    Intent intent = new Intent(manager_serivce.this, two_depth.class);
                    intent.putExtra("id", getID);
                    intent.putExtra("depthName", depthName1);
                    intent.putExtra("storeName",getStoreName);
                    startActivity(intent);
                    finish(); // ?????? ???????????? ????????? ???????????? ??? ????????? ??????????????? ??????????????? ????????? ???????????? !!!!!!
                }
            }
        });
        
        // ------------ ?????? ?????? ?????? ??? ------------
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(manager_serivce.this, one_depth_add.class);
                intent.putExtra("id",getID);
                startActivity(intent);
                finish(); // ?????? ???????????? ????????? ???????????? ??? ????????? ??????????????? ??????????????? ????????? ???????????? !!!!!!
            }
        });

        // ------------ ?????? ?????? ?????? ??? ------------
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pos=String.valueOf(position);
                if(pos.equals("0") && depthName1.equals("")){
                    Toast.makeText(getApplication(),"????????? ?????? ??????????????????",Toast.LENGTH_SHORT).show();
                }else{ // ?????? ????????? ?????? ????????????????????? ????????? ????????? ???????????? !
                    Intent intent=new Intent(manager_serivce.this, one_depth_modify.class);
                    intent.putExtra("id",getID);
                    intent.putExtra("depthName", depthName1);
                    startActivity(intent);
                    finish(); // ?????? ???????????? ????????? ???????????? ??? ????????? ??????????????? ??????????????? ????????? ???????????? !!!!!!
                }

            }
        });

        // ------------ ?????? ?????? ?????? ??? ------------
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pos=String.valueOf(position);
                if(pos.equals("0") && depthName1.equals("")){
                    Toast.makeText(getApplication(),"????????? ?????? ??????????????????",Toast.LENGTH_SHORT).show();
                }else{
                    new AlertDialog.Builder(manager_serivce.this).setTitle("??????").setMessage("???????????????????????? ?")
                            .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        String str = null;
                                        str = new CustomTask1().execute(depth, getID, depthName1,"delete").get();
                                        System.out.println("setOnClick: "+str);
                                        if(str.equals("??????")){
                                            Toast.makeText(manager_serivce.this, "?????????????????????.",Toast.LENGTH_SHORT).show();
                                            finish(); // ?????????????????? !
                                            Intent intent=new Intent(manager_serivce.this, manager_serivce.class);
                                            intent.putExtra("id",getID);
                                            intent.putExtra("storeName",getStoreName);
                                            startActivity(intent);
                                        }else{
                                            Toast.makeText(manager_serivce.this, "????????? ????????????.",Toast.LENGTH_SHORT).show();
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
            }
        });

        // ------------ ???????????? ?????? ??? ------------
        btnMenu.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                PopupMenu popup= new PopupMenu(getApplicationContext(), v);//v??? ????????? ?????? ??????

                getMenuInflater().inflate(R.menu.mymenu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        switch (item.getItemId()){
                            case R.id.m1:
                                Toast.makeText(getApplication(), "?????? ???????????????.", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.m2:
                                Toast.makeText(getApplication(),"?????? ?????? ????????? ???????????????",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(manager_serivce.this, manager_select_main.class); // ????????? ????????? ?????? ???????????????
                                intent.putExtra("id",getID);
                                intent.putExtra("storeName",getStoreName);
                                startActivity(intent);
                                finish(); // ???????????? ?????????
                                break;
                            case R.id.m3:
                                new AlertDialog.Builder(manager_serivce.this).setTitle("????????????").setMessage("???????????? ?????????????????? ?")
                                        .setPositiveButton("????????????", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(manager_serivce.this, MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                ActivityCompat.finishAffinity(manager_serivce.this); // ?????? ???????????? ??? ????????? ????????? ???????????? ?????????
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
    }

    class CustomTask1 extends AsyncTask<String, Void, String> {
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
                sendMsg = "depth="+strings[0]+"&getID="+strings[1]+"&oldContent="+strings[2]+"&kind="+strings[3];
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
                sendMsg="depth="+strings[0]+"&getID="+strings[1]; //

                //URLEncoder.encode(strings[5], "UTF-8");
                osw1.write(sendMsg);
                System.out.println("sendMsg"+sendMsg); // ?????? ??????
                osw1.flush(); // ????????? ?????? ????????? ????????? ?????????.
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
                    list = new ArrayList<one_depth_item>(); // ??? ??? ?????????
                    for (int i = 0; i < jArr.length(); i++) {
                        bus = new one_depth_item();
                        json = jArr.getJSONObject(i);
                        bus.setDepthName(json.getString("name"));
                        list.add(bus);
                    }
                    //System.out.print("????????? ?????????:"+list.size());

                    receiveMsg=buffer.toString();
                    if(page.isEmpty()){
                        receiveMsg="null";
                    }
                    conn.disconnect();
                }else{
                    Log.i("????????????",conn.getResponseCode()+"??????");
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

            //????????? ??????
            adapter = new one_depth_recyclerView_adapter(getApplicationContext(), list);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new one_depth_recyclerView_adapter.OnItemClickListener() { // ???????????? ???????????? ???????????? ????????? ????????? ?????????
                @Override
                public void onItemClick(int pos, String depthName) {
                    Toast.makeText(manager_serivce.this, depthName+"??? ?????????????????????.", Toast.LENGTH_SHORT).show();
                    System.out.println("????????? ???????????? ?????? : "+pos+" "+depthName);
                    position=pos;
                    depthName1=depthName;
                    //Intent intent = new Intent(manager_serivce.this, two_depth.class); // 2????????? ????????????
                    //intent.putExtra("id", getID); // ????????? ????????? ??????
                    //intent.putExtra("depthName", depthName1); // 1?????? ??????
                    //startActivity(intent);
                    //finish();
                }
            });

        }
    }
}