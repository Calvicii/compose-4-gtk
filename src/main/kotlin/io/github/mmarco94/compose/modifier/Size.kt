package io.github.mmarco94.compose.modifier

fun Modifier.sizeRequest(
    width: Int = -1,
    height: Int = -1,
) = combine(
    apply = {
        it.setSizeRequest(width, height)
    },
    undo = {
        it.setSizeRequest(-1, -1)
    }
)
