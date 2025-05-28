package aharon.tours;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ToursServiceFactory {
    public ToursService create() {
        // configure Retrofit for the website
        Retrofit retrofit = new Retrofit.Builder()      // this is an object, the below is method calls on this object
                .baseUrl("https://api.artic.edu/api/v1/")      // the server we're connecting to
                // Configure Retrofit to use Gson to turn the Json into Objects
                // uses Gson to make Java objects
                .addConverterFactory(GsonConverterFactory.create())
                // Configure Retrofit to use Rx Java - it lets it do it asynchronously
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        ToursService service = retrofit.create(ToursService.class);
        return service;
    }
}
