package technokek.alchotracker.ui.activity

import androidx.appcompat.app.AppCompatActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_signup.*
import technokek.alchotracker.R
import technokek.alchotracker.api.AuthListener
import technokek.alchotracker.databinding.ActivitySignupBinding
import technokek.alchotracker.ui.utils.startMainActivity
import technokek.alchotracker.viewmodels.AuthViewModel
import technokek.alchotracker.viewmodels.factories.AuthViewModelFactory

class SignupActivity : AppCompatActivity(), AuthListener, KodeinAware {

    override val kodein by kodein()
    private val factory : AuthViewModelFactory by instance()

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val binding: ActivitySignupBinding = DataBindingUtil.setContentView(this, R.layout.activity_signup)
        viewModel = ViewModelProviders.of(this, factory).get(AuthViewModel::class.java)
        binding.viewmodel = viewModel

        viewModel.authListener = this
    }

    override fun onStarted() {
        progressbar.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        viewModel.setDefaultValue()
        progressbar.visibility = View.GONE
        startMainActivity()
    }

    override fun onFailure(message: String) {
        progressbar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
