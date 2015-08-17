# Lyft City Guide

My implementation of the Lyft City Guide exercice. 

Supports:

* Any required functional feature
* Settings
* Google Distance API
* Pull to refresh

## Architecture

### UI

* Activities
* Adapters
* Components: custom UI components. Here, includes a custom slider and rating bar.
* Events: Any event raised by the event bus.
* Fragments

### Model

* Beans: aka POJOs
* BLL: the smartest part of the model. Handles any connection with the APIs and engines data if needed. Runs long operations in the background, whenever it is possible.
* DAO: data access layer. Only saves settings

## Libraries

* [AndroidViewAnimations](https://github.com/daimajia/AndroidViewAnimations)
* [ButterKnife](http://jakewharton.github.io/butterknife/)
* [Crouton](https://github.com/keyboardsurfer/Crouton)
* [EventBus](https://github.com/greenrobot/EventBus)
* [JodaTime](http://www.joda.org/joda-time/)
* [RetroFit](http://square.github.io/retrofit/)
* [RetroLambda](https://github.com/orfjackal/retrolambda)
* [SmoothProgressBar](https://github.com/castorflex/SmoothProgressBar)
