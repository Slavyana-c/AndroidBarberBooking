package com.example.androidbarberbooking.Database;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface CartDataSource {

    Single<Double> sumPrice(String userEmail);

    Flowable<List<CartItem>> getAllItemsFromCart (String userEmail);

    Single<Integer> countItemsInCart (String userEmail);

    Flowable<CartItem> getProductInCart(String productId, String userEmail);

    Completable insert(CartItem...carts);

    Single<Integer> update(CartItem cart);

    Single<Integer> delete(CartItem cartItem);

    Single<Integer> clearCart(String userEmail);
}
