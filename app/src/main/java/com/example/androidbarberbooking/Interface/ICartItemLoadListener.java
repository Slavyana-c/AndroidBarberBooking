package com.example.androidbarberbooking.Interface;

import com.example.androidbarberbooking.Database.CartItem;

import java.util.List;

public interface ICartItemLoadListener {
    void onGetAllItemsFromCartSuccess(List<CartItem> cartItemList);
}
