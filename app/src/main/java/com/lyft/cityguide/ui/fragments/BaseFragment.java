package com.lyft.cityguide.ui.fragments;

import android.app.Fragment;

import com.lyft.cityguide.models.bll.BLLFactory;
import com.lyft.cityguide.models.bll.interfaces.IPlaceBLL;

/**
 * @class BaseFragment
 * @brief
 */
public class BaseFragment extends Fragment {
    IPlaceBLL getPlaceBLL() {
        return BLLFactory.place(getActivity());
    }

    void onError(String message) {

    }
}
