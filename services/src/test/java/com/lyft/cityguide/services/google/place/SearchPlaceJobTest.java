package com.lyft.cityguide.services.google.place;

import android.location.Location;

import com.lyft.cityguide.domain.PointOfInterest;
import com.lyft.cityguide.domain.SearchRangeSetting;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
    public void shouldSearchValidPlaces() {
        Observable<List<PointOfInterest>> outcome;
        Location currentLocation = mock(Location.class);
        TestSubscriber<List<PointOfInterest>> subscriber = new TestSubscriber<>();
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
        when(googlePlaceAPI.search("45.0,-45.67", 2 * 1609, "bistro", null)).thenReturn(searchOutcomeDTO);

        // Act
        outcome = searchPlacesJob.search(currentLocation, SearchRangeSetting.TWO_MILE, PointOfInterest.Kind.BISTRO);

        outcome.toBlocking().subscribe(subscriber);

        verify(googlePlaceAPI, times(1)).search("45.0,-45.67", 2 * 1609, "bistro", null);
        verify(pointOfInterestMapper, times(1)).map(PointOfInterest.Kind.BISTRO);
        verify(pointOfInterestMapper, times(1)).map(searchOutcomeDTO.getPlaces(), PointOfInterest.Kind.BISTRO);

        subscriber.assertNoErrors();
        subscriber.assertValueCount(1);
        subscriber.assertValue(expectedPois);
        subscriber.assertCompleted();
    }
}
