package com.example.addverb.activities

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.AsyncTask.execute
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.addverb.R
import com.example.addverb.entities.db
import com.example.addverb.entities.entity
import com.example.addverb.entities.entityForRecyclerview
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.ByteArrayBuffer
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.InputStream
import java.lang.Exception
import java.net.URL
import java.net.URLConnection
import java.util.HashMap
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import android.graphics.BitmapFactory
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import java.io.IOException


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        val db=db.getDb(applicationContext).countriesDao()
        class addCountryTask() : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg params: Void?): String? {
                // ...
                var shown=""
                 if(db.getallcountries().size>0) {
                     shoFromRoomDb()
                     shown="Showing from Room DB"
                 }else {
                     showFromApi()
                     shown="Showing from Api"
                 }
                return shown
            }

            override fun onPreExecute() {
                super.onPreExecute()

                // ...
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                Toast.makeText(applicationContext,
                    "$result",Toast.LENGTH_SHORT).show()

                // ...
            }
        }
        addCountryTask().execute()
        findViewById<Button>(R.id.delete).setOnClickListener{
            val db= com.example.addverb.entities.db.getDb(applicationContext).countriesDao()
            class addCountryTask() : AsyncTask<Void, Void, String>() {
                override fun doInBackground(vararg params: Void?): String? {
                    // ...
                    db.deleteAll()

                    return db.getallcountries().size.toString()
                }

                override fun onPreExecute() {
                    super.onPreExecute()

                    // ...
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onPostExecute(result: String?) {
                    Toast.makeText(applicationContext,
                        "Database Deleted,Entries left= $result",Toast.LENGTH_SHORT).show()
                    super.onPostExecute(result)

                    // ...
                }
            }
            addCountryTask().execute()
        }

    }

    private fun shoFromRoomDb() {
        var list= arrayListOf<entity>()

        val db=db.getDb(applicationContext).countriesDao()
        class addCountryTask() : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg params: Void?): String? {
                // ...
               list= db.getallcountries() as ArrayList<entity>
                val s=list
                return null
            }

            override fun onPreExecute() {
                super.onPreExecute()

                // ...
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                var recyclerView=findViewById<RecyclerView>(R.id.recyclerView)
                recyclerView.layoutManager=LinearLayoutManager(applicationContext)
                var adapter=adapter(applicationContext,null,list)
                recyclerView.adapter=adapter
                findViewById<ProgressBar>(R.id.pb).visibility= View.GONE


                // ...
            }
        }
        addCountryTask().execute()

    }

    private fun showFromApi() {
        val url = "https://restcountries.com/v3.1/region/asia"
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.DEPRECATED_GET_OR_POST, url,
            { response ->
                try {
                    val resAll=JSONArray(response)
                    var list= arrayListOf<entityForRecyclerview>()
                    var list2= arrayListOf<entity>()
                    var recyclerView=findViewById<RecyclerView>(R.id.recyclerView)
                    recyclerView.layoutManager=LinearLayoutManager(applicationContext)
                    var adapter=adapter(applicationContext,list,null)
                    recyclerView.adapter=adapter
                    var db=db.getDb(applicationContext).countriesDao()
                    for (i in 0..resAll.length()-1){
                            val obj=resAll.getJSONObject(i)
                            val country_name=obj.getJSONObject("name").getString("common")
                            var capital=""
                            if(obj.has("capital")) {

                                capital = obj.getJSONArray("capital").get(0).toString()
                            }
                            val flag=obj.getJSONObject("flags").getString("png")
                            val region=obj.getString("region")
                            val subregion=obj.getString("subregion")
                            val population=obj.getInt("population")
                            var borders = ""
                            if(obj.has("borders")) {

                                val bordersArray = obj.getJSONArray("borders")
                                for (j in 0..bordersArray.length() - 1) {
                                    if (j + 1 == bordersArray.length())
                                        borders += bordersArray.getString(j)
                                    else
                                        borders += bordersArray.getString(j) + ","
                                }
                            }
                            val langobj=obj.getJSONObject("languages")
                            val langkeys=langobj.keys()
                            var languages=""
                            for (key in langkeys){
                                languages+=langobj.getString(key)+","
                            }
                        var entity=entity()
                        entity.name=country_name
                        entity.capital=capital
                        entity.borders=borders
                        entity.languages=languages
                        var imageBytes:ByteArray?=null
                        val thread = Thread {
                            try {
                                imageBytes=getByteArrayImage(flag)!!
                                entity.flag=imageBytes!!
                                val s=""

                                entity.region=region
                                entity.subregion=subregion
                                entity.population=population.toString()

                                class addCountryTask() : AsyncTask<Void, Void, String>() {
                                    override fun doInBackground(vararg params: Void?): String? {
                                        // ...
                                        db.inserCountry(entity)
                                        return db.getallcountries().size.toString()
                                    }

                                    override fun onPreExecute() {
                                        super.onPreExecute()
                                        // ...
                                    }

                                    override fun onPostExecute(result: String?) {
                                        super.onPostExecute(result)
                                        if(resAll.length().toString()==result)
                                        Toast.makeText(applicationContext,
                                            "Added All Countries to Room DB",Toast.LENGTH_SHORT).show()
                                        // ...
                                    }
                                }
                                addCountryTask().execute()
                                //Your code goes here
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }

                        thread.start()

                       var entity2= entityForRecyclerview()
                        entity2.name=country_name
                        entity2.capital=capital
                        entity2.borders=borders
                        entity2.languages=languages
                        entity2.flag=flag
                        entity2.region=region
                        entity2.subregion=subregion
                        entity2.population=population.toString()
                        list.add(entity2)
                        adapter.notifyDataSetChanged()
                        findViewById<ProgressBar>(R.id.pb).visibility= View.GONE
                    }


                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener {
                val error=it.localizedMessage
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val param: MutableMap<String, String> = HashMap()
                return param
            }
        }

        val queue = Volley.newRequestQueue(applicationContext)
        queue.add(stringRequest)
    }
    private fun getByteArrayImage(url: String): ByteArray? {
        val bmp: Bitmap = loadBitmap(url) //w w w.  ja v a 2s.c o m

        if (bmp != null) {
            val stream = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
            bmp.recycle()
            return stream.toByteArray()
        }
        return null

    }
    fun loadBitmap(url: String?): Bitmap {
        var bm: Bitmap? = null
        var `is`: InputStream? = null
        var bis: BufferedInputStream? = null
        try {
            val conn = URL(url).openConnection()
            conn.connect()
            `is` = conn.getInputStream()
            bis = BufferedInputStream(`is`, 8192)
            bm = BitmapFactory.decodeStream(bis)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (bis != null) {
                try {
                    bis.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            if (`is` != null) {
                try {
                    `is`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return bm!!
    }
}