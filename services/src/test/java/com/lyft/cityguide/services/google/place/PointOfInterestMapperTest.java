package com.lyft.cityguide.services.google.place;

import com.lyft.cityguide.domain.PointOfInterest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

/**
 * PointOfInterestMapperTest
 * <p>
 */
public class PointOfInterestMapperTest {

    PointOfInterestMapper mapper;

    @Before
    public void setup() {
        mapper = new PointOfInterestMapper();
    }

    @After
    public void tearDown() {
        mapper = null;
    }

    @Test
    public void mapShouldReturnAValidPointOfInterest() {
        PlaceDTO source = new PlaceDTO();
        PointOfInterest.Kind kind = PointOfInterest.Kind.BISTRO;
        PointOfInterest value;

        source.setId("abcdef");
        source.setRating(5.6f);
        source.setName("My POI");
        source.setLatitude(5.78f);
        source.setLongitude(-56.78f);

        value = mapper.map(source, kind);

        assertThat(value).isNotNull();
        assertThat(value.getId()).isEqualTo("abcdef");
        assertThat(value.getRating()).isEqualTo(5.6f);
        assertThat(value.getName()).isEqualTo("My POI");
        assertThat(value.getLatitude()).isEqualTo(5.78f);
        assertThat(value.getLongitude()).isEqualTo(-56.78f);
    }

    @Test
    public void mapShouldReturnAValidPointOfInterestList() {
        List<PlaceDTO> source = new ArrayList<>();
        PointOfInterest.Kind kind = PointOfInterest.Kind.CAFE;
        List<PointOfInterest> outcome;
        PlaceDTO placeDTO;

        placeDTO = new PlaceDTO();
        placeDTO.setId("foobar");
        placeDTO.setRating(4.6f);
        placeDTO.setName("Coffeeshop");
        source.add(placeDTO);
        placeDTO = new PlaceDTO();
        placeDTO.setName("Restaurant");
        placeDTO.setLatitude(45.67f);
        placeDTO.setLongitude(-3.45f);
        source.add(placeDTO);
        placeDTO = new PlaceDTO();
        placeDTO.setName("Bistro");
        placeDTO.setLatitude(-32.56f);
        placeDTO.setRating(-34f);
        source.add(placeDTO);

        outcome = mapper.map(source, kind);

        assertThat(outcome).isNotNull();
        assertThat(outcome.size()).isEqualTo(3);
        assertThat(outcome.get(0).getId()).isEqualTo("foobar");
        assertThat(outcome.get(0).getRating()).isEqualTo(4.6f);
        assertThat(outcome.get(0).getName()).isEqualTo("Coffeeshop");
        assertThat(outcome.get(1).getName()).isEqualTo("Restaurant");
        assertThat(outcome.get(1).getLatitude()).isEqualTo(45.67f);
        assertThat(outcome.get(1).getLongitude()).isEqualTo(-3.45f);
        assertThat(outcome.get(2).getName()).isEqualTo("Bistro");
        assertThat(outcome.get(2).getLatitude()).isEqualTo(-32.56f);
        assertThat(outcome.get(2).getRating()).isEqualTo(-34f);
    }
}
