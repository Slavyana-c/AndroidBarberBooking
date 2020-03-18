package com.example.androidbarberbooking.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.androidbarberbooking.Common.Common;
import com.example.androidbarberbooking.R;
import com.example.androidbarberbooking.Service.PicassoImageLoadingService;
import com.facebook.AccessToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ss.com.bannerslider.Slider;

import static com.example.androidbarberbooking.Common.Common.IS_LOGIN;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    private Unbinder unbinder;

    @BindView(R.id.layout_user_information )
    LinearLayout layout_user_information;

    @BindView(R.id.txt_user_name)
    TextView  txt_user_name;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        // Init
        Slider.init(new PicassoImageLoadingService());

        // Check if logged
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        boolean isLogin = Boolean.parseBoolean(IS_LOGIN);

        if(isLoggedIn || isLogin) {
            setUserInformation();
        }



        return view;
    }

    private void setUserInformation() {
        layout_user_information.setVisibility(View.VISIBLE);
        txt_user_name.setText(Common.currentUser.getName());
    }

}
