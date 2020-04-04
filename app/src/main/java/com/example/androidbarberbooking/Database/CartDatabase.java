package com.example.androidbarberbooking.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(version = 1, entities = CartItem.class, exportSchema = false)
public abstract class CartDatabase extends RoomDatabase {
}
