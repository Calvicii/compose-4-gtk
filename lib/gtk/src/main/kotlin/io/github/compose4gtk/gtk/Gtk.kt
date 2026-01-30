package io.github.compose4gtk.gtk

import androidx.compose.runtime.Composable
import io.github.compose4gtk.ApplicationScope
import io.github.compose4gtk.initializeApplication
import org.gnome.gio.ApplicationFlags
import kotlin.run

@Deprecated(
    "Use adwApplication instead",
    replaceWith = ReplaceWith(
        expression = "io.github.compose4gtk.gtk.gtkApplication(appId, args) { content() }",
        imports = [".io.github.compose4gtk.gtk.gtkApplication"],
    ),
)
fun application(
    appId: String,
    args: Array<String>,
    content: @Composable ApplicationScope.() -> Unit,
) = gtkApplication(appId, args, content = content)

/**
 * This is the entry point of LibAdwaita applications.
 *
 * This will start an io.github.compose4gtk.gtk.application. [components.GtkApplicationWindow] can be added inside the [content] lambda.
 *
 * @param appId the GTK io.github.compose4gtk.gtk.application id. If not null, it must be valid, see [org.gnome.gio.Application.idIsValid].
 * @param args the io.github.compose4gtk.gtk.application arguments. Usually the same as the ones in your `main`. See [org.gnome.gio.Application.run].
 * @param flags the flags used when creating the io.github.compose4gtk.gtk.application. See [org.gnome.gio.ApplicationFlags].
 * @param content the lambda where your io.github.compose4gtk.gtk.application is defined. You can start by adding a [components.GtkApplicationWindow].
 */
fun gtkApplication(
    appId: String,
    args: Array<String>,
    flags: Set<ApplicationFlags> = setOf(ApplicationFlags.DEFAULT_FLAGS),
    content: @Composable ApplicationScope.() -> Unit,
) {
    val app = org.gnome.gtk.Application(appId, flags)
    app.initializeApplication(args, content)
}
