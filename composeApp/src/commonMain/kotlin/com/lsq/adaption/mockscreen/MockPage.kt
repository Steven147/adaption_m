package com.lsq.adaption.mockscreen

import adaption_m.composeapp.generated.resources.Res
import adaption_m.composeapp.generated.resources.example11
import adaption_m.composeapp.generated.resources.example169
import adaption_m.composeapp.generated.resources.example916
import adaption_m.composeapp.generated.resources.white_background
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.sharp.KeyboardArrowRight
import androidx.compose.material.icons.sharp.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.lsq.adaption.ScreenSettings
import com.lsq.adaption.ui.PagerTabView
import com.ss.android.ugc.aweme.adaptionmonitor.AdaptionDisplayUtil.getScaleBy9
import com.ss.android.ugc.aweme.adaptionmonitor.AdaptionDisplayUtil.round1
import com.ss.android.ugc.aweme.adaptionmonitor.AdaptionDisplayUtil.round3
import com.ss.android.ugc.aweme.videoadaption.AdaptionMockDataUtil.getMockAdaptionParamsOperator
import com.ss.android.ugc.aweme.videoadaption.BaseAdaptionParamOperator
import com.ss.android.ugc.aweme.videoadaption.BaseAdaptionStrategyFactory
import com.ss.android.ugc.aweme.videoadaption.adaptionhandler.AdaptionScaleType
import com.ss.android.ugc.aweme.videoadaption.adaptionhandler.FeedScreenContext
import com.ss.android.ugc.aweme.videoadaption.adaptionmanager.VideoAdaptionManager
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.resultoperator.MultiContainerThresholdResultOperator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource

const val FOLLOW = "Follow"
const val FOR_YOU = "For You"
val topTabName = listOf(FOLLOW, FOR_YOU)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MockPage(
    screenSettingsState: MutableState<ScreenSettings>
) {

    val selectedTab = remember { mutableIntStateOf(1) }
    val topTabPagerState = rememberPagerState(selectedTab.intValue, 0F) { topTabName.size }

    val mockScreenScale = screenSettingsState.value.mockScreenScaleValue
    val baseAdaptionStrategyFactory = screenSettingsState.value.adaptionContext.strategyFactory
            as? BaseAdaptionStrategyFactory ?: return
    val screenWidth = baseAdaptionStrategyFactory.feedScreenContext.screenWidth.dp * mockScreenScale
    val screenHeight = baseAdaptionStrategyFactory.feedScreenContext.screenHeight.dp * mockScreenScale

    // 手机边框的宽度
    val borderThickness = 3.dp * mockScreenScale

    // 计算带边框的总宽度和高度
    val totalWidth = screenWidth + 2 * borderThickness
    val totalHeight = screenHeight + 2 * borderThickness

    Box(
        modifier = Modifier
//                .width(totalWidth)
//                .height(totalHeight)
//                .border(borderThickness, Color.Cyan)
    ) {
        Box(
            modifier = Modifier
                .width(screenWidth)
                .height(screenHeight)
                .align(Alignment.Center),
        ) {
            // interact area
            Box (
                modifier = Modifier
                    .width(screenWidth)
                    .height(screenHeight)
                    .align(Alignment.Center),
            ) {
                val baseAdaptionStrategyFactory = screenSettingsState.value.adaptionContext.strategyFactory
                        as BaseAdaptionStrategyFactory
                val topHeight = baseAdaptionStrategyFactory.feedScreenContext.statusBarHeight.dp *
                        screenSettingsState.value.mockScreenScaleValue

                val bottomTabHeight = baseAdaptionStrategyFactory.feedScreenContext.bottomTabHeight.dp *
                        screenSettingsState.value.mockScreenScaleValue

                val coroutineScope = rememberCoroutineScope()
                HorizontalPager(state = topTabPagerState, modifier = Modifier.fillMaxSize()) {
                    when (topTabName[it]) {
                        FOLLOW -> FollowPage(
                            screenSettingsState,
                        )
                        FOR_YOU -> StaticVerticalPages(
                            screenSettingsState,
                            PaddingValues(top = topHeight, bottom = bottomTabHeight)
                        )
                    }
                }
                StatusBar(topHeight)
                // 当Tab被点击时，更新Pager的当前页面
                PagerTabView(
                    paddingValue = PaddingValues(top = topHeight),
                    screenSettingsState = screenSettingsState,
                    selectedTab = selectedTab,
                    pagerState = topTabPagerState,
                    coroutineScope = coroutineScope,
                    tabNameList = topTabName,
                    tabHeight = baseAdaptionStrategyFactory.feedScreenContext.topTabHeight.dp *
                            screenSettingsState.value.mockScreenScaleValue
                )
                BottomTabBarView(bottomTabHeight)

                LaunchedEffect(topTabPagerState) {
                    snapshotFlow { topTabPagerState.currentPage }.collect {
                        selectedTab.intValue = it
                    }
                }
            }
        }

        // container border
//            Box(
//                modifier = Modifier
//                    .width(totalWidth)
//                    .height(totalHeight)
//                    .padding(
//                        getAdaptionTypePadding(screenSettingsState.value)
//                    )
//                    .border(borderThickness, Color.Cyan)
//            )
    }

}

@Composable
private fun FollowPage(
    screenSettingsState: MutableState<ScreenSettings>,
) {
    Text(FOLLOW, modifier = Modifier.fillMaxSize())
}


// 自定义圆点指示器
@Composable
fun VerticalClickableDotsIndicator(
    totalDots: Int,
    selectedIndex: Int,
    dotSize: Int,
    onDotClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        repeat(totalDots) { index ->
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(dotSize.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            index == selectedIndex -> Color.Black
                            else -> Color.Gray
                        }
                    )
                    .clickable {
                        onDotClick(index)
                    }
            )
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun StaticVerticalPages(
    screenSettingsState: MutableState<ScreenSettings>,
    paddingValues: PaddingValues,
) {

    val baseAdaptionStrategyFactory = screenSettingsState.value.adaptionContext.strategyFactory
            as? BaseAdaptionStrategyFactory ?: return
    val drawableResources = listOf(Res.drawable.example169, Res.drawable.example916, Res.drawable.example11)
    val scalesList = listOf(16f/9, 9f/16, 1f/1)
    val pagerState = rememberPagerState(0, 0F) { drawableResources.size }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // video area
        VerticalPager(state = pagerState, modifier = Modifier.fillMaxSize()) {
            //
            val settings = screenSettingsState.value
            val mockScreenScale = settings.mockScreenScaleValue

            val videoRatio: Float
            val thisRes: DrawableResource
            if (settings.mockVideoRatioEnable) {
                thisRes = Res.drawable.white_background
                videoRatio = settings.videoRatio
            } else {
                thisRes = drawableResources[it]
                videoRatio = scalesList[it]
            }

            val feedScreenContext: FeedScreenContext
            if (settings.mockPaddingEnable) {
                feedScreenContext = baseAdaptionStrategyFactory.feedScreenContext.copy(
                    topTypeList = listOf(settings.screenAdaptionTopType),
                    bottomTypeList = listOf(settings.screenAdaptionBottomType)
                )
            } else {
                feedScreenContext = baseAdaptionStrategyFactory.feedScreenContext
            }

            val adaptionParamsOperator = screenSettingsState.value.adaptionParams.paramsOperator
                    as? BaseAdaptionParamOperator ?: getMockAdaptionParamsOperator()

            val forceScaleType: AdaptionScaleType?
            if (settings.mockScaleEnable) {
                forceScaleType = settings.mockScaleMode.toAdaptionScaleType()
            } else {
                forceScaleType = adaptionParamsOperator.forceScaleType
            }

            val adaptionParams = screenSettingsState.value.adaptionParams.copy(
                containerWidth = feedScreenContext.screenWidth.toInt(),
                containerHeight = feedScreenContext.screenHeight.toInt(),
                videoWidth = 1000,
                videoHeight = (videoRatio * 1000).toInt(),
            )

            val videoAdaptionManager = VideoAdaptionManager("mock_display", settings.adaptionContext)
            videoAdaptionManager.doAdaption(
                adaptionParams.copy(
                    paramsOperator = adaptionParamsOperator.copy(
                        forceScaleType = forceScaleType
                    )
                )
            )

            val result = videoAdaptionManager.getAdaptionResult() ?: return@VerticalPager
            screenSettingsState.value = screenSettingsState.value.copy(
                adaptionParams = adaptionParams,
                adaptionResult = result
            )
            val operator = result.resultOperator as? MultiContainerThresholdResultOperator
            val scale = operator?.adaptionScaleType?.toContentScale() ?: ContentScale.None
            val padding = operator?.paddingValues?.toPaddingValues(mockScreenScale) ?: PaddingValues()


            Box { // full area container
                // interact area
                BottomBannerView(paddingValues, screenSettingsState)
                Box(modifier = Modifier.padding(padding)) { // video container
                    Box(modifier = Modifier.border(
                        3.dp * mockScreenScale,
                        Color.Cyan
                    )) {

                        // image
                        AdaptionImageView(
                            PaddingValues(),
                            thisRes,
                            width = result.width * mockScreenScale,
                            height = result.height * mockScreenScale,
                            currentContentScale = scale
                        ) {
                            if (settings.mockVideoRatioEnable) {
                                Text(text =
                                "video_view:${result.width}x${result.height}, scale:${settings.videoRatio.getScaleBy9()}\n" +
                                        "target_container:${operator?.adjustContainerWidth?.round1()}x${operator?.adjustContainerHeight?.round1()}, scale:${operator?.adjustContainerRatio?.getScaleBy9()}\n" +
                                        "padding_top:${operator?.paddingValues?.top},padding_bottom:${operator?.paddingValues?.bottom}, \n" +
                                        "scale_type:${operator?.adaptionScaleType}, \n" +
                                        "align_type:${operator?.alignType}, \n" +
                                        "area_diff:${operator?.areaDiff?.round3()}, \n",
                                    fontSize = 10.sp,
                                    modifier = Modifier.align(Alignment.Center),
                                    color = Color.Blue
                                )
                            }
                        }
                    }
                }
            }

        }

        val coroutineScope = rememberCoroutineScope()

        VerticalClickableDotsIndicator(
            totalDots = drawableResources.size,
            selectedIndex = pagerState.currentPage,
            dotSize = 20,
            onDotClick = { page ->
                coroutineScope.launch {
                    pagerState.animateScrollToPage(page)
                }
            },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
        )
    }

}

@Composable
fun StatusBar(
    height: Dp
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(0.5f)
            .height(height)
            .background(Color.DarkGray),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(Icons.Sharp.Notifications, contentDescription = null, Modifier.padding(end = 5.dp))
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopTabBarView(
    paddingValue: PaddingValues,
    screenSettingsState: MutableState<ScreenSettings>,
    selectedTab: MutableIntState,
    pagerState: PagerState,
    coroutineScope: CoroutineScope
) {
    val baseAdaptionStrategyFactory = screenSettingsState.value.adaptionContext.strategyFactory
            as? BaseAdaptionStrategyFactory ?: return
    PagerTabView(
        paddingValue = paddingValue,
        screenSettingsState = screenSettingsState,
        selectedTab = selectedTab,
        pagerState = pagerState,
        coroutineScope = coroutineScope,
        tabNameList = topTabName,
        tabHeight = baseAdaptionStrategyFactory.feedScreenContext.topTabHeight.dp *
                screenSettingsState.value.mockScreenScaleValue
    )
}


@Composable
fun BottomBannerView(
    paddingValue: PaddingValues,
    screenSettingsState: MutableState<ScreenSettings>
) {
    val baseAdaptionStrategyFactory = screenSettingsState.value.adaptionContext.strategyFactory
            as? BaseAdaptionStrategyFactory ?: return
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValue)
    ) {
        val height = baseAdaptionStrategyFactory.feedScreenContext.bottomBannerHeight.dp *
                screenSettingsState.value.mockScreenScaleValue
        Box(
            contentAlignment = Alignment.CenterEnd,
            modifier = Modifier
                .fillMaxWidth()
                .alpha(0.5f)
                .height(height)
                .background(Color.DarkGray)
        ) {
            Icon(Icons.Sharp.KeyboardArrowRight, contentDescription = null)
        }
    }
}


@Composable
fun BottomTabBarView(
    height: Dp
) {
    var currentTab by remember { mutableStateOf(bottomNavItems[0]) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        BottomTabBar(
            items = bottomNavItems,
            onTabSelected = {
                currentTab = it
//            onTypeChanged(getNextTypeValue(currentType))
            },
            currentTab = currentTab,
            height = height
        )
    }
}

@Composable
fun BottomTabBar(
    items: List<BottomNavItem>,
    onTabSelected: (BottomNavItem) -> Unit,
    currentTab: BottomNavItem,
    height: Dp
) {

    var selectedItem by remember { mutableStateOf(currentTab) }

    BottomNavigation(
        modifier = Modifier.height(height),
        backgroundColor = Color.Black,
    ) {
        items.forEach { item ->
            BottomNavigationItem(
                modifier = Modifier.size(26.dp),
                icon = { Icon(item.icon, contentDescription = item.title, tint = Color.White) },
//                label = { Text(item.title) },
                selected = selectedItem == item,
                onClick = {
                    selectedItem = item
                    onTabSelected(item)
                },
                alwaysShowLabel = false
            )
        }
    }
}

data class BottomNavItem(
    val title: String,
    val icon: ImageVector
)

// 示例：创建底部导航栏的选项
val bottomNavItems = listOf(
    BottomNavItem("Home", Icons.Filled.Home),
    BottomNavItem("Friends", Icons.Filled.Face),
    BottomNavItem("Shot", Icons.Filled.AddCircle),
    BottomNavItem("Inbox", Icons.Filled.Email),
    BottomNavItem("Profile", Icons.Filled.Person)
)