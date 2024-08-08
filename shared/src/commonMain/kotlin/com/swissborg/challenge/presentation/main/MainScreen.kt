package com.swissborg.challenge.presentation.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.swissborg.challenge.domain.formatter.dailyChangeFormat
import com.swissborg.challenge.domain.formatter.format
import com.swissborg.challenge.domain.formatter.lastPriceFormat
import com.swissborg.challenge.domain.model.ConnectionState
import com.swissborg.challenge.domain.model.TradingPair
import com.swissborg.challenge.presentation.theme.DarkGreen
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import swissborgmobiletechchallenge.shared.generated.resources.Res
import swissborgmobiletechchallenge.shared.generated.resources.connection_lost
import swissborgmobiletechchallenge.shared.generated.resources.header_item_daily_change
import swissborgmobiletechchallenge.shared.generated.resources.header_item_last_price
import swissborgmobiletechchallenge.shared.generated.resources.header_item_name
import swissborgmobiletechchallenge.shared.generated.resources.search_bar_action_clear
import swissborgmobiletechchallenge.shared.generated.resources.search_bar_action_filter
import swissborgmobiletechchallenge.shared.generated.resources.search_bar_hint

@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun MainScreen(
    viewModel: MainViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val state by viewModel.state.collectAsState()
    var showProgress by remember { mutableStateOf(value = false) }

    ScreenContent(
        onFilter = { query ->
            MainIntent.FilterTradingPairs(query = query).run(viewModel::submitIntent)
        },
        state = state,
        showProgress = showProgress,
        snackbarHostState = snackbarHostState,
    )

    LaunchedEffect(key1 = viewModel) {
        viewModel.action.collect { action ->
            when (action) {
                MainAction.ShowProgress -> showProgress = true
                MainAction.HideProgress -> showProgress = false
                is MainAction.ShowError -> snackbarHostState.showSnackbar(message = action.message)
            }
        }
    }

    RefreshDisposableEffect(
        onStartRefresh = { MainIntent.StartPeriodicRefresh.run(viewModel::submitIntent) },
        onStopRefresh = { MainIntent.StopPeriodicRefresh.run(viewModel::submitIntent) },
    )
}

@Composable
private fun ScreenContent(
    onFilter: (String) -> Unit,
    state: MainState,
    showProgress: Boolean,
    snackbarHostState: SnackbarHostState,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            FilterBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onFilter = onFilter,
            )
        },
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding(),
                hostState = snackbarHostState,
            )
        },
        contentWindowInsets = WindowInsets.ime,
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues = paddingValues)) {
            AnimatedVisibility(visible = state.connectionState is ConnectionState.Disconnected) {
                ConnectionLost(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            Box(contentAlignment = Alignment.BottomCenter) {
                Header(modifier = Modifier.fillMaxWidth())
                if (showProgress) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height = 1.dp)
                    )
                }
            }
            TradingPairsList(
                modifier = Modifier.weight(weight = 1f),
                tradingPairs = state.tradingPairs,
            )
        }
    }
}

@Composable
private fun ConnectionLost(modifier: Modifier = Modifier) = Surface(
    modifier = modifier.height(intrinsicSize = IntrinsicSize.Min),
    color = MaterialTheme.colorScheme.errorContainer,
    shape = MaterialTheme.shapes.medium,
) {
    Row(
        modifier = Modifier
            .padding(all = 16.dp)
            .fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.WifiOff,
            contentDescription = stringResource(resource = Res.string.connection_lost),
        )
        Text(text = stringResource(resource = Res.string.connection_lost))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterBar(
    onFilter: (String) -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    focusManager: FocusManager = LocalFocusManager.current,
    softwareKeyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
) {
    var query by rememberSaveable { mutableStateOf(value = "") }
    SearchBar(
        modifier = modifier,
        query = query,
        onQueryChange = { q -> query = q; onFilter(query) },
        onSearch = { softwareKeyboardController?.hide(); focusManager.clearFocus() },
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

@Composable
private fun Header(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(space = 16.dp),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
) = ProvideTextStyle(value = MaterialTheme.typography.labelMedium) {
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
private fun TradingPairsList(
    tradingPairs: List<TradingPair>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(space = 8.dp),
) = LazyColumn(
    modifier = modifier,
    contentPadding = contentPadding,
    verticalArrangement = verticalArrangement,
) {
    items(items = tradingPairs, key = { item -> item.symbol.key }) { tradingPair ->
        TradingPairListItem(tradingPair = tradingPair)
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
            text = buildAnnotatedString {
                val formattedPair = tradingPair.symbol.format()
                append(text = formattedPair.substringBefore(delimiter = "/"))
                withStyle(
                    style = MaterialTheme.typography.labelMedium.toSpanStyle()
                        .copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                ) { append(text = "/${formattedPair.substringAfter(delimiter = "/")}") }
            },
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
            .padding(all = 4.dp),
    ) {
        Text(
            modifier = Modifier.align(alignment = Alignment.Center),
            text = tradingPair.dailyChangeRelative.dailyChangeFormat(),
            style = MaterialTheme.typography.bodySmall,
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
