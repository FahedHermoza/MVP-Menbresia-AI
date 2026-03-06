package com.fahed.perupass.feature.screen.feed

sealed class FeedSideEffect {
    data class NavigateToDetail(val venueId: String) : FeedSideEffect()
}
