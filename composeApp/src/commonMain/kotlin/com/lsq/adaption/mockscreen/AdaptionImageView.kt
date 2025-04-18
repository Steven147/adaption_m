package com.lsq.adaption.mockscreen

import adaption_m.composeapp.generated.resources.Res
import adaption_m.composeapp.generated.resources.example169
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun AdaptionImageViewPreview(
    paddingValues: PaddingValues = PaddingValues(),
    id: DrawableResource = Res.drawable.example169,
    currentContentScale: ContentScale = ContentScale.Crop,
    foreground: @Composable ()-> Unit = {}
) {
    AdaptionImageView(
        paddingValues = paddingValues,
        id = id,
        currentContentScale = currentContentScale,
        width = 100f,
        height = 300f,
        foreground = foreground
    )
}

@Composable
fun AdaptionImageView(
    paddingValues: PaddingValues,
    id: DrawableResource,
    currentContentScale: ContentScale,
    width: Float?,
    height: Float?,
    foreground: @Composable ()-> Unit
) {
//    var scaleMode by remember { mutableStateOf(ContentScale.Crop) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
        ,
        contentAlignment = Alignment.Center,
    ) {
        val modifier: Modifier
        val contentScale: ContentScale
        if (width != null && height != null && currentContentScale != ContentScale.Crop) {
            contentScale = ContentScale.FillBounds
            modifier = Modifier.width(width.dp).height(height.dp)
        } else {
            contentScale = currentContentScale
            modifier = Modifier.fillMaxSize()
        }

        Image(
            painter = painterResource(id),
            contentDescription = "bg",
            modifier = modifier,
            contentScale = contentScale
        )
        // 在这里放置Scaffold的内容
        foreground()

    }
}