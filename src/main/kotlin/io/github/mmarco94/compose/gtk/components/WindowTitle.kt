package io.github.mmarco94.compose.gtk.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.GtkComposeNode
import io.github.mmarco94.compose.LeafComposeNode
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.adw.WindowTitle

@Composable
fun WindowTitle(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
) {
    ComposeNode<GtkComposeNode<WindowTitle>, GtkApplier>({
        LeafComposeNode(WindowTitle.builder().build())
    }) {
        set(modifier) { applyModifier(it) }
        set(title) { this.gObject.title = it }
        set(subtitle) { this.gObject.subtitle = it }
    }
}