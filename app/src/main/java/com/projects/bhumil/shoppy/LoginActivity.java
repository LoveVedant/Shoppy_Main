package com.projects.bhumil.shoppy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.projects.bhumil.shoppy.UtilityClasses.Utility;
import com.projects.bhumil.shoppy.UtilityClasses.Validations;

import java.util.Locale;


public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView tvSignUp, tvLogin;
    EditText tvEmail,tvPassword;
    private  long mBackPressedTime;
    private  int counter = 0;
    private  Intent mIntent;
    public SharedPreferences mSharedPreferences;
    public String tempUserName = "";

    private Spinner Language ;
    String [] Languages = {"English","Gujarati","Hindi"};
    @Override
    protected void onStart()
    {
        super.onStart();

        mSharedPreferences = Utility.getPreference(this);

        if (!(mSharedPreferences.getString(Utility.USEREMAIL, "") == null && mSharedPreferences.getString(Utility.USERPASSWORD, "") == null))
        {
            tvEmail.setText(mSharedPreferences.getString(Utility.USEREMAIL, ""));
            tvPassword.setText(mSharedPreferences.getString(Utility.USERPASSWORD, ""));
            tempUserName= mSharedPreferences.getString(Utility.USERNAME, "");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tvSignUp = (TextView) findViewById(R.id.tv_sign_up);
        tvLogin = (TextView) findViewById(R.id.tv_login);
        tvEmail = (EditText) findViewById(R.id.tv_login_email);
        tvPassword = (EditText) findViewById(R.id.tv_login_pass);
        Language = (Spinner) findViewById(R.id.language);
        Language.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,Languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Language.setAdapter(adapter);
        tvSignUp.setOnClickListener(new View.OnClickListener()
        {
            //Sign Up clicked
            @Override
            public void onClick(View v)
            {
                Intent signupIntent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(signupIntent);
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String Username;
                if(tempUserName.isEmpty())
                {
                    Username = "Demo user";
                }
                else
                {
                    Username = tempUserName;
                }

                String Email = tvEmail.getText().toString();
                String Password = tvPassword.getText().toString();
                if(Email == "Raj" && Password == "Raj")
                {
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString(Utility.USEREMAIL, Email.trim());
                    editor.putString(Utility.USERNAME, "Raj");
                    editor.putString(Utility.USERPASSWORD, Password.trim());
                    editor.commit();
                    Intent productIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(productIntent);
                }
                else
                {
                    Validations userData = new Validations();
                    int validated = userData.ValidateLogin(Email.trim(), Password.trim());
                    if(validated == Validations.VALID_DATA)
                    {
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.putString(Utility.USEREMAIL, Email.trim());
                        editor.putString(Utility.USERNAME, Username);
                        editor.putString(Utility.USERPASSWORD, Password.trim());
                        editor.commit();
                        Intent productIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(productIntent);
                    }
                    else if(validated == Validations.INVALID_EMAIL)
                    {
                        tvEmail.setError("Invalid Email");
                    }
                    else if(validated == Validations.INVALID_PASSWORD)
                    {
                        tvPassword.setError("Atleast 4 characters");
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        mBackPressedTime = System.currentTimeMillis();
        if(counter == 0)
        {
            Toast.makeText(LoginActivity.this, getString(R.string.st_back_pressed), Toast.LENGTH_SHORT).show();
            counter = 1;
        }
        else
        {
            if (mBackPressedTime + 5000 > System.currentTimeMillis())
            {
                mIntent = new Intent(Intent.ACTION_MAIN);
                mIntent.addCategory(Intent.CATEGORY_HOME);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mIntent);
                finish();
                super.onBackPressed();

            } else
            {
                Toast.makeText(LoginActivity.this, getString(R.string.st_back_pressed), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        String selectedlang = parent.getItemAtPosition(i).toString();
        if (selectedlang.equals("English"))
        {
            setlocal(LoginActivity.this,"en");
        }
        else if (selectedlang.equals("Gujarati"))
        {
            setlocal(LoginActivity.this,"gu");
            finish();
            startActivity(getIntent());

        }
        else if (selectedlang.equals("Hindi"))
        {
            setlocal(LoginActivity.this,"hi");
            finish();
            startActivity(getIntent());

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    public void setlocal(Activity activity , String langcode)
    {
        Locale locale = new Locale(langcode);
        locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config= resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config , resources.getDisplayMetrics());

    }
}
