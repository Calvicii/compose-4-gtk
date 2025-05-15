import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.compose4gtk.adw.application
import io.github.compose4gtk.adw.components.ApplicationWindow
import io.github.compose4gtk.adw.components.HeaderBar
import io.github.compose4gtk.adw.components.StatusPage
import io.github.compose4gtk.gtk.components.RadioButton
import io.github.compose4gtk.gtk.components.VerticalBox
import io.github.compose4gtk.modifier.Modifier
import io.github.compose4gtk.modifier.cssClasses

fun main(args: Array<String>) {
    application("my.example.hello-app", args) {
        ApplicationWindow(title = "Radio Buttons", onClose = ::exitApplication) {
            VerticalBox {
                HeaderBar(modifier = Modifier.cssClasses("flat"))

                StatusPage(
                    title = "Radio Button", description = "Useful for selecting one option out of many"
                ) {
                    VerticalBox {
                        val radioOptions = remember { mutableListOf("Calls", "Missed", "Contacts", "Contacts") }
                        var selected by remember { mutableStateOf(radioOptions.first()) }

                        radioOptions.forEach { option ->
                            RadioButton(active = (selected == option), label = option) {
                                selected = option
                            }
                        }
                    }
                }
            }
        }
    }
}