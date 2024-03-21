# Advanced Options

## Create a Web Fragment

### Create the TurboWebFragment class

You'll need at least one web Fragment that will serve as a destination for urls that display web content in your app.

A web Fragment is straightforward and needs to implement the [`TurboWebFragment`](../turbo/src/main/kotlin/dev/hotwire/turbo/fragments/TurboWebFragment.kt) abstract class. This abstract class implements the [`TurboWebFragmentCallback`](../turbo/src/main/kotlin/dev/hotwire/turbo/fragments/TurboWebFragmentCallback.kt) interface, which provides a number of functions available to customize your Fragment.

You'll need to annotate each Fragment in your app with a `@TurboNavGraphDestination` annotation with a URI of your own scheme. This URI is used by the library to build an internal navigation graph and map url path patterns to the destination Fragment with the corresponding URI. See the [Path Configuration documentation](PATH-CONFIGURATION.md) to learn how to map url paths to destination Fragments.

In its simplest form, your web Fragment will look like:

**`WebFragment.kt`:**
```kotlin
@TurboNavGraphDestination(uri = "turbo://fragment/web")
class WebFragment : TurboWebFragment()
```

The library automatically inflates a default `R.layout.turbo_fragment_web` layout to host a `TurboView`. If you'd like to create your own custom layout for your web Fragment, you can override the `onCreateView()` function and inflate your own layout. See the demo [`WebHomeFragment`](../demo/src/main/kotlin/dev/hotwire/turbo/demo/features/web/WebHomeFragment.kt) as an example of of providing your own layout.

You can also provide your own custom progress view or error view by overriding the `createProgressView()` and `createErrorView()` functions in your web Fragment.

Refer to demo [`WebFragment`](../demo/src/main/kotlin/dev/hotwire/turbo/demo/features/web/WebFragment.kt) as an example.

## Create a Native Fragment
You don't need to rely on your web app for every screen in your app. Elevating destinations that would benefit from a higher fidelity experience to fully-native is often a great idea. For example, here's how you would create a fully native image viewer fragment:

### Create the TurboFragment layout resource
You need to create a layout resource file that your native `TurboFragment` will use as its content view.

In its simplest form, your Fragment layout file will look like:

**`res/layout/fragment_image_viewer.xml`:**
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <!-- Your layout views here -->

</androidx.constraintlayout.widget.ConstraintLayout>
```

Refer to the demo [`fragment_image_viewer.xml`](../demo/src/main/res/layout/fragment_image_viewer.xml) for an example.

### Create the TurboFragment class
A native Fragment is straightforward and needs to implement the [`TurboFragment`](../turbo/src/main/kotlin/dev/hotwire/turbo/fragments/TurboFragment.kt) abstract class.

You'll need to annotate each Fragment in your app with a `@TurboNavGraphDestination` annotation with a URI of your own scheme. This URI is used by the library to build an internal navigation graph and map url path patterns to the destination Fragment with the corresponding URI. See the [Path Configuration guide](PATH-CONFIGURATION.md) to learn how to map url paths to destination Fragments.

In its simplest form, your native Fragment will look like:

**`ImageViewerFragment.kt`:**
```kotlin
@TurboNavGraphDestination(uri = "turbo://fragment/image_viewer")
class ImageViewerFragment : TurboFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_image_viewer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadImage(view)
    }

    private fun loadImage(view: View) {
        // Load the image into the view using the `location`
    }
}
```

Don't forget to register the `ImageViewerFragment` class in your app's `TurboSessionNavHostFragment` as a Fragment destination.

Refer to demo [`ImageViewerFragment`](../demo/src/main/kotlin/dev/hotwire/turbo/demo/features/imageviewer/ImageViewerFragment.kt) as an example.

## Display Bottom Sheet Dialogs
Fragment destinations can be dislplayed as bottom sheet dialogs. These are transitional, modal Fragments that can be easily dismissed. Bottom sheet Fragments can be web or native.

### Create a Web Bottom Sheet Fragment
A web bottom sheet Fragment is straightforward and needs to implement the [`TurboWebBottomSheetDialogFragment`](../turbo/src/main/kotlin/dev/hotwire/turbo/fragments/TurboWebBottomSheetDialogFragment.kt) abstract class. This abstract class implements the [`TurboWebFragmentCallback`](../turbo/src/main/kotlin/dev/hotwire/turbo/fragments/TurboWebFragmentCallback.kt) interface, which provides a number of functions available to customize your Fragment.

In its simplest form, your web bottom sheet Fragment will look like:

**`WebBottomSheetFragment.kt`:**
```kotlin
@TurboNavGraphDestination(uri = "turbo://fragment/web/modal/sheet")
class WebBottomSheetFragment : TurboWebBottomSheetDialogFragment()
```
The library automatically inflates a default `R.layout.turbo_fragment_web_bottom_sheet` layout to host a `TurboView`. If you'd like to create your own custom layout for your web bottom sheet Fragment, you can override the `onCreateView()` function and inflate your own layout.

You can also provide your own custom progress view or error view by overriding the `createProgressView()` and `createErrorView()` functions in your web Fragment.

Refer to demo [`WebBottomSheetFragment`](../demo/src/main/kotlin/dev/hotwire/turbo/demo/features/web/WebBottomSheetFragment.kt) as an example.

## Fragment Transition Animations
The transition animations when navigating between Fragments can be fully customized. To do this, override the `TurboNavDestination.getNavigationOptions()` interface function (available in all Fragment destinations). Place your custom XML animation resources in the `/res/anim` directory and provide these animations using the AndroidX [`NavOptions`](https://developer.android.com/reference/androidx/navigation/NavOptions) DSL. An example looks like:

```kotlin
override fun getNavigationOptions(
    newLocation: String,
    newPathProperties: TurboPathConfigurationProperties
): NavOptions {
    return navOptions {
        anim {
            enter = R.anim.custom_anim_enter
            exit = R.anim.custom_anim_exit
            popEnter = R.anim.custom_anim_pop_enter
            popExit = R.anim.custom_anim_pop_exit
        }
    }
}
```

Refer to the demo [`NavDestination.kt`](../demo/src/main/kotlin/dev/hotwire/turbo/demo/base/NavDestination.kt) as an example.

## Using Multiple Activities
You may encounter situations where a truly single-`Activity` app may not be feasible. For example, you may need an `Activity` for logged-out state and a separate `Activity` for logged-in state.

In such cases, you need to create an additional `Activity` that also implements the `TurboActivity` interface. You will need to be sure to register each `Activity` by calling [`TurboSessionNavHostFragment.registeredActivities()`](../turbo/src/main/kotlin/dev/hotwire/turbo/session/TurboSessionNavHostFragment.kt) so that you can navigate between them.

## Enable Debug Logging
During development, you may want to see what `turbo-android` is doing behind the scenes. To enable debug logging, call `Turbo.config.debugLoggingEnabled = true`. Debug logging should always be disabled in your production app. For example:

```kotlin
if (BuildConfig.DEBUG) {
    Turbo.config.debugLoggingEnabled = true
}
```
