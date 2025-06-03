package com.example.steamapp.auth.presentation

import android.content.Context
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.steamapp.api.presentation.APIEvents
import com.example.steamapp.auth.domain.models.Role
import com.example.steamapp.auth.domain.models.User
import com.example.steamapp.auth.domain.repository.AuthRepository
import com.example.steamapp.core.presentation.toString
import com.example.steamapp.core.util.networking.onError
import com.example.steamapp.core.util.networking.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel(
    private val dataStore: DataStore<Preferences>,
    private val authRepository: AuthRepository
): ViewModel() {
    private val _events= Channel<APIEvents>()
    val events= _events.receiveAsFlow()

    val userId= dataStore.data.map {
        val userIdKey= stringPreferencesKey("userId")
        it[userIdKey]
    }

    val userRole= dataStore.data.map {
        val userRoleKey= stringPreferencesKey("userRole")
        it[userRoleKey]
    }

    private val _authState= MutableStateFlow(AuthState())

    val authState= _authState
        .onStart {
            fetchAuthenticationState()
        }
        .stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AuthState()
    )
    fun onAction(actions: AuthActions){
        when(actions){
            is AuthActions.OnUserIdChange -> {changeUserId(actions.newUserId)}
            is AuthActions.OnPasswordChange -> {changePassword(actions.newPassword)}
            is AuthActions.OnSignInWithUserIdAndPassword -> {signIn(actions.context)}
            is AuthActions.OnSignOut -> {signOut()}
            is AuthActions.OnChangeUserRole -> {changeRole(actions.newRole)}
        }
    }

    private fun fetchAuthenticationState(){
        viewModelScope.launch {
            val authKey= booleanPreferencesKey("isAuthenticated")
            val isAuthenticated= dataStore.data.map {
                it[authKey]?:false
            }.first()
            _authState.update {
                it.copy(isSignedIn = if(isAuthenticated) AuthResponse.Authenticated else AuthResponse.Unauthenticated)
            }
        }
    }

    private fun changePassword( newPassword: String){
        _authState.update {
            it.copy(password = newPassword)
        }
    }

    private fun changeRole( newRole: Role){
        _authState.update {
            it.copy(userRole = newRole )
        }
    }

    private fun changeUserId( newUserId: String){
        _authState.update {
            it.copy(userId = newUserId)
        }
    }

    private fun signOut(){
        viewModelScope.launch {
            _authState.update {
                it.copy(isSignedIn = AuthResponse.Unauthenticated)
            }
            saveUserData(null)
            saveUserRole(null)
            dataStore.edit {
                val isAuthenticated= booleanPreferencesKey("isAuthenticated")
                it[isAuthenticated]= false
            }
        }
    }

    private suspend fun saveUserData(userId: String?):Boolean{
        dataStore.edit {
            val userIdKey= stringPreferencesKey("userId")
            if(userId==null){
                it.remove(userIdKey)
            } else{
                it[userIdKey]= userId
            }
        }
        return true
    }

    private suspend fun saveUserRole(role: String?):Boolean{
        dataStore.edit {
            val userRoleKey= stringPreferencesKey("userRole")
            if(role==null){
                it.remove(userRoleKey)
            } else{
                it[userRoleKey]= role
            }
        }
        return true
    }

    private fun signIn(context: Context){
        _authState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.signInWithUserIdAndPassword(
                User(
                    userId = _authState.value.userId,
                    password = _authState.value.password,
                    role = _authState.value.userRole
                )
            )
                .onSuccess {response->
                    if(response.status){
                        dataStore.edit {
                            val isAuthenticated= booleanPreferencesKey("isAuthenticated")
                            it[isAuthenticated]= true
                        }
                        val saved= async{saveUserData(_authState.value.userId)}.await()
                        async {  saveUserRole(
                            _authState.value.userRole.toString()
                        )}.await()
                        if(saved){
                        _authState.update {
                                it.copy(
                                    isSignedIn = AuthResponse.Authenticated,
                                    isLoading = false,
                                    userId = "",
                                    password = ""
                                )
                            }
                        }
                    } else{
                        val message= "User doesn't exist"
                        _authState.update {
                            it.copy(
                                isSignedIn = AuthResponse.Error(message),
                                isLoading = false,
                                userId = "",
                                password = ""
                            )
                        }
                        withContext(Dispatchers.Main){
                            Toast.makeText(context, message , Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .onError {error->
                    _events.send(APIEvents.Error(error))
                    _authState.update {
                        it.copy(isSignedIn = AuthResponse.Error(error.toString(context)), isLoading = false)
                    }
                }
        }
    }
}
