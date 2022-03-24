### Simmple MVVM Project using [Giphy Apis](https://developers.giphy.com/) to get random Gif every 10 seconds. also with the ability of searching through Gifs.

---------------------------------------------------------------------

### Architecture :
I used MVVM approach recommended by [developer.android](https://developer.android.com/jetpack/guide) as my project Architecture.

------------------------------------------------------------------------

### Project layers :

+ UI ( responsible for showing data )
+ ViewModel ( UI logic, sends users event and provide data for UI layer )
+ Repository ( responsible for communicating with data sources )

---------------------------------------------------------------------

### Package structure :

+ data (data sources , repository, consts and enums,...)
+ di
+ presentation (fragments and their associated ViewModels, Adapters,... )
+ util

-------------------------------------------------------------------------

### 3rd party libs :
+ Couroutines/Flow/LiveData - Asynchronous programming with observable pattern.
+ Hilt - Dependency injection.
+ Glide - Loading and caching Gifs/Images
+ Retrofit/OkHttp - Api calls and intercepting network layer.
+ Navigation component - Navigating between fragments
+ Truth, Mockito, and Hilt testing - Local Unit Tests and Instrumentation Tests.
+ Espresso - UI Testing
