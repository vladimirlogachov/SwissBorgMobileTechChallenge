package com.swissborg.challenge.presentation.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.swissborg.challenge.domain.formatter.dailyChangeFormat
import com.swissborg.challenge.domain.formatter.format
import com.swissborg.challenge.domain.formatter.lastPriceFormat
import com.swissborg.challenge.domain.model.TradingPair
import com.swissborg.challenge.presentation.theme.DarkGreen
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import swissborgmobiletechchallenge.shared.generated.resources.Res
import swissborgmobiletechchallenge.shared.generated.resources.header_item_daily_change
import swissborgmobiletechchallenge.shared.generated.resources.header_item_last_price
import swissborgmobiletechchallenge.shared.generated.resources.header_item_name
import swissborgmobiletechchallenge.shared.generated.resources.search_bar_action_clear
import swissborgmobiletechchallenge.shared.generated.resources.search_bar_action_filter
import swissborgmobiletechchallenge.shared.generated.resources.search_bar_hint

@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun MainScreen(viewModel: MainViewModel = koinViewModel()) {
    val tradingPairs by viewModel.tradingPairs.collectAsState(initial = emptyList())

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            FilterBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onFilter = { query ->
                    viewModel.intents.trySend(element = MainIntent.FilterTradingPairs(query = query))
                },
            )
        },
        contentWindowInsets = WindowInsets.ime,
    ) { paddingValues ->
        TradingPairsList(
            modifier = Modifier.padding(paddingValues = paddingValues),
            tradingPairs = tradingPairs,
        )
    }

    RefreshDisposableEffect(
        onStartRefresh = {
            viewModel.intents.trySend(element = MainIntent.StartPeriodicRefresh)
        },
        onStopRefresh = {
            viewModel.intents.trySend(element = MainIntent.StopPeriodicRefresh)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterBar(
    onFilter: (String) -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    softwareKeyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
) {
    var query by rememberSaveable { mutableStateOf(value = "") }
    SearchBar(
        modifier = modifier,
        query = query,
        onQueryChange = { q -> query = q; },
        onSearch = {
            softwareKeyboardController?.hide()
            onFilter(query)
        },
        active = false,
        onActiveChange = { /* no need to expand the bar */ },
        placeholder = { Text(text = stringResource(resource = Res.string.search_bar_hint)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = stringResource(resource = Res.string.search_bar_action_filter),
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = query.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                IconButton(onClick = { query = ""; onFilter(query) }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(resource = Res.string.search_bar_action_clear),
                    )
                }
            }
        },
        interactionSource = interactionSource,
        content = {}
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TradingPairsList(
    tradingPairs: List<TradingPair>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(all = 16.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(space = 8.dp),
) = LazyColumn(
    modifier = modifier,
    contentPadding = contentPadding,
    verticalArrangement = verticalArrangement,
) {
    stickyHeader {
        Header(modifier = Modifier.fillMaxWidth())
    }
    items(items = tradingPairs, key = { item -> item.symbol.key }) { tradingPair ->
        TradingPairListItem(tradingPair = tradingPair)
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(vertical = 4.dp),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(space = 16.dp),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
) = ProvideTextStyle(value = MaterialTheme.typography.labelLarge) {
    Row(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(paddingValues = contentPadding),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
    ) {
        Box(modifier = Modifier.weight(weight = 2f)) {
            Text(
                modifier = Modifier.align(alignment = Alignment.CenterStart),
                text = stringResource(resource = Res.string.header_item_name),
            )
        }
        Box(modifier = Modifier.weight(weight = 2f)) {
            Text(
                modifier = Modifier.align(alignment = Alignment.CenterEnd),
                text = stringResource(resource = Res.string.header_item_last_price),
            )
        }
        Box(modifier = Modifier.weight(weight = 1f)) {
            Text(
                modifier = Modifier.align(alignment = Alignment.CenterEnd),
                text = stringResource(resource = Res.string.header_item_daily_change),
            )
        }
    }
}

@Composable
private fun TradingPairListItem(
    tradingPair: TradingPair,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(vertical = 4.dp),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(space = 16.dp),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
) = Row(
    modifier = modifier.padding(paddingValues = contentPadding),
    horizontalArrangement = horizontalArrangement,
    verticalAlignment = verticalAlignment,
) {
    Box(modifier = Modifier.weight(weight = 2f)) {
        Text(
            modifier = Modifier.align(alignment = Alignment.CenterStart),
            text = tradingPair.symbol.format(),
            fontWeight = FontWeight.SemiBold,
        )
    }
    Box(modifier = Modifier.weight(weight = 2f)) {
        Column(
            modifier = Modifier.align(alignment = Alignment.CenterEnd),
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                text = tradingPair.lastPrice.toStringExpanded(),
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = tradingPair.lastPrice.lastPriceFormat(),
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
    Box(
        modifier = Modifier
            .weight(weight = 1f)
            .background(
                color = when (tradingPair.dailyChange.signum()) {
                    1 -> Color.DarkGreen
                    -1 -> Color.Red
                    else -> Color.LightGray
                },
                shape = MaterialTheme.shapes.small,
            )
            .padding(vertical = 4.dp),
    ) {
        Text(
            modifier = Modifier.align(alignment = Alignment.Center),
            text = tradingPair.dailyChangeRelative.dailyChangeFormat(),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = Color.White,
        )
    }
}

@Composable
private fun RefreshDisposableEffect(
    onStartRefresh: () -> Unit,
    onStopRefresh: () -> Unit,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> onStartRefresh()
                Lifecycle.Event.ON_PAUSE -> onStopRefresh()
                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer = observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer = observer)
        }
    }
}
