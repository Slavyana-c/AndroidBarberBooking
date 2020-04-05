package com.example.androidbarberbooking;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidbarberbooking.Adapter.MyCartAdapter;
import com.example.androidbarberbooking.Database.CartDatabase;
import com.example.androidbarberbooking.Database.CartItem;
import com.example.androidbarberbooking.Database.DatabaseUtils;
import com.example.androidbarberbooking.Interface.ICartItemLoadListener;
import com.example.androidbarberbooking.Interface.ICartItemUpdateListener;
import com.example.androidbarberbooking.Interface.ISumCartListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartActivity extends AppCompatActivity implements ICartItemLoadListener, ICartItemUpdateListener, ISumCartListener {

    @BindView(R.id.recycler_cart)
    RecyclerView recycler_cart;
    @BindView(R.id.txt_total_price)
    TextView txt_total_price;
    @BindView(R.id.btn_submit_cart)
    Button btn_submit_cart;

    CartDatabase cartDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ButterKnife.bind(CartActivity.this);
        cartDatabase = CartDatabase.getInstance(this);
        DatabaseUtils.getAllCart(cartDatabase,this);

        // View
        recycler_cart.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_cart.setLayoutManager(linearLayoutManager);
        recycler_cart.addItemDecoration(new DividerItemDecoration(this, linearLayoutManager.getOrientation()));
    }

    @Override
    public void onGetAllItemsFromCartSuccess(List<CartItem> cartItemList) {
        // Display items from db in recycler view
        MyCartAdapter adapter = new MyCartAdapter(this, cartItemList, this);
        recycler_cart.setAdapter(adapter);

    }

    @Override
    public void onCartItemUpdateSuccess() {
        DatabaseUtils.sumCart(cartDatabase, this);

    }

    @Override
    public void onSumCartSuccess(double value) {
        txt_total_price.setText(new StringBuilder("$ ").append(value));
    }
}