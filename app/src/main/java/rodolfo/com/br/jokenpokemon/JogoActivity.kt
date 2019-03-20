package rodolfo.com.br.jokenpokemon

import android.app.Activity
import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_jogo.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import rodolfo.com.br.jokenpokemon.model.Escolha
import rodolfo.com.br.jokenpokemon.model.Ranking
import rodolfo.com.br.jokenpokemon.viewmodel.JogoViewModel

class JogoActivity : AppCompatActivity() {

    lateinit var jogoViewModel: JogoViewModel
    lateinit var suaEscolha:Escolha
    lateinit var escolhaAdversario:Escolha
    lateinit var nome : String
    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jogo)

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                nome = dataSnapshot.child("nome").value as String
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@JogoActivity, "Erro", Toast.LENGTH_LONG).show()
            }
        }

        mAuth = FirebaseAuth.getInstance()
        if(mAuth.currentUser!=null){
            var ref = FirebaseDatabase.getInstance().getReference("Usuario")
                    .child(mAuth.uid!!).addValueEventListener(valueEventListener)

        }



        jogoViewModel = ViewModelProviders.of(this).get(JogoViewModel::class.java)
        btn_bulbasaur.setOnClickListener {
            iv_jogador.setImageDrawable(resources.getDrawable(R.drawable.bulbasaur))
            suaEscolha=Escolha.BULBASAUR
            logicaJogo()
        }

        btn_charmander.setOnClickListener {
            iv_jogador.setImageDrawable(resources.getDrawable(R.drawable.charmander))
            suaEscolha=Escolha.CHARMANDAER
            logicaJogo()
        }

        btn_squirtle.setOnClickListener {
            iv_jogador.setImageDrawable(resources.getDrawable(R.drawable.squirtle))
            suaEscolha=Escolha.SQUIRTLE
            logicaJogo()
        }


        jogoViewModel.numeroDeVidas.observe(this, Observer {
            var numeroDeVidas = it
            tv_vidas.text = "Vidas : " + numeroDeVidas
            if(numeroDeVidas==0){
                finalizarPartida(Ranking(nome, jogoViewModel.pontos.value!!))
            }
        })

        jogoViewModel.pontos.observe(this, Observer {
            var pontos = it
            tv_pontuacao.text= "Pontos: " + pontos
        })

        jogoViewModel.rodadaIniciada.observe(this, Observer {
            var rodadaIniciada = it
            if(rodadaIniciada==true){
                tv_resultado.visibility = View.VISIBLE
                btn_bulbasaur.visibility = View.INVISIBLE
                btn_charmander.visibility = View.INVISIBLE
                btn_squirtle.visibility = View.INVISIBLE
            }else{
                iv_adversario.setImageDrawable(null)
                iv_jogador.setImageDrawable(null)
                btn_bulbasaur.visibility = View.VISIBLE
                btn_charmander.visibility = View.VISIBLE
                btn_squirtle.visibility = View.VISIBLE
                tv_resultado.visibility = View.INVISIBLE
            }
        })

    }

    private fun finalizarPartida(ranking : Ranking) {
        var FB_DB = FirebaseDatabase.getInstance().getReference("Ranking").child("").push()
                .setValue(ranking).addOnCompleteListener{
                    if(it.isSuccessful){
                        finish()
                    }else{
                        Toast.makeText(applicationContext,it.exception?.message, Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
    }


    private fun logicaJogo(){
        jogoViewModel.iniciaARodada()
        escolhaAdversario = gerarEscolhaDoAdversario()
        if(suaEscolha.equals(Escolha.BULBASAUR)){
            if(escolhaAdversario.equals(Escolha.CHARMANDAER)){
                tv_resultado.text = "Perdeu"
                jogoViewModel.perdeu()
            }else if(escolhaAdversario.equals(Escolha.SQUIRTLE)){
                tv_resultado.text = "Venceu"
                jogoViewModel.adicionarPonto(3)
            }else if(escolhaAdversario.equals(Escolha.BULBASAUR)){
                tv_resultado.text = "Empate"
                jogoViewModel.adicionarPonto(1)
            }
        }
        if(suaEscolha.equals(Escolha.CHARMANDAER)){
            if(escolhaAdversario.equals(Escolha.CHARMANDAER)){
                tv_resultado.text = "Empate"
                jogoViewModel.adicionarPonto(1)
            }else if(escolhaAdversario.equals(Escolha.SQUIRTLE)){
                tv_resultado.text = "Perdeu"
                jogoViewModel.perdeu()
            }else if(escolhaAdversario.equals(Escolha.BULBASAUR)){
                tv_resultado.text = "Venceu"
                jogoViewModel.adicionarPonto(3)
            }
        }
        if(suaEscolha.equals(Escolha.SQUIRTLE)){
            if(escolhaAdversario.equals(Escolha.CHARMANDAER)){
                tv_resultado.text = "Venceu"
                jogoViewModel.adicionarPonto(3)
            }else if(escolhaAdversario.equals(Escolha.SQUIRTLE)){
                tv_resultado.text = "Empate"
                jogoViewModel.adicionarPonto(1)
            }else if(escolhaAdversario.equals(Escolha.BULBASAUR)){
                tv_resultado.text = "Perdeu"
                jogoViewModel.perdeu()
            }
        }
        reniciarRodada()

    }

    private fun reniciarRodada() {
        val runnable = Runnable {
            jogoViewModel.finalizaRodada()
        }

        Handler().postDelayed(runnable,1000)
    }


    private fun gerarEscolhaDoAdversario(): Escolha {
        lateinit var escolha : Escolha
        val random = (0..2).random()
        if(random==0){
            escolha= Escolha.BULBASAUR
            iv_adversario.setImageDrawable(resources.getDrawable(R.drawable.bulbasaur))
        }
        if(random==1){
            escolha = Escolha.CHARMANDAER
            iv_adversario.setImageDrawable(resources.getDrawable(R.drawable.charmander))
        }
        if(random==2){
            escolha = Escolha.SQUIRTLE
            iv_adversario.setImageDrawable(resources.getDrawable(R.drawable.squirtle))
        }
        return escolha!!
    }
}
