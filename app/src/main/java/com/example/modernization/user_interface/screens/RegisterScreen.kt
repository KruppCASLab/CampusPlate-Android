package com.example.modernization.user_interface.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.modernization.user_interface.viewmodel.RegisterState
import com.example.modernization.user_interface.viewmodel.RegisterViewModel



// Brand green
private val BrandGreen = Color(0xFF3CB94A)
private val BrandGreenDark = Color(0xFF2A9438)
private val White = Color.White

@Composable
fun RegisterScreen(
    onRegistrationSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: RegisterViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Navigate on success
    LaunchedEffect(state) {
        if (state is RegisterState.Success) {
            onRegistrationSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(BrandGreen, BrandGreenDark)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = state is RegisterState.PinSent,
            transitionSpec = {
                slideInHorizontally { it } + fadeIn() togetherWith
                        slideOutHorizontally { -it } + fadeOut()
            },
            label = "register_step"
        ) { isPinStep ->
            if (isPinStep) {
                PinConfirmStep(
                    email = (state as? RegisterState.PinSent)?.email ?: "",
                    isLoading = false,
                    error = (state as? RegisterState.Error)?.message,
                    onConfirm = { pin -> viewModel.confirmPin(pin) },
                    onClearError = { viewModel.resetState() }
                )
            } else {
                CreateAccountStep(
                    isLoading = state is RegisterState.Loading,
                    error = (state as? RegisterState.Error)?.message,
                    onRegister = { name, email -> viewModel.registerAndSendPin(name, email) },
                    onClearError = { viewModel.resetState() },
                    onNavigateToLogin = onNavigateToLogin
                )
            }
        }
    }
}

@Composable
private fun CreateAccountStep(
    isLoading: Boolean,
    error: String?,
    onRegister: (String, String) -> Unit,
    onClearError: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Logo / Title
        Text(
            text = "Campus Plate",
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            color = White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Create your account",
            fontSize = 16.sp,
            color = White.copy(alpha = 0.85f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Username field
        CampusTextField(
            value = userName,
            onValueChange = {
                userName = it
                if (error != null) onClearError()
            },
            label = "Username",
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        // Email field
        CampusTextField(
            value = email,
            onValueChange = {
                email = it
                if (error != null) onClearError()
            },
            label = "University Email",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    if (userName.isNotBlank() && email.isNotBlank()) {
                        onRegister(userName, email)
                    }
                }
            )
        )

        // Error message
        AnimatedVisibility(visible = error != null) {
            Text(
                text = error ?: "",
                color = Color(0xFFFFCDD2),
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
        }

        // Register button
        Button(
            onClick = { onRegister(userName.trim(), email.trim()) },
            enabled = userName.isNotBlank() && email.isNotBlank() && !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = White,
                contentColor = BrandGreenDark,
                disabledContainerColor = White.copy(alpha = 0.5f),
                disabledContentColor = BrandGreenDark.copy(alpha = 0.5f)
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = BrandGreenDark,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Get Pin",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Login link
        TextButton(onClick = onNavigateToLogin) {
            Text(
                text = "Already have an account? Sign in",
                color = White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun PinConfirmStep(
    email: String,
    isLoading: Boolean,
    error: String?,
    onConfirm: (String) -> Unit,
    onClearError: () -> Unit
) {
    var pin by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Campus Plate",
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            color = White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Check your email",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = White
        )

        Text(
            text = "We sent a PIN to\n$email",
            fontSize = 14.sp,
            color = White.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        // PIN field
        CampusTextField(
            value = pin,
            onValueChange = {
                if (it.length <= 6 && it.all { c -> c.isDigit() }) {
                    pin = it
                    if (error != null) onClearError()
                }
            },
            label = "Enter PIN",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    if (pin.isNotBlank()) onConfirm(pin)
                }
            )
        )

        // Error
        AnimatedVisibility(visible = error != null) {
            Text(
                text = error ?: "",
                color = Color(0xFFFFCDD2),
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
        }

        Button(
            onClick = { onConfirm(pin.trim()) },
            enabled = pin.isNotBlank() && !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = White,
                contentColor = BrandGreenDark,
                disabledContainerColor = White.copy(alpha = 0.5f),
                disabledContentColor = BrandGreenDark.copy(alpha = 0.5f)
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = BrandGreenDark,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Confirm Pin",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun CampusTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = White.copy(alpha = 0.8f)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = White,
            unfocusedTextColor = White,
            focusedBorderColor = White,
            unfocusedBorderColor = White.copy(alpha = 0.6f),
            cursorColor = White,
            focusedLabelColor = White,
            unfocusedLabelColor = White.copy(alpha = 0.7f)
        )
    )
}