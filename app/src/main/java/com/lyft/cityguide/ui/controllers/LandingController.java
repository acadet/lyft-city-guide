package com.lyft.cityguide.ui.controllers;

import android.graphics.Point;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.lyft.cityguide.R;
import com.lyft.cityguide.models.bll.dto.PointOfInterestBLLDTO;
import com.lyft.cityguide.structs.PlaceType;
import com.lyft.cityguide.ui.adapters.PointOfInterestAdapter;
import com.lyft.cityguide.ui.components.Slider;
import com.nineoldandroids.animation.Animator;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTouch;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * LandingController
 * <p>
 */
public class LandingController extends BaseController {
    private PlaceType              currentType;
    private PointOfInterestAdapter pointOfInterestAdapter;
    private Subscription           listPointOfInterestsAroundSubscription;
    private Subscription           listMoreSubscription;
    private Point                  menuStartTouchPoint;

    @Bind(R.id.partial_menu)
    View menuView;

    @Bind(R.id.partial_landing_header_menu_trigger_slider)
    Slider headerSlider;

    @Bind(R.id.partial_result_list_no_content_label)
    TextView noContentLabelView;

    @Bind(R.id.partial_result_list_wrapper)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.partial_result_list_listview)
    ListView resultListView;

    private void listContent() {
        if (listPointOfInterestsAroundSubscription != null) {
            listPointOfInterestsAroundSubscription.unsubscribe();
        }

        showSpinner();
        listPointOfInterestsAroundSubscription = dataReadingBLL
            .listPointOfInterestsAround(currentType)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new BaseSubscriber<List<PointOfInterestBLLDTO>>() {
                @Override
                public void onCompleted() {
                    hideSpinner();
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    hideSpinner();
                }

                @Override
                public void onNext(List<PointOfInterestBLLDTO> pointOfInterestBLLDTOs) {
                    if (pointOfInterestBLLDTOs.isEmpty()) {
                        noContentLabelView.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setVisibility(View.GONE);
                    } else {
                        pointOfInterestAdapter.setCurrentType(currentType);
                        pointOfInterestAdapter.setItems(pointOfInterestBLLDTOs);
                        noContentLabelView.setVisibility(View.GONE);
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                    }
                }
            });
    }

    private void fetchMoreContent() {
        if (listMoreSubscription != null) {
            listMoreSubscription.unsubscribe();
        }

        listMoreSubscription = dataReadingBLL
            .listMore()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new BaseSubscriber<List<PointOfInterestBLLDTO>>() {
                @Override
                public void onCompleted() {
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onNext(List<PointOfInterestBLLDTO> pointOfInterestBLLDTOs) {
                    Stream.of(pointOfInterestBLLDTOs).forEach(pointOfInterestAdapter::addItem);
                    pointOfInterestAdapter.notifyDataSetChanged();
                }
            });
    }

    @Override
    protected int layoutId() {
        return R.layout.landing_layout;
    }

    @Override
    public void onAttach() {
        super.onAttach();

        currentType = PlaceType.BAR;
        pointOfInterestAdapter = new PointOfInterestAdapter(context, currentType);
        resultListView.setAdapter(pointOfInterestAdapter);

        listContent();

        resultListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // Nothing to do
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int latestIndex = pointOfInterestAdapter.getCount() - 1;

                if (resultListView.getLastVisiblePosition() == latestIndex
                    && resultListView.getChildAt(latestIndex).getBottom() <= resultListView.getHeight()) {
                    // Display more content when reaching the bottom of the list
                    // Do not fetch if latest element is not at the bottom (no
                    // more element)
                    swipeRefreshLayout.setRefreshing(true);
                    fetchMoreContent();
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this::fetchMoreContent);

        headerSlider.setOnSlideListener((index, label) -> {
            switch (index) {
                case 1:
                    currentType = PlaceType.BISTRO;
                    break;
                case 2:
                    currentType = PlaceType.CAFE;
                    break;
                default:
                    currentType = PlaceType.BAR;
                    break;
            }

            listContent();
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (listPointOfInterestsAroundSubscription != null) {
            listPointOfInterestsAroundSubscription.unsubscribe();
        }

        if (listMoreSubscription != null) {
            listMoreSubscription.unsubscribe();
        }
    }

    @OnClick(R.id.partial_landing_header_menu_trigger)
    public void onMenuTriggerClick() {
        menuView.setVisibility(View.VISIBLE);
        YoYo
            .with(Techniques.SlideInLeft)
            .duration(300)
            .playOn(menuView);
    }

    @OnTouch(R.id.partial_menu)
    public boolean onMenuViewTouch(View view, MotionEvent e) {
        // Hide if slightly touched
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            menuStartTouchPoint = new Point(Math.round(e.getX()), Math.round(e.getY()));
        } else if (e.getAction() == MotionEvent.ACTION_MOVE && menuStartTouchPoint != null) {
            int distance = Math.abs(menuStartTouchPoint.x - Math.round(e.getX()));

            if (distance > 100) {
                menuStartTouchPoint = null; // Prevent any extra call to hide
                YoYo
                    .with(Techniques.SlideOutLeft)
                    .duration(300)
                    .withListener(
                        new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                menuView.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        }
                    )
                    .playOn(menuView);
            }
        }

        return true;
    }
}
