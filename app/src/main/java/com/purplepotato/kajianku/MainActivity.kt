package com.purplepotato.kajianku

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.purplepotato.kajianku.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.chipNavBar.setItemSelected(R.id.homeFragment)

        val navController = Navigation.findNavController(this, R.id.fragment_container)
        NavigationUI.setupWithNavController(binding.bottomNavBar, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.detailFragment -> binding.chipNavBar.collapse()
                R.id.profileFragment -> binding.chipNavBar.expand()
                R.id.homeFragment -> binding.chipNavBar.expand()
                R.id.savedKajianFragment -> binding.chipNavBar.expand()
            }
        }

        binding.chipNavBar.setOnItemSelectedListener { itemId ->
            binding.bottomNavBar.selectedItemId = itemId
        }
    }
}