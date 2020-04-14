package com.example.androidbarberbooking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidbarberbooking.Database.CartDataSource;
import com.example.androidbarberbooking.Database.CartDatabase;
import com.example.androidbarberbooking.Database.CartItem;
import com.example.androidbarberbooking.Database.LocalCartDataSource;
import com.example.androidbarberbooking.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyViewHolder> {

    Context context;
    List<CartItem> cartItemList;
    CartDataSource cartDataSource;
    CompositeDisposable compositeDisposable;

    public void onDestroy() {
        compositeDisposable.clear();
    }

    public MyCartAdapter(Context context, List<CartItem> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList;
        this.cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(context).cartDAO());
        this.compositeDisposable = new CompositeDisposable();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(
                R.layout.layout_cart_item, parent, false
        );
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(cartItemList.get(position).getProductImage()).into(holder.img_product);
        holder.txt_cart_name.setText(new StringBuilder(cartItemList.get(position).getProductName()));
        holder.txt_cart_price.setText(new StringBuilder("$ ").append(cartItemList.get(position).getProductPrice()));
        holder.txt_quantity.setText(new StringBuilder(String.valueOf(cartItemList.get(position).getProductQuantity())));

        // Event
        holder.setListener(new IImageButtonListener() {
            @Override
            public void onImageButtonClicked(View view, int pos, boolean isDecreased) {
                if(isDecreased) {
                    if(cartItemList.get(pos).getProductQuantity() > 1) {
                        cartItemList.get(pos)
                                .setProductQuantity(cartItemList
                                        .get(pos)
                                        .getProductQuantity()-1);

                        cartDataSource.update(cartItemList.get(pos))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new SingleObserver<Integer>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onSuccess(Integer integer) {
                                        holder.txt_quantity.setText(new StringBuilder(String.valueOf(cartItemList.get(position).getProductQuantity())));

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }
                    else if(cartItemList.get(pos).getProductQuantity() == 1) {

                        cartDataSource.delete(cartItemList.get(pos))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new SingleObserver<Integer>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onSuccess(Integer integer) {
                                        cartItemList.remove(pos);
                                        notifyItemRemoved(pos);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });

                    }

                }
                else {

                    if(cartItemList.get(pos).getProductQuantity() < 99) {
                        cartItemList.get(pos)
                                .setProductQuantity(cartItemList
                                        .get(pos)
                                        .getProductQuantity()+1);


                        cartDataSource.update(cartItemList.get(pos))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(AndroidSchedulers.mainThread())
                                .subscribe(new SingleObserver<Integer>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onSuccess(Integer integer) {
                                        holder.txt_quantity.setText(new StringBuilder(String.valueOf(cartItemList.get(position).getProductQuantity())));

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });

                    }

                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    interface IImageButtonListener {
        void onImageButtonClicked(View view, int pos, boolean isDecreased);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_cart_name, txt_cart_price, txt_quantity;
        ImageView img_decrease, img_increase, img_product;

        IImageButtonListener listener;

        public void setListener(IImageButtonListener listener) {
            this.listener = listener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_cart_name = (TextView)itemView.findViewById(R.id.txt_cart_name);
            txt_cart_price = (TextView)itemView.findViewById(R.id.txt_cart_price);
            txt_quantity = (TextView)itemView.findViewById(R.id.txt_cart_quantity);

            img_decrease = (ImageView)itemView.findViewById(R.id.img_decrease);
            img_increase = (ImageView)itemView.findViewById(R.id.img_increase);
            img_product = (ImageView)itemView.findViewById(R.id.cart_img);

            // Event
            img_decrease.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.onImageButtonClicked(v, getAdapterPosition(), true);
                }
            });

            img_increase.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.onImageButtonClicked(v, getAdapterPosition(), false);
                }
            });
        }
    }
}
