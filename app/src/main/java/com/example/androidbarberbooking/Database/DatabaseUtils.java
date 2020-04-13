package com.example.androidbarberbooking.Database;

import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.androidbarberbooking.Common.Common;
import com.example.androidbarberbooking.Interface.ICartItemLoadListener;
import com.example.androidbarberbooking.Interface.ICountItemsInCartListener;
import com.example.androidbarberbooking.Interface.ISumCartListener;

import java.util.List;

public class DatabaseUtils {

    // Handle on another thread

    public static void clearCart(CartDatabase db) {
        ClearCartAsync task = new ClearCartAsync(db);
        task.execute();

    }

    public static void sumCart(CartDatabase db, ISumCartListener iSumCartListener) {
        SumCartAsync task = new SumCartAsync(db, iSumCartListener);
        task.execute();

    }

    public static void getAllCart(CartDatabase db, ICartItemLoadListener cartItemLoadListener) {
        GetAllCartAsync task = new GetAllCartAsync(db, cartItemLoadListener);
        task.execute();
    }

    public static void updateCart(CartDatabase db, CartItem cartItem) {
        UpdateCartAsync task = new UpdateCartAsync(db);
        task.execute(cartItem);
    }

    public static void insertToCart(CartDatabase db, CartItem... cartItems) {
        InsertToCartAsync task = new InsertToCartAsync(db);
        task.execute(cartItems);
    }

    public static void countItemsInCart(CartDatabase db, ICountItemsInCartListener iCountItemsInCartListener) {
        CountItemsInCartAsync task = new CountItemsInCartAsync(db, iCountItemsInCartListener);
        task.execute();
    }

    public static void deleteCart(@NonNull final CartDatabase db, CartItem cartItem) {
        DeleteCartAsync task = new DeleteCartAsync(db);
        task.execute(cartItem);
    }


    /*
    * Define Async tasks
    */

    private static class SumCartAsync extends AsyncTask<Void,Void,Double> {
        private final CartDatabase db;
        private final ISumCartListener listener;

        public SumCartAsync(CartDatabase db, ISumCartListener listener) {
            this.db = db;
            this.listener = listener;
        }

        @Override
        protected Double doInBackground(Void... voids) {
            double a = db.cartDAO().sumPrice(Common.currentUser.getEmail());
            return Math.round(a * 100.0) / 100.0;
        }

        @Override
        protected void onPostExecute(Double aDouble) {
            super.onPostExecute(aDouble);
            listener.onSumCartSuccess(aDouble);
        }
    }

    private static class UpdateCartAsync extends AsyncTask<CartItem,Void,Void> {

        private final CartDatabase db;

        public UpdateCartAsync(CartDatabase db) {
            this.db = db;
        }

        @Override
        protected Void doInBackground(CartItem... cartItems) {
            db.cartDAO().update(cartItems[0]);
            return null;
        }
    }

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

        @Override
        protected void onPostExecute(List<CartItem> cartItemList) {
            super.onPostExecute(cartItemList);
            listener.onGetAllItemsFromCartSuccess(cartItemList);
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

    private static class DeleteCartAsync extends AsyncTask<CartItem,Void,Void> {

        private final CartDatabase db;

        public DeleteCartAsync(CartDatabase db) {
            this.db = db;
        }

        @Override
        protected Void doInBackground(CartItem... cartItems) {
            db.cartDAO().delete(cartItems[0]);
            return null;
        }
    }


    private static class ClearCartAsync extends AsyncTask<Void,Void,Void> {

        private final CartDatabase db;

        public ClearCartAsync(CartDatabase db) {
            this.db = db;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            clearAllItemsFromCart(db);
            return null;
        }

        private void clearAllItemsFromCart(CartDatabase db) {
            db.cartDAO().clearCart(Common.currentUser.getEmail());
        }
    }
}
