package com.example.steamapp.auth.presentation.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.steamapp.R
import com.example.steamapp.auth.presentation.AuthActions
import com.example.steamapp.auth.presentation.AuthResponse
import com.example.steamapp.auth.presentation.AuthState
import com.example.steamapp.ui.theme.SteamAppTheme

@Composable
fun LoginScreen(
    onAction: (AuthActions)-> Unit,
    state: AuthState,
    modifier: Modifier = Modifier,
    onSignIn: ()-> Unit
) {
    val context= LocalContext.current
    LaunchedEffect(state.isSignedIn) {
        when(state.isSignedIn){
            is AuthResponse.Authenticated-> {onSignIn()}
            else-> {}
        }
    }

    Surface(
        modifier= modifier.fillMaxSize()
    ) {
        Surface(
            modifier = modifier
                .padding(16.dp)
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier.weight(0.5f),
                    painter = painterResource(R.drawable.logo),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(10.dp))
                Column(
                    modifier = Modifier.weight(0.7f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = "Login",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(22.dp))
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(30),
                        value = state.userId,
                        onValueChange = {
                            onAction(AuthActions.OnUserIdChange(it))
                        },
                        placeholder = { Text("User id") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Person,
                                contentDescription = null
                            )
                        }
                    )
                    Spacer(Modifier.height(15.dp))
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(30),
                        value = state.password,
                        onValueChange = {
                            onAction(AuthActions.OnPasswordChange(it))
                        },
                        placeholder = { Text("Password") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Lock,
                                contentDescription = null
                            )
                        },
                        visualTransformation = PasswordVisualTransformation()
                    )
                    Spacer(Modifier.height(15.dp))
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onAction(AuthActions.OnSignInWithUserIdAndPassword(context))
                        }
                    ) {
                        Text(
                            text = "Sign-In",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    if(state.isLoading){
                        CircularProgressIndicator()
                    }

                }
            }

        }
    }
}

@Preview
@Composable
private fun LoginPreview() {
    SteamAppTheme {
     LoginScreen(
         onAction = { },
         state = AuthState(),
         modifier = Modifier,
         onSignIn = {}
     )
    }
}