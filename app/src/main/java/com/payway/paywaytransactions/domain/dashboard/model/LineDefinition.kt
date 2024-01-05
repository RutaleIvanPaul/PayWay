package com.payway.paywaytransactions.domain.dashboard.model

import com.payway.paywaytransactions.data.dashboard.model.RemoteTransaction

/**
 * Model for the details needed to create a line for the line charts
 */
data class LineDefinition(
    val filteredList: List<RemoteTransaction>,
    val color: Int,
    val label: String
)
