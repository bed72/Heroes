package github.bed72.bedapp.presentation.extensions

import github.bed72.core.usecase.base.ResultStatus
import github.bed72.core.usecase.base.ResultStatus.Error
import github.bed72.core.usecase.base.ResultStatus.Loading
import github.bed72.core.usecase.base.ResultStatus.Success
import kotlinx.coroutines.flow.Flow

suspend fun <T> Flow<ResultStatus<T>>.watchStatus(
    loading: suspend () -> Unit = {},
    success: suspend (data: T) -> Unit,
    error: suspend (throwable: Throwable) -> Unit
) {
    collect { status ->
        when (status) {
            Loading -> loading()
            is Success -> success(status.data)
            is Error -> error(status.throwable)
        }
    }
}
