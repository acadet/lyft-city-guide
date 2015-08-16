package com.lyft.cityguide.ui.events;

/**
 * @class MessageEvent
 * @brief
 */
abstract class MessageEvent {
    private String _message;

    MessageEvent(String message) {
        _message = message;
    }

    public String getMessage() {
        return _message;
    }
}
