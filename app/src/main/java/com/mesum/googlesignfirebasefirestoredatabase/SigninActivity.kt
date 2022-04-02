package com.mesum.googlesignfirebasefirestoredatabase

import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mesum.googlesignfirebasefirestoredatabase.databinding.ActivitySigninBinding
import java.lang.Exception


private const val TAG = "SignInActivity"
class SigninActivity :  AppCompatActivity() {
private lateinit var mAuth : FirebaseAuth
private lateinit var googleSignInClient: GoogleSignInClient
private lateinit var binding : ActivitySigninBinding
private lateinit var fStore : FirebaseFirestore



companion object{
    private const val RC_SIGN_IN = 120
}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val gson = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gson)

        //Firebase auth
        mAuth = FirebaseAuth.getInstance()
        binding.signInButton.setSize(SignInButton.SIZE_WIDE)
        binding.signInButton.setOnClickListener {
            signIn()
        }

    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Result returned from launching the intent from the googleSignInApi.signInIntent
        if (requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful){
                try {
                    //Google Sign in was successful, authenticate with firebase
                    val account = task.getResult(ApiException::class.java)
                    Log.d(TAG, "firebaseAuthGoogle: ${account.id}" )
                    firebaseAuthWithGoogle(account.idToken!!)
                }catch (e : ApiException){
                    //Google sign in failed, update UI appropiately
                    Log.w(TAG, "Google sign in failed", e)
                }
            }   else{
                Log.w(TAG, exception.toString())

            }

        }

    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) {
                if (it.isSuccessful){

                    //Add User to Firebase
                     saveUser()

                    Log.d(TAG, "Sing in with credential is a success")
                    val intent = Intent(this, DashBoardActivity::class.java)
                    startActivity(intent)
                } else{
                    //if sign in fails, display a message to the user
                    Log.w(TAG, " Sign in with credential failed!!")
                    Snackbar.make(binding.root, "Authentication Failed", Snackbar.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUser() {
        val db = Firebase.firestore
        val user = User(mAuth.currentUser?.displayName.toString(), mAuth.currentUser?.email.toString())
        db.collection("users")
            .document(mAuth.uid!!)
            .set(user).addOnSuccessListener() {
                Toast.makeText(this, "user added successfully ", Toast.LENGTH_SHORT).show()
            }
    }


}