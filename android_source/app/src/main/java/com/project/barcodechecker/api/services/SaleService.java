package com.project.barcodechecker.api.services;

import com.project.barcodechecker.models.Sale;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by lucky on 31-Oct-17.
 */

public interface SaleService {
    @GET("/api/products/{id}/sales")
    Call<List<Sale>> getSaleByProductID(@Path("id") Integer id);
     @GET("/api/users/{id}/sales")
    Call<List<Sale>> getSaleByUserID(@Path("id") Integer id);

}
