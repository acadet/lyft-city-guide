package com.lyft.cityguide.models.services;

/**
 * ServiceErrors
 * <p>
 */
public class ServiceErrors {
    private ServiceErrors() {
    }

    public static class NoConnection extends Throwable {

    }

    public static class ServerError extends Throwable {

    }
}
