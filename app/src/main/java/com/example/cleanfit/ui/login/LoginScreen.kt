package com.example.cleanfit.ui.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cleanfit.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: (String) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(viewModel.uiMessage) {
        viewModel.uiMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.uiMessage = null
        }
    }

    val launchGoogleSheet: () -> Unit = {
        val credentialManager = CredentialManager.create(context)
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val hashedNonce = md.digest(bytes).fold("") { str, it -> str + "%02x".format(it) }

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("YOUR_WEB_CLIENT_ID_HERE") // ⚠️ Paste Client ID
            .setNonce(hashedNonce)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        coroutineScope.launch {
            try {
                val result = credentialManager.getCredential(request = request, context = context)
                val credential = GoogleIdTokenCredential.createFrom(result.credential.data)
                viewModel.onGoogleSignInResult(credential.idToken, rawNonce)
            } catch (e: Exception) {
                Toast.makeText(context, "Google Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(), // making compose full screen
        horizontalAlignment = Alignment.CenterHorizontally, // centering horizontally and vertically
        verticalArrangement = Arrangement.Center
    )
    {
        Image(painter = painterResource(id = R.drawable.mobile_login),
            contentDescription = null,
            modifier = Modifier.size(200.dp))


        Text("Welcome Back", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(4.dp))
        Text("Sign in to continue")

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = password, onValueChange = { password = it}, label = { Text("Password") })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {onLoginSuccess(email)}){
            Text("Login")
        }

        Spacer(modifier = Modifier.height(24.dp))

        TextButton(onClick = {}) {
            Text("Forgot Password")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = launchGoogleSheet) {
            Text("Sign in with google")
        }
    }
}