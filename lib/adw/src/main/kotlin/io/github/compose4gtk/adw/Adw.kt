package io.github.compose4gtk.adw

import androidx.compose.runtime.Composable
import io.github.compose4gtk.ApplicationScope
import io.github.compose4gtk.adw.components.ApplicationWindow
import io.github.compose4gtk.initializeApplication
import org.gnome.adw.Application
import org.gnome.gio.ApplicationFlags

@Deprecated(
    "Use io.github.compose4gtk.adw.adwApplication instead",
    replaceWith = ReplaceWith(
        expression = "io.github.compose4gtk.adw.adwApplication(appId, args) { content() }",
        imports = [".io.github.compose4gtk.adw.adwApplication"],
    ),
)
fun application(
    appId: String,
    args: Array<String>,
    content: @Composable ApplicationScope.() -> Unit,
) = adwApplication(appId, args, content = content)

/**
 * This is the entry point of LibAdwaita applications.
 *
 * This will start an application. [ApplicationWindow] can be added inside the [content] lambda.
 *
 * @param appId the GTK io.github.compose4gtk.adw.application id. If not null, it must be valid, see [Application.idIsValid].
 * @param args the io.github.compose4gtk.adw.application arguments. Usually the same as the ones in your `main`. See [Application.run].
 * @param flags the flags used when creating the io.github.compose4gtk.adw.application. See [ApplicationFlags].
 * @param content the lambda where your io.github.compose4gtk.adw.application is defined. You can start by adding an [ApplicationWindow].
 */
fun adwApplication(
    appId: String?,
    args: Array<String>,
    flags: Set<ApplicationFlags> = setOf(ApplicationFlags.DEFAULT_FLAGS),
    content: @Composable ApplicationScope.() -> Unit,
) {
    val app = Application(appId, flags)
    app.initializeApplication(args, content)
}
