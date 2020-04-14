package com.example.androidbarberbooking;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidbarberbooking.Adapter.MyCartAdapter;
import com.example.androidbarberbooking.Common.Common;
import com.example.androidbarberbooking.Database.CartDataSource;
import com.example.androidbarberbooking.Database.CartDatabase;
import com.example.androidbarberbooking.Database.LocalCartDataSource;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class  CartActivity extends AppCompatActivity  {

    MyCartAdapter adapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    CartDataSource cartDataSource;

    @BindView(R.id.recycler_cart)
    RecyclerView recycler_cart;
    @BindView(R.id.txt_total_price)
    TextView txt_total_price;
    @BindView(R.id.btn_clear_cart)
    Button btn_clear_cart;

    @OnClick(R.id.btn_clear_cart)
    void clearCart() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Clear Cart")
                .setMessage("Do you really want to clear your cart?")
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("CLEAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cartDataSource.clearCart(Common.currentUser.getEmail())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new SingleObserver<Integer>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onSuccess(Integer integer) {
                                        Toast.makeText(CartActivity.this, "Cart has been cleared!", Toast.LENGTH_SHORT).show();
                                        // Load cart again after we clear
                                        compositeDisposable.add(
                                                cartDataSource.getAllItemsFromCart(Common.currentUser.getEmail())
                                                        .subscribeOn(Schedulers.io())
                                                        .observeOn(AndroidSchedulers.mainThread())
                                                        .subscribe(cartItemList -> {

                                                            cartDataSource.sumPrice(Common.currentUser.getEmail())
                                                                    .subscribeOn(Schedulers.io())
                                                                    .observeOn(AndroidSchedulers.mainThread())
                                                                    .subscribe(updatePrice());

                                                        }, throwable -> {
                                                            Toast.makeText(CartActivity.this, ""+throwable.getMessage()  , Toast.LENGTH_SHORT).show();

                                                        })

                                        );
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Toast.makeText(CartActivity.this, ""+e.getMessage()  , Toast.LENGTH_SHORT).show();
                                    }
                                });

                        // Update adapter
                        getAllCart();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private SingleObserver<? super Double> updatePrice() {
        return new SingleObserver<Double>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Double aDouble) {
                txt_total_price.setText(new StringBuilder("$ ").append(aDouble));
            }

            @Override
            public void onError(Throwable e) {


                if(e.getMessage().contains("Query returned empty")) {
                    txt_total_price.setText("$ 0");
                }
                else {
                    Toast.makeText(CartActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                finish();

            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ButterKnife.bind(CartActivity.this);
        cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(this).cartDAO());


        getAllCart();

        // View
        recycler_cart.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_cart.setLayoutManager(linearLayoutManager);
        recycler_cart.addItemDecoration(new DividerItemDecoration(this, linearLayoutManager.getOrientation()));
    }

    private void getAllCart() {
        compositeDisposable.add(cartDataSource.getAllItemsFromCart(Common.currentUser.getEmail())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cartItemList -> {

                    // Display items from db in recycler view
                    adapter = new MyCartAdapter(this, cartItemList);
                    recycler_cart.setAdapter(adapter);

                    // Update price
                    cartDataSource.sumPrice(Common.currentUser.getEmail())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(updatePrice());

                }, throwable -> {
                    Toast.makeText(this, ""+throwable.getMessage()  , Toast.LENGTH_SHORT).show();

                }));

    }

    @Override
    protected void onDestroy() {
        if(adapter != null) {
            adapter.onDestroy();
        }
        compositeDisposable.clear();
        super.onDestroy();
    }
}