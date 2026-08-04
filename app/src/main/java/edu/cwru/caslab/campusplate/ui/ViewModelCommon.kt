package edu.cwru.caslab.campusplate.ui

import androidx.lifecycle.ViewModel
import edu.cwru.caslab.campusplate.model.GenericResponse
import edu.cwru.caslab.campusplate.repository.CampusPlateApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import okio.IOException
import retrofit2.Response

abstract class UiStateCommon ( 
  open val state: State = State.Idle
)

abstract class ViewModelCommon<UiState: UiStateCommon>(
  defaultState: UiState,
): ViewModel() {

  protected var _uiState: MutableStateFlow<UiState> = MutableStateFlow(defaultState)
  val uiState: StateFlow<UiState> = _uiState.asStateFlow()

  open suspend fun <T> commonApiCall (
      apiCall: suspend () -> Response<GenericResponse<T>>,
      onLoad: (UiState) -> UiState,
      onSuccess: (UiState) -> UiState,
      onError: (UiState) -> UiState
  ): T? {

    _uiState.update { onLoad(it) }

    try { 

      val listResult = apiCall()

      if (listResult.isSuccessful) {
        if (listResult.body()?.data != null) {
          _uiState.update { onSuccess(it) }
          
        } else _uiState.update { onError(it) }
      } else _uiState.update { onError(it) }

      return listResult.body()?.data


    } catch (e: IOException) { 
      _uiState.update { onError(it) } 

    }

    return null

}

  open fun fieldUpdate(
    onCall: (UiState) -> UiState
  ) {
    _uiState.update { onCall(it) }
  }

}
