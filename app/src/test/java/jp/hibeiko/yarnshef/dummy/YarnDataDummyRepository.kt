package jp.hibeiko.yarnshef.dummy

import jp.hibeiko.yarnshelf.R
import jp.hibeiko.yarnshelf.common.YarnRoll
import jp.hibeiko.yarnshelf.common.YarnThickness
import jp.hibeiko.yarnshelf.data.YarnData
import jp.hibeiko.yarnshelf.repository.YarnDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import java.util.Date

class YarnDataDummyRepository : YarnDataRepository {
    private val flowList = MutableSharedFlow<List<YarnData>>()

    private val yarnDummyData = YarnDummyData.dummyDataList

    override suspend fun insert(yarnData: YarnData) {
        yarnDummyData.removeIf { it.yarnId == yarnData.yarnId}
        yarnDummyData.add(yarnData)
    }

    override suspend fun update(yarnData: YarnData) {
        yarnDummyData.removeIf { it.yarnId == yarnData.yarnId}
        yarnDummyData.add(yarnData)
    }

    override suspend fun delete(yarnData: YarnData) {
        yarnDummyData.remove(yarnData)
    }

    override fun select(yarnId: Int): Flow<YarnData?> = flow {
        emit(yarnDummyData.first { it.yarnId == yarnId })
    }

    override fun selectAll(sortStr: String): Flow<List<YarnData>> = flowList
    suspend fun emitSelectAll(sortStr: String) = flowList.emit(
        // 名前の昇順
        YarnDummyData.dummyDataList
//        yarnDummyData.sortedBy { it.yarnName }

    )

    override fun selectWithQuery(query: String, sortStr: String): Flow<List<YarnData>> = flowList
    suspend fun emitSelectWithQuery(query: String, sortStr: String) = flowList.emit(
        // 名前の昇順
        YarnDummyData.dummyDataList
//        yarnDummyData.filter { it.yarnName.contains(query) }.sortedBy { it.yarnName }
    )
}

object YarnDummyData {
    val dummyDataList =
        mutableListOf(
            YarnData(
                1,
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
                2,
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
                17,
                "毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです",
                Date(),
                "",
                R.drawable.spin_102_crpd_1625194839510_400
            ),
            YarnData(
                3,
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
                15,
                "毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです",
                Date(),
                "",
                R.drawable.spin_1020_crpd_1625196650659_400
            ),
            YarnData(
                4,
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
                18,
                "毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです",
                Date(),
                "",
                R.drawable.spin_103_crpd_1625194837544_400
            ),
            YarnData(
                5,
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
                11,
                "毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです",
                Date(),
                "",
                R.drawable.spin_104_crpd_1625194838195_400
            ),
            YarnData(
                6,
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
                19,
                "毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです",
                Date(),
                "",
                R.drawable.spin_105_crpd_1625194836934_400
            ),
            YarnData(
                7,
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
                14,
                "毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです",
                Date(),
                "",
                R.drawable.spin_106_crpd_1625194835979_400
            ),
            YarnData(
                8,
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
                12,
                "毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです",
                Date(),
                "",
                R.drawable.spin_107_crpd_1625194834874_400
            ),
            YarnData(
                9,
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
                13,
                "毛糸になるまでのすべての工程を島内で行う、純粋なシェットランドヤーンです",
                Date(),
                "",
                R.drawable.spin_101_crpd_1625194841231_400
            ),
        )
}