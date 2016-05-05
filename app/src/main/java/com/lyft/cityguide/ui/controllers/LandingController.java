package com.lyft.cityguide.ui.controllers;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.lyft.cityguide.R;
import com.lyft.cityguide.bll.BLLErrors;
import com.lyft.cityguide.domain.PointOfInterest;
import com.lyft.cityguide.ui.adapters.PointOfInterestAdapter;
import com.lyft.cityguide.ui.components.Slider;
import com.lyft.cityguide.ui.screens.menu.ShowMenuScreen;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * LandingController
 * <p>
 */
public class LandingController extends BaseController {
    private PointOfInterest.Kind   currentKind;
    private PointOfInterestAdapter pointOfInterestAdapter;
    private Subscription           listPointOfInterestsAroundSubscription;
    private Subscription           listMoreSubscription;

    @Bind(R.id.partial_landing_header_menu_trigger_slider)
    Slider headerSlider;

    @Bind(R.id.partial_result_list_no_content_label)
    TextView noContentLabelView;

    @Bind(R.id.partial_result_list_wrapper)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.partial_result_list_listview)
    ListView resultListView;

    private void listContent() {
        listContent(false);
    }

    private void listContent(boolean isRefreshing) {
        if (listPointOfInterestsAroundSubscription != null) {
            listPointOfInterestsAroundSubscription.unsubscribe();
        }

        if (!isRefreshing) {
            showSpinner();
        }

        listPointOfInterestsAroundSubscription =
            pointOfInterestBLL
                .listAround(currentKind)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<List<PointOfInterest>>() {
                    @Override
                    public void onCompleted() {
                        if (isRefreshing) {
                            swipeRefreshLayout.setRefreshing(false);
                        } else {
                            hideSpinner();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof BLLErrors.DisabledLocation) {
                            alert(context.getString(R.string.error_location_disabled));
                        } else {
                            super.onError(e);
                        }

                        if (isRefreshing) {
                            swipeRefreshLayout.setRefreshing(false);
                        } else {
                            hideSpinner();
                        }
                    }

                    @Override
                    public void onNext(List<PointOfInterest> pointOfInterestBLLDTOs) {
                        if (pointOfInterestBLLDTOs.isEmpty()) {
                            noContentLabelView.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setVisibility(View.GONE);
                        } else {
                            pointOfInterestAdapter = new PointOfInterestAdapter(context, pointOfInterestBLLDTOs, currentKind);
                            resultListView.setAdapter(pointOfInterestAdapter);
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

        listMoreSubscription = pointOfInterestBLL
            .listMore()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new BaseSubscriber<List<PointOfInterest>>() {
                @Override
                public void onCompleted() {
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof BLLErrors.NoMorePOI) {
                        inform(context.getString(R.string.no_more_result));
                    } else {
                        super.onError(e);
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onNext(List<PointOfInterest> newPOIs) {
                    pointOfInterestAdapter.addItems(newPOIs);
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

        currentKind = PointOfInterest.Kind.BAR;
        listContent();

        resultListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // Nothing to do
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int latestIndexForAdapter, latestIndexForListView;

                if (pointOfInterestAdapter == null) {
                    return;
                }

                latestIndexForAdapter = pointOfInterestAdapter.getCount() - 1;
                latestIndexForListView = resultListView.getChildCount() - 1;

                if (resultListView.getLastVisiblePosition() == latestIndexForAdapter
                    && resultListView.getChildAt(latestIndexForListView).getBottom() <= resultListView.getHeight()) {
                    // Display more content when reaching the bottom of the list
                    // Do not fetch if latest element is not at the bottom (no
                    // more element)
                    swipeRefreshLayout.setRefreshing(true);
                    fetchMoreContent();
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            this.listContent(true);
        });

        headerSlider.observe((index, label) -> {
            final PointOfInterest.Kind formerKind = currentKind;

            switch (index) {
                case 1:
                    currentKind = PointOfInterest.Kind.BISTRO;
                    break;
                case 2:
                    currentKind = PointOfInterest.Kind.CAFE;
                    break;
                default:
                    currentKind = PointOfInterest.Kind.BAR;
                    break;
            }

            if (currentKind != formerKind) {
                listContent();
            }
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
        menuRouter.goTo(new ShowMenuScreen());
    }
}
