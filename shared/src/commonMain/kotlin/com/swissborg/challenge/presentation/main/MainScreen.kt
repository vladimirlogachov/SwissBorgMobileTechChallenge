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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.swissborg.challenge.domain.formatter.dailyChangeFormat
import com.swissborg.challenge.domain.formatter.format
import com.swissborg.challenge.domain.formatter.lastPriceFormat
import com.swissborg.challenge.domain.model.TradingPair
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import swissborgmobiletechchallenge.shared.generated.resources.Res
import swissborgmobiletechchallenge.shared.generated.resources.header_item_daily_change
import swissborgmobiletechchallenge.shared.generated.resources.header_item_last_price
import swissborgmobiletechchallenge.shared.generated.resources.header_item_name
import swissborgmobiletechchallenge.shared.generated.resources.search_bar_action_clear
import swissborgmobiletechchallenge.shared.generated.resources.search_bar_action_search

@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun MainScreen(viewModel: MainViewModel = koinViewModel()) {
    val tradingPairs by viewModel.tradingPairs.collectAsState(initial = emptyList())
    var query by remember { mutableStateOf(value = "") }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CryptoSearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                query = query,
                onQueryChange = { q -> query = q },
                onSearch = { q -> },
            )
        },
    ) { paddingValues ->
        TradingPairsList(
            modifier = Modifier.padding(paddingValues = paddingValues),
            tradingPairs = tradingPairs,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CryptoSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    softwareKeyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
) = SearchBar(
    modifier = modifier,
    query = query,
    onQueryChange = onQueryChange,
    onSearch = {
        softwareKeyboardController?.hide()
        onSearch(query)
    },
    active = false,
    onActiveChange = { },
    placeholder = {
        Text(text = stringResource(resource = Res.string.search_bar_action_search))
    },
    leadingIcon = {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = stringResource(resource = Res.string.search_bar_action_search),
        )
    },
    trailingIcon = {
        AnimatedVisibility(
            visible = query.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            IconButton(onClick = { onQueryChange("") }) {
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
        modifier = modifier.padding(paddingValues = contentPadding),
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
                color = if (tradingPair.dailyChange.signum() > 0) {
                    Color.Green
                } else {
                    Color.Red
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
