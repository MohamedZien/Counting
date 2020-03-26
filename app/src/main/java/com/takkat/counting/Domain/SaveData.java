package com.takkat.counting.Domain;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.takkat.counting.LoginActivity;

public class SaveData {

    private Context context;
    private SharedPreferences sharedPreferences;
    public static final String MyPREFERNCES = "MyPrefs3";
    public static String Token = "0";
    public static String Password = "0";
    public static String Name = "";


    public SaveData(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(MyPREFERNCES, Context.MODE_PRIVATE);
    }

    public void SaveData() {

        try {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Token", String.valueOf(Token));
            editor.putString("Password", String.valueOf(Password));
            editor.putString("Name", String.valueOf(Name));

            editor.commit();

            LoadData();
        } catch (Exception e) {

        }

    }



    public void LoadData() {

        String TempToken = sharedPreferences.getString("Token", "empty");
        String TempPassword = sharedPreferences.getString("Password", "empty");
        String TempUserName = sharedPreferences.getString("Name", "empty");

        if (!TempToken.equals("empty") && !TempPassword.equals("empty") && !TempUserName.equals("empty")) {
            Token = TempToken;
            Password = TempPassword;
            Name = TempUserName;


        }else {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);


        }
    }


    public void LogOutM() {

        try {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Token", null);
            editor.putString("Password", null);
            editor.putString("Name", null);


            editor.commit();

            Intent intent = new Intent(context , LoginActivity.class);
            context.startActivity(intent);
        } catch (Exception e) {

        }
    }

    }

