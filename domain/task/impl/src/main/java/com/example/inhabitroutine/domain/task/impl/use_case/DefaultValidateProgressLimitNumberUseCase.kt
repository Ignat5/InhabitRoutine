package com.example.inhabitroutine.domain.task.impl.use_case

import com.example.inhabitroutine.domain.task.api.use_case.ValidateProgressLimitNumberUseCase
import com.example.inhabitroutine.domain.task.impl.util.DomainConst

internal class DefaultValidateProgressLimitNumberUseCase : ValidateProgressLimitNumberUseCase {

    override operator fun invoke(limitNumber: Double): Boolean {
        return limitNumber in DomainConst.MIN_LIMIT_NUMBER..DomainConst.MAX_LIMIT_NUMBER &&
                limitNumber.toString().length <= DomainConst.MAX_LIMIT_NUMBER_LENGTH
    }

}