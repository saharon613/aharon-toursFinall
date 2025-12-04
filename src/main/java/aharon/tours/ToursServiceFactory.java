package aharon.tours;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ToursServiceFactory {
    public ToursService create() {
        // configure Retrofit for the website
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.artic.edu/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        ToursService service = retrofit.create(ToursService.class);
        return service;
    }
}
