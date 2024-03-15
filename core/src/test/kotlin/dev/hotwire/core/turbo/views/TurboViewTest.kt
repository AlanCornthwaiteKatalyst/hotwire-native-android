package dev.hotwire.core.turbo.views

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.FrameLayout.LayoutParams
import androidx.test.core.app.ApplicationProvider
import com.nhaarman.mockito_kotlin.whenever
import dev.hotwire.core.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.R])
class TurboViewTest {
    @Mock private lateinit var webView: WebView
    private lateinit var context: Context
    private lateinit var view: ViewGroup
    private lateinit var turboView: TurboView

    @Before fun setup() {
        MockitoAnnotations.openMocks(this)

        context = ApplicationProvider.getApplicationContext()
        view = LayoutInflater.from(context).inflate(R.layout.turbo_view, null) as ViewGroup
        turboView = view.findViewById(R.id.turbo_view)

        whenever(webView.layoutParams).thenReturn(
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        )
    }

    @Test fun refreshLayoutIsFirstChild() {
        assertThat(turboView.getChildAt(0) is TurboSwipeRefreshLayout).isTrue()
    }

    @Test fun webviewAttachedToRefreshLayout() {
        turboView.attachWebView(webView) {
            // Child at 0 is CircleImageView
            assertThat(turboView.webViewRefresh?.getChildAt(1)).isEqualTo(webView)
        }
    }
}
