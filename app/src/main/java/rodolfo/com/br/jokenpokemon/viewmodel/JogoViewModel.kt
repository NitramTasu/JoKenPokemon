package rodolfo.com.br.jokenpokemon.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel


class JogoViewModel : ViewModel(){


    val numeroDeVidas = MutableLiveData<Int>()
    val pontos = MutableLiveData<Int>()
    val rodadaIniciada = MutableLiveData<Boolean>()

    init {
        numeroDeVidas.value = 3
        pontos.value = 0
        rodadaIniciada.value = false
    }

    fun perdeu() {
        numeroDeVidas.value = numeroDeVidas.value?.minus(1)
    }

    fun adicionarPonto(ponto:Int){
        pontos.value = pontos.value?.plus(ponto)
    }

    fun iniciaARodada() {
        rodadaIniciada.value = true
    }

    fun finalizaRodada() {
        rodadaIniciada.value = false
    }
}