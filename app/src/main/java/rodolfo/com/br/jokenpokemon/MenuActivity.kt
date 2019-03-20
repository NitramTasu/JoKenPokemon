package rodolfo.com.br.jokenpokemon

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {

    lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        mAuth = FirebaseAuth.getInstance()
        btJogar.setOnClickListener {
            val intent = Intent(this,JogoActivity::class.java)
            startActivity(intent)
        }

        btRanking.setOnClickListener {
            val intent = Intent(this,RankingActivity::class.java)
            startActivity(intent)
        }

        btSair.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
