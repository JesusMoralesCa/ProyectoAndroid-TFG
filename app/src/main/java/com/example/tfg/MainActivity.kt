package com.example.tfg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var  navegation : BottomNavigationView

    private  val  mOnNavMenu = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            //
            R.id.itemFragment1 -> {
                supportFragmentManager.commit {
                    replace<Fragment_Content>(R.id.frameContainer)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
                }

                return@OnNavigationItemSelectedListener true
            }


                    R.id.itemFragment2 -> {
                supportFragmentManager.commit {
                    replace<Fragment_Content>(R.id.frameContainer)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
                }
                return@OnNavigationItemSelectedListener true
            }



                    R.id.itemFragment3 ->{
                supportFragmentManager.commit {
                    replace<Fragment_Profile>(R.id.frameContainer)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
                }
                return@OnNavigationItemSelectedListener true
            }


        }


        false
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navegation = findViewById(R.id.navMenu)
        navegation.setOnNavigationItemSelectedListener(mOnNavMenu)


    supportFragmentManager.commit {
        replace<Fragment_Profile>(R.id.frameContainer)
        setReorderingAllowed(true)
        addToBackStack("replacement")
    }





    }
}