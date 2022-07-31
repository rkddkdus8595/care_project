package com.example.care_project;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.regex.Pattern;


public class customer_login_Fra extends Fragment {

    public static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String title;
    private int page;
    EditText edtPhone;
    Button btnLogin;
    public customer_login_Fra() {

    }

    public static customer_login_Fra newInstance(int param1, String param2) {
        customer_login_Fra fragment = new customer_login_Fra();
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

        View view = inflater.inflate(R.layout.fragment_customer_login_, container, false);
        btnLogin=(Button)view.findViewById(R.id.btnLogin);
        edtPhone=(EditText) view.findViewById(R.id.edtPhone);

        edtPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher()); // 자동 하이픈 설정
        btnLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                String phoneNum = edtPhone.getText().toString();
                phoneNum = phoneNum.trim();
                if(phoneNum.isEmpty() || phoneNum.equals("")){
                    edtPhone.setError("번호를 입력해주세요 !");
                }else if(Pattern.matches("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", phoneNum)){ // 번호 유효성 확인
                    Toast.makeText(getActivity(), "로그인", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), customer_one_depth.class); // 여기서 아이디 전달해주기
                    intent.putExtra("phoneNum", phoneNum); // 아이디 전달
                    startActivity(intent);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager(); // 종료
                    fragmentManager.beginTransaction().remove(customer_login_Fra.this).commit();
                    fragmentManager.popBackStack();

                }else if(!Pattern.matches("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", phoneNum)){
                    edtPhone.setError("올바른 번호를 입력해주세요");
                }
            }

        });


        return view;
    }
}