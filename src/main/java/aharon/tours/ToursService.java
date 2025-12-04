package aharon.tours;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ToursService {
    @GET("tours?limit=18")
    Single<ToursResponse> getTours();

    @GET("artworks/search")
    Single<ArtSearchResponse> searchArtworks(@Query("q") String query);

    @GET("artworks/{id}?fields=id,title,image_id,description")
    Single<ArtworkDetailsResponse> getArtworkDetails(@Path("id") int id);
}


