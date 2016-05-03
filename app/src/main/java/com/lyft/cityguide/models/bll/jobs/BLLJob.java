package com.lyft.cityguide.models.bll.jobs;

import com.lyft.cityguide.models.bll.BLLErrors;
import com.lyft.cityguide.services.ServiceErrors;

import rx.Subscriber;

/**
 * BLLJob
 * <p>
 */
abstract class BLLJob {
    <T> void handleError(Throwable e, Subscriber<T> subscriber) {
        if (e instanceof ServiceErrors.NoConnection) {
            subscriber.onError(new BLLErrors.NoConnection());
        } else if (e instanceof ServiceErrors.ServerError) {
            subscriber.onError(new BLLErrors.ServiceError());
        } else {
            subscriber.onError(e);
        }
    }
}
