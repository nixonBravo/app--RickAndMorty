package com.example.rickandmorty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.rickandmorty.api.RetrofitClientLocal;
import com.example.rickandmorty.resource.local.AutenticacionService;
import com.example.rickandmorty.resource.local.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private AutenticacionService autenticacionService;
    private EditText usernameEditText, passwordEditText;
    private Button loginButton, registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton  = findViewById(R.id.registerButton);

        autenticacionService = RetrofitClientLocal.getInstance().getAutenticacionService();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                User user = new User();
                user.setUsername(username);
                user.setPassword(password);

                Call<User> call = autenticacionService.loginUser(user);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            User user = response.body();
                            if (user.getAccessToken() != null) {
                                Log.d("Login", "User logged in: " + user.getUsername());
                                Intent intent = new Intent(LoginActivity.this, EpisodesActivity.class);
                                intent.putExtra("user", user);
                                startActivity(intent);
                                SharedPreferences preferences = getSharedPreferences("MyApp", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("token", user.getAccessToken());
                                editor.apply();
                            } else {
                                Log.e("Login", "Invalid token");
                            }
                        } else {
                            Log.e("Login", "Error: " + response.code());
                        }
                    }
                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        // Handle the failure
                        Log.e("Login", "Failure: " + t.getMessage());
                    }
                });
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
   });
}
}