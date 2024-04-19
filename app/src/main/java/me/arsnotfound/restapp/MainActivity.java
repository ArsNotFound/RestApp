package me.arsnotfound.restapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import me.arsnotfound.restapp.databinding.ActivityMainBinding;
import me.arsnotfound.restapp.dummyapi.DummyJSONService;
import me.arsnotfound.restapp.dummyapi.Product;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.sendBtn.setOnClickListener(this::getProduct);
    }

    private void getProduct(View view) {
       HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
       loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

       OkHttpClient client = new OkHttpClient.Builder()
               .addInterceptor(loggingInterceptor)
               .build();

       Retrofit retrofit = new Retrofit.Builder()
               .client(client)
               .baseUrl("https://dummyjson.com/")
               .addConverterFactory(MoshiConverterFactory.create())
               .build();

        DummyJSONService service = retrofit.create(DummyJSONService.class);

        int id = Integer.parseInt(binding.idField.getText().toString());

        service.getProduct(id).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                if (!response.isSuccessful()) {
                    Log.e("CALLBACK", "Unexpected response code " + response.code());
                    Toast.makeText(MainActivity.this, "Unexpected response code " + response.code(), Toast.LENGTH_LONG).show();
                }

                if (response.body() != null) {
                    binding.result.setText(response.body().getTitle());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Product> call, @NonNull Throwable throwable) {
                Log.e("CALLBACK", "Failed to call API", throwable);
                Toast.makeText(MainActivity.this, "Failed to call API", Toast.LENGTH_LONG).show();
            }
        });
    }

}