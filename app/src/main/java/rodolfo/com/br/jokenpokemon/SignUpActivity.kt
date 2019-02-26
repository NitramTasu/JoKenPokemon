package rodolfo.com.br.jokenpokemon

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*
import rodolfo.com.br.jokenpokemon.model.Usuario

class SignUpActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()

        btCriar.setOnClickListener {
            cadastrarUsuario()
        }
    }



    private fun cadastrarUsuario() {
        mAuth.createUserWithEmailAndPassword(etEmail.text.toString(), etSenha.text.toString())
                .addOnCompleteListener{
                    if(it.isSuccessful){
                        salvaNoRealtimeDatabase(Usuario(etNome.text.toString(),etEmail.text.toString(),etTelefone.text.toString()));
                    }else{
                        Toast.makeText(applicationContext,it.exception?.message,Toast.LENGTH_SHORT).show()
                    }
                }
    }

    private fun salvaNoRealtimeDatabase(user:Usuario) {
        var FB_DB = FirebaseDatabase.getInstance().getReference("Usuario")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .setValue(user).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(applicationContext,"Usuário cadastrado com sucesso",Toast.LENGTH_LONG).show()
                        var intent = Intent()
                        intent.putExtra("email",user.email)
                        intent.putExtra("senha",etSenha.text.toString())
                        setResult(Activity.RESULT_OK,intent)
                        finish()
                    }else{
                        Toast.makeText(applicationContext,it.exception?.message,Toast.LENGTH_LONG).show()
                    }
                }
    }


}
