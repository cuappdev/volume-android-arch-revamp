package com.example.volume_android_revamp

import android.content.Context
import android.graphics.Color.red
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.volume_android_revamp.navigation.NavigationItem
import com.example.volume_android_revamp.state.State
import com.example.volume_android_revamp.viewmodels.HomeTabViewModel
import okhttp3.internal.http2.Header
import org.intellij.lang.annotations.JdkConstants

@Composable
fun HomeScreen(homeTabViewModel: HomeTabViewModel){
    val trendingArticleState = homeTabViewModel.trendingArticlesState.collectAsState().value
    val otherArticleState = homeTabViewModel.allArticlesState.collectAsState().value
    LazyColumn() {
        item {
            volumeTitle() }
        trendingArticleState.value?.getTrendingArticles?.let {
            item {
                    when (val trendingArticlesState =
                        homeTabViewModel.trendingArticlesState.collectAsState().value) {
                        is State.Success<TrendingArticlesQuery.Data> ->
                            LazyRow(modifier = Modifier.padding(start = 20.dp)) {
                                trendingArticlesState.value?.getTrendingArticles?.let {
                                    items(it.size) { data ->
                                        trendingArticleItem(data = trendingArticlesState.value.getTrendingArticles[data])
                                    }
                                }
                            }
                        is State.Error<TrendingArticlesQuery.Data> -> Log.d("HomeTab", "EROROROROROROR")
                        is State.Loading<TrendingArticlesQuery.Data> -> Log.d("HomeTab", "loading")
                        is State.Empty<TrendingArticlesQuery.Data> -> Log.d("HomeTab", "empty")
                    }
            }
        }

        item { Text(text = "Following", modifier = Modifier.padding(start = 20.dp)) }
        item {
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.ic_bar_chart_2),
                    contentDescription = null,
                )
                Text(text = "Nothing to see here!")
                Text(text = "Follow some student publications that you are interested in")
                Text(text = "Other Articles")
            }
        }
        otherArticleState.value?.getAllArticles?.let {
            items(it.size) {
                when (val allArticlesState = homeTabViewModel.allArticlesState.collectAsState().value) {
                    is State.Success<AllArticlesQuery.Data> ->
                        otherArticleItem(data = allArticlesState.value?.getAllArticles?.get(it))
                    is State.Error<AllArticlesQuery.Data> -> Log.d("HomeTab", "EROROROROROROR aa")
                    is State.Loading<AllArticlesQuery.Data> -> Log.d("HomeTab", "loading aa")
                    is State.Empty<AllArticlesQuery.Data> -> Log.d("HomeTab", "empty aa")
                }
            }
        }
    }
}

@Composable
fun volumeTitle(){
    Column{
        Image(painter = painterResource(R.drawable.volume_icon), contentDescription = null, modifier = Modifier
            .size(150.dp)
            .padding(start = 20.dp, top = 0.dp, bottom = 40.dp, end = 0.dp))
        Text(text = "The Big Read", modifier = Modifier.padding(start = 20.dp, top = 5.dp))
    }
}

@Composable
fun trendingArticleItem(data: TrendingArticlesQuery.GetTrendingArticle){
    Column(modifier = Modifier.width(204.dp)) {
        AsyncImage(model = data.imageURL, modifier = Modifier
            .height(180.dp)
            .width(180.dp), contentDescription = null, contentScale = ContentScale.Crop)
        Text(text=data.publication.name, overflow = TextOverflow.Clip)
        Text(text = truncateTitle(data.title), softWrap = false, maxLines = 3, overflow = TextOverflow.Ellipsis)
        Text(text = data.date.toString()+" ?? " +data.shoutouts.toString())
    }
}

@Composable
fun otherArticleItem(data: AllArticlesQuery.GetAllArticle?){
    Box(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth()
        .padding(start = 20.dp)) {
        Column {
            data?.publication?.let { Text(text = it.name) }
            if (data != null) {
                Text(text = truncateTitle(data.title))
            }
            if (data != null) {
                Text(text = data.date.toString()+" ?? " +data.shoutouts.toString())
            }
        }
        Box(modifier = Modifier.align(Alignment.CenterEnd)) {
            if (data != null) {
                AsyncImage(model = data.imageURL, modifier = Modifier
                    .height(100.dp)
                    .width(100.dp), contentDescription = null, contentScale = ContentScale.Crop)
            }
        }
    }
}

fun truncateTitle(title: String):String = if (title.length > 57) (title.substring(0, 57) + "...") else title