package com.example.sostoanie;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_USER = "user_json";

    private UserViewModel viewModel;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        loadUserFromPrefs();
    }

    public void saveData(View view) {
        EditText nameEx = findViewById(R.id.nameEx);
        EditText yearEx = findViewById(R.id.yearEx);

        String name = nameEx.getText().toString();
        int age = 0;
        try {
            age = Integer.parseInt(yearEx.getText().toString());
        } catch (NumberFormatException ignored) {
        }

        viewModel.setUser(new User(name, age));
        saveUserToPrefs(viewModel.getUser());
    }

    public void getData(View view) {
        showUserInTextView();
    }

    private void showUserInTextView() {
        TextView dataView = findViewById(R.id.dataView);
        User user = viewModel.getUser();
        dataView.setText("Имя: " + user.getName() + " Возраст: " + user.getAge());
    }

    private void saveUserToPrefs(User user) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", user.getName());
            jsonObject.put("age", user.getAge());

            prefs.edit().putString(KEY_USER, jsonObject.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadUserFromPrefs() {
        String json = prefs.getString(KEY_USER, null);
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                String name = jsonObject.getString("name");
                int age = jsonObject.getInt("age");

                User savedUser = new User(name, age);
                viewModel.setUser(savedUser);

                EditText nameEx = findViewById(R.id.nameEx);
                EditText yearEx = findViewById(R.id.yearEx);
                nameEx.setText(savedUser.getName());
                yearEx.setText(String.valueOf(savedUser.getAge()));

                showUserInTextView();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}