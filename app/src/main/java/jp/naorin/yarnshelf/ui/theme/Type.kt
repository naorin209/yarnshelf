package jp.naorin.yarnshelf.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import jp.naorin.yarnshelf.R

val YasashisaGothic = FontFamily(
    Font(R.font.yasashisa)
)
val BIZUDGothic_Bold = FontFamily(
    Font(R.font.bizudgothic_bold)
)
val BIZUDPGothic_Bold = FontFamily(
    Font(R.font.bizudpgothic_bold)
)
val BIZUDGothic_Regular = FontFamily(
    Font(R.font.bizudgothic_regular)
)
val BIZUDPGothic_Regular = FontFamily(
    Font(R.font.bizudpgothic_regular)
)
val YarnShelfTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = YasashisaGothic,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = YasashisaGothic,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),
    displayLarge = TextStyle(
        fontFamily = BIZUDPGothic_Regular,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp
    ),
    displayMedium = TextStyle(
        fontFamily = BIZUDPGothic_Regular,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),
    labelSmall = TextStyle(
        fontFamily = BIZUDPGothic_Regular,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = BIZUDPGothic_Regular,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    )
)
