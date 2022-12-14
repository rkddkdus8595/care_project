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
        phoneNum=intent.getStringExtra("phoneNum");// ????????? ????????? ??????
        depthNameOne=intent.getStringExtra("depthName"); // 1?????? ??????
        depthNameTwo=intent.getStringExtra("depthNameTwo"); // 2?????? ??????
        storeName=intent.getStringExtra("storeName"); // ??? ??????

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        txtOneDepthName.setText(depthNameOne);
        txtTwoDepthName.setText(depthNameTwo);

        // ?????????????????? ????????????????????? ?????? ??????
        try {
            String str = new CustomTask().execute("3", storeName, depthNameOne, depthNameTwo).get();
            System.out.println("????????????: "+str);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ------------ ?????????????????? ?????? ???------------
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(),"2????????? ???????????????.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(customer_three_depth.this, customer_two_depth.class);
                intent.putExtra("phoneNum",phoneNum);
                intent.putExtra("depthName",depthNameOne);
                intent.putExtra("storeName",storeName);
                startActivity(intent);
                finish(); // ???????????? ?????????
            }
        });

        // ------------ ?????? ?????? ?????? ???------------
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subjectStr=new ArrayList<>();
                depthNameStr=new ArrayList<>();

                // Iterator ?????? 3 - entrySet() : key / value
                Set set2 = adapter.hash.entrySet();
                Iterator iterator2 = set2.iterator();
                int i=0;
                while(iterator2.hasNext()){
                    Map.Entry<Integer,String> entry = (Map.Entry)iterator2.next();
                    int key = (Integer)entry.getKey();
                    String value = (String)entry.getValue();
                    subjectStr.add(adapter.mList.get(i++).subject.trim());
                    depthNameStr.add(value);
                    //System.out.println("??????: "+adapter.mList.get(i++).subject);
                    //System.out.println("hashMap Key : " + key);
                    //System.out.println("hashMap Value : " + value);

                }
                System.out.println(subjectStr+"/"+depthNameStr);

                Toast.makeText(getApplication(),"??????????????? ???????????????.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(customer_three_depth.this, customer_final_depth.class);
                intent.putExtra("phoneNum",phoneNum); //  ?????????
                intent.putExtra("depthName",depthNameOne); // ????????? 1??????
                intent.putExtra("depthNameTwo",depthNameTwo); // ????????? 2??????
                intent.putExtra("subjectThree",subjectStr); // ????????? 3?????? ?????????
                intent.putExtra("depthNameThree", depthNameStr); // ????????? 3?????? ????????? ??????
                intent.putExtra("storeName",storeName); // ??? ?????? 
                startActivity(intent);
                //finish(); // ???????????? ????????????
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
                                Intent intent1 = new Intent(customer_three_depth.this, customer_one_depth.class); // ????????? ????????? ?????? ???????????????
                                intent1.putExtra("phoneNum",phoneNum);
                                startActivity(intent1);
                                finish(); // ???????????? ?????????
                                break;
                            case R.id.m2: // ?????????
                                Toast.makeText(getApplication(),"?????? ?????? ????????? ???????????????",Toast.LENGTH_SHORT).show();
                                Intent intent2 = new Intent(customer_three_depth.this, customer_receipt.class); // ????????? ????????? ?????? ???????????????
                                intent2.putExtra("phoneNum",phoneNum);
                                intent2.putExtra("storeName",storeName);
                                startActivity(intent2);
                                finish(); // ???????????? ?????????
                                break;
                            case R.id.m3:
                                new AlertDialog.Builder(customer_three_depth.this).setTitle("????????????").setMessage("???????????? ?????????????????? ?")
                                        .setPositiveButton("????????????", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(customer_three_depth.this, MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                ActivityCompat.finishAffinity(customer_three_depth.this); // ?????? ???????????? ??? ????????? ????????? ???????????? ?????????
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

    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override

        protected String doInBackground(String... strings){
            try{

                String str;
                java.net.URL url=new URL("http://13.125.14.62/today/oneDepth_customer.jsp"); // ???????????? ????????????????????? ????????? ????????? ?????? ???????????? ??? jsp ???????????? ?????? ??????
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
                    JSONArray jArr = json.getJSONArray("question");
                    list = new ArrayList<three_depth_item>(); // ??? ??? ?????????
                    for (int i = 0; i < jArr.length(); i++) {
                        bus = new three_depth_item();
                        json = jArr.getJSONObject(i);
                        bus.setSubject(json.getString("subject"));
                        bus.setDepthName(json.getString("name")); //?????? ??????????????? ?????? ???????????? ???????????? ?????????????????? ?????? !
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
            adapter = new three_depth_recyclerView_adapter(getApplicationContext(), list);
            recyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(new three_depth_recyclerView_adapter.OnItemClickListener() { // ???????????? ???????????? ???????????? ????????? ????????? ?????????
                @Override
                public void onItemClick(int pos, String depthName, String depthName2, String subject) {
                    Toast.makeText(customer_three_depth.this, depthName+"??? ?????????????????????.", Toast.LENGTH_SHORT).show();
                    System.out.println("????????? ???????????? ?????????/?????? : "+pos+" "+subject+"/"+depthName);
                    position=pos;
                    subjectThree=subject;
                    depthNameThree=depthName; // split??? ?????? ?????? ?????????
                    noSplit=depthName2; // ????????? ?????? ?????????
                }
            });

        }
    }

}