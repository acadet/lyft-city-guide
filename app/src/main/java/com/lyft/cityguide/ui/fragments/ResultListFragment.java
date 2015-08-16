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
import com.lyft.cityguide.ui.adapters.ResultAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @class ResultListFragment
 * @brief
 */
public class ResultListFragment extends BaseFragment {
    private boolean       _isFetchingMore;
    private ResultAdapter _currentAdapter;

    @Bind(R.id.fragment_result_list_no_content)
    TextView _noContent;

    @Bind(R.id.fragment_result_list)
    ListView _list;

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
        fork();
        getPlaceBLL().getBarsAround(
            (pois) -> {
                done();
                if (pois.size() == 0) {
                    _noContent.setVisibility(View.VISIBLE);
                    _list.setVisibility(View.GONE);
                } else {
                    _noContent.setVisibility(View.GONE);
                    _list.setVisibility(View.VISIBLE);
                    _currentAdapter = new ResultAdapter(pois, getActivity(), PlaceType.BAR);
                    _list.setAdapter(_currentAdapter);
                }
            },
            this::onError
        );
    }

    @Override
    public void onPause() {
        super.onPause();

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
}
