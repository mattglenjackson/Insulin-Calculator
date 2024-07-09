package au.edu.jcu.insulincalculator

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import au.edu.jcu.insulincalculator.databinding.ActivityMainBinding
// Allow for making sure conversion to 2 decimal places is displayed in UK formatting
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPrefs: SharedPreferences

    companion object {
        const val KEY_CARB_RATIO = "carbRatio"
        const val ZERO = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get a reference to SharedPreferences for Activity
        sharedPrefs = getPreferences(Context.MODE_PRIVATE) ?: return

        getCarbRatio()

        // On Calculate Insulin Dosage Button click.
        binding.calculateButton.setOnClickListener {
            Log.i("MainActivity", "Calculate Insulin Dosage button pressed")
            if (checkForValidInput()) {
                calculateInsulinDosage()
                Log.i("MainActivity", "Insulin Dosage Calculated Successfully")
            }
        }

        // On Settings Button click.
        binding.settingsButton.setOnClickListener {
            Log.i("MainActivity", "Settings button pressed")
            openSettingsActivity()
        }

    }

    private fun checkForValidInput(): Boolean {
        // Check Total Carbs and Insulin Ratio values for valid input.
        val totalCarbs = binding.editCarbValue.text.toString()
        val carbRatio = binding.carbRatioValue.text.toString().toDouble()

        // Check Total Carbs is not empty string or less than or equal to zero.
        if (totalCarbs == "" || totalCarbs.toInt() <= ZERO) {
            Toast.makeText(this, "Please enter a total carb amount.", Toast.LENGTH_SHORT)
                .show()
            Log.i("MainActivity", "Total Carbs Amount is not valid")
            return false
        }

        // Check Carb Ratio is not less than or equal to zero.
        if (carbRatio <= ZERO) {
            Toast.makeText(this, "Please enter a carb ratio greater than 0.", Toast.LENGTH_SHORT)
                .show()
            Log.i("MainActivity", "Insulin Ratio is not valid")
            return false
        }
        Toast.makeText(this, "Insulin Dosage Calculated!", Toast.LENGTH_SHORT)
            .show()
        return true
    }

    private fun calculateInsulinDosage() {
        val carbRatio = binding.carbRatioValue.text.toString().toDouble()
        val totalCarbs = binding.editCarbValue.text.toString().toDouble()
        var dosage = ((totalCarbs / 10) * carbRatio)

        // Format dosage variable to two decimal points.
        dosage = String.format(Locale.UK, "%.2f", dosage).toDouble()

        // Update insulin_dosage TextView to dosage amount.
        binding.insulinDosageValue.text = dosage.toString()
    }

    private fun getSharedPrefs() {
        val key = KEY_CARB_RATIO
        val carbRatio = sharedPrefs.getString(key, ZERO.toString())
        binding.carbRatioValue.text = carbRatio.toString()
        Log.i("MainActivity", "carbRatio loaded from sharedPrefs")
    }

    private fun getCarbRatio() {
        // Check if intent has Extra values and update view.
        if (intent.hasExtra(KEY_CARB_RATIO)) {
            val carbRatio = intent.getStringExtra(KEY_CARB_RATIO)
            binding.carbRatioValue.text = carbRatio
            Log.i("MainActivity", "carbRatio loaded from intent")

        } else {
            // Get sharedPrefs for Main Activity if intent has no extras.
            getSharedPrefs()
        }
        // Write carbRatio value to Main Activity sharedPrefs.
        writeSharedPrefs()
    }

    private fun openSettingsActivity() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun writeSharedPrefs() {
        with(sharedPrefs.edit()) {
            putString(SettingsActivity.KEY_CARB_RATIO, binding.carbRatioValue.text.toString())
            apply()
        }
    }
}



