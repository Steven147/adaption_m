package com.lsq.adaption.mockscreen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lsq.adaption.ui.theme.AdaptionTheme

@Composable
@Preview(showBackground = false)
fun PreviewPanel() {
    AdaptionTheme(
        darkTheme = true
    ) {
        Scaffold { paddingValue ->
            // 设置Scaffold的背景图片
//        AdaptionImageView {
////            Text(text = "hi")
//        }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValue),
                contentAlignment = Alignment.Center
            ) {

                Column(
                ) {
                    Row(
                        modifier = Modifier
                            .width(341.dp)
                            .height(44.dp)
                            .border(
                                width = 1.dp, brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFBD5675),
                                        Color(0xFFE2F56D),
                                        Color(0xFFEC7B7B),
                                        Color(0xFF8050CF),
                                        Color(0xFF16B9DC)
//                                     Color(0xFFFF6A97),
//                                     Color(0xFFFF9547),
//                                     Color(0xFFFF4F4F),
//                                     Color(0xFF8E4AFF),
//                                     Color(0xFF00A3FF),
                                    )
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
//                        Text(
//                            text = "I want something else"
//                        )
//                        Image(
//                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
//                            contentDescription = "image description",
//                            contentScale = ContentScale.None
//                        )
                    }

                    Row(modifier = Modifier.height(10.dp)) {

                    }

                    Row(
                        modifier = Modifier
                            .width(341.dp)
                            .height(44.dp)
                            .border(
                                width = 1.dp, brush = Brush.horizontalGradient(
                                    colors = listOf(
//                                    Color(0xFFBD5675),
//                                    Color(0xFFE2F56D),
//                                    Color(0xFFEC7B7B),
//                                    Color(0xFF8050CF),
//                                    Color(0xFF16B9DC)
                                        Color(0xFFFF6A97),
                                        Color(0xFFFF9547),
                                        Color(0xFFFF4F4F),
                                        Color(0xFF8E4AFF),
                                        Color(0xFF00A3FF),
                                    )
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {}
                }
            }
        }
    }
}