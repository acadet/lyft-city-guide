package com.lyft.cityguide.bll;

import android.location.Location;

import com.lyft.cityguide.domain.Distance;
import com.lyft.cityguide.domain.PointOfInterest;
import com.lyft.cityguide.domain.SearchRangeSetting;
import com.lyft.cityguide.services.ServiceErrors;
import com.lyft.cityguide.services.google.distancematrix.IGoogleDistanceMatrixService;
import com.lyft.cityguide.services.google.place.GooglePlaceErrors;
import com.lyft.cityguide.services.google.place.IGooglePlaceService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * ListPointsOfInterestJobTest
 * <p>
 */
public class ListPointsOfInterestJobTest {

    IGooglePlaceService          googlePlaceService;
    IGoogleDistanceMatrixService googleDistanceMatrixService;
    ListPointsOfInterestJob      subject;

    @Before
    public void setup() {
        googlePlaceService = mock(IGooglePlaceService.class);
        googleDistanceMatrixService = mock(IGoogleDistanceMatrixService.class);
        subject = new ListPointsOfInterestJob(googlePlaceService, googleDistanceMatrixService);
    }

    @After
    public void tearDown() {
        googlePlaceService = null;
        googleDistanceMatrixService = null;
        subject = null;
    }

    @Test
    public void listShouldReturnValidPointsOfInterests() {
        Location currentLocation = mock(Location.class);
        SearchRangeSetting searchRangeSetting = SearchRangeSetting.FIVE_MILE;
        PointOfInterest.Kind kind = PointOfInterest.Kind.BISTRO;
        List<PointOfInterest> expectedPOIs = new ArrayList<>();
        Observable<List<PointOfInterest>> googlePlaceServiceObservable = Observable.just(expectedPOIs);
        Observable<Void> googleDistanceMatrixObservable = Observable.empty();

        expectedPOIs.add(new PointOfInterest().setDistance(Distance.fromMeters(4f)).setName("My POI"));
        expectedPOIs.add(new PointOfInterest().setId("ID").setKind(PointOfInterest.Kind.BISTRO));
        expectedPOIs.add(new PointOfInterest().setLatitude(45f).setLongitude(34f).setRating(3f));

        when(googlePlaceService.search(currentLocation, searchRangeSetting, kind)).thenReturn(googlePlaceServiceObservable);

        when(googleDistanceMatrixService.fetchDistances(currentLocation, expectedPOIs)).thenReturn(googleDistanceMatrixObservable);

        // Act
        Observable<List<PointOfInterest>> outcome = subject.list(currentLocation, searchRangeSetting, kind);

        TestSubscriber<List<PointOfInterest>> subscriber = new TestSubscriber<>();
        outcome.toBlocking().subscribe(subscriber);

        verify(googlePlaceService, times(1)).search(currentLocation, searchRangeSetting, kind);
        verify(googleDistanceMatrixService, times(1)).fetchDistances(currentLocation, expectedPOIs);

        subscriber.assertNoErrors();
        subscriber.assertCompleted();
        subscriber.assertValueCount(1);
        subscriber.assertValue(expectedPOIs);
    }

    @Test
    public void listShouldPropagateNoConnectionErrorIfGooglePlaceRaisesNoConnectionError() {
        Location currentLocation = mock(Location.class);
        SearchRangeSetting searchRangeSetting = SearchRangeSetting.FIVE_MILE;
        PointOfInterest.Kind kind = PointOfInterest.Kind.BISTRO;
        List<PointOfInterest> expectedPOIs = new ArrayList<>();
        Observable<List<PointOfInterest>> googlePlaceServiceObservable = Observable.error(new ServiceErrors.NoConnection());
        Observable<Void> googleDistanceMatrixObservable = Observable.empty();

        expectedPOIs.add(new PointOfInterest().setDistance(Distance.fromMeters(4f)).setName("My POI"));
        expectedPOIs.add(new PointOfInterest().setId("ID").setKind(PointOfInterest.Kind.BISTRO));
        expectedPOIs.add(new PointOfInterest().setLatitude(45f).setLongitude(34f).setRating(3f));

        when(googlePlaceService.search(currentLocation, searchRangeSetting, kind)).thenReturn(googlePlaceServiceObservable);

        when(googleDistanceMatrixService.fetchDistances(currentLocation, expectedPOIs)).thenReturn(googleDistanceMatrixObservable);

        // Act
        Observable<List<PointOfInterest>> outcome = subject.list(currentLocation, searchRangeSetting, kind);

        TestSubscriber<List<PointOfInterest>> subscriber = new TestSubscriber<>();
        outcome.toBlocking().subscribe(subscriber);

        verify(googlePlaceService, times(1)).search(currentLocation, searchRangeSetting, kind);
        verify(googleDistanceMatrixService, never()).fetchDistances(currentLocation, expectedPOIs);

        subscriber.assertError(BLLErrors.NoConnection.class);
        subscriber.assertNotCompleted();
        subscriber.assertNoValues();
    }

    @Test
    public void listShouldPropagateServiceErrorIfGoogleDistanceMatrixRaisesServerError() {
        Location currentLocation = mock(Location.class);
        SearchRangeSetting searchRangeSetting = SearchRangeSetting.FIVE_MILE;
        PointOfInterest.Kind kind = PointOfInterest.Kind.BISTRO;
        List<PointOfInterest> expectedPOIs = new ArrayList<>();
        Observable<List<PointOfInterest>> googlePlaceServiceObservable = Observable.just(expectedPOIs);
        Observable<Void> googleDistanceMatrixObservable = Observable.error(new ServiceErrors.ServerError());

        expectedPOIs.add(new PointOfInterest().setDistance(Distance.fromMeters(4f)).setName("My POI"));
        expectedPOIs.add(new PointOfInterest().setId("ID").setKind(PointOfInterest.Kind.BISTRO));
        expectedPOIs.add(new PointOfInterest().setLatitude(45f).setLongitude(34f).setRating(3f));

        when(googlePlaceService.search(currentLocation, searchRangeSetting, kind)).thenReturn(googlePlaceServiceObservable);

        when(googleDistanceMatrixService.fetchDistances(currentLocation, expectedPOIs)).thenReturn(googleDistanceMatrixObservable);

        // Act
        Observable<List<PointOfInterest>> outcome = subject.list(currentLocation, searchRangeSetting, kind);

        TestSubscriber<List<PointOfInterest>> subscriber = new TestSubscriber<>();
        outcome.toBlocking().subscribe(subscriber);

        verify(googlePlaceService, times(1)).search(currentLocation, searchRangeSetting, kind);
        verify(googleDistanceMatrixService, times(1)).fetchDistances(currentLocation, expectedPOIs);

        subscriber.assertError(BLLErrors.ServiceError.class);
        subscriber.assertNotCompleted();
        subscriber.assertNoValues();
    }

    @Test
    public void moreShouldReturnValidPointsOfInterests() {
        Location currentLocation = mock(Location.class);
        SearchRangeSetting searchRangeSetting = SearchRangeSetting.FIVE_MILE;
        PointOfInterest.Kind kind = PointOfInterest.Kind.BISTRO;
        List<PointOfInterest> expectedPOIs = new ArrayList<>();
        Observable<List<PointOfInterest>> googlePlaceServiceSearchObservable = Observable.just(null);
        Observable<List<PointOfInterest>> googlePlaceServiceMoreObservable = Observable.just(expectedPOIs);
        Observable<Void> googleDistanceMatrixObservable = Observable.empty();

        expectedPOIs.add(new PointOfInterest().setDistance(Distance.fromMeters(4f)).setName("My POI"));
        expectedPOIs.add(new PointOfInterest().setId("ID").setKind(PointOfInterest.Kind.BISTRO));
        expectedPOIs.add(new PointOfInterest().setLatitude(45f).setLongitude(34f).setRating(3f));

        when(googlePlaceService.search(currentLocation, searchRangeSetting, kind)).thenReturn(googlePlaceServiceSearchObservable);
        when(googlePlaceService.more()).thenReturn(googlePlaceServiceMoreObservable);

        when(googleDistanceMatrixService.fetchDistances(any(), any())).thenReturn(googleDistanceMatrixObservable);

        // Act
        subject.list(currentLocation, searchRangeSetting, kind).toBlocking().subscribe();
        Observable<List<PointOfInterest>> outcome = subject.more();

        TestSubscriber<List<PointOfInterest>> subscriber = new TestSubscriber<>();
        outcome.toBlocking().subscribe(subscriber);

        verify(googlePlaceService, times(1)).search(currentLocation, searchRangeSetting, kind);
        verify(googlePlaceService, times(1)).more();
        verify(googleDistanceMatrixService, times(1)).fetchDistances(currentLocation, null);
        verify(googleDistanceMatrixService, times(1)).fetchDistances(currentLocation, expectedPOIs);

        subscriber.assertNoErrors();
        subscriber.assertCompleted();
        subscriber.assertValueCount(1);
        subscriber.assertValue(expectedPOIs);
    }

    @Test
    public void moreShouldReturnNoMorePOIIfNoMoreData() {
        Location currentLocation = mock(Location.class);
        SearchRangeSetting searchRangeSetting = SearchRangeSetting.FIVE_MILE;
        PointOfInterest.Kind kind = PointOfInterest.Kind.BISTRO;
        List<PointOfInterest> pois = new ArrayList<>();
        Observable<List<PointOfInterest>> googlePlaceServiceSearchObservable = Observable.just(pois);
        Observable<List<PointOfInterest>> googlePlaceServiceMoreObservable = Observable.error(new GooglePlaceErrors.NoMoreResult());
        Observable<Void> googleDistanceMatrixObservable = Observable.empty();

        pois.add(new PointOfInterest().setDistance(Distance.fromMeters(4f)).setName("My POI"));
        pois.add(new PointOfInterest().setId("ID").setKind(PointOfInterest.Kind.BISTRO));
        pois.add(new PointOfInterest().setLatitude(45f).setLongitude(34f).setRating(3f));

        when(googlePlaceService.search(currentLocation, searchRangeSetting, kind)).thenReturn(googlePlaceServiceSearchObservable);
        when(googlePlaceService.more()).thenReturn(googlePlaceServiceMoreObservable);

        when(googleDistanceMatrixService.fetchDistances(currentLocation, pois)).thenReturn(googleDistanceMatrixObservable);

        // Act
        subject.list(currentLocation, searchRangeSetting, kind).toBlocking().subscribe();
        Observable<List<PointOfInterest>> outcome = subject.more();

        TestSubscriber<List<PointOfInterest>> subscriber = new TestSubscriber<>();
        outcome.toBlocking().subscribe(subscriber);

        verify(googlePlaceService, times(1)).search(currentLocation, searchRangeSetting, kind);
        verify(googlePlaceService, times(1)).more();
        verify(googleDistanceMatrixService, times(1)).fetchDistances(currentLocation, pois);
        verify(googleDistanceMatrixService, never()).fetchDistances(currentLocation, null);

        subscriber.assertError(BLLErrors.NoMorePOI.class);
        subscriber.assertNotCompleted();
        subscriber.assertNoValues();
    }

    @Test
    public void moreShouldReturnNoMorePOIIfNoCurrentLocationYet() {
        Location currentLocation = mock(Location.class);
        SearchRangeSetting searchRangeSetting = SearchRangeSetting.FIVE_MILE;
        PointOfInterest.Kind kind = PointOfInterest.Kind.BISTRO;
        List<PointOfInterest> pois = new ArrayList<>();
        Observable<List<PointOfInterest>> googlePlaceServiceSearchObservable = Observable.just(pois);
        Observable<List<PointOfInterest>> googlePlaceServiceMoreObservable = Observable.error(new GooglePlaceErrors.NoMoreResult());
        Observable<Void> googleDistanceMatrixObservable = Observable.empty();

        pois.add(new PointOfInterest().setDistance(Distance.fromMeters(4f)).setName("My POI"));
        pois.add(new PointOfInterest().setId("ID").setKind(PointOfInterest.Kind.BISTRO));
        pois.add(new PointOfInterest().setLatitude(45f).setLongitude(34f).setRating(3f));

        when(googlePlaceService.search(currentLocation, searchRangeSetting, kind)).thenReturn(googlePlaceServiceSearchObservable);
        when(googlePlaceService.more()).thenReturn(googlePlaceServiceMoreObservable);

        when(googleDistanceMatrixService.fetchDistances(currentLocation, pois)).thenReturn(googleDistanceMatrixObservable);

        // Act
        Observable<List<PointOfInterest>> outcome = subject.more();

        TestSubscriber<List<PointOfInterest>> subscriber = new TestSubscriber<>();
        outcome.toBlocking().subscribe(subscriber);

        verify(googlePlaceService, never()).search(currentLocation, searchRangeSetting, kind);
        verify(googlePlaceService, never()).more();
        verify(googleDistanceMatrixService, never()).fetchDistances(currentLocation, pois);
        verify(googleDistanceMatrixService, never()).fetchDistances(currentLocation, null);

        subscriber.assertError(BLLErrors.NoMorePOI.class);
        subscriber.assertNotCompleted();
        subscriber.assertNoValues();
    }
}
