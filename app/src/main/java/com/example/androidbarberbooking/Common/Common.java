package com.example.androidbarberbooking.Common;

import android.content.Intent;

import com.example.androidbarberbooking.Model.Barber;
import com.example.androidbarberbooking.Model.Salon;
import com.example.androidbarberbooking.Model.User;

public class Common {
    public static final String KEY_ENABLE_BUTTON_NEXT = "ENABLE_BUTTON_NEXT";
    public static final String KEY_SALON_STORE = "SALON_SAVE";
    public static final String KEY_BARBER_LOAD_DONE = "BARBER_LOAD_DONE";
    public static final String KEY_DISPLAY_TIME_SLOT = "DISPLAY_TIME_SLOT";
    public static final String KEY_STEP = "STEP";
    public static final String KEY_BARBER_SELECTED = "BARBER_SELECTED";
    public static String IS_LOGIN = "IsLogin";
    public static User currentUser;
    public static Salon currentSalon;
    public static Barber currentBarber;
    public static int step = 0;
    public static String city = "";
}
