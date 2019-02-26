package rodolfo.com.br.jokenpokemon

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val CADASTRO_REQUEST_CODE =1
    private val CADASTRO_RESULT_CODE = 2
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()
        if(mAuth.currentUser!=null){
            vaiParaTelaMenu()
        }

        btn_cadastrar.setOnClickListener{
            var intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivityForResult(intent,1)
        }

        btn_logar.setOnClickListener {
            realizarLogin()
        }
    }

    private fun realizarLogin() {
        mAuth.signInWithEmailAndPassword(et_email.text.toString(),et_senha.text.toString())
                .addOnCompleteListener{
                    if(it.isSuccessful){
                        vaiParaTelaMenu()
                    }else{
                        Toast.makeText(applicationContext,"Email ou senha inválidos",Toast.LENGTH_SHORT).show()
                    }
                }

    }

    private fun vaiParaTelaMenu() {
        var intent = Intent(this@LoginActivity, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==CADASTRO_REQUEST_CODE && resultCode==Activity.RESULT_OK)
                //vaiParaTelaMenu()
                et_email.setText(data?.getStringExtra("email"))
                et_senha.setText(data?.getStringExtra("senha"))
        }
}
