package com.example.androidbarberbooking.Database;

import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.util.Log;

import com.example.androidbarberbooking.Common.Common;
import com.example.androidbarberbooking.Interface.ICartItemLoadListener;
import com.example.androidbarberbooking.Interface.ICountItemsInCartListener;

import java.util.List;

public class DatabaseUtils {

    // Handle on another thread

    public static void getAllCart(CartDatabase db, ICartItemLoadListener cartItemLoadListener) {
        GetAllCartAsync task = new GetAllCartAsync(db, cartItemLoadListener);
        task.execute();

    }

    public static void insertToCart(CartDatabase db, CartItem... cartItems) {
        InsertToCartAsync task = new InsertToCartAsync(db);
        task.execute(cartItems);
    }

    public static void countItemsInCart(CartDatabase db, ICountItemsInCartListener iCountItemsInCartListener) {
        CountItemsInCartAsync task = new CountItemsInCartAsync(db, iCountItemsInCartListener);
        task.execute();
    }


    /*
    * Define Async tasks
    */

    private static class GetAllCartAsync extends AsyncTask<String,Void,List<CartItem>> {

        CartDatabase db;
        ICartItemLoadListener listener;

        public GetAllCartAsync(CartDatabase cartDatabase, ICartItemLoadListener iCartItemLoadListener) {
            db = cartDatabase;
            listener = iCartItemLoadListener;
        }
        @Override
        protected List<CartItem> doInBackground(String... strings) {
            return db.cartDAO().getAllItemsFromCart(Common.currentUser.getEmail());
        }
    }

    private static class InsertToCartAsync extends AsyncTask<CartItem,Void,Void> {

        CartDatabase db;
        public InsertToCartAsync(CartDatabase cartDatabase) {
            db = cartDatabase;
        }

        @Override
        protected Void doInBackground(CartItem... cartItems) {
            insertToCart(db, cartItems[0]);
            return null;
        }

        private void insertToCart(CartDatabase db, CartItem cartItem) {
            // If item is already in the cart, just increase quantity
            try {
                db.cartDAO().insert(cartItem);
            } catch (SQLiteConstraintException exception) {
                CartItem updateCartItem = db.cartDAO().getProductInCart(cartItem.getProductId(),
                        Common.currentUser.getEmail());
                updateCartItem.setProductQuantity(updateCartItem.getProductQuantity() + 1);
                db.cartDAO().update(updateCartItem);
            }
        }


    }

    private static class CountItemsInCartAsync extends AsyncTask<Void,Void,Integer> {

        CartDatabase db;
        ICountItemsInCartListener listener;

        public CountItemsInCartAsync(CartDatabase cartDatabase, ICountItemsInCartListener iCountItemsInCartListener) {
            db = cartDatabase;
            listener = iCountItemsInCartListener;
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            countItemsInCartRun(db);
            return Integer.parseInt(String.valueOf(countItemsInCartRun(db)));
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            listener.onCartItemsCountSuccess(integer.intValue());
        }

        private int countItemsInCartRun(CartDatabase db) {
            return db.cartDAO().countItemsInCart(Common.currentUser.getEmail());

        }
    }
}
