package com.android.abhishek.megamovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.abhishek.megamovies.model.PersonProfile;
import com.android.abhishek.megamovies.viewModel.ProfileVM;
import com.squareup.picasso.Picasso;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CastProfileAct extends AppCompatActivity {

    @BindView(R.id.profileName) TextView name;
    @BindView(R.id.profileYear) TextView year;
    @BindView(R.id.profileLocation) TextView location;
    @BindView(R.id.profileBio) TextView bio;
    @BindView(R.id.profilePhoto) ImageView photo;

    @BindString(R.string.apiKey) String API_KEY;
    @BindString(R.string.imageBaseUrl) String IMAGE_BASE_URL;
    @BindString(R.string.infoUnavailable) String DATA_NOT_AVAILABLE;

    private String id;
    private PersonProfile personProfile;
    private Toast toast;

    public static final String EXTRA_CURVE = "EXTRA_CURVE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast_profile);

        ButterKnife.bind(this);
        networkStatus();

        Intent intent = getIntent();
        if(intent == null){
            closeOnError(getResources().getString(R.string.somethingWrong));
        }

        id = intent.getStringExtra(getResources().getString(R.string.intentPassingOne));
        if(id == null) {
            closeOnError(getResources().getString(R.string.somethingWrong));
        }

        boolean curve = getIntent().getBooleanExtra(EXTRA_CURVE, false);
        if(Build.VERSION.SDK_INT>=21){
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(curve ? R.transition.curve : R.transition.move));
        }

        Picasso.get()
                .load(intent.getData())
                .placeholder(R.drawable.loading_place_holder)
                .error(R.drawable.error_place_holder)
                .into(photo);

        loadProfile();
    }

    private void loadProfile(){
        final ProfileVM profileVm = ViewModelProviders.of(this).get(ProfileVM.class);
        profileVm.getPersonProfile(API_KEY,id).observe(this, new Observer<PersonProfile>() {
            @Override
            public void onChanged(@Nullable PersonProfile personProf) {
                profileVm.getPersonProfile(API_KEY,id).removeObserver(this);
                personProfile  = personProf;
                setProfile();
            }
        });
    }

    private void setProfile(){
        String nameStr = personProfile.getName()==null?"":personProfile.getName();
        String yearStr = personProfile.getBirthday()==null?"":personProfile.getBirthday();
        String locationStr = personProfile.getBirthPlace()==null?DATA_NOT_AVAILABLE:personProfile.getBirthPlace();
        String bioStr;
        try{
            bioStr = personProfile.getBiography().isEmpty()?DATA_NOT_AVAILABLE:personProfile.getBiography();
        }catch (Exception e){
            bioStr = DATA_NOT_AVAILABLE;
        }

        name.setText(nameStr);
        year.setText(changeFormatOfDate(yearStr));
        location.setText(locationStr);
        bio.setText(bioStr);
    }

    private String changeFormatOfDate(String releaseDate){
        try{
            String day = releaseDate.substring(8,10);
            int month;
            try{
                month = Integer.parseInt(releaseDate.substring(5,7));
            }catch (ClassCastException e){
                month = 0;
            }
            String year = releaseDate.substring(0,4);
            String changedFormatDate = day;
            switch (month){
                case 1:
                    changedFormatDate += getResources().getString(R.string.jan);
                    break;
                case 2:
                    changedFormatDate += getResources().getString(R.string.Feb);
                    break;
                case 3:
                    changedFormatDate += getResources().getString(R.string.Mar);
                    break;
                case 4:
                    changedFormatDate += getResources().getString(R.string.April);
                    break;
                case 5:
                    changedFormatDate += getResources().getString(R.string.May);
                    break;
                case 6:
                    changedFormatDate += getResources().getString(R.string.June);
                    break;
                case 7:
                    changedFormatDate += getResources().getString(R.string.july);
                    break;
                case 8:
                    changedFormatDate += getResources().getString(R.string.Aug);
                    break;
                case 9:
                    changedFormatDate += getResources().getString(R.string.Sept);
                    break;
                case 10:
                    changedFormatDate += getResources().getString(R.string.Oct);
                    break;
                case 11:
                    changedFormatDate += getResources().getString(R.string.Nov);
                    break;
                case 12:
                    changedFormatDate += getResources().getString(R.string.Dec);
                    break;
            }

            changedFormatDate += " "+year;
            return changedFormatDate;
        }catch (Exception e){
            return releaseDate;
        }
    }

    private void networkStatus(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(!(connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isAvailable() && connectivityManager.getActiveNetworkInfo().isConnected())){
            closeOnError(getResources().getString(R.string.netProblem));
        }
    }

    private void closeOnError(String message){
        if(toast != null){
            toast.cancel();
        }
        toast = Toast.makeText(CastProfileAct.this,message,Toast.LENGTH_SHORT);
        toast.show();
        finish();
    }
}
