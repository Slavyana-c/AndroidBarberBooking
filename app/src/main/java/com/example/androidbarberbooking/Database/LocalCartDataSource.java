package com.example.androidbarberbooking.Database;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class LocalCartDataSource implements CartDataSource {

    private CartDAO cartDAO;

    public LocalCartDataSource(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }

    @Override
    public Single<Double> sumPrice(String userEmail) {
        return cartDAO.sumPrice(userEmail);
    }

    @Override
    public Flowable<List<CartItem>> getAllItemsFromCart(String userEmail) {
        return cartDAO.getAllItemsFromCart(userEmail);
    }

    @Override
    public Single<Integer> countItemsInCart(String userEmail) {
        return cartDAO.countItemsInCart(userEmail);
    }

    @Override
    public Flowable<CartItem> getProductInCart(String productId, String userEmail) {
        return cartDAO.getProductInCart(productId, userEmail);
    }

    @Override
    public Completable insert(CartItem... carts) {
        return cartDAO.insert(carts);
    }

    @Override
    public Single<Integer> update(CartItem cart) {
        return cartDAO.update(cart);
    }

    @Override
    public Single<Integer> delete(CartItem cartItem) {
        return cartDAO.delete(cartItem);
    }

    @Override
    public Single<Integer> clearCart(String userEmail) {
        return cartDAO.clearCart(userEmail);
    }
}
