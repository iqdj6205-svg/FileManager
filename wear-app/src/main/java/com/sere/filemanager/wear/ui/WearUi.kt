package com.sere.filemanager.wear.ui

import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListScope
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Text
import com.sere.filemanager.core.ui.DesignTokens
import com.sere.filemanager.core.ui.FileManagerText
import kotlinx.coroutines.launch

@Composable
fun WearRotaryList(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = DesignTokens.WatchEdgePadding, vertical = 12.dp),
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: ScalingLazyListScope.() -> Unit,
) {
    val state = rememberScalingLazyListState()
    val requester = remember { FocusRequester() }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) { requester.requestFocus() }
    // ScalingLazyColumn provides the native Wear motion: the centered item is largest,
    // while items approaching the top and bottom edges shrink and fade.
    ScalingLazyColumn(
        state = state,
        modifier = modifier.fillMaxSize().focusRequester(requester).focusable().onRotaryScrollEvent {
            val next = (state.centerItemIndex + if (it.verticalScrollPixels > 0) 1 else -1).coerceAtLeast(0)
            scope.launch { state.scrollToItem(next) }
            true
        },
        contentPadding = contentPadding,
        horizontalAlignment = horizontalAlignment,
        flingBehavior = ScrollableDefaults.flingBehavior(),
        content = content,
    )
}

fun ScalingLazyListScope.wearTitle(title: String, subtitle: String? = null) {
    item { Text(title, textAlign = TextAlign.Center, maxLines = 1, overflow = TextOverflow.Ellipsis, style = androidx.wear.compose.material.MaterialTheme.typography.title3) }
    if (!subtitle.isNullOrBlank()) item { Text(subtitle, textAlign = TextAlign.Center, maxLines = 1, overflow = TextOverflow.Ellipsis, style = androidx.wear.compose.material.MaterialTheme.typography.caption2) }
}

fun ScalingLazyListScope.wearInfo(text: String?) { if (!text.isNullOrBlank()) item { Text(text, textAlign = TextAlign.Center, maxLines = 2, overflow = TextOverflow.Ellipsis, style = androidx.wear.compose.material.MaterialTheme.typography.caption2) } }
fun ScalingLazyListScope.wearLoading(isLoading: Boolean) { if (isLoading) item { Text(FileManagerText.Loading, textAlign = TextAlign.Center, style = androidx.wear.compose.material.MaterialTheme.typography.caption2) } }
fun ScalingLazyListScope.wearEmpty(show: Boolean, text: String = FileManagerText.Empty) { if (show) item { Text(text, textAlign = TextAlign.Center, maxLines = 2, overflow = TextOverflow.Ellipsis, style = androidx.wear.compose.material.MaterialTheme.typography.caption2) } }
fun ScalingLazyListScope.wearError(text: String?) { if (!text.isNullOrBlank()) item { Text(text, textAlign = TextAlign.Center, color = androidx.wear.compose.material.MaterialTheme.colors.error, maxLines = 2, overflow = TextOverflow.Ellipsis, style = androidx.wear.compose.material.MaterialTheme.typography.caption2) } }

fun ScalingLazyListScope.wearPrimaryAction(label: String, onClick: () -> Unit) { item { Chip(label = { Text(label, maxLines = 1, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center) }, onClick = onClick, modifier = Modifier.fillMaxWidth(DesignTokens.WatchControlWidthFraction).height(DesignTokens.WatchChipHeight)) } }
fun ScalingLazyListScope.wearSecondaryAction(label: String, onClick: () -> Unit) { item { Chip(label = { Text(label, maxLines = 1, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center, style = androidx.wear.compose.material.MaterialTheme.typography.caption1) }, onClick = onClick, modifier = Modifier.fillMaxWidth(DesignTokens.WatchControlWidthFraction).height(DesignTokens.WatchChipHeight)) } }
fun ScalingLazyListScope.wearBackAction(onBack: () -> Unit, label: String = FileManagerText.Back) { item { Button(onClick = onBack, modifier = Modifier.fillMaxWidth(DesignTokens.WatchControlWidthFraction).height(36.dp)) { Text(label, maxLines = 1, overflow = TextOverflow.Ellipsis) } } }
