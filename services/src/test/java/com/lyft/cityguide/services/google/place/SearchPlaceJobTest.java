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
        Observable<List<PointOfInterest>> outcome;
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
        outcome = searchPlacesJob.search(currentLocation, SearchRangeSetting.TWO_MILE, PointOfInterest.Kind.BISTRO);

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
    public void searchShouldReturnErrorIfAnyRetrofitErrorIsRaised() {
        Observable<List<PointOfInterest>> outcome;
        Location currentLocation = mock(Location.class);
        RetrofitError raisedError = mock(RetrofitError.class);

        when(raisedError.getKind()).thenReturn(RetrofitError.Kind.NETWORK);
        when(raisedError.getResponse()).thenReturn(null);

        when(currentLocation.getLatitude()).thenReturn(-23.4);
        when(currentLocation.getLongitude()).thenReturn(-45.95);

        when(pointOfInterestMapper.map(PointOfInterest.Kind.CAFE)).thenReturn("cafe");
        when(googlePlaceAPI.search("-23.4,-45.95", 8045, "cafe", null)).thenThrow(raisedError);

        // Act
        outcome = searchPlacesJob.search(currentLocation, SearchRangeSetting.FIVE_MILE, PointOfInterest.Kind.CAFE);

        TestSubscriber<List<PointOfInterest>> subscriber = new TestSubscriber<>();
        outcome.toBlocking().subscribe(subscriber);

        verify(pointOfInterestMapper, times(1)).map(PointOfInterest.Kind.CAFE);
        verify(googlePlaceAPI, times(1)).search("-23.4,-45.95", 8045, "cafe", null);

        subscriber.assertError(ServiceErrors.NoConnection.class);
        subscriber.assertNotCompleted();
        subscriber.assertNoValues();
    }

    
}
