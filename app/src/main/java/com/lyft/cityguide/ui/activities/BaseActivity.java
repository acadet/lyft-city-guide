package com.lyft.cityguide.ui.activities;

import android.R;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

import com.lyft.cityguide.models.bll.BLLFactory;
import com.lyft.cityguide.models.bll.interfaces.IPlaceBLL;

import java.util.Map;

/**
 * @class BaseActivity
 * @brief
 */
public abstract class BaseActivity extends Activity {

    IPlaceBLL getPlaceBLL() {
        return BLLFactory.place(getApplicationContext());
    }

    void setFragment(int resourceId, Fragment fragment) {
        getFragmentManager()
            .beginTransaction()
            .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
            .replace(resourceId, fragment)
            .addToBackStack(fragment.getClass().getSimpleName())
            .commit();
    }

    void setFragments(Map<Integer, Fragment> fragments, boolean keepInStack) {
        FragmentTransaction t = getFragmentManager().beginTransaction();

        t.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        for (Map.Entry<Integer, Fragment> e : fragments.entrySet()) {
            t.replace(e.getKey().intValue(), e.getValue());
        }

        if (keepInStack) {
            t.addToBackStack(null);
        }

        t.commit();
    }

    void setFragments(Map<Integer, Fragment> fragments) {
        setFragments(fragments, true);
    }
}
