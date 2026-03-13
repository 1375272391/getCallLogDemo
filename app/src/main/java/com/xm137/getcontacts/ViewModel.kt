package com.xm137.getcontacts

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

sealed interface CallLogUiState {
    data class CallLog(val callLog: List<CallLogInfo>) : CallLogUiState
    data class Info(val info: String?) : CallLogUiState
}

class CallLogViewModel : ViewModel() {
    private val _callLogState = MutableStateFlow(CallLogUiListState())
    val callLogState: StateFlow<CallLogUiListState> = _callLogState.asStateFlow()

    fun addData(callLogData: List<CallLogInfo>) {
        _callLogState.update { state ->
            state.copy(
                callLog = callLogData
            )
        }
    }
}