package com.leecoder.stockchart.model.screen

sealed class BollingerSetting(
    val menuId: String,
    val title: String,
    val description: String,
) {
    data object DailyBollinger: BollingerSetting(
        menuId = "daily_bollinger",
        title = "일봉 볼린저 밴드 하한가 알림",
        description = "일봉 기준으로 종목 현재 체결가가 볼린저 밴드 하한가 아래로 떨어지면 알림을 드려요."
    )

    data object LiveBollinger: BollingerSetting(
        menuId = "live_bollinger",
        title = "분봉 볼린저 밴드 하한가 알림",
        description = "분봉 기준으로 종목 현재 체결가가 볼린저 밴드 하한가 아래로 떨어지면 알림을 드려요."
    )

    companion object {
        val bollingerMenus = listOf(
            DailyBollinger,
            LiveBollinger,
        )
    }
}



sealed class MarketInfo(
    val menuId: String,
    val title: String,
    val subTitle: String, // 개장 시간 정보
    val description: String,
) {
    data object Kospi: MarketInfo(
        menuId = "kospi",
        title = "코스피 (KOSPI)",
        subTitle = "",
        description = "종목 검색 및 구독 알림 등 모든 기능이 코스피 기준으로 동작해요."
    )

    data object Nasdaq: MarketInfo(
        menuId = "nasdaq",
        title = "나스닥 (NASDAQ)",
        subTitle = "",
        description = "종목 검색 및 구독 알림 등 모든 기능이 나스닥 기준으로 동작해요."
    )
}