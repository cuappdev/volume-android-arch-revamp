package com.example.volume_android_revamp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.volume_android_revamp.AllArticlesQuery
import com.example.volume_android_revamp.R
import com.example.volume_android_revamp.TrendingArticlesQuery
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun displayArticle(article:TrendingArticlesQuery.GetTrendingArticle){
    Scaffold(
            topBar = { TopAppBar(title = {Text(article.publication.name)},
                backgroundColor = Color(R.color.white))  },
        bottomBar = { BottomAppBar(backgroundColor =Color(R.color.white)) {
            AsyncImage(model = article.publication.profileImageURL, contentDescription = null)
            Text("See More")
            Image(painter = painterResource(id = R.drawable.ic_bookmark), contentDescription = null)
            Image(painter = painterResource(id = R.drawable.ic_share), contentDescription = null)
            Image(painter = painterResource(id = R.drawable.ic_shoutout), contentDescription = null)
        }}) {
        val state = rememberWebViewState(url = article.articleURL)
        WebView(state = state)
    }
}