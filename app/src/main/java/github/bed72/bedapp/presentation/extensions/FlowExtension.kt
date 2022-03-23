package github.bed72.bedapp.presentation.extensions

import kotlinx.coroutines.flow.Flow
import github.bed72.core.usecase.base.ResultStatus
import github.bed72.core.usecase.base.ResultStatus.Error
import github.bed72.core.usecase.base.ResultStatus.Success
import github.bed72.core.usecase.base.ResultStatus.Loading

suspend fun <T> Flow<ResultStatus<T>>.watchStatus(
    loading: () -> Unit = {},
    success: (data: T) -> Unit,
    error: (throwable: Throwable) -> Unit = {}
) {
    collect { status ->
        when (status) {
            Loading -> loading()
            is Success -> success(status.data)
            is Error -> error(status.throwable)
        }
    }
}