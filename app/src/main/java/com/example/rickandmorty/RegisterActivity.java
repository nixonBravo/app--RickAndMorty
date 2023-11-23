package com.example.rickandmorty;

import androidx.appcompat.app.AppCompatActivity;

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

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameEditText, emailEditText, passwordEditText;
    private AutenticacionService autenticacionService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText usernameEditText = findViewById(R.id.usernameEditText);
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);

        autenticacionService = RetrofitClientLocal.getInstance().getAutenticacionService();

        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

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
                            Toast.makeText(RegisterActivity.this, "Usuario Registraso", Toast.LENGTH_SHORT).show();
                            Log.d("Register", "User registered: " + user.getUsername());
                        } else {
                            Log.e("Register", "Error: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.e("Register", "Failure: " + t.getMessage());
                    }
                });
            }
   });
}
}