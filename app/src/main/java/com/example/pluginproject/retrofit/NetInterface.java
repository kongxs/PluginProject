package com.example.pluginproject.retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;

public interface NetInterface {

    /*
        https://www.mxnzp.com/image/girl/list/random
     */

    @GET("image/girl/list/random")
    Call<ReqResult> getCall();

}
