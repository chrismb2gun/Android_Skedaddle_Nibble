package com.nibble.skedaddle.nibble;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {


    EditText etName,etSurname,etEmail,etPhone,etPassword,etCPassword;
    Button bRegister;
    RequestQueue requestQueue;
    String insertUrl = "http://chrismb2gun.heliohost.org/registerCustomer.php";
    ProgressBar pb_Register;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//Prevent keyboard opening on activity launch

        final TextView tvLogin = (TextView) findViewById(R.id.tvLogin);

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(loginIntent);

            }
        });


        etName =  findViewById(R.id.etName);

        etSurname =  findViewById(R.id.etSurname);

        etEmail =  findViewById(R.id.etEmail);

        etPhone =  findViewById(R.id.etPhone);

        etPassword =  findViewById(R.id.etPassword);

        etCPassword =  findViewById(R.id.etCPassword);

        bRegister =  findViewById(R.id.bRegister);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        pb_Register = findViewById(R.id.pb_Register);


        LoginActivity.buttonEffect(bRegister);


        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb_Register.setVisibility(View.VISIBLE);
                String name = etName.getText().toString();
                String surname = etSurname.getText().toString();
                String email = etEmail.getText().toString();
                String phone = etPhone.getText().toString();
                String password = etPassword.getText().toString();
                String cpassword = etCPassword.getText().toString();
                if(name.matches("")) {
                    messageshow("Please enter name.");
                }else if(surname.matches("")) {
                    messageshow("Please enter surname.");
                }else if(email.matches(""))  {
                    messageshow("Please enter email.");
                }  else if(phone.matches(""))  {
                    messageshow("Please enter phone.");
                }else if(password.matches(""))  {
                    messageshow("Please enter password.");
                }else if(!password.matches(cpassword)){
                    messageshow("Password mismatch");

                    }else{
                    StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    pb_Register.setVisibility(View.INVISIBLE);

                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    RegisterActivity.this.startActivity(intent);
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage("Register Failed")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                    pb_Register.setVisibility(View.INVISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> parameters = new HashMap<String, String>();
                            parameters.put("firstname", etName.getText().toString());
                            parameters.put("lastname", etSurname.getText().toString());
                            parameters.put("email", etEmail.getText().toString());
                            parameters.put("phone", etPhone.getText().toString());
                            parameters.put("password", etPassword.getText().toString());

                            return parameters;

                        }
                    };
                    requestQueue.add(request);
                }




            }//endbregister

            public void messageshow(String message){
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setMessage(message)
                        .setNegativeButton("Retry", null)
                        .create()
                        .show();
                pb_Register.setVisibility(View.INVISIBLE);
            }
        });

    }
}
