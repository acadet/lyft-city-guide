package com.lyft.cityguide.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

    @Bind(R.id.fragment_result_list)
    ListView _list;

    private void _fetchPOIsCallback(List<PointOfInterest> pois) {
        if (pois.size() == 0) {
            _noContent.setVisibility(View.VISIBLE);
            _list.setVisibility(View.GONE);
        } else {
            _noContent.setVisibility(View.GONE);
            _list.setVisibility(View.VISIBLE);
            _currentAdapter = new ResultAdapter(pois, getActivity(), _currentType);
            _list.setAdapter(_currentAdapter);
        }
        done();
    }

    private void _setBarContent() {
        _currentType = PlaceType.BAR;
        fork();
        getPlaceBLL().getBarsAround(this::_fetchPOIsCallback, this::onError);
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
        if (_currentAdapter == null || _isFetchingMore) {
            return;
        }

        _isFetchingMore = true;
        fork();
        getPlaceBLL().moreBarsAround(
            (pois) -> {
                done();
                _isFetchingMore = false;
                if (_currentAdapter == null) {
                    return;
                }
                if (pois.size() == 0) {
                    inform(getString(R.string.no_more_result));
                } else {
                    _currentAdapter.appendItems(pois);
                    _currentAdapter.notifyDataSetChanged();
                }
            },
            (error) -> {
                _isFetchingMore = false;
                onError(error);
            }
        );
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
        _currentType = PlaceType.BISTRO;

        fork();
        getPlaceBLL().getBistrosAround(this::_fetchPOIsCallback, this::onError);
    }

    public void onEventMainThread(ShowCafesEvent e) {
        if (_currentType == PlaceType.CAFE) {
            return;
        }
        _currentType = PlaceType.CAFE;

        fork();
        getPlaceBLL().getCafesAround(this::_fetchPOIsCallback, this::onError);
    }
}
