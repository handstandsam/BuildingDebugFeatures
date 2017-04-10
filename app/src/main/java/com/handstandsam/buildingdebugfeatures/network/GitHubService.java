package com.handstandsam.buildingdebugfeatures.network;

import com.handstandsam.buildingdebugfeatures.network.model.GitHubRepo;
import com.handstandsam.buildingdebugfeatures.network.model.GitHubUser;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubService {
    @GET("users/{user}")
    Observable<GitHubUser> user(@Path("user") String user);

    @GET("users/{user}/repos")
    Call<List<GitHubRepo>> repos(@Path("user") String user);
}
