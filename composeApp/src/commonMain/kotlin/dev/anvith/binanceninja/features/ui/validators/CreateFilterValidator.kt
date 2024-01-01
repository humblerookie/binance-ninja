package dev.anvith.binanceninja.features.ui.validators

import dev.anvith.binanceninja.core.ui.data.IMap
import dev.anvith.binanceninja.core.ui.data.lock
import dev.anvith.binanceninja.features.ui.CreateFilterContract
import dev.anvith.binanceninja.features.ui.CreateFilterContract.ErrorTarget
import me.tatarka.inject.annotations.Inject


@Inject
class CreateFilterValidator {

    fun validate(state: CreateFilterContract.State): IMap<ErrorTarget, Boolean> {
        val min = state.min.text.trim().toDoubleOrNull()
        val max = state.max.text.trim().toDoubleOrNull()
        val errors = mutableMapOf<ErrorTarget, Boolean>()
        if (min == null && max == null) {
            errors[ErrorTarget.MIN_MAX] = true
        }
        val amount = state.amount.text.trim().toDoubleOrNull()
        if (amount == null) {
            errors[ErrorTarget.AMOUNT] = true
        }
        return errors.lock()
    }
}