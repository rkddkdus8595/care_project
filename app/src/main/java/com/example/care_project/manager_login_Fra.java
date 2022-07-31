package com.example.care_project;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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


public class manager_login_Fra extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    EditText edtID;
    EditText edtPasswd;
    Button btnLogin;
    String arr[];
    // TODO: Rename and change types of parameters
    private String title;
    private int page;

    public manager_login_Fra() {
        // Required empty public constructor
    }

    public static manager_login_Fra newInstance(int param1, String param2) {
        manager_login_Fra fragment = new manager_login_Fra();
        Bundle args = new Bundle();
        args.putInt("someInt", param1);
        args.putString("someTitle", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            page = getArguments().getInt("someInt", 0);
            title = getArguments().getString("someTitle");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manager_login_, container, false);
        edtID=(EditText)view.findViewById(R.id.edtID);
        edtPasswd=(EditText)view.findViewById(R.id.edtPasswd);
        btnLogin=(Button)view.findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                try {
                    String id=edtID.getText().toString();
                    id=id.trim();
                    String passwd=edtPasswd.getText().toString();
                    passwd=passwd.trim();
                    String result  = new CustomTask().execute(id, passwd, "login").get();
                    System.out.println("result: "+result);
                    if(result.contains(";")){
                        arr=result.split(";");
                        System.out.println("포힘됭");
                        if(arr[1].equals("N")) {
                            System.out.println("로그인 true");
                            Toast.makeText(getActivity(),"로그인",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), manager_select_main.class); // 여기서 아이디 전달해주기
                            intent.putExtra("id",id); // 아이디 전달
                            intent.putExtra("storeName", arr[0]);
                            startActivity(intent);

                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager(); // 종료
                            fragmentManager.beginTransaction().remove(manager_login_Fra.this).commit();
                            fragmentManager.popBackStack();
                        }
                    }

                    else if(result.equals("S")) { // 슈퍼관리자는 값이 하나니까,,,
                        System.out.println("로그인 true");
                        Toast.makeText(getActivity(),"로그인",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), manager_super_select_main.class); // 슈퍼관리자 !!!!
                        intent.putExtra("id",id); // 아이디 전달
                        startActivity(intent);

                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager(); // 종료
                        fragmentManager.beginTransaction().remove(manager_login_Fra.this).commit();
                        fragmentManager.popBackStack();
                    }else if(result.equals("false")) {
                        System.out.println("로그인 false");
                        Toast.makeText(getActivity(),"아이디 또는 비밀번호가 틀렸음",Toast.LENGTH_SHORT).show();
                        edtID.setText("");
                        edtPasswd.setText("");
                    } else if(result.equals("noId")) {
                        System.out.println("로그인 noId");
                        Toast.makeText(getActivity(),"존재하지 않는 아이디",Toast.LENGTH_SHORT).show();
                        edtID.setText("");
                        edtPasswd.setText("");
                    }
                }catch (Exception e) {

                }
            }
        });
        return view;
    }

    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override

        protected String doInBackground(String... strings){
            try{

                String str;
                java.net.URL url=new URL("http://13.125.14.62/today/data.jsp");
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                System.out.println("conn: "+conn);
                conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoOutput(true); //ENABLE POST REQUEST
                conn.setDoInput(true);
                OutputStreamWriter osw1=new OutputStreamWriter(conn.getOutputStream());
                sendMsg="id="+strings[0]+"&passwd="+strings[1]+"&kind="+strings[2];

                osw1.write(sendMsg);
                System.out.println("sendMsg"+sendMsg); // 출력 확인
                osw1.flush(); // 스트림 값을 목적지 파일로 보낸다.
                if(conn.getResponseCode()==conn.HTTP_OK){
                    InputStreamReader tmp=new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader=new BufferedReader(tmp);
                    StringBuffer buffer=new StringBuffer();
                    while((str=reader.readLine())!=null){
                        buffer.append(str);
                    }

                    receiveMsg=buffer.toString();
                    conn.disconnect();
                }else{
                    Log.i("통신결과",conn.getResponseCode()+"에러");
                }
            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }

            return receiveMsg;

        }
    }

}