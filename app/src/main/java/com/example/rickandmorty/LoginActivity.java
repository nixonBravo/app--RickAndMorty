package com.example.rickandmorty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        setTheme(R.style.Theme_RickAndMorty);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        autenticacionService = RetrofitClientLocal.getInstance().getAutenticacionService();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Validar los campos de usuario y contraseña
                if (isValidCredentials(username, password)) {
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
                                    showToast("Token inválido");
                                }
                            } else {
                                showToast("Error: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            showToast("Failure: " + t.getMessage());
                        }
                    });
                }
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

    private boolean isValidCredentials(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            showToast("Los campos de usuario y contraseña son obligatorios");
            return false;
        } else if (username.length() < 4 || username.length() > 20) {
            showToast("El nombre de usuario debe tener entre 4 y 20 caracteres");
            return false;
        } else if (password.length() < 6) {
            showToast("La contraseña debe tener al menos 6 caracteres");
            return false;
        }
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}