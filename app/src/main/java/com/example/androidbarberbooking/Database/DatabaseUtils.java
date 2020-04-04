package com.example.androidbarberbooking.Database;

import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.util.Log;

import com.example.androidbarberbooking.Common.Common;
import com.example.androidbarberbooking.Interface.ICountItemsInCartListener;

import java.util.List;

public class DatabaseUtils {

    // Handle on another thread
    public static void getAllItemsFromCart(CartDatabase db) {
        GetAllCartAsync task = new GetAllCartAsync(db);
        task.execute(Common.currentUser.getEmail());
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

    private static class GetAllCartAsync extends AsyncTask<String,Void,Void> {

        CartDatabase db;
        public GetAllCartAsync(CartDatabase cartDatabase) {
            db = cartDatabase;
        }

        @Override
        protected Void doInBackground(String... strings) {
            getAllItemsFromCartByUserEmail(db, strings[0]);
            return null;
        }

        private void getAllItemsFromCartByUserEmail(CartDatabase db, String userEmail) {
            List<CartItem> cartItems = db.cartDAO().getAllItemsFromCart(userEmail);
            Log.d("COUNT_CART", ""+cartItems.size());

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
