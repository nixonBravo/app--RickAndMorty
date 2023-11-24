package com.example.rickandmorty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameEditText, emailEditText, passwordEditText;
    private AutenticacionService autenticacionService;
    private Button loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton  = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);

        autenticacionService = RetrofitClientLocal.getInstance().getAutenticacionService();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Validar los campos de usuario, email y contraseña
                if (isValidCredentials(username, email, password)) {
                    User user = new User();
                    user.setUsername(username);
                    user.setEmail(email);
                    user.setPassword(password);

                    Call<User> call = autenticacionService.registerUser(user);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {
                                User user = response.body();
                                Toast.makeText(RegisterActivity.this, "Usuario Registrado", Toast.LENGTH_SHORT).show();
                                Log.d("Register", "User registered: " + user.getUsername());

                                // Redirigir a la actividad de inicio de sesión después del registro exitoso
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish(); // Cierra la actividad actual para que no pueda volver atrás.
                            } else {
                                Toast.makeText(RegisterActivity.this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
                                Log.e("Register", "Error: " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(RegisterActivity.this, "Error al realizar la solicitud", Toast.LENGTH_SHORT).show();
                            Log.e("Register", "Failure: " + t.getMessage());
                        }
                    });
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean isValidCredentials(String username, String email, String password) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Existen Campos Vacios", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.length() < 8) {
            Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isValidEmail(email)) {
            Toast.makeText(this, "Formato de correo electrónico no válido", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        // Patrón para verificar el formato del correo electrónico
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z]+\\.(com|es|net|org|edu|gov|mil|biz|info)";

        // Verificar si el correo electrónico coincide con el patrón
        return email.matches(emailPattern);
    }
}