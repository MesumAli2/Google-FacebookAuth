package com.mesum.googlesignfirebasefirestoredatabase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.mesum.googlesignfirebasefirestoredatabase.databinding.ActivityDashBoardBinding

class DashBoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashBoardBinding
    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        mAuth = FirebaseAuth.getInstance()

        binding.name.setText(mAuth.currentUser?.displayName.toString())

        binding.lgtBtn.setOnClickListener {
                val user = mAuth.currentUser
                Toast.makeText(this, "User ${user?.displayName} is logged", Toast.LENGTH_SHORT).show()

                mAuth.signOut()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            }
        }

    }
