import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.compose4gtk.adw.adwApplication
import io.github.compose4gtk.adw.components.ApplicationWindow
import io.github.compose4gtk.adw.components.HeaderBar
import io.github.compose4gtk.adw.components.StatusPage
import io.github.compose4gtk.gtk.ImageSource
import io.github.compose4gtk.gtk.components.ScaleButton
import io.github.compose4gtk.gtk.components.VerticalBox
import io.github.compose4gtk.modifier.Modifier
import io.github.compose4gtk.modifier.alignment
import io.github.compose4gtk.modifier.cssClasses
import io.github.compose4gtk.useGioResource
import org.gnome.gtk.Align

fun main(args: Array<String>) {
    useGioResource("resources.gresource") {
        adwApplication("my.example.hello-app", args) {
            ApplicationWindow(title = "Scale Button", onClose = ::exitApplication) {
                VerticalBox {
                    HeaderBar(modifier = Modifier.cssClasses("flat"))

                    var scaleValue by remember { mutableDoubleStateOf(25.5) }

                    StatusPage(
                        title = "Scale Button",
                        description = "Button that exposes a scale when pressed",
                    ) {
                        VerticalBox(
                            spacing = 16,
                        ) {
                            ScaleButton(
                                true,
                                icon = ImageSource.Icon("speaker-0-symbolic"),
                                modifier = Modifier.alignment(Align.CENTER),
                            )
                        }
                    }
                }
            }
        }
    }
}
