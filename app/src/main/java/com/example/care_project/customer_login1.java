package com.example.care_project;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;


public class customer_login1 extends AppCompatActivity {
    EditText edtPhone;
    Button btnLogin, btnGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login1);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        edtPhone=(EditText) findViewById(R.id.edtPhone);
        //btnGo=(Button)findViewById(R.id.btnGo);

        btnLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                String phoneNum = edtPhone.getText().toString();
                phoneNum = phoneNum.trim();
                if(phoneNum.isEmpty() || phoneNum.equals("")){
                    edtPhone.setError("번호를 입력해주세요 !");
                }else if(Pattern.matches("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", phoneNum)){ // 번호 유효성 확인
                    Toast.makeText(customer_login1.this, "로그인", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(customer_login1.this, customer_one_depth.class); // 여기서 아이디 전달해주기
                    intent.putExtra("phoneNum", phoneNum); // 아이디 전달
                    startActivity(intent);
                    finish();
                }else if(!Pattern.matches("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", phoneNum)){
                    edtPhone.setError("올바른 번호를 입력해주세요 ex) 010-1234-1234");
                }
            }

        });

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(customer_login1.this, manager_login.class);
                startActivity(intent);
                finish();
            }
        });

    }

}