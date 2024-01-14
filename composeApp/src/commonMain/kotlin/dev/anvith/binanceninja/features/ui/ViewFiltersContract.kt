package dev.anvith.binanceninja.features.ui

import dev.anvith.binanceninja.core.ui.data.IList
import dev.anvith.binanceninja.core.ui.data.emptyIList
import dev.anvith.binanceninja.domain.models.FilterModel

interface ViewFiltersContract {
  sealed class Event {
    data class RemoveFilter(val filter: FilterModel) : Event()
  }

  data class State(
    val isLoading: Boolean = true,
    val filters: IList<FilterModel> = emptyIList(),
    val error: String? = null
  )
}
