package com.lyft.cityguide.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.lyft.cityguide.R;
import com.lyft.cityguide.models.structs.PlaceType;
import com.lyft.cityguide.models.structs.PointOfInterest;
import com.lyft.cityguide.ui.adapters.ResultAdapter;
import com.lyft.cityguide.ui.events.ShowBarsEvent;
import com.lyft.cityguide.ui.events.ShowBistrosEvent;
import com.lyft.cityguide.ui.events.ShowCafesEvent;
import com.lyft.cityguide.utils.actions.Action;
import com.lyft.cityguide.utils.actions.Action0;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @class ResultListFragment
 * @brief
 */
public class ResultListFragment extends BaseFragment {
    private PlaceType     _currentType;
    private boolean       _isFetchingMore;
    private ResultAdapter _currentAdapter;

    @Bind(R.id.fragment_result_list_no_content)
    TextView _noContent;

    @Bind(R.id.fragment_result_list_wrapper)
    SwipeRefreshLayout _listWrapper;

    @Bind(R.id.fragment_result_list)
    ListView _list;

    private void _fetchPOIsCallback(List<PointOfInterest> pois, Action0 customDone) {
        if (pois.size() == 0) {
            _noContent.setVisibility(View.VISIBLE);
            _list.setVisibility(View.GONE);
        } else {
            _noContent.setVisibility(View.GONE);
            _list.setVisibility(View.VISIBLE);
            _currentAdapter = new ResultAdapter(pois, getActivity(), _currentType);
            _list.setAdapter(_currentAdapter);
        }
        if (customDone != null) {
            customDone.run();
        } else {
            done();
        }
    }

    private void _setBarContent() {
        _setBarContent(null);
    }

    private void _setBarContent(Action0 customDone) {
        _currentType = PlaceType.BAR;
        if (customDone == null) {
            fork();
        }
        getPlaceBLL().cancelAllTasks();
        getPlaceBLL().getBarsAround((pois) -> _fetchPOIsCallback(pois, customDone), this::onError);
    }

    private void _setBistroContent() {
        _setBistroContent(null);
    }

    private void _setBistroContent(Action0 customDone) {
        _currentType = PlaceType.BISTRO;
        if (customDone == null) {
            fork();
        }
        getPlaceBLL().cancelAllTasks();
        getPlaceBLL().getBistrosAround((pois) -> _fetchPOIsCallback(pois, customDone), this::onError);
    }

    private void _setCafeContent() {
        _setCafeContent(null);
    }

    private void _setCafeContent(Action0 customDone) {
        _currentType = PlaceType.CAFE;
        if (customDone == null) {
            fork();
        }
        getPlaceBLL().cancelAllTasks();
        getPlaceBLL().getCafesAround((pois) -> _fetchPOIsCallback(pois, customDone), this::onError);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment;

        fragment = inflater.inflate(R.layout.fragment_result_list, container, false);
        ButterKnife.bind(this, fragment);

        _list.setOnScrollListener(
            new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (_currentAdapter != null
                        && _list.getLastVisiblePosition() == (_list.getAdapter().getCount() - 1)
                        && _list.getChildAt(_list.getChildCount() - 1).getBottom() <= _list
                        .getHeight()) {
                        onMore();
                    }
                }
            }
        );

        _listWrapper.setOnRefreshListener(
            () -> {
                Action0 customDone = () -> _listWrapper.setRefreshing(false);

                if (_currentAdapter == null) {
                    // Content no set yet
                    customDone.run();
                    return;
                }
                switch (_currentType) {
                    case BAR:
                        _setBarContent(customDone);
                        break;
                    case BISTRO:
                        _setBistroContent(customDone);
                        break;
                    case CAFE:
                        _setCafeContent(customDone);
                        break;
                }
            }
        );


        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        _isFetchingMore = false;
        _setBarContent();
        getResultListBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        getResultListBus().unregister(this);
        getPlaceBLL().cancelAllTasks();
    }

    public void onMore() {
        Action<List<PointOfInterest>> successCallback;
        Action<String> errorCallback;

        if (_currentAdapter == null || _isFetchingMore) {
            return;
        }

        _isFetchingMore = true;
        fork();
        successCallback = (pois) -> {
            _isFetchingMore = false;
            if (pois.size() == 0) {
                inform(getString(R.string.no_more_result));
            } else {
                _currentAdapter.appendItems(pois);
                _currentAdapter.notifyDataSetChanged();
            }
            done();
        };
        errorCallback = (error) -> {
            _isFetchingMore = false;
            onError(error);
        };

        switch (_currentType) {
            case BAR:
                getPlaceBLL().moreBarsAround(successCallback, errorCallback);
                break;
            case BISTRO:
                getPlaceBLL().moreBistrosAround(successCallback, errorCallback);
                break;
            case CAFE:
                getPlaceBLL().moreCafesAround(successCallback, errorCallback);
                break;
        }
    }

    public void onEventMainThread(ShowBarsEvent e) {
        if (_currentType == PlaceType.BAR) {
            return;
        }

        _setBarContent();
    }

    public void onEventMainThread(ShowBistrosEvent e) {
        if (_currentType == PlaceType.BISTRO) {
            return;
        }
        _setBistroContent();
    }

    public void onEventMainThread(ShowCafesEvent e) {
        if (_currentType == PlaceType.CAFE) {
            return;
        }
        _setCafeContent();
    }
}
