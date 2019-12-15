package com.example.carservice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import androidx.navigation.NavOptions

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)

        val navOptions = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setPopUpTo(navController.graph.startDestination, false)
            .build()

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {}

            override fun onTabUnselected(p0: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    when (tab.position) {
                        0 -> {
                            navController.navigate(R.id.carInfoListFragment, null, navOptions)
                        }
                        1 -> {
                            navController.navigate(R.id.mapFragment, null, navOptions)
                        }
                    }
                }
            }

        })
        mainViewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        mainViewModel.requestCarList()
    }

}
