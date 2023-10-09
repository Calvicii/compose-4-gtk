package io.github.mmarco94.compose.gtk.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import io.github.mmarco94.compose.*
import io.github.mmarco94.compose.GtkApplier
import io.github.mmarco94.compose.GtkComposeNode
import io.github.mmarco94.compose.SingleChildComposeNode
import io.github.mmarco94.compose.VirtualComposeNode
import io.github.mmarco94.compose.modifier.Modifier
import org.gnome.gobject.GObject
import org.gnome.gtk.Overlay
import org.gnome.gtk.Widget

private class OverlaysComposeNode(gObject: Overlay) : GtkComposeNode<Overlay>(gObject) {
    val children = mutableListOf<Widget>()

    override fun add(index: Int, child: GtkComposeNode<GObject>) {
        val overlay = child.gObject as Widget
        val toReinsert = children.drop(index)
        toReinsert.forEach { gObject.removeOverlay(it) }
        this.children.add(index, overlay)
        gObject.addOverlay(overlay)
        toReinsert.forEach { gObject.addOverlay(it) }
    }

    override fun remove(index: Int) {
        gObject.removeOverlay(children.removeAt(index))
    }

    override fun clear() {
        children.forEach { gObject.removeOverlay(it) }
        children.clear()
    }
}


@Composable
fun Overlay(
    mainChild: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    overlays: @Composable () -> Unit,
) {
    ComposeNode<GtkComposeNode<Overlay>, GtkApplier>(
        factory = {
            VirtualComposeNodeContainer(Overlay.builder().build())
        },
        update = {
            set(modifier) { applyModifier(it) }
        },
        content = {
            MainChild {
                mainChild()
            }
            OverlayChildren {
                overlays()
            }
        },
    )
}

@Composable
private fun MainChild(
    content: @Composable () -> Unit,
) {
    ComposeNode<GtkComposeNode<Nothing?>, GtkApplier>(
        factory = {
            VirtualComposeNode<Overlay> { overlay ->
                SingleChildComposeNode(
                    overlay,
                    add = { child = it.gObject as Widget },
                    remove = { child = null },
                )
            }
        },
        update = { },
        content = content,
    )
}

@Composable
private fun OverlayChildren(
    content: @Composable () -> Unit,
) {
    ComposeNode<GtkComposeNode<Nothing?>, GtkApplier>(
        factory = {
            VirtualComposeNode<Overlay> { overlay ->
                OverlaysComposeNode(overlay)
            }
        },
        update = { },
        content = content,
    )
}