package com.example.demowebservices

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.demowebservices.R
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btPesquisar.setOnClickListener {
            pesquisar()
        }

    }

    private fun pesquisar(){
        // Instanciando Stetho
        val OkHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .build()
        // Instanciando Retrofit e chamando Stetho para dentro dele (EM CLIENTE)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient)
            .build()

        val gitHubService = retrofit.create(GitHubService::class.java)
        gitHubService.buscarUsuario(etUsuario.text.toString()).enqueue(object : Callback<Usuario>{
            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                Toast.makeText(this@MainActivity,
                    t.message,
                    Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                if(response.isSuccessful) {
                    val usuario = response.body()
                    tvUsuario.text = usuario?.nome
                    // Picasso serve para pegar imagens remotas e baixar sozinha para nosso repositorio e atualizar quando necessario
                    Picasso.get()
                        .load(usuario?.imagem)
                        .error(R.mipmap.ic_launcher)
                        .placeholder(R.mipmap.ic_launcher)
                        .into(ivUsuario)
                }else{
                    Toast.makeText(this@MainActivity,
                        "Deu ruim!",
                        Toast.LENGTH_LONG).show()
                }
            }


        })
    }
}
