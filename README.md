# Lyft City Guide

My **revised\*\*2** implementation of the Lyft City Guide exercice. 

Supports:

* Any required functional feature
* Settings
* Google Distance Matrix API
* Pull to refresh

## Architecture

### UI

* Activities
* Adapters
* Components: custom UI components. Here, includes a custom slider and rating bar.
* No Fragment. Using Scoop from Lyft instead.

### Model

* Domain: core domain of the application.
* BLL - Business logic layer. Bridge between UI and other model layers.
* DAO: data access layer. Only saves settings
* Service layer: groups the different used services such as Google Places and Google Distance Matrix

## Libraries

* [AndroidViewAnimations](https://github.com/daimajia/AndroidViewAnimations)
* [ButterKnife](http://jakewharton.github.io/butterknife/)
* [Dagger 2.0](https://github.com/google/dagger)
* [JodaTime](http://www.joda.org/joda-time/)
* [RetroFit 1.9](http://square.github.io/retrofit/)
* [RetroLambda](https://github.com/orfjackal/retrolambda)
* [RxAndroid](https://github.com/ReactiveX/RxAndroid)
* [RxJava](https://github.com/ReactiveX/RxJava)
* [Scoop](https://github.com/lyft/scoop)
* [SmoothProgressBar](https://github.com/castorflex/SmoothProgressBar)
* [Stream](https://github.com/aNNiMON/Lightweight-Stream-API)
* [Timber](https://github.com/JakeWharton/timber)
