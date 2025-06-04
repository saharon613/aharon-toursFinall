package aharon.tours;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ToursServiceTest {

    ToursService service = new ToursServiceFactory().create();

    @Test
    void getTours() {
        // when
        ToursResponse response = service.getTours().blockingGet();

        // then
        assertNotNull(response);
        assertNotNull(response.data);
        assertEquals(18, response.data.length);
    }

    @Test
    void searchArtworks() {
        // when
        ArtSearchResponse response = service.searchArtworks("monet").blockingGet();

        // then
        assertNotNull(response);
        assertNotNull(response.data);
        assertFalse(response.data.isEmpty());
        assertTrue(response.data.get(0).id > 0);
    }

    @Test
    void getArtworkDetails() {
        // given
        int artworkId = 127874;

        // when
        ArtworkDetailsResponse response = service.getArtworkDetails(artworkId).blockingGet();

        // then
        assertNotNull(response);
        assertNotNull(response.data);
        assertNotNull(response.data.title);
    }
}
