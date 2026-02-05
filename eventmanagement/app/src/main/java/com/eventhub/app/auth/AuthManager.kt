package com.eventhub.app.auth

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

object AuthManager {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    
    fun initialize(context: Context) {
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("136657022894-9ri0rfn6c3255e34t2tr5mn03levu6gm.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }
    
    fun isLoggedIn(): Boolean = auth.currentUser != null
    
    fun getCurrentUserEmail(): String? = auth.currentUser?.email
    
    fun getSignInIntent(): Intent = googleSignInClient.signInIntent
    
    fun clearSignInState() {
        googleSignInClient.signOut()
    }
    
    suspend fun signInWithGoogle(data: Intent?): Result<String> {
        return try {
            val account = GoogleSignIn.getSignedInAccountFromIntent(data).await()
            val email = account.email ?: return Result.failure(Exception("Email not found"))
            
            if (!email.endsWith("@bvrithyderabad.edu.in")) {
                // Sign out from Google to allow retry with different account
                googleSignInClient.signOut().await()
                return Result.failure(Exception("Please use your college email ID"))
            }
            
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential).await()
            Result.success(email)
        } catch (e: Exception) {
            // Sign out from Google to allow retry
            try {
                googleSignInClient.signOut().await()
            } catch (ignored: Exception) {}
            Result.failure(e)
        }
    }
    
    fun signOut() {
        auth.signOut()
        googleSignInClient.signOut()
    }
}
