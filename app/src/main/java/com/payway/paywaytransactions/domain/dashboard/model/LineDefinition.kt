package com.payway.paywaytransactions.domain.dashboard.model

import com.payway.paywaytransactions.data.dashboard.model.RemoteTransaction

data class LineDefinition(
    val filteredList: List<RemoteTransaction>,
    val color: Int,
    val label: String
)
