package com.android.abhishek.megamovies.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.android.abhishek.megamovies.model.PersonProfile;
import com.android.abhishek.megamovies.network.ApiRepository;

public class ProfileVM extends ViewModel {

    private MutableLiveData<PersonProfile>  personProfile;
    private ApiRepository apiRepository = new ApiRepository();

    public LiveData<PersonProfile> getPersonProfile(String API_KEY,String id) {
        if(personProfile == null){
            personProfile = apiRepository.loadProfile(API_KEY,id);
        }
        return personProfile;
    }
}
