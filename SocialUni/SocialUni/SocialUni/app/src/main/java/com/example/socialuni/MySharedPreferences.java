package com.example.socialuni;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
    private Context context;

    public MySharedPreferences(Context context) {
        this.context = context;
    }

    public void setMyData(String number){
        SharedPreferences.Editor editor = context.getSharedPreferences("mydata",Context.MODE_PRIVATE).edit();
        editor.putString("number", number);
        editor.apply();
    }
    public String getMyNumber(String number){
        SharedPreferences editor = context.getSharedPreferences("mydata",Context.MODE_PRIVATE);
        return editor.getString("number",null);
    }

}
