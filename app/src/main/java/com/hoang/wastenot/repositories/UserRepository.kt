package com.hoang.wastenot.repositories

import android.content.Context
import androidx.activity.result.ActivityResultRegistry
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.FirebaseAuth
import com.hoang.wastenot.R

import com.hoang.wastenot.models.User

class UserRepository {

    private val auth = FirebaseAuth.getInstance()

    fun getCurrentUser(): User? {
        val email = auth.currentUser?.email
        val displayName = auth.currentUser?.displayName

        if (email != null && displayName != null) {
            return User(displayName, email)
        } else {
            return null
        }
    }

    fun launchSignIn(
        activityResultRegistry: ActivityResultRegistry,
        resultCallback: (Boolean) -> Unit
    ) {
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
        )

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.drawable.wn_logo_white)
            .setTheme(R.style.Theme_WasteNot)
            .build()

        val signInLauncher = activityResultRegistry.register(
            "User Repository",
            FirebaseAuthUIActivityResultContract()
        ) { result ->
            resultCallback(result.resultCode == AppCompatActivity.RESULT_OK)
        }
        signInLauncher.launch(signInIntent)
    }

    fun signOut(context: Context, callback: () -> Unit) {
        AuthUI.getInstance()
            .signOut(context)
            .addOnCompleteListener {
                callback()
            }
    }

}