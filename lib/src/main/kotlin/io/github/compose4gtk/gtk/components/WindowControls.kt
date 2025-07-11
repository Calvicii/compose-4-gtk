package io.github.compose4gtk.gtk.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.compose4gtk.GtkApplier
import io.github.compose4gtk.GtkComposeWidget
import io.github.compose4gtk.LeafComposeNode
import io.github.compose4gtk.modifier.Modifier
import org.gnome.gtk.PackType
import org.gnome.gtk.WindowControls

/**
 * Creates a [org.gnome.gtk.WindowControls] that displays a window's control buttons.
 *
 * @param modifier Compose [Modifier] for layout and styling.
 * @param side Determines where the controls are packed.
 */
@Composable
fun WindowControls(
    modifier: Modifier = Modifier,
    side: PackType = PackType.START,
) {
    ComposeNode<GtkComposeWidget<WindowControls>, GtkApplier>({
        LeafComposeNode(WindowControls.builder().build())
    }) {
        set(modifier) { applyModifier(it) }
        set(side) { widget.side = it }
    }
}
