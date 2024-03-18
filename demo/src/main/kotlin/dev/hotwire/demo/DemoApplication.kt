package dev.hotwire.demo

import android.app.Application
import dev.hotwire.core.BuildConfig
import dev.hotwire.core.bridge.BridgeComponentFactory
import dev.hotwire.core.bridge.KotlinXJsonConverter
import dev.hotwire.core.config.Hotwire
import dev.hotwire.core.turbo.config.TurboPathConfiguration
import dev.hotwire.demo.bridge.FormComponent
import dev.hotwire.demo.bridge.MenuComponent
import dev.hotwire.demo.bridge.OverflowMenuComponent
import dev.hotwire.demo.features.imageviewer.ImageViewerFragment
import dev.hotwire.demo.features.numbers.NumberBottomSheetFragment
import dev.hotwire.demo.features.numbers.NumbersFragment
import dev.hotwire.demo.features.web.WebBottomSheetFragment
import dev.hotwire.demo.features.web.WebFragment
import dev.hotwire.demo.features.web.WebHomeFragment
import dev.hotwire.demo.features.web.WebModalFragment

class DemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        configureApp()
    }

    private fun configureApp() {
        // Register fragment destinations
        Hotwire.registerFragmentDestinations(listOf(
            WebFragment::class,
            WebHomeFragment::class,
            WebModalFragment::class,
            WebBottomSheetFragment::class,
            NumbersFragment::class,
            NumberBottomSheetFragment::class,
            ImageViewerFragment::class
        ))

        // Register bridge components
        Hotwire.registerBridgeComponentFactories(listOf(
            BridgeComponentFactory("form", ::FormComponent),
            BridgeComponentFactory("menu", ::MenuComponent),
            BridgeComponentFactory("overflow-menu", ::OverflowMenuComponent)
        ))

        // Set configuration options
        Hotwire.config.jsonConverter = KotlinXJsonConverter()
        Hotwire.config.userAgent = "Hotwire Demo; ${Hotwire.config.userAgentSubstring()}"
        Hotwire.config.pathConfigurationLocation = TurboPathConfiguration.Location(
            assetFilePath = "json/configuration.json"
        )

        // Configure debugging
        Hotwire.config.debugLoggingEnabled = BuildConfig.DEBUG
        Hotwire.config.webViewDebuggingEnabled = BuildConfig.DEBUG
    }
}