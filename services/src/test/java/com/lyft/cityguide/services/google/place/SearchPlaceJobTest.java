package com.lyft.cityguide.services.google.place;

import android.location.Location;

import com.lyft.cityguide.domain.PointOfInterest;
import com.lyft.cityguide.domain.SearchRangeSetting;
import com.lyft.cityguide.services.ServiceErrors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * SearchPlaceJobTest
 * <p>
 */
public class SearchPlaceJobTest {

    Configuration          configuration;
    IGooglePlaceAPI        googlePlaceAPI;
    IPointOfInterestMapper pointOfInterestMapper;

    SearchPlacesJob searchPlacesJob;

    @Before
    public void setup() {
        configuration = mock(Configuration.class);
        googlePlaceAPI = mock(IGooglePlaceAPI.class);
        pointOfInterestMapper = mock(IPointOfInterestMapper.class);

        searchPlacesJob = new SearchPlacesJob(configuration, googlePlaceAPI, pointOfInterestMapper);
    }

    @After
    public void tearDown() {
        configuration = null;
        googlePlaceAPI = null;
        pointOfInterestMapper = null;
        searchPlacesJob = null;
    }

    @Test
    public void searchShouldReturnValidPlaces() {
        Location currentLocation = mock(Location.class);
        SearchOutcomeDTO searchOutcomeDTO;
        PlaceDTO p;
        PointOfInterest poi;
        List<PointOfInterest> expectedPois = new ArrayList<>();

        searchOutcomeDTO = new SearchOutcomeDTO();
        searchOutcomeDTO.setNextPageToken("nextPageToken");
        p = new PlaceDTO();
        p.setName("foobar");
        p.setLatitude(45.56f);
        p.setLongitude(-34f);
        searchOutcomeDTO.addPlace(p);
        p = new PlaceDTO();
        p.setName("barbar");
        p.setRating(45f);
        searchOutcomeDTO.addPlace(p);
        p = new PlaceDTO();
        p.setName("foobarbar");
        p.setLatitude(-45f);
        searchOutcomeDTO.addPlace(p);

        poi = new PointOfInterest().setId("foobar").setName("barbar");
        expectedPois.add(poi);
        poi = new PointOfInterest().setName("foofoo").setKind(PointOfInterest.Kind.BISTRO).setLatitude(45f);
        expectedPois.add(poi);
        poi = new PointOfInterest().setId("myUUID").setRating(4f).setLongitude(-45f);
        expectedPois.add(poi);

        when(currentLocation.getLatitude()).thenReturn(45.0);
        when(currentLocation.getLongitude()).thenReturn(-45.67);
        when(pointOfInterestMapper.map(PointOfInterest.Kind.BISTRO)).thenReturn("bistro");
        when(pointOfInterestMapper.map(searchOutcomeDTO.getPlaces(), PointOfInterest.Kind.BISTRO)).thenReturn(expectedPois);
        when(googlePlaceAPI.search("45.0,-45.67", 3218, "bistro", null)).thenReturn(searchOutcomeDTO);

        // Act
        Observable<List<PointOfInterest>> outcome = searchPlacesJob.search(currentLocation, SearchRangeSetting.TWO_MILE, PointOfInterest.Kind.BISTRO);

        TestSubscriber<List<PointOfInterest>> subscriber = new TestSubscriber<>();
        outcome.toBlocking().subscribe(subscriber);

        verify(googlePlaceAPI, times(1)).search("45.0,-45.67", 2 * 1609, "bistro", null);
        verify(pointOfInterestMapper, times(1)).map(PointOfInterest.Kind.BISTRO);
        verify(pointOfInterestMapper, times(1)).map(searchOutcomeDTO.getPlaces(), PointOfInterest.Kind.BISTRO);

        subscriber.assertNoErrors();
        subscriber.assertValueCount(1);
        subscriber.assertValue(expectedPois);
        subscriber.assertCompleted();
    }

    @Test
    public void searchShouldReturnNoConnectionIfNoConnection() {
        Location currentLocation = mock(Location.class);
        RetrofitError raisedError = mock(RetrofitError.class);

        when(raisedError.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        when(raisedError.getResponse()).thenReturn(null);

        when(currentLocation.getLatitude()).thenReturn(-23.4);
        when(currentLocation.getLongitude()).thenReturn(-45.95);

        when(pointOfInterestMapper.map(PointOfInterest.Kind.CAFE)).thenReturn("cafe");
        when(googlePlaceAPI.search("-23.4,-45.95", 8045, "cafe", null)).thenThrow(raisedError);

        // Act
        Observable<List<PointOfInterest>> outcome = searchPlacesJob.search(currentLocation, SearchRangeSetting.FIVE_MILE, PointOfInterest.Kind.CAFE);

        TestSubscriber<List<PointOfInterest>> subscriber = new TestSubscriber<>();
        outcome.toBlocking().subscribe(subscriber);

        verify(pointOfInterestMapper, times(1)).map(PointOfInterest.Kind.CAFE);
        verify(googlePlaceAPI, times(1)).search("-23.4,-45.95", 8045, "cafe", null);

        subscriber.assertError(ServiceErrors.NoConnection.class);
        subscriber.assertNotCompleted();
        subscriber.assertNoValues();
    }

    @Test
    public void moreShouldReturnValidPointOfInterests() {
        List<PlaceDTO> placesFromAPI = new ArrayList<>();
        List<PointOfInterest> expectedPOIs = new ArrayList<>();
        Location currentLocation = mock(Location.class);
        SearchOutcomeDTO s1 = mock(SearchOutcomeDTO.class), s2 = mock(SearchOutcomeDTO.class);

        when(currentLocation.getLatitude()).thenReturn(12.0);
        when(currentLocation.getLongitude()).thenReturn(-87.4);

        when(s1.getNextPageToken()).thenReturn("token");
        when(s2.getNextPageToken()).thenReturn(null);
        when(s2.getPlaces()).thenReturn(placesFromAPI);

        when(pointOfInterestMapper.map(PointOfInterest.Kind.CAFE)).thenReturn("pools");
        when(pointOfInterestMapper.map(placesFromAPI, PointOfInterest.Kind.CAFE)).thenReturn(expectedPOIs);

        when(googlePlaceAPI.search("12.0,-87.4", 1609, "pools", null)).thenReturn(s1);
        when(googlePlaceAPI.more("token", null)).thenReturn(s2);

        // Act
        searchPlacesJob
            .search(currentLocation, SearchRangeSetting.ONE_MILE, PointOfInterest.Kind.CAFE)
            .toBlocking()
            .subscribe();
        Observable<List<PointOfInterest>> outcome = searchPlacesJob.more();

        TestSubscriber<List<PointOfInterest>> subscriber = new TestSubscriber<>();
        outcome.toBlocking().subscribe(subscriber);

        verify(googlePlaceAPI, times(1)).more("token", null);
        verify(s1, times(1)).getNextPageToken();
        verify(s2, times(1)).getNextPageToken();
        verify(pointOfInterestMapper, times(1 + 1)).map(placesFromAPI, PointOfInterest.Kind.CAFE);

        subscriber.assertNoErrors();
        subscriber.assertCompleted();
        subscriber.assertValueCount(1);
        subscriber.assertValue(expectedPOIs);
    }

    @Test
    public void moreShouldKeepReturningResultsIfAny() {

    }

    @Test
    public void moreShouldKeepSameTokenIfNoSubscriber() {

    }

    @Test
    public void moreShouldReturnNoMoreResultIfNoMorePlaces() {

        // Act
        Observable<List<PointOfInterest>> outcome = searchPlacesJob.more();

        TestSubscriber<List<PointOfInterest>> subscriber = new TestSubscriber<>();
        outcome.toBlocking().subscribe(subscriber);

        subscriber.assertError(GooglePlaceErrors.NoMoreResult.class);
        subscriber.assertNotCompleted();
        subscriber.assertNoValues();
    }

    @Test
    public void moreShouldReturnServerErrorIfUnhandledRetrofitError() {
        RetrofitError raisedError = mock(RetrofitError.class);
        Location currentLocation = mock(Location.class);
        SearchOutcomeDTO searchOutcomeDTO = mock(SearchOutcomeDTO.class);

        when(raisedError.getKind()).thenReturn(RetrofitError.Kind.HTTP);
        when(raisedError.getResponse()).thenReturn(null);

        when(currentLocation.getLatitude()).thenReturn(2.4);
        when(currentLocation.getLongitude()).thenReturn(3.5);

        when(pointOfInterestMapper.map(PointOfInterest.Kind.BAR)).thenReturn("bar");

        when(searchOutcomeDTO.getNextPageToken()).thenReturn("myPageToken");

        when(googlePlaceAPI.search("2.4,3.5", 1609, "bar", null)).thenReturn(searchOutcomeDTO);
        when(googlePlaceAPI.more("myPageToken", null)).thenThrow(raisedError);

        // Act
        searchPlacesJob
            .search(currentLocation, SearchRangeSetting.ONE_MILE, PointOfInterest.Kind.BAR)
            .toBlocking()
            .subscribe();
        Observable<List<PointOfInterest>> outcome = searchPlacesJob.more();

        TestSubscriber<List<PointOfInterest>> subscriber = new TestSubscriber<>();
        outcome.toBlocking().subscribe(subscriber);

        subscriber.assertError(ServiceErrors.ServerError.class);
        subscriber.assertNotCompleted();
        subscriber.assertNoValues();
    }
}
