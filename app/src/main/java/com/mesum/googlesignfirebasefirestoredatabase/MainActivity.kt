 package com.mesum.googlesignfirebasefirestoredatabase

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.mesum.googlesignfirebasefirestoredatabase.databinding.ActivityMainBinding
import com.mesum.googlesignfirebasefirestoredatabase.databinding.ActivitySigninBinding

 class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        mAuth = FirebaseAuth.getInstance()

        //Delay set fot the splash Screen
        Handler().postDelayed({
          val user = mAuth.currentUser
            if (user != null){
                Toast.makeText(this, "Welcome ${user.displayName}", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, DashBoardActivity::class.java)
                startActivity(intent)
                finish()

            }
            else{
                val activity = Intent(this, SigninActivity::class.java)
                startActivity(activity)
                finish()
            }
        }, 2000)
    }
}