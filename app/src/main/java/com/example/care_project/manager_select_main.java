package com.example.care_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class manager_select_main extends AppCompatActivity {

    TabLayout tabLayout;
    ImageButton btnMenu, btnCal1, btnCal2, btnNext, btnPrevious;
    Button btnSearch;
    EditText edtCal1, edtCal2;
    ArrayList<item> list = new ArrayList<>();
    ArrayList<item> list2=new ArrayList<>();
    item bus = null;
    RecyclerView recyclerView;
    Calendar cal = Calendar.getInstance();
    int year1, month1, day1, year2, month2, day2 ;
    public String getID="";
    int tabPosition;
    String tabPosition1 , getStoreName;
    TextView txtCurrentPage, txtTotalPage;
    RecyclerAdapter_select adapter;
    int start=0;
    int currentNum=1, pageNum=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_select_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        tabLayout=(TabLayout)findViewById(R.id.tab_layout); // ???
        btnMenu=(ImageButton)findViewById(R.id.imgBtnMenu); // ?????? ??????
        btnCal1=(ImageButton)findViewById(R.id.imgBtnCal1); // ?????? ??????
        btnCal2=(ImageButton)findViewById(R.id.imgBtnCal2); // ?????? ?????????

        btnSearch=(Button) findViewById(R.id.btnSearch); // ?????? ??????
        btnNext=(ImageButton)findViewById(R.id.btnNext);
        btnPrevious=(ImageButton)findViewById(R.id.btnPrevious);
        edtCal1=(EditText)findViewById(R.id.edtCal1); // ?????? ?????? ??????
        edtCal2=(EditText)findViewById(R.id.edtCal2); // ????????? ?????? ??????

        txtCurrentPage=(TextView)findViewById(R.id.txtCurrentPage);
        txtTotalPage=(TextView)findViewById(R.id.txtTotalPage);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        list2 = new ArrayList<item>();

        Intent intent = getIntent(); /*????????? ??????*/
        getID = intent.getExtras().getString("id"); // ??? ??????????????? ???????????? ????????????
        getStoreName=intent.getStringExtra("storeName");

        AlertDialog.Builder dialog = new AlertDialog.Builder(manager_select_main.this);
        dialog.setTitle("Welcome")
                .setMessage("???????????????, "+getStoreName+" ???????????? ??????????????? ")
                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create().show();

        System.out.println("??? ??????: "+tabLayout.getSelectedTabPosition());
        tabPosition=tabLayout.getSelectedTabPosition(); // ???????????? ?????? !
        if(tabPosition==0)
            tabPosition1="??????";
        else if(tabPosition==1)
            tabPosition1="?????????";
        else if(tabPosition==2)
            tabPosition1="????????????";

        // ----------- ??? ?????? ??? ------------
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                System.out.println("??? ??????: "+tabLayout.getSelectedTabPosition());
                tabPosition=tabLayout.getSelectedTabPosition(); // ?????????????????? ???????????? ?????????
                if(tabPosition==0)
                    tabPosition1="??????";
                else if(tabPosition==1)
                    tabPosition1="?????????";
                else if(tabPosition==2)
                    tabPosition1="????????????";
                int pos = tab.getPosition() ;
                if (pos == 0) { // ??? ?????? ??? ??????.

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // TODO : tab??? ????????? ???????????? ???????????? ??????.
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // TODO : ?????? ????????? tab??? ??????
            }
        });

        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        Date today = null;
        Date today1 = null;

        try { // ?????? ??????????????? ?????????????????? ???????????? !
            today = sdf.parse(cal.get(Calendar.YEAR) +"/"+ (cal.get(Calendar.MONTH)+1) +"/"+ (cal.get(Calendar.DATE)-7));
            today1 = sdf.parse(cal.get(Calendar.YEAR) +"/"+ (cal.get(Calendar.MONTH)+1) +"/"+ cal.get(Calendar.DATE));
            String s = sdf.format(today);
            String s1=sdf.format(today1);
            edtCal1.setText(s);
            edtCal2.setText(s1);
            String[] str123=s.split("/");
            year1=Integer.valueOf(str123[0]);
            month1=Integer.valueOf(str123[1]);
            day1=Integer.valueOf(str123[2]);

            String[] str1234=s1.split("/");
            year2=Integer.valueOf(str1234[0]);
            month2=Integer.valueOf(str1234[1]);
            day2=Integer.valueOf(str1234[2]);

            String str = new CustomTask().execute(Integer.toString(year1), Integer.toString(month1), Integer.toString(day1),
                    Integer.toString(year2), Integer.toString(month2), Integer.toString(day2), getID, tabPosition1, "manager").get(); // ???????????? ???????????? ?????? ?????? ????????? ?????????????????? ????????????
            System.out.println("setOnClick: "+str);

            start=0;
            System.out.println("????????? ????????? :"+list.size());
            if(list.isEmpty() || list.size()==0){
                currentNum=0;
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(manager_select_main.this);
                dialog1.setTitle("??????")
                        .setMessage("?????? ?????? ?????? ????????? ????????????.")
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create().show();
                Toast.makeText(getApplication(),"????????? ????????? ????????????.",Toast.LENGTH_SHORT).show();
            }else{
                currentNum=1;
            }
            //currentNum=1;
            list2.clear();
            loadData1();


        } catch (ParseException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        edtCal1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        new DatePickerDialog(manager_select_main.this, picker1, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();

                        break;
                    }
                }
                return false;
            }
        });

        edtCal2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        new DatePickerDialog(manager_select_main.this, picker2, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();

                        break;
                    }
                }
                return false;
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("start"+start);
                start=start-5; // ?????? ???????????? ?????????
                int end=start+5;

                if(start<0){
                    start=0;
                }
                if(end>list.size()){
                    end=list.size();
                }
                if(end<list2.size()){
                    end=list2.size();
                }
                System.out.println("start"+start+"/end:"+end+"/"+adapter.getItemCount());
                if(start>=0){
                    if(currentNum<=1){
                        currentNum=1;
                    }else{
                        currentNum--;
                    }
                    txtCurrentPage.setText(currentNum + ""); // ?????? ??????????????? ????????? ????????? ?????????
                    list2 = new ArrayList<>(); // ??? ??? ?????????
                    for (int i = start; i < end; i++) {
                        bus = new item();
                        bus.setDate(list.get(i).txtDate);
                        bus.setOneContent(list.get(i).txtOneContent);
                        bus.setTwoContent(list.get(i).txtTwoContent);
                        bus.setThreeContent(list.get(i).txtThreeContent);
                        bus.setProcess(list.get(i).txtProcess);
                        bus.setProcessT(list.get(i).txtProcessT);
                        bus.setPhoneNum(list.get(i).phoneNum);
                        list2.add(bus);
                    }

                    adapter = new RecyclerAdapter_select(getApplicationContext(), list2);
                    recyclerView.setAdapter(adapter);
                }

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(start>=list.size()-1){
                    start=start;
                }else{
                    start = start+adapter.getItemCount();
                }

                int end = start + 5;

                if(end>list.size()){
                    end=list.size();
                }

                if(start>=list.size()){

                }else{
                    if(currentNum>=pageNum){
                        currentNum=pageNum;
                    }else{
                        currentNum++;
                    }
                    txtCurrentPage.setText(currentNum + ""); // ?????? ??????????????? 1???????????? ?????????
                    list2 = new ArrayList<>(); // ??? ??? ?????????
                    for (int i = start; i < end; i++) {
                        bus = new item();
                        bus.setDate(list.get(i).txtDate);
                        bus.setOneContent(list.get(i).txtOneContent);
                        bus.setTwoContent(list.get(i).txtTwoContent);
                        bus.setThreeContent(list.get(i).txtThreeContent);
                        bus.setProcess(list.get(i).txtProcess);
                        bus.setProcessT(list.get(i).txtProcessT);
                        bus.setPhoneNum(list.get(i).phoneNum);
                        list2.add(bus);
                        //System.out.println(list.get(i).txtDate);
                    }

                    adapter = new RecyclerAdapter_select(getApplicationContext(), list2);
                    recyclerView.setAdapter(adapter);
                }
                System.out.println(start+"/"+end+"/"+list2.size()+"/"+list.size());

            }
        });


        // ----------- ???????????? ?????? ??? ------------
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // ???, ???, ??? ??????
                    // customTask??? ?????? ????????? list??? ????????? ?????? ????????? ?????????
                    String str = new CustomTask().execute(Integer.toString(year1), Integer.toString(month1), Integer.toString(day1),
                            Integer.toString(year2), Integer.toString(month2), Integer.toString(day2), getID, tabPosition1, "manager").get(); // ???????????? ???????????? ?????? ?????? ????????? ?????????????????? ????????????
                    System.out.println("setOnClick: "+str);
                    start=0;
                    if(list.isEmpty() || list.size()==0){
                        currentNum=0;
                        AlertDialog.Builder dialog = new AlertDialog.Builder(manager_select_main.this);
                        dialog.setTitle("??????")
                                .setMessage("?????? ?????? ?????? ????????? ????????????.")
                                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .create().show();
                        Toast.makeText(getApplication(),"????????? ????????? ????????????.",Toast.LENGTH_SHORT).show();
                    }else{
                        currentNum=1;
                    }
                    list2.clear();
                    loadData1();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
                                Toast.makeText(getApplication(),"????????? ????????? ???????????????.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(manager_select_main.this, manager_serivce.class);
                                intent.putExtra("id",getID); // ????????? ?????????????????? ?????? ??????????????? ???????????? ???????????? ?????????,,,
                                intent.putExtra("storeName",getStoreName);
                                startActivity(intent);
                                finish(); // ???????????? ????????????
                                break;
                            case R.id.m2:
                                Toast.makeText(getApplication(), "?????? ???????????????.", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.m3:
                                new AlertDialog.Builder(manager_select_main.this).setTitle("????????????").setMessage("???????????? ?????????????????? ?")
                                        .setPositiveButton("????????????", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(manager_select_main.this, MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                ActivityCompat.finishAffinity(manager_select_main.this); // ?????? ???????????? ??? ????????? ????????? ???????????? ?????????
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

        // ------------??????-------------
        btnCal1.setOnClickListener(new View.OnClickListener() { // ?????? ?????? ?????? ?????? ???
            @Override
            public void onClick(View v) {
                new DatePickerDialog(manager_select_main.this, picker1, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnCal2.setOnClickListener(new View.OnClickListener() { // ????????? ?????? ?????? ?????? ???
            @Override
            public void onClick(View v) {
                new DatePickerDialog(manager_select_main.this, picker2, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) { // ??????X?????? -> ???????????? ??????

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { // ?????? X ??????

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void loadData1() {
        //currentNum=1;
        list2.clear();
        list2 = new ArrayList<item>(); // ??? ??? ?????????
        if(list.size()>=0){
            int number=0;
            if(list.size()<5){
                number=list.size();
            }else{
                number=5;
            }
            for (int i = 0; i < number; i++) {
                bus = new item(); // ????????? ???????????? ????????????
                bus.setDate(list.get(i).txtDate);
                bus.setOneContent(list.get(i).txtOneContent);
                bus.setTwoContent(list.get(i).txtTwoContent);
                bus.setThreeContent(list.get(i).txtThreeContent);
                bus.setProcess(list.get(i).txtProcess);
                bus.setProcessT(list.get(i).txtProcessT);
                bus.setPhoneNum(list.get(i).phoneNum);
                list2.add(bus);
            }
            pageNum=0;
            if(list.size()%5==0){ // ??? ?????? ????????????????????? ?????? ??????????????? ??????
                pageNum=list.size()/5;
            }else if(list.size()<5){ // 5?????? ??????????????? ???????????? ????????? 1
                pageNum=1;
            }else{ // 5?????? ?????? ?????? ?????? &
                pageNum=list.size()/5+1;
            }
            txtTotalPage.setText(pageNum+"");  // ?????? ???????????? ?????????????????? ?????????
            txtCurrentPage.setText(currentNum+"");
            adapter = new RecyclerAdapter_select(getApplicationContext(), list2);  //----?????? ????????? ????????? ????????? ???????????? ???????????? !!!!!
            recyclerView.setAdapter(adapter);
        }
    }

    DatePickerDialog.OnDateSetListener picker1 = new DatePickerDialog.OnDateSetListener() { // ?????? ??????
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            year1=year;
            month1=month+1; // 1??? ???????????? ????????? ???????????? ?????? ??????
            day1=dayOfMonth;

            updateLabel1();
        }
    };

    DatePickerDialog.OnDateSetListener picker2 = new DatePickerDialog.OnDateSetListener() { // ????????? ??????
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            year2=year;
            month2=month+1; // 1??? ???????????? ????????? ???????????? ?????? ??????
            day2=dayOfMonth;

            updateLabel2();
        }
    };

    // ???????????? ?????? ????????? ??????????????? ?????? ?????????
    private void updateLabel1() {
        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
        edtCal1.setText(sdf.format(cal.getTime()));
    }

    private void updateLabel2() {
        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        edtCal2.setText(sdf.format(cal.getTime()));

    }

    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override

        protected String doInBackground(String... strings){
            try{

                String str;
                java.net.URL url=new URL("http://13.125.14.62/today/data1.jsp");
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                System.out.println("conn: "+conn);
                conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                conn.setRequestProperty("Accept-Charset","UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestMethod("POST");
                conn.setDoOutput(true); //ENABLE POST REQUEST
                conn.setDoInput(true);

                OutputStreamWriter osw1=new OutputStreamWriter(conn.getOutputStream());
                sendMsg="year1="+strings[0]+"&month1="+strings[1]+
                        "&day1="+strings[2]+"&year2="+strings[3]+
                        "&month2="+strings[4]+"&day2="+strings[5]+"&getID="+strings[6]+"&tabPosition="+strings[7]+"&kind="+strings[8]; //

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
                    /*String[] data = page.split("|");
                    for (String string : data) {
                        System.out.println(string);
                    }*/
                    System.out.println("doIn: "+page);

                    JSONObject json = new JSONObject(page);
                    JSONArray jArr = json.getJSONArray("receipt");
                    list = new ArrayList<item>(); // ??? ??? ?????????
                    for (int i = 0; i < jArr.length(); i++) {
                        bus = new item();
                        json = jArr.getJSONObject(i);
                        bus.setDate(json.getString("date"));
                        bus.setOneContent(json.getString("oneContent"));
                        bus.setTwoContent(json.getString("twoContent"));
                        bus.setThreeContent(json.getString("threeContent"));
                        bus.setProcess(json.getString("process"));
                        bus.setProcessT(json.getString("processT"));
                        bus.setPhoneNum(json.getString("phone"));
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
            //RecyclerAdapter_select adapter = new RecyclerAdapter_select(getApplicationContext(), list);
            //recyclerView.setAdapter(adapter);

        }
    }
}