package com.example.core.utils.common_components

//
//@Composable
//fun MovieList(
//    items: List<Movie>,
//    onItemClick: (Int) -> Unit,
//    isLoadingNextPage: Boolean,
//    onLoadNextPage: () -> Unit,
//) {
//    LazyVerticalStaggeredGrid(
//        columns = StaggeredGridCells.Fixed(2),
//        verticalItemSpacing = 12.dp,
//        horizontalArrangement = Arrangement.spacedBy(12.dp),
//        contentPadding = PaddingValues(12.dp)
//    ) {
//        itemsIndexed(items) { index, movie ->
////            ListItem(
////                headlineContent = {  Text(movie.title)  },
////                supportingContent = { Text(movie.releaseYear ?: "-") },
////                modifier = Modifier.clickable { onItemClick(movie.id) }
////            )
//            MovieCard(
//                title = movie.title,
//                posterUrl = movie.posterUrl,
//                releaseYear = movie.releaseYear,
//                onClick = { onItemClick(movie.id) }
//            )
//
//            if (index == items.lastIndex && !isLoadingNextPage) {
//                LaunchedEffect(Unit) {
//                    onLoadNextPage()
//                }
//            }
//        }
//        if (isLoadingNextPage) {
//            item {
//                LoadingView()
//            }
//        }
//    }
//}