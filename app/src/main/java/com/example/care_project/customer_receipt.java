package com.example.care_project;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.text.ParseException;
import java.util.Date;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class customer_receipt extends AppCompatActivity{ //implements customer_RecyclerAdapter_select.OnLoadMoreListener{

    TabLayout tabLayout;
    Spinner storeSelect1, storeSelect2;
    ImageButton btnMenu, btnCal1, btnCal2 , btnNext, btnPrevious;
    Button btnSearch;
    EditText edtCal1, edtCal2;
    ArrayList<customer_item> list = new ArrayList<>();
    ArrayList<customer_item> list2=null;
    customer_item bus = null;
    RecyclerView recyclerView;
    Calendar cal = Calendar.getInstance();
    int year1, month1, day1, year2, month2, day2 ;
    public String phoneNum="", storeName="";
    int tabPosition;
    String tabPosition1, store, detailStore;
    customer_Adapter adapter;
    int start=0;
    int currentNum=1, pageNum=0, noneChk=0;
    TextView txtCurrentPage, txtTotalPage;
    ArrayList<String> seoulStorelst=new ArrayList<>(), capitalStorelst=new ArrayList<>(), provinceStorelst=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_receipt);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        tabLayout=(TabLayout)findViewById(R.id.tab_layout); // 탭
        btnMenu=(ImageButton)findViewById(R.id.imgBtnMenu); // 메뉴 버튼
        btnCal1=(ImageButton)findViewById(R.id.imgBtnCal1); // 달력 왼쪽
        btnCal2=(ImageButton)findViewById(R.id.imgBtnCal2); // 달력 오른쪽

        btnSearch=(Button)findViewById(R.id.btnSearch); // 조회 버튼
        btnNext=(ImageButton)findViewById(R.id.btnNext);
        btnPrevious=(ImageButton)findViewById(R.id.btnPrevious);

        edtCal1=(EditText)findViewById(R.id.edtCal1); // 왼쪽 달력 출력
        edtCal2=(EditText)findViewById(R.id.edtCal2); // 오른쪽 달력 출력

        txtCurrentPage=(TextView)findViewById(R.id.txtCurrentPage);
        txtTotalPage=(TextView)findViewById(R.id.txtTotalPage);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        list2 = new ArrayList<customer_item>();
        String[] nothing=getResources().getStringArray(R.array.nothing); // 전체 눌렀을때

        storeSelect1=(Spinner)findViewById(R.id.storeSelect1); // 서울 이런거 선택
        storeSelect2=(Spinner)findViewById(R.id.storeSelect2); // 지점 선택

        Intent intent = getIntent(); /*데이터 수신*/
        phoneNum = intent.getExtras().getString("phoneNum"); //
        //storeName = intent.getExtras().getString("storeName"); // 전 인텐트에서 아이디를 받아오기

        System.out.println("탭 위치: "+tabLayout.getSelectedTabPosition());
        tabPosition=tabLayout.getSelectedTabPosition(); // 탭위치를 저장 !
        if(tabPosition==0)
            tabPosition1="전체";
        else if(tabPosition==1)
            tabPosition1="미처리";
        else if(tabPosition==2)
            tabPosition1="처리완료";

        // ----------- 탭 클릭 시 ------------
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                System.out.println("탭 위치: "+tabLayout.getSelectedTabPosition());
                tabPosition=tabLayout.getSelectedTabPosition(); // 변경될때마다 포지션에 저장함
                if(tabPosition==0)
                    tabPosition1="전체";
                else if(tabPosition==1)
                    tabPosition1="미처리";
                else if(tabPosition==2)
                    tabPosition1="처리완료";
                int pos = tab.getPosition() ;
                if (pos == 0) { // 첫 번째 탭 선택.

                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // TODO : tab의 상태가 선택되지 않음으로 변경.
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // TODO : 이미 선택된 tab이 다시
            }
        });




        storeSelect1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                store=parent.getItemAtPosition(position).toString();
                System.out.println("store:"+store);
                String str = null;
                try {
                    str = new CustomTask2().execute(store, "customer").get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("setOnClick: "+str);
                if(store.equals("서울")){
                    ArrayAdapter adapter=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, seoulStorelst);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    storeSelect2.setAdapter(adapter);
                }else if(store.equals("수도권")){
                    ArrayAdapter adapter=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, capitalStorelst);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    storeSelect2.setAdapter(adapter);
                }else if(store.equals("지방")){
                    ArrayAdapter adapter=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, provinceStorelst);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    storeSelect2.setAdapter(adapter);
                }else if(store.equals("전체")){
                    ArrayAdapter adapter=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, nothing);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    storeSelect2.setAdapter(adapter);
                    detailStore="전체";
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        storeSelect2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!store.equals("전체")){ // 전체가 아닐경우에만 detailStore변수에 값 저장
                    detailStore=parent.getItemAtPosition(position).toString();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        Date today = null;
        Date today1 = null;

        try { // 얘는 기본값으로 조회하자마자 보여지게 !
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

            detailStore="전체";
            String str = new CustomTask().execute(Integer.toString(year1), Integer.toString(month1), Integer.toString(day1),
                    Integer.toString(year2), Integer.toString(month2), Integer.toString(day2), phoneNum, detailStore, tabPosition1, "customer").get(); // 해당하는 아이디를 가진 점의 정보만 조회가능하게 만들었음
            System.out.println("setOnClick: "+str);

            start=0;
            System.out.println("리스트 사이즈 :"+list.size());
            if(list.isEmpty() || list.size()==0){
                currentNum=0;
                AlertDialog.Builder dialog = new AlertDialog.Builder(customer_receipt.this);
                dialog.setTitle("알림")
                        .setMessage("해당 기간 접수 내용이 없습니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create().show();
                Toast.makeText(getApplication(),"접수된 내용이 없습니다.",Toast.LENGTH_SHORT).show();
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
                        new DatePickerDialog(customer_receipt.this, picker1, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();

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
                        new DatePickerDialog(customer_receipt.this, picker2, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();

                        break;
                    }
                }
                return false;
            }
        });


        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list.size()!=0){
                    System.out.println("start"+start);
                    start=start-5; // 뒤로 가기니까 빼주기
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
                        txtCurrentPage.setText(currentNum + ""); // 이전 누를대마다 페이지 하나씩 줄어듬
                        list2 = new ArrayList<>(); // 큰 틀 만들기
                        for (int i = start; i < end; i++) {
                            bus = new customer_item();
                            bus.setDate(list.get(i).txtDate);
                            bus.setOneContent(list.get(i).txtOneContent);
                            bus.setTwoContent(list.get(i).txtTwoContent);
                            bus.setThreeContent(list.get(i).txtThreeContent);
                            bus.setProcess(list.get(i).txtProcess);
                            bus.setProcessT(list.get(i).txtProcessT);
                            bus.setPhoneNum(list.get(i).phoneNum);
                            bus.setStoreName(list.get(i).storeName);
                            list2.add(bus);
                        }

                        adapter = new customer_Adapter(getApplicationContext(), list2);
                        recyclerView.setAdapter(adapter);
                    }

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
                    txtCurrentPage.setText(currentNum + ""); // 다음 누를때마다 1페이지씩 늘어남
                    list2 = new ArrayList<>(); // 큰 틀 만들기
                    for (int i = start; i < end; i++) {
                        bus = new customer_item();
                        bus.setDate(list.get(i).txtDate);
                        bus.setOneContent(list.get(i).txtOneContent);
                        bus.setTwoContent(list.get(i).txtTwoContent);
                        bus.setThreeContent(list.get(i).txtThreeContent);
                        bus.setProcess(list.get(i).txtProcess);
                        bus.setProcessT(list.get(i).txtProcessT);
                        bus.setPhoneNum(list.get(i).phoneNum);
                        bus.setStoreName(list.get(i).storeName);
                        list2.add(bus);
                        //System.out.println(list.get(i).txtDate);
                    }

                    adapter = new customer_Adapter(getApplicationContext(), list2);
                    recyclerView.setAdapter(adapter);
                }
                System.out.println(start+"/"+end+"/"+list2.size()+"/"+list.size());

            }
        });





        // ----------- 조회버튼 클릭 시 ------------
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // 년, 월, 일 전달
                    // customTask를 미리 불러서 list에 필요한 값을 가져온 다음에

                    list.clear(); // 조회할때마다 달라지게 !
                    if (noneChk != 0) {
                        String str = new CustomTask().execute(Integer.toString(year1), Integer.toString(month1), Integer.toString(day1),
                                Integer.toString(year2), Integer.toString(month2), Integer.toString(day2), phoneNum, detailStore, tabPosition1, "customer").get(); // 해당하는 아이디를 가진 점의 정보만 조회가능하게 만들었음
                        System.out.println("setOnClick: "+str);
                    }

                    if(detailStore.equals("전체")) {
                        String str = new CustomTask().execute(Integer.toString(year1), Integer.toString(month1), Integer.toString(day1),
                                Integer.toString(year2), Integer.toString(month2), Integer.toString(day2), phoneNum, detailStore, tabPosition1, "customer").get(); // 해당하는 아이디를 가진 점의 정보만 조회가능하게 만들었음
                        System.out.println("setOnClick: " + str);
                    }
                    start=0;
                    if(list.isEmpty() || list.size()==0){
                        currentNum=0;
                        AlertDialog.Builder dialog = new AlertDialog.Builder(customer_receipt.this);
                        dialog.setTitle("알림")
                                .setMessage("해당 기간 접수 내용이 없습니다.")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .create().show();
                        Toast.makeText(getApplication(),"접수된 내용이 없습니다.",Toast.LENGTH_SHORT).show();
                    }else{
                        currentNum=1;
                    }
                    //currentNum=1;
                    list2.clear();
                    loadData1();
                    //System.out.println("조회쪽에 adapter"+adapter.getItemCount());
                    //System.out.println("test"+list.get(0));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

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
                                Toast.makeText(getApplication(), "접수 신청 화면으로 넘어갑니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(customer_receipt.this, customer_one_depth.class); // 받아온 아이디 다시 전달해주기
                                intent.putExtra("phoneNum",phoneNum);
                                intent.putExtra("storeName",storeName);
                                startActivity(intent);
                                finish(); // 종료하고 넘어가
                                break;
                            case R.id.m2:
                                Toast.makeText(getApplication(),"현재 화면입니다.",Toast.LENGTH_SHORT).show();

                                break;
                            case R.id.m3:
                                new AlertDialog.Builder(customer_receipt.this).setTitle("로그아웃").setMessage("로그아웃 하시겠습니까 ?")
                                        .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent intent = new Intent(customer_receipt.this, MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                ActivityCompat.finishAffinity(customer_receipt.this); // 모든 액티비티 다 종료후 로그인 화면으로 돌아감
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

        // ------------달력-------------
        btnCal1.setOnClickListener(new View.OnClickListener() { // 왼쪽 달력 버튼 클릭 시
            @Override
            public void onClick(View v) {
                new DatePickerDialog(customer_receipt.this, picker1, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnCal2.setOnClickListener(new View.OnClickListener() { // 오른쪽 달력 버튼 클릭 시
            @Override
            public void onClick(View v) {
                new DatePickerDialog(customer_receipt.this, picker2, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) { // 선택X상태 -> 선택상태 변경

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { // 선택 X 상태

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void loadData1() {
        list2.clear();
        list2 = new ArrayList<customer_item>(); // 큰 틀 만들기
        if(list.size()>=0){
            int number=0;
            if(list.size()<5){
                number=list.size();
            }else{
                number=5;
            }
            for (int i = 0; i < number; i++) {
                bus = new customer_item(); // 인덱스 아이디를 넣어주기
                bus.setDate(list.get(i).txtDate);
                bus.setOneContent(list.get(i).txtOneContent);
                bus.setTwoContent(list.get(i).txtTwoContent);
                bus.setThreeContent(list.get(i).txtThreeContent);
                bus.setProcess(list.get(i).txtProcess);
                bus.setProcessT(list.get(i).txtProcessT);
                bus.setPhoneNum(list.get(i).phoneNum);
                bus.setStoreName(list.get(i).storeName);
                list2.add(bus);
            }
            pageNum=0;
            if(list.size()%5==0){ // 딱 맞아 떨어지는거니까 바로 페이지수로 직행
                pageNum=list.size()/5;
            }else if(list.size()<5){ // 5보다 작은경우는 페이지수 무조건 1
                pageNum=1;
            }else{ // 5보다 작지 않은 경우 &
                pageNum=list.size()/5+1;
            }
            txtTotalPage.setText(pageNum+"");  // 토탈 페이지가 몇페이지까지 있는지
            txtCurrentPage.setText(currentNum+""); //
            adapter = new customer_Adapter(getApplicationContext(), list2);  //----잠깐 테스트 원래는 여기서 어댑터랑 연결해줌 !!!!!
            recyclerView.setAdapter(adapter);
        }

    }


    DatePickerDialog.OnDateSetListener picker1 = new DatePickerDialog.OnDateSetListener() { // 왼쪽 달력
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            year1=year;
            month1=month+1; // 1을 더해줘야 우리가 알고있는 달이 나옴
            day1=dayOfMonth;

            updateLabel1();
        }
    };

    DatePickerDialog.OnDateSetListener picker2 = new DatePickerDialog.OnDateSetListener() { // 오른쪽 달력
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            year2=year;
            month2=month+1; // 1을 더해줘야 우리가 알고있는 달이 나옴
            day2=dayOfMonth;

            updateLabel2();
        }
    };

    // 달력에서 날짜 선택시 셋팅해주기 위한 메서드
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
                        "&month2="+strings[4]+"&day2="+strings[5]+"&getID="+strings[6]+"&storeName="+strings[7]+"&tabPosition="+strings[8]+"&kind="+strings[9]; //

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
                    /*String[] data = page.split("|");
                    for (String string : data) {
                        System.out.println(string);
                    }*/
                    System.out.println("doIn: "+page);

                    JSONObject json = new JSONObject(page);
                    JSONArray jArr = json.getJSONArray("receipt");
                    list = new ArrayList<customer_item>(); // 큰 틀 만들기
                    for (int i = 0; i < jArr.length(); i++) {
                        bus = new customer_item(); // 인덱스 아이디를 넣어주기
                        json = jArr.getJSONObject(i);
                        bus.setDate(json.getString("date"));
                        bus.setOneContent(json.getString("oneContent"));
                        bus.setTwoContent(json.getString("twoContent"));
                        bus.setThreeContent(json.getString("threeContent"));
                        bus.setProcess(json.getString("process"));
                        bus.setProcessT(json.getString("processT"));
                        bus.setPhoneNum(json.getString("phone"));
                        if(strings[7].equals("전체")){
                            bus.setStoreName(json.getString("storeName"));
                        }else{
                            bus.setStoreName(strings[7]); // 전체가 아닐때는 storeName이 없기대문에 바로 집어넣어줌
                        }
                        list.add(bus);
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

            //어답터 연결
            //adapter = new customer_RecyclerAdapter_select(getApplicationContext(), list);  ----잠깐 테스트 원래는 여기서 어댑터랑 연결해줌 !!!!!
            //recyclerView.setAdapter(adapter);
        }
    }

    class CustomTask2 extends AsyncTask<String, Void, String> {
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
                        noneChk=0;
                    }
                    for (int i = 0; i < jArr.length(); i++) {
                        noneChk=1;
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