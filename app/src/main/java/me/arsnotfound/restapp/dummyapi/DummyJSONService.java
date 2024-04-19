package me.arsnotfound.restapp.dummyapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DummyJSONService {
    @GET("products/{id}")
    Call<Product> getProduct(@Path("id") int id);
}
