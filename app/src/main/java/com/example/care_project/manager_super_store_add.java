package com.example.care_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class manager_super_store_add extends AppCompatActivity {
    ImageButton btnMenu, btnCommit;
    EditText edtStoreID, edtStoreName, edtID, edtPasswd;
    String groupName="", groupName2="", storeID="", storeName="", id="", passwd="";
    String getID="", store="", store_C; // 인텐트에서 넘어온 슈퍼매니저 아이디
    RadioGroup rdoG;
    Spinner storeSelect1,customer_store;
    TextView txtID, txtPasswd;
    boolean rdoCheck=false; // 자동 추가인 상태

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_super_store_add);

        btnMenu=(ImageButton)findViewById(R.id.imgBtnMenu);
        btnCommit=(ImageButton)findViewById(R.id.btnCommit);
        //edtGroupName=(EditText)findViewById(R.id.edtGroupName);
        edtStoreID=(EditText)findViewById(R.id.edtStoreID);
        edtStoreName=(EditText)findViewById(R.id.edtStoreName);
        edtID=(EditText)findViewById(R.id.edtID);
        edtPasswd=(EditText)findViewById(R.id.edtPasswd);
        rdoG=(RadioGroup)findViewById(R.id.rdoGroup);
        txtID=(TextView)findViewById(R.id.textView18);
        txtPasswd=(TextView)findViewById(R.id.textView19);
        storeSelect1=(Spinner)findViewById(R.id.storeSelect1);
        customer_store=(Spinner)findViewById(R.id.customer_store);

        Intent intent=getIntent();
        getID=intent.getStringExtra("id");
        
        rdoG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(checkedId); // 체크된 라디오 버튼 갖고오기
                if(radioButton.getText().toString().equals("수동 추가")){ // 수동 추가를 했을때 !
                    txtID.setVisibility(View.VISIBLE);
                    edtID.setVisibility(View.VISIBLE);
                    txtPasswd.setVisibility(View.VISIBLE);
                    edtPasswd.setVisibility(View.VISIBLE);
                    rdoCheck=true;
                }if(radioButton.getText().toString().equals("자동 추가")){ //
                    txtID.setVisibility(View.INVISIBLE);
                    edtID.setVisibility(View.INVISIBLE);
                    txtPasswd.setVisibility(View.INVISIBLE);
                    edtPasswd.setVisibility(View.INVISIBLE);
                    rdoCheck=false;
                    Toast.makeText(manager_super_store_add.this, "아이디와 비밀번호는 지점 코드로 자동추가 됩니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        storeSelect1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                store=parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        customer_store.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                store_C=parent.getItemAtPosition(position).toString();
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

                getMenuInflater().inflate(R.menu.supermanager_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        switch (item.getItemId()){
                            case R.id.m1:
                                Toast.makeText(getApplication(),"현재화면입니다.",Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.m2:
                                Toast.makeText(getApplication(),"접수 내역 조회로 넘어갑니다.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(manager_super_store_add.this, manager_super_select_main.class);
                                intent.putExtra("id",getID); // 서비스 관리화면에서 다시 돌아올때도 아이디가 필요하기 때문에,,,
                                startActivity(intent);
                                finish(); // 종료하고 넘어간다
                                break;
                            case R.id.m3:
                                new AlertDialog.Builder(manager_super_store_add.this).setTitle("로그아웃").setMessage("로그아웃 하시겠습니까 ?")
                                        .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(manager_super_store_add.this, MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                ActivityCompat.finishAffinity(manager_super_store_add.this); // 모든 액티비티 다 종료후 로그인 화면으로 돌아감
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
                boolean flag=false;
                boolean commitChk=true;

                if(edtStoreID.getText().toString().trim().isEmpty()){
                    edtStoreID.setError("내용을 입력해주세요");
                    commitChk=false;
                }if(!edtStoreID.getText().toString().trim().isEmpty() && edtStoreID.getText().toString().length()==2){ // 길이가 두글자여야함
                    char chr;
                    flag=false;
                    int cnt=0;
                    for(int i=0; i<edtStoreID.getText().toString().trim().length();i++){
                        chr = edtStoreID.getText().toString().trim().charAt(i); // 입력받은 텍스트에서 문자 하나하나 가져와서 체크
                        if (chr >= 0x30 && chr <= 0x39) { // 숫자만 입력하기
                            flag=true;
                            cnt++;
                        }
                        else if(chr >=0x41 && chr <= 0x5A){ // 대문자만 입력
                            cnt++;
                            flag=true;
                        }
                    }
                    if(cnt!=2){ // 숫자하나 대문자 하나니까 2여야함
                        edtStoreID.setError("숫자와 대문자를 한글자씩 섞어서 입력해주세요 !");
                        commitChk=false;
                    }
                }if(edtStoreName.getText().toString().trim().isEmpty()){
                    edtStoreName.setError("내용을 입력해주세요");
                    commitChk=false;
                }else if(!edtStoreName.getText().toString().trim().substring(edtStoreName.getText().toString().trim().length()-1).equals("점")){
                    edtStoreName.setError("~점의 형태로 입력해주세요");
                    commitChk=false;
                }
                groupName=store.trim();
                groupName2=store_C.trim();
                storeID=edtStoreID.getText().toString().trim();
                storeName=edtStoreName.getText().toString().trim();
                
                if(rdoCheck){ // 수동추가이므로 텍스트를 갖고온다.
                    id=edtID.getText().toString().trim();
                    passwd=edtPasswd.getText().toString().trim();
                }else{ // 자동추가일경우
                    id=storeID;
                    passwd=storeID;
                }
                if(commitChk && flag){  // 다 제대로 입력했을 경우 작동
                    new AlertDialog.Builder(manager_super_store_add.this).setTitle("추가").setMessage("추가하시겠습니까 ?")
                            .setPositiveButton("추가", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        String str = new CustomTask().execute(groupName, groupName2, storeID, storeName, id, passwd).get(); //
                                        System.out.println("setOnClick: "+str);
                                        if(str.equals("성공")){
                                            Toast.makeText(manager_super_store_add.this, "처리되었습니다.",Toast.LENGTH_SHORT).show();
                                            finish();
                                            Intent intent=new Intent(manager_super_store_add.this, manager_super_select_main.class);
                                            intent.putExtra("id",getID);
                                            startActivity(intent);
                                        }else if(str.equals("점이름중복")){
                                            Toast.makeText(manager_super_store_add.this, "중복된 이름입니다.",Toast.LENGTH_SHORT).show();
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

                }else{
                    Toast.makeText(manager_super_store_add.this, "요구사항대로 입력해주세요.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://13.125.14.62/today/store_add.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                System.out.println("conn: "+conn);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept-Charset","UTF-8");
                conn.setDoOutput(true); //ENABLE POST REQUEST
                conn.setDoInput(true);
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                //groupName, groupName2, storeID, storeName, id, passwd
                sendMsg = "groupName="+strings[0]+"&groupName2="+strings[1]+"&storeID="+strings[2]+
                        "&storeName="+strings[3]+"&id="+strings[4]+"&passwd="+strings[5];
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

}