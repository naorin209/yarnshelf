package jp.hibeiko.yarnshelf.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import jp.hibeiko.yarnshelf.R
import jp.hibeiko.yarnshelf.common.SortKey
import jp.hibeiko.yarnshelf.common.YarnRoll
import jp.hibeiko.yarnshelf.common.YarnThickness
import jp.hibeiko.yarnshelf.common.formatGaugeStringForScreen
import jp.hibeiko.yarnshelf.common.formatNeedleSizeStringForScreen
import jp.hibeiko.yarnshelf.common.formatWeightStringForScreen
import jp.hibeiko.yarnshelf.data.YarnData
import jp.hibeiko.yarnshelf.ui.navigation.NavigationDestination
import jp.hibeiko.yarnshelf.ui.theme.YarnShelfTheme
import kotlinx.coroutines.launch
import java.util.Date

object HomeDestination : NavigationDestination {
    override val route = "Home"
    override val title = "けいとのたな"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    // ViewModel(UiStateを使うため)
    homeScreenViewModel: HomeScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    editButtonOnClicked: (Int) -> Unit,
    addButtonOnClicked: () -> Unit,
) {
    // UiStateを取得
    val homeScreenUiState by homeScreenViewModel.homeScreenUiState.collectAsState()
    val dialogViewFlag by homeScreenViewModel.dialogViewFlag.collectAsState()
    val dialogViewYarnId by homeScreenViewModel.dialogViewYarnId.collectAsState()
    val bottomSheetViewFlag by homeScreenViewModel.bottomSheetViewFlag.collectAsState()
    val homeScreenSearchConditionState by homeScreenViewModel.homeScreenSearchConditionState.collectAsState()

//    Log.i("HomeScreen","$homeScreenUiState")
//    Log.i("HomeScreen","$homeScreenSearchConditionState")
    // 画面トップ
    // Surface は色を受け取る。Surface と MaterialTheme はマテリアル デザインに関連するコンセプトです。
    // Surface 内にネストされているコンポーネントは、その背景色の上に描画されます。
    Surface(
        modifier = modifier
    ) {
        Scaffold(
            // トップバー
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(
                            text = HomeDestination.title,
                            style = MaterialTheme.typography.headlineLarge,
                        )
                    },
//                    navigationIcon = {
//                        if (navController.previousBackStackEntry != null) {
//                            IconButton(onClick = { navController.navigateUp() }) {
//                                Icon(
//                                    Icons.Filled.ArrowBack,
//                                    contentDescription = "戻る",
//                                    tint = MaterialTheme.colorScheme.primary
//                                )
//                            }
//                        }
//                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = addButtonOnClicked,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .padding(
                            end = WindowInsets.safeDrawing.asPaddingValues()
                                .calculateEndPadding(LocalLayoutDirection.current)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.homescreen_add_button_name)
                    )
                }
            },
        ) { innerPadding ->
            Column(modifier = modifier.padding(innerPadding)) {
                HomeScreenTop(
                    homeScreenSearchConditionState,
                    bottomSheetViewFlag,
                    homeScreenViewModel::queryUpdate,
                    homeScreenViewModel::sortKeyUpdate,
                    homeScreenViewModel::sortOrderUpdate,
                    homeScreenViewModel::bottomSheetOnClick,
//                    homeScreenViewModel::sortList,
                    modifier
                )
                HomeScreenBody(
                    homeScreenUiState,
                    dialogViewFlag,
                    dialogViewYarnId,
                    homeScreenViewModel::cardOnClick,
                    homeScreenViewModel::dialogOnClick,
                    editButtonOnClicked,
                    modifier.weight(1.0f, false)
                )
            }
        }
    }
}

@Composable
fun HomeScreenBody(
    homeScreenUiState: HomeScreenUiState,
    dialogViewFlag: Boolean,
    dialogViewYarnId: Int,
    cardOnClick: (Int) -> Unit,
    dialogOnClick: () -> Unit,
    editButtonOnClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {


    LazyColumn(modifier = modifier) {
        items(homeScreenUiState.yarnDataList) {
            YarnCard(
                it,
                cardOnClick = cardOnClick,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
    if (dialogViewFlag) {
        YarnDialog(
            homeScreenUiState.yarnDataList.first { it.yarnId == dialogViewYarnId },
            dialogOnClick,
            editButtonOnClicked,
        )
    }
}

@Composable
fun YarnCard(
    yarnData: YarnData,
    cardOnClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        onClick = { cardOnClick(yarnData.yarnId) },
    ) {
//        Log.d("HomeScreen","$yarnData")

        Row {
            when (yarnData.imageUrl) {
                "" ->
                    Image(
                        painter = painterResource(
                            when (yarnData.drawableResourceId) {
                                0 -> R.drawable.not_found
                                else -> yarnData.drawableResourceId
                            }
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(140.dp),
                    )

                else -> AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(yarnData.imageUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.loading_img),
                    error = painterResource(R.drawable.not_found),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(140.dp),
                )
            }
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                if (yarnData.yarnMakerName.isNotBlank()) {
                    Text(
                        text = yarnData.yarnMakerName,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
                Text(
                    text = yarnData.yarnName,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = "残量：${yarnData.havingNumber}玉",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                if (yarnData.yarnDescription.isNotBlank()) {
                    Text(
                        text = yarnData.yarnDescription,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
//                Spacer(modifier = Modifier.weight(1.0f))
//                Text(
//                    text = "最終更新日：${SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(yarnData.lastUpdateDate)}",
//                    color = MaterialTheme.colorScheme.onSurface,
//                    style = MaterialTheme.typography.labelSmall,
//                    modifier = Modifier.align(Alignment.End),
////                    textAlign = TextAlign.End
//                )
            }
        }
    }
}

@Composable
fun YarnDialog(
    yarnData: YarnData,
    dialogOnClick: () -> Unit,
    editButtonOnClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = {
            dialogOnClick()
        },
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        title = {
            Column {
                if (yarnData.yarnMakerName.isNotBlank()) {
                    Text(
                        text = yarnData.yarnMakerName,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Text(
                    text = yarnData.yarnName,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.displayMedium
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (yarnData.imageUrl) {
                    "" ->
                        Image(
                            painter = painterResource(
                                when (yarnData.drawableResourceId) {
                                    0 -> R.drawable.not_found
                                    else -> yarnData.drawableResourceId
                                }
                            ),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(140.dp)
                                .width(140.dp)
                                .padding(5.dp),
                        )

                    else -> AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(yarnData.imageUrl)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.loading_img),
                        error = painterResource(R.drawable.not_found),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(140.dp)
                            .width(140.dp)
                            .padding(5.dp),
                    )
                }
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row {
                        Text(
                            text = "残量：",
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .padding(2.dp)
                                .weight(0.35f)
                        )
                        Text(
                            text = "${yarnData.havingNumber}玉",
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(2.dp)
                                .weight(0.65f)
                        )
                    }
                    Row {
                        Text(
                            text = "品質：",
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .padding(2.dp)
                                .weight(0.35f)
                        )
                        Text(
                            text = yarnData.quality.ifBlank { "-" },
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(2.dp)
                                .weight(0.65f)
                        )
                    }
                    Row {
                        Text(
                            text = "標準状態重量：",
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .padding(2.dp)
                                .weight(0.35f)
                        )
                        Text(
                            text = formatWeightStringForScreen(
                                yarnData.weight,
                                yarnData.length,
                                yarnData.roll
                            ),
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(2.dp)
                                .weight(0.65f)
                        )
                    }
                    Row {
                        Text(
                            text = "糸の太さ：",
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .padding(2.dp)
                                .weight(0.35f)
                        )
                        Text(
                            text = yarnData.thickness.value,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(2.dp)
                                .weight(0.65f)
                        )
                    }
                    Row {
                        Text(
                            text = "標準ゲージ：",
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .padding(2.dp)
                                .weight(0.35f)
                        )
                        Text(
                            text = formatGaugeStringForScreen(
                                yarnData.gaugeColumnFrom,
                                yarnData.gaugeColumnTo,
                                yarnData.gaugeRowFrom,
                                yarnData.gaugeRowTo,
                                yarnData.gaugeStitch
                            ),
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(2.dp)
                                .weight(0.65f)
                        )
                    }
                    Row {
                        Text(
                            text = "参考使用針：",
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .padding(2.dp)
                                .weight(0.35f)
                        )
                        Text(
                            text = formatNeedleSizeStringForScreen(
                                yarnData.needleSizeFrom,
                                yarnData.needleSizeTo,
                                yarnData.crochetNeedleSizeFrom,
                                yarnData.crochetNeedleSizeTo
                            ),
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(2.dp)
                                .weight(0.65f)
                        )
                    }
                }
                if (yarnData.yarnDescription.isNotBlank()) {
                    Text(
                        text = yarnData.yarnDescription,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 5,
                        modifier = Modifier.padding(2.dp)
                    )
                }
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = {
                    dialogOnClick()
                    editButtonOnClicked(yarnData.yarnId)
                }
            ) {
                Text(text = "詳細")
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    dialogOnClick()
                }
            ) {
                Text(text = "OK")
            }
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenTop(
    homeScreenSearchConditionState: HomeScreenSearchConditionState,
    bottomSheetViewFlag: Boolean,
    queryUpdate: (String) -> Unit,
    sortKeyUpdate: (SortKey) -> Unit,
    sortOrderUpdate: () -> Unit,
    bottomSheetOnClick: (Boolean) -> Unit,
    modifier: Modifier
) {
    // BottomSheet作成用
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // 検索
        TextField(
            label = {
                Text(
                    "けいとを検索",
                    style = MaterialTheme.typography.labelSmall
                )
            },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            value = homeScreenSearchConditionState.query,
            onValueChange = { queryUpdate(it) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            textStyle = MaterialTheme.typography.displayMedium,
            modifier = Modifier.weight(1.0f, false)
        )
        IconButton(
            onClick = { bottomSheetOnClick(true) }
        ) {
            Icon(painterResource(R.drawable.baseline_sort_24), contentDescription = null)
        }
        IconButton(
            onClick = { sortOrderUpdate() }
        ) {
            Icon(painterResource(R.drawable.baseline_swap_vert_24), contentDescription = null)
        }
    }
    if (bottomSheetViewFlag) {
        ModalBottomSheet(
            onDismissRequest = { bottomSheetOnClick(false) },
            sheetState = sheetState
        ) {
            // Sheet content
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "並び替え",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                )
                HorizontalDivider(thickness = 2.dp)
                Column(
                    Modifier
                        .selectableGroup()
                        .align(Alignment.CenterHorizontally)
                ) {
                    SortKey.entries.forEach {
                        Row(
                            Modifier
//                            .fillMaxWidth()
//                            .height(56.dp)
                                .selectable(
                                    selected = (it == homeScreenSearchConditionState.sortKey),
                                    onClick = {
                                        sortKeyUpdate(it)
                                        coroutineScope
                                            .launch { sheetState.hide() }
                                            .invokeOnCompletion {
                                                if (!sheetState.isVisible) {
                                                    bottomSheetOnClick(false)
                                                }
                                            }
                                    },
                                    role = Role.RadioButton
                                )
                                .padding(top = 10.dp, bottom = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (it == homeScreenSearchConditionState.sortKey),
                                onClick = null // null recommended for accessibility with screenreaders
                            )
                            Text(
                                text = it.value,
                                style = MaterialTheme.typography.displayMedium,
//                            modifier = Modifier
//                                .padding(top = 0.dp, start = 10.dp, bottom = 5.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    YarnShelfTheme {
        HomeScreenBody(
            HomeScreenUiState(
                yarnDataList =
                listOf(
                    YarnData(
                        0,
                        "10001",
                        "1010 Seabright",
                        "Jamieson's",
                        "1010 Seabright",
                        "1548",
                        "シェットランドウール１００％",
                        25.01,
                        YarnRoll.BALL,
                        105.0,
                        20.0,
                        21.0,
                        27.0,
                        28.0,
                        "メリヤス編み",
                        3.0,
                        5.0,
                        0.0,
                        0.0,
                        YarnThickness.THICK,
                        10,
                        "毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです",
                        Date(),
                        "",
                        R.drawable.spin_1010_crpd_1625196651766_400
                    ),
                    YarnData(
                        1,
                        "10002",
                        "Shaela",
                        "Jamieson's",
                        "102 Shaela",
                        "1548",
                        "シェットランドウール１００％",
                        25.0,
                        YarnRoll.BALL,
                        105.0,
                        20.0,
                        21.0,
                        27.0,
                        28.0,
                        "メリヤス編み",
                        3.0,
                        5.0,
                        0.0,
                        0.0,
                        YarnThickness.THICK,
                        10,
                        "毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです",
                        Date(),
                        "",
                        R.drawable.spin_102_crpd_1625194839510_400
                    ),
                    YarnData(
                        2,
                        "10003",
                        "Night Hawk",
                        "Jamieson's",
                        "1020 Night Hawk",
                        "1548",
                        "シェットランドウール１００％",
                        25.0,
                        YarnRoll.BALL,
                        105.0,
                        20.0,
                        21.0,
                        27.0,
                        28.0,
                        "メリヤス編み",
                        3.0,
                        5.0,
                        0.0,
                        0.0,
                        YarnThickness.THICK,
                        10,
                        "毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです",
                        Date(),
                        "",
                        R.drawable.spin_1020_crpd_1625196650659_400
                    ),
                    YarnData(
                        3,
                        "10004",
                        "Sholmit",
                        "Jamieson's",
                        "103 Sholmit",
                        "1548",
                        "シェットランドウール１００％",
                        25.0,
                        YarnRoll.BALL,
                        105.0,
                        20.0,
                        21.0,
                        27.0,
                        28.0,
                        "メリヤス編み",
                        3.0,
                        5.0,
                        0.0,
                        0.0,
                        YarnThickness.THICK,
                        10,
                        "毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです",
                        Date(),
                        "",
                        R.drawable.spin_103_crpd_1625194837544_400
                    ),
                    YarnData(
                        4,
                        "10005",
                        "Natural White",
                        "Jamieson's",
                        "104 Natural White",
                        "1548",
                        "シェットランドウール１００％",
                        25.0,
                        YarnRoll.BALL,
                        105.0,
                        20.0,
                        21.0,
                        27.0,
                        28.0,
                        "メリヤス編み",
                        3.0,
                        5.0,
                        0.0,
                        0.0,
                        YarnThickness.THICK,
                        10,
                        "毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです",
                        Date(),
                        "",
                        R.drawable.spin_104_crpd_1625194838195_400
                    ),
                    YarnData(
                        5,
                        "10006",
                        "Eesit",
                        "Jamieson's",
                        "105 Eesit",
                        "1548",
                        "シェットランドウール１００％",
                        25.0,
                        YarnRoll.BALL,
                        105.0,
                        20.0,
                        21.0,
                        27.0,
                        28.0,
                        "メリヤス編み",
                        3.0,
                        5.0,
                        0.0,
                        0.0,
                        YarnThickness.THICK,
                        10,
                        "毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです",
                        Date(),
                        "",
                        R.drawable.spin_105_crpd_1625194836934_400
                    ),
                    YarnData(
                        6,
                        "10007",
                        "Mooskit",
                        "Jamieson's",
                        "106 Mooskit",
                        "1548",
                        "シェットランドウール１００％",
                        25.0,
                        YarnRoll.BALL,
                        105.0,
                        20.0,
                        21.0,
                        27.0,
                        28.0,
                        "メリヤス編み",
                        3.0,
                        5.0,
                        0.0,
                        0.0,
                        YarnThickness.THICK,
                        10,
                        "毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです",
                        Date(),
                        "",
                        R.drawable.spin_106_crpd_1625194835979_400
                    ),
                    YarnData(
                        7,
                        "10008",
                        "Mogit",
                        "Jamieson's",
                        "107 Mogit",
                        "1548",
                        "シェットランドウール１００％",
                        25.0,
                        YarnRoll.BALL,
                        105.0,
                        20.0,
                        21.0,
                        27.0,
                        28.0,
                        "メリヤス編み",
                        3.0,
                        5.0,
                        0.0,
                        0.0,
                        YarnThickness.THICK,
                        10,
                        "毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです",
                        Date(),
                        "",
                        R.drawable.spin_107_crpd_1625194834874_400
                    ),
                    YarnData(
                        8,
                        "10009",
                        "Natural Black",
                        "Jamieson's",
                        "101 Natural Black(Shetland Black)",
                        "1548",
                        "シェットランドウール１００％",
                        25.0,
                        YarnRoll.BALL,
                        105.0,
                        20.0,
                        21.0,
                        27.0,
                        28.0,
                        "メリヤス編み",
                        3.0,
                        5.0,
                        0.0,
                        0.0,
                        YarnThickness.THICK,
                        10,
                        "毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです",
                        Date(),
                        "",
                        R.drawable.spin_101_crpd_1625194841231_400
                    ),
                )
            ),
            dialogViewFlag = true,
            dialogViewYarnId = 0,
            cardOnClick = {},
            dialogOnClick = {},
            editButtonOnClicked = {},
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(5.dp)
        )
    }
}

@Preview
@Composable
fun HomeScreenTopPreview() {
    YarnShelfTheme {
        HomeScreenTop(
            homeScreenSearchConditionState = HomeScreenSearchConditionState(query = "ダルマ毛糸"),
            bottomSheetViewFlag = false,
            queryUpdate = { _ -> },
            sortKeyUpdate = { _ -> },
            sortOrderUpdate = { },
            bottomSheetOnClick = { _ -> },
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(10.dp)
        )
    }
}