package com.example.consumoapibitcoin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.bloque_cotizacion.*
import kotlinx.android.synthetic.main.bloque_data.*
import kotlinx.android.synthetic.main.bloque_entrada.*
import kotlinx.android.synthetic.main.bloque_mayor.*
import kotlinx.android.synthetic.main.bloque_menor.*
import kotlinx.android.synthetic.main.bloque_salida.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.net.URL
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    val API_URL = "https://www.mercadobitcoin.net/api/BTC/ticker/"

    var cotizacionBitcoin: Double = 0.0
    var mayorBitcoin: Double = 0.0
    var menorBitcoin: Double = 0.0
    var dataBitcoin: Long = 0
    private val TAG = "Test"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buscaCotizacion()
        btnCalcular.setOnClickListener(){
            calcular()
        }
    }

    /*
    {"ticker":
    {"high":"360083.00000000",
    "low":"343300.69916000",
    "vol":"83.99825268",
    "last":"355000.00000000",
    "buy":"355000.00001000",
    "sell":"355000.00999000",
    "open":"357928.00000000",
    "date":1636754361}}
     */

    fun buscaCotizacion(){
        doAsync {
            val respuesta = URL(API_URL).readText()
            cotizacionBitcoin = JSONObject(respuesta).getJSONObject("ticker").getDouble("last")
            mayorBitcoin = JSONObject(respuesta).getJSONObject("ticker").getDouble("high")
            menorBitcoin = JSONObject(respuesta).getJSONObject("ticker").getDouble("low")
            dataBitcoin = JSONObject(respuesta).getJSONObject("ticker").getLong("date")
            val f = NumberFormat.getCurrencyInstance(Locale("es", "cl"))
            val date: Date = Date(dataBitcoin * 1000L)
            val formato = "dd/MM/yyyy HH:mm:ss"
            val format = SimpleDateFormat(formato)
            val dataFormateada = format.format(date)
            val cotizacionFormateada = f.format(cotizacionBitcoin * 146)
            val mayorFormateada = f.format(mayorBitcoin * 146)
            val menorFormateada = f.format(menorBitcoin * 146)
            uiThread {
                txtCotizacion.setText("$cotizacionFormateada")
                txtMayor.setText("$mayorFormateada")
                txtMenor.setText("$menorFormateada")
                txtData.setText("$dataFormateada")
            }
        }
    }
    fun calcular(){
        if(txtValor.text.isEmpty()){
            txtValor.error = "Favor llenar con un valor"
            return
        }

        val valorDigitado = txtValor.text.toString()
            .replace(",", ".").toDouble()

        val resultado = if(cotizacionBitcoin>0) valorDigitado / (cotizacionBitcoin * 146)
        else 0.0
        txtQtdBitcoins.text = "%.8f".format(resultado)
    }
}
