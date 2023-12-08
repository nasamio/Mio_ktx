//package com.mio.compose.ui
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.navigationBarsPadding
//import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemScope
//import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
//import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
//import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
//import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan.Companion.FullLine
//import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
//import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.dp
//
////@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
//@Composable
//fun <T> SwipeStaggeredGridRefresh(
//    limitRefresh: Boolean = true,
//    limitLoadMore: Boolean = true,
//    refreshingUI: @Composable () -> Unit = {},
//    loadMoreUI: @Composable () -> Unit,
//    emptyLayout: @Composable () -> Unit,
//    contentUI: @Composable () -> Unit = {},
//    items: List<T>?,
//    refreshing: Boolean,
//    onRefresh: () -> Unit,
//    loading: Boolean,
//    onLoad: () -> Unit,
//    modifier: Modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
//    listState: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
//    contentPadding: PaddingValues = PaddingValues(
//        start = 10.dp,
//        top = 0.dp,
//        end = 10.dp,
//        bottom = 0.dp
//    ),
//    verticalItemSpacing: Dp = 10.dp,
//    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(10.dp),
//    key: ((index: Int, item: T) -> Any)? = null,
//    contentType: (index: Int, item: T) -> Any? = { _, _ -> null },
//    itemContent: @Composable LazyStaggeredGridItemScope.(index: Int, item: T) -> Unit
//) {
//    val pullRefreshState = rememberPullRefreshState(refreshing, { onRefresh() })
//
//    if (items.isNullOrEmpty()) {
//        if (!refreshing) {
//            emptyLayout()
//        }
//    } else {
//        Box(Modifier.pullRefresh(pullRefreshState)) {
//            LazyVerticalStaggeredGrid(
//                modifier = modifier.navigationBarsPadding(),
//                state = listState,
//                columns = StaggeredGridCells.Fixed(2),
//                contentPadding = contentPadding,
//                verticalItemSpacing = verticalItemSpacing,
//                horizontalArrangement = horizontalArrangement,
//            ) {
//                item {
//                    contentUI()
//                }
//
//                itemsIndexed(items, key = key, contentType = contentType) { index, item ->
//                    itemContent(index, item)
//
//                    LaunchedEffect(items.size) {
//                        if (!refreshing && limitLoadMore && items.size - index < 2) {
//                            onLoad()
//                        }
//                    }
//                }
//
//                if (!refreshing && loading && limitLoadMore) {
//                    item(span = FullLine) {
//                        loadMoreUI()
//                    }
//                }
//            }
//
//            if (limitRefresh && !loading) {
//                PullRefreshIndicator(
//                    refreshing = refreshing,
//                    state = pullRefreshState,
//                    backgroundColor = MaterialTheme.colorScheme.background,
//                    contentColor = MaterialTheme.colorScheme.primary,
//                    modifier = Modifier.align(Alignment.TopCenter),
//                )
//            }
//        }
//    }
//}
