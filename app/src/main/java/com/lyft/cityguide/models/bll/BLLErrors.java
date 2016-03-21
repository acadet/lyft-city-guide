package com.lyft.cityguide.models.bll;

/**
 * BLLErrors
 * <p>
 */
public class BLLErrors {
    private BLLErrors() {
    }

    public static class DisabledLocation extends Throwable {
    }

    public static class NoConnection extends Throwable {
    }

    public static class ServiceError extends Throwable {
    }

    public static class NoMorePOI extends Throwable {

    }
}
