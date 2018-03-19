package com.example.j.findingpokemon

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.io.FileNotFoundException
import java.io.IOException
import java.net.URL
import java.nio.file.Files.exists

class MainActivity : AppCompatActivity() {


    private val url = "https://pokeapi.co/api/v2/pokemon/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        search_btn.setOnClickListener {
            scrollView1.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            doAsync {
                val pokeSearched = pokeSearched_et.text.toString()
                val resultJson = URL(url + pokeSearched + "/").readText()
                uiThread {
                    val jsonObj = JSONObject(resultJson)
                    val name = jsonObj.getString("name")
                    val weight = jsonObj.getString("weight")
                    val height = jsonObj.getString("height")
                    val base_experience = jsonObj.getString("base_experience")
                    val sprites = jsonObj.getString("sprites")
                    val jsonObj1 = JSONObject(sprites)
                    val typeList = ArrayList<String>()
                    var indexLooper = 0
                    val pokeType = jsonObj.getJSONArray("types").getJSONObject(indexLooper).getJSONObject("type").getString("name")
                   /* for (i in 1..10) {
                        if (jsonObj.getJSONArray("types").getJSONObject(indexLooper) != null) {


                            typeList.add(pokeType)
                            Toast.makeText(this@MainActivity, pokeType, Toast.LENGTH_LONG).show()
                            indexLooper++
                        }


                    }*/

                    val image = jsonObj1.getString("front_default")
                    val Capitalizes = name.substring(0, 1).toUpperCase() + name.substring(1)
                    val myPokemonData = PokemonData(Capitalizes, image, weight, height, base_experience,pokeType)
                    setViews(myPokemonData)
                }
            }


        }
    }

    fun setViews(MyPokeData: PokemonData) {
        progressBar.visibility = View.GONE
        scrollView1.visibility = View.VISIBLE

        pokeName_tv.text = MyPokeData.pokeName
        weightScale_tv.text = MyPokeData.pokeWeight
        heightScale_tv.text = MyPokeData.pokeHeight
        expScale_tv.text = MyPokeData.pokeBaseExp
        val PokeImage = poke_image
        Picasso.with(this@MainActivity).load(MyPokeData.pokeImage).into(PokeImage)
        typeScale_tv.text= MyPokeData.pokeType
    }

    private val mTag = "PokeAPI"

}
