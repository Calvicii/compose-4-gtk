package io.github.compose4gtk.gtk.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import io.github.compose4gtk.Gtk
import io.github.compose4gtk.GtkApplier
import io.github.compose4gtk.LeafComposeNode
import io.github.compose4gtk.gtk.ImageSource
import io.github.compose4gtk.modifier.Modifier
import io.github.jwharm.javagi.gobject.SignalConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.gnome.gtk.ToggleButton
import org.gnome.gtk.ScaleButton as GtkScaleButton

private class GtkScaleButtonComposeNode(gObject: GtkScaleButton) : LeafComposeNode<GtkScaleButton>(gObject) {
    var onToggle: SignalConnection<ToggleButton.ToggledCallback>? = null
    val onPopup: SignalConnection<GtkScaleButton.PopupCallback>? = null
    val onPopdown: SignalConnection<GtkScaleButton.PopdownCallback>? = null
}

@Composable
fun ScaleButton(
    active: Boolean,
    icon: ImageSource.Icon,
    modifier: Modifier = Modifier,
    onToggle: () -> Unit = {},
) {
    val scope = rememberCoroutineScope { Dispatchers.Gtk }
    var pendingChange by remember { mutableIntStateOf(0) }

    fun popup(scaleButton: GtkScaleButton) {
        scope.launch {
            delay(10)
            scaleButton.emitPopup()
        }
    }

    fun popdown(scaleButton: GtkScaleButton) {
        scope.launch {
            delay(10)
            scaleButton.emitPopdown()
        }
    }

    ComposeNode<GtkScaleButtonComposeNode, GtkApplier>(
        factory = {
            GtkScaleButtonComposeNode(GtkScaleButton())
        },
        update = {
            set(active to pendingChange) {
                this.onPopup?.block()
                this.onPopdown?.block()
                if (active) popup(this.widget) else popdown(this.widget)
                this.onPopup?.unblock()
                this.onPopdown?.unblock()
            }
            set(icon) { this.widget.setIcons(arrayOf(icon.iconName)) }
            set(modifier) { applyModifier(it) }
            set(onToggle) {
                this.onToggle?.disconnect()
                val toggleButton = this.widget.firstChild as ToggleButton
                this.onToggle = toggleButton.onToggled {
                    pendingChange++
                    onToggle()
                }
            }
        },
    )
}