# Lyft City Guide

My **revised** implementation of the Lyft City Guide exercice. 

Supports:

* Any required functional feature
* Settings
* Google Distance API
* Pull to refresh

## Installation

Create a `SecretApplicationConfiguration` class in `com.lyft.cityguide` with the following body:

```java
public class SecretApplicationConfiguration {
    public final String GOOGLE_API_KEY = "KEY";
}
```

## Architecture

### UI

* Activities
* Adapters
* Components: custom UI components. Here, includes a custom slider and rating bar.
* Events: used by the different event buses.
* No Fragment. Use Scoop from Lyft instead.

### Model

* BLL - Business logic layer. Bridge between UI and other model layers.
* DAO: data access layer. Only saves settings
* Service layer: groups the different used services such as Google Places and Google Distance Matrix

## Libraries

* [AndroidViewAnimations](https://github.com/daimajia/AndroidViewAnimations)
* [ButterKnife](http://jakewharton.github.io/butterknife/)
* [Crouton](https://github.com/keyboardsurfer/Crouton)
* [Dagger 2.0](https://github.com/google/dagger)
* [EventBus](https://github.com/greenrobot/EventBus)
* [JodaTime](http://www.joda.org/joda-time/)
* [RetroFit](http://square.github.io/retrofit/)
* [RetroLambda](https://github.com/orfjackal/retrolambda)
* [RxAndroid](https://github.com/ReactiveX/RxAndroid)
* [RxJava](https://github.com/ReactiveX/RxJava)
* [Scoop](https://github.com/lyft/scoop)
* [SmoothProgressBar](https://github.com/castorflex/SmoothProgressBar)
* [Stream](https://github.com/aNNiMON/Lightweight-Stream-API)
* [Timber](https://github.com/JakeWharton/timber)
