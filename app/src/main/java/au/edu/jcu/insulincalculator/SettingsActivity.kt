package au.edu.jcu.insulincalculator

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import au.edu.jcu.insulincalculator.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var sharedPrefs: SharedPreferences

    companion object {
        const val KEY_CARB_RATIO = "carbRatio"
        const val ZERO = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get a reference to SharedPreferences for Settings Activity
        sharedPrefs = getPreferences(Context.MODE_PRIVATE) ?: return

        // Get value for carbRatio stored in sharedPrefs
        getSharedPrefs()

        binding.setRatioButton.setOnClickListener {
            Log.i("SettingsActivity", "Set Ratio Button pressed")
            writeSharedPrefs(binding.editCarbRatio.text.toString())

        }

        binding.homeButton.setOnClickListener {
            Log.i("SettingsActivity", "Home Button pressed")
            openMainActivity()
        }
    }

    private fun getSharedPrefs() {
        val valueFromPrefs = sharedPrefs.getString(KEY_CARB_RATIO, ZERO.toString())
        binding.currentCarbRatio.text = valueFromPrefs.toString()
        Log.i("SettingsActivity", "Get carbRatio from sharedPrefs")

    }

    private fun getValidInput(): Boolean {
        val carbRatio = binding.editCarbRatio.text.toString()

        if (carbRatio == "") {
            // Check Carb Ratio is not empty string.
            Toast.makeText(this, "Carb Ratio cannot be empty.", Toast.LENGTH_SHORT)
                .show()
            Log.i("SettingsActivity", "carbRatio cannot be empty")
            return false

        } else if (carbRatio.toDouble() <= ZERO || carbRatio.toDouble() > 5) {
            // Check Carb Ratio is greater than zero and less than or equal to 5
            Toast.makeText(this, "Carb Ratio must be set between 0 and 5.", Toast.LENGTH_SHORT).show()
            Log.i("SettingsActivity", "carbRatio must be set between 0 and 5")
            return false
        }
         else {
            Toast.makeText(this, "Carb Ratio is set!", Toast.LENGTH_SHORT)
                .show()
            Log.i("SettingsActivity", "carbRatio set")
            return true
        }
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(KEY_CARB_RATIO, binding.currentCarbRatio.text.toString())
        startActivity(intent)
        finish()
    }

    private fun writeSharedPrefs(value: String) {
        if (getValidInput()) {
            with(sharedPrefs.edit()) {
                putString(KEY_CARB_RATIO, value)
                binding.editCarbRatio.setText("")
                binding.currentCarbRatio.text = value
                apply()
                Log.i("SettingsActivity", "Wrote carbRatio to sharedPrefs.")
            }
        } else {
            Log.i("SettingsActivity", "SharedPrefs not written input validation failed")
        }
    }
}

