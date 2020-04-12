package com.example.androidbarberbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.androidbarberbooking.Common.Common;
import com.example.androidbarberbooking.Fragments.HomeFragment;
import com.example.androidbarberbooking.Fragments.ShoppingFragment;
import com.example.androidbarberbooking.Model.User;
import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Collection;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    BottomSheetDialog bottomSheetDialog;

    CollectionReference userRef;

    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(HomeActivity.this);

        // Init
        userRef = FirebaseFirestore.getInstance().collection("User");
        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();

        // View
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Fragment fragment = null;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.action_home) {
                    fragment = new HomeFragment();
                }
                else if(menuItem.getItemId() == R.id.action_shopping) {
                    fragment = new ShoppingFragment();
                }

                return loadFragment(fragment);
            }
        });


        // Check intent
        // login = true, enable full access
        // login = false, shopping
        if(getIntent() != null)
        {
            boolean isLogin = getIntent().getBooleanExtra(Common.IS_LOGIN, false);
            if(isLogin)
            {
                dialog.show();

                // Check if user exists
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

                // if not registered
                // TODO implement fb login / profile
                if(!isLoggedIn) {
                    showUpdateDialog("email@email.com");
                }
                else {
                     // if registered
                    // TODO change to registered
                    Common.currentUser = new User("Registered Name", "LE1", "mail@me.com");
                    bottomNavigationView.setSelectedItemId(R.id.action_home);
                    FirebaseInstanceId.getInstance()
                            .getInstanceId()
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if(task.isSuccessful()) {
                                Common.updateToken(getBaseContext(), task.getResult().getToken());
                                Log.d("TOKEN", task.getResult().getToken());
                                Paper.init(HomeActivity.this);
                                Paper.book().write(Common.LOGGED_KEY, Common.currentUser.getEmail());
                            }
                        }
                    });
                }



                if(dialog.isShowing())
                    dialog.dismiss();




            }
            else {
                bottomNavigationView.setSelectedItemId(R.id.action_shopping);
            }
        }


    }

    private boolean loadFragment(Fragment fragment) {
        if(fragment != null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

            return true;
        }
        return false;
    }

    private void showUpdateDialog(String email) {

        // Init dialog
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setTitle("One more step!");
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setCancelable(false);
        View sheetView = getLayoutInflater().inflate(R.layout.layout_update_information, null);

        Button btn_update = (Button)sheetView.findViewById(R.id.btn_update);
        TextInputEditText edit_name = (TextInputEditText) sheetView.findViewById(R.id.edit_name);
        TextInputEditText edit_address = (TextInputEditText) sheetView.findViewById(R.id.edit_address);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!dialog.isShowing())
                    dialog.show();

                    User user = new User(edit_name.getText().toString(),
                        edit_address.getText().toString(), email);

                userRef.document(email).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        bottomSheetDialog.dismiss();
                        if(dialog.isShowing())
                            dialog.dismiss();

                        Common.currentUser = user;
                        Common.IS_LOGIN = "true";
                        bottomNavigationView.setSelectedItemId(R.id.action_home);

                        Toast.makeText(HomeActivity.this, "Thank you", Toast.LENGTH_SHORT).show();

                        FirebaseInstanceId.getInstance()
                                .getInstanceId()
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if(task.isSuccessful()) {
                                    Common.updateToken(getBaseContext(),task.getResult().getToken());
                                    Log.d("TOKEN", task.getResult().getToken());
                                    Paper.init(HomeActivity.this);
                                    Paper.book().write(Common.LOGGED_KEY, Common.currentUser.getEmail());
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        bottomSheetDialog.dismiss();
                        if(dialog.isShowing())
                            dialog.dismiss();
                        Toast.makeText(HomeActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();

    }
}
