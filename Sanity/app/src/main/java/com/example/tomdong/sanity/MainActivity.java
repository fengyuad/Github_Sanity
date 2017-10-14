package com.example.tomdong.sanity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import Model.*;

public class MainActivity extends AppCompatActivity implements Animation.AnimationListener,View.OnClickListener {

    private ImageView SanityImage;
    private EditText Account;
    private  EditText PassWord;
    private Button Login;
    private Button Register;
    private TextView ForgetPassword;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Declare and Initialization
        firebaseAuth=FirebaseAuth.getInstance();
        SanityImage=(ImageView) findViewById(R.id.SanityImage);
        Account=(EditText) findViewById(R.id.UserT);
        PassWord=(EditText) findViewById(R.id.PwT);
        Login=(Button) findViewById(R.id.LoginButton);
        Register=(Button) findViewById(R.id.RegisterButton);
        ForgetPassword=(TextView) findViewById(R.id.ForgetTextView);

        //Login Animation
        Account.setVisibility(View.GONE);
        PassWord.setVisibility(View.GONE);
        Login.setVisibility(View.GONE);
        Register.setVisibility(View.GONE);
        ForgetPassword.setVisibility(View.GONE);
        Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.sanityicon_move);
        animation.setFillAfter(true);
        animation.setAnimationListener(this);
        SanityImage.startAnimation(animation);

        //AddListener
        Login.setOnClickListener(this);
        Register.setOnClickListener(this);

        // Test database
        CategoryModel test = new CategoryModel();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        //Animate other GUI
        Account.setAlpha(0f);
        Account.setVisibility(View.VISIBLE);
        PassWord.setAlpha(0f);
        PassWord.setVisibility(View.VISIBLE);
        Login.setAlpha(0f);
        Login.setVisibility(View.VISIBLE);
        Register.setAlpha(0f);
        Register.setVisibility(View.VISIBLE);
        ForgetPassword.setAlpha(0f);
        ForgetPassword.setVisibility(View.VISIBLE);
        //Finish animating and show
        int AnimationTime=getResources().getInteger(android.R.integer.config_mediumAnimTime);
        Account.animate().alpha(1f).setDuration(AnimationTime).setListener(null);
        PassWord.animate().alpha(1f).setDuration(AnimationTime).setListener(null);

        Login.animate().alpha(0.8f).setDuration(AnimationTime).setListener(null);
        Register.animate().alpha(0.8f).setDuration(AnimationTime).setListener(null);
        ForgetPassword.animate().alpha(1f).setDuration(AnimationTime).setListener(null);

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onClick(View view) {

        Log.d("MyApp","I am here");
        Toast.makeText(this,"clicked",Toast.LENGTH_SHORT).show();

        if(view==Register)
        {
            Log.d("MyApp","Register");
            RegisterUser();
        }
        if(view==Login)
        {
            Log.d("MyApp","Login");
            LoginUser();
        }

    }

    public void RegisterUser()
    {
        String email=Account.getText().toString();
        String pw=PassWord.getText().toString();
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(MainActivity.this,"Email Empty!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(MainActivity.this,"Password Empty!",Toast.LENGTH_SHORT).show();
            return;
        }
        firebaseAuth.createUserWithEmailAndPassword(email,pw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(MainActivity.this,"Register Succeeded",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Fail",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void LoginUser()
    {
        String email=Account.getText().toString();
        String pw=PassWord.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(MainActivity.this,"Email Empty!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(MainActivity.this,"Password Empty!",Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email,pw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(MainActivity.this,"Login Succeeded",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Username does not match",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
