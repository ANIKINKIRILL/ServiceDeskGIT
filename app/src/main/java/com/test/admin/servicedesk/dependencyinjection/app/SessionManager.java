package com.test.admin.servicedesk.dependencyinjection.app;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.test.admin.servicedesk.models.User;
import com.test.admin.servicedesk.ui.auth.AuthResource;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * This class keeps track of authenticated user
 * with the status, and current user data
 * The same like in the Firebase
 */

@Singleton
public class SessionManager {

    private MediatorLiveData<AuthResource<User>> cachedUser = new MediatorLiveData<>();

    @Inject
    public SessionManager(){}

    /**
     * Authenticate user in ServiceDesk
     * @param source   AuthApi.authenticateUser(login, password)...
     */

    public void authenticateUser(LiveData<AuthResource<User>> source){
        cachedUser.setValue(AuthResource.loading(null));
        cachedUser.addSource(source, new Observer<AuthResource<User>>() {
            @Override
            public void onChanged(@Nullable AuthResource<User> userAuthResource) {
                cachedUser.setValue(userAuthResource);
                cachedUser.removeSource(source);
            }
        });
    }

    /**
     * Logout current user from ServiceDesk
     */

    public void logout(){
        cachedUser.setValue(AuthResource.logout());
    }

    /**
     * Get current authenticated user
     * @return      current user
     */

    public LiveData<AuthResource<User>> getAuthUser(){
        return cachedUser;
    }

}
