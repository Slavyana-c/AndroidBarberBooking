package com.example.androidbarberbooking.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidbarberbooking.Adapter.HomeSliderAdapter;
import com.example.androidbarberbooking.Adapter.LookbookAdapter;
import com.example.androidbarberbooking.Common.Common;
import com.example.androidbarberbooking.Interface.IBannerLoadListener;
import com.example.androidbarberbooking.Interface.ILookbookLoadListener;
import com.example.androidbarberbooking.Model.Banner;
import com.example.androidbarberbooking.R;
import com.example.androidbarberbooking.Service.PicassoImageLoadingService;
import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ss.com.bannerslider.Slider;

import static com.example.androidbarberbooking.Common.Common.IS_LOGIN;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements ILookbookLoadListener, IBannerLoadListener {


    private Unbinder unbinder;

    @BindView(R.id.layout_user_information )
    LinearLayout layout_user_information;

    @BindView(R.id.txt_user_name)
    TextView  txt_user_name;

    @BindView(R.id.banner_slider)
    Slider banner_slider;

    @BindView(R.id.recycler_look_book)
    RecyclerView recycler_look_book;

    // FireStore
    CollectionReference bannerRef, lookbookRef;

    // Interface
    IBannerLoadListener iBannerLoadListener;
    ILookbookLoadListener iLookbookLoadListener;


    public HomeFragment() {
        bannerRef = FirebaseFirestore.getInstance().collection("Banner");
        lookbookRef = FirebaseFirestore.getInstance().collection("Lookbook");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        // Init
        Slider.init(new PicassoImageLoadingService());
        iBannerLoadListener = this;
        iLookbookLoadListener = this;

        // Check if logged
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        boolean isLogin = Boolean.parseBoolean(IS_LOGIN);

        if(isLoggedIn || isLogin) {
            setUserInformation();
            loadBanner();
            loadLookBook();
        }

        return view;
    }

    private void loadLookBook() {
        lookbookRef .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Banner> lookbooks = new ArrayList<>();
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot bannerSnapshot:task.getResult()) {
                        Banner banner = bannerSnapshot.toObject(Banner.class);
                        lookbooks.add(banner);
                    }
                    iLookbookLoadListener.onLookbookLoadSuccess(lookbooks);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iLookbookLoadListener.onLookbookLoadFailed(e.getMessage());
            }
        });
    }

    private void loadBanner() {
        bannerRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Banner> banners = new ArrayList<>();
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot bannerSnapshot:task.getResult()) {
                        Banner banner = bannerSnapshot.toObject(Banner.class);
                        banners.add(banner);
                    }
                    iBannerLoadListener.onBannerLoadSuccess(banners);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                 iBannerLoadListener.onBannerLoadFailed(e.getMessage());
            }
        });
    }

    private void setUserInformation() {
        layout_user_information.setVisibility(View.VISIBLE);
        txt_user_name.setText(Common.currentUser.getName());
    }

    @Override
    public void onLookbookLoadSuccess(List<Banner> banners) {
        recycler_look_book.setHasFixedSize(true);
        recycler_look_book.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_look_book.setAdapter(new LookbookAdapter(getActivity(), banners));
    }

    @Override
    public void onLookbookLoadFailed(String message) {
        Toast.makeText(getActivity(), message , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBannerLoadSuccess(List<Banner> banners) {
        banner_slider.setAdapter(new HomeSliderAdapter(banners));

    }

    @Override
    public void onBannerLoadFailed(String message) {
        Toast.makeText(getActivity(), message , Toast.LENGTH_SHORT).show();

    }
}
