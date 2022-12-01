package com.coco_hkk.coffee

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var numberOfCoffees: Int = 2
    private var orderNumber: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun increment(view: View) {
        if (numberOfCoffees == 10) {
            return
        }

        display(++numberOfCoffees)
    }

    fun decrement(view: View) {
        if (numberOfCoffees == 1) {
            return
        }

        display(--numberOfCoffees)
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun submitOrder(view: View) {
        var toppings = ""

        val whippedCream = findViewById<View>(R.id.whipped_cream_checkbox) as CheckBox
        val chocolate = findViewById<View>(R.id.chocolate_checkbox) as CheckBox
        val noteMessage = findViewById<View>(R.id.note) as EditText

        if (whippedCream.isChecked)
            toppings += "${whippedCream.text},"

        if (chocolate.isChecked)
            toppings += "${chocolate.text},"

        if (toppings.isEmpty())
            toppings = getString(R.string.nothing)

        val note = noteMessage.text.toString()

        val priceMessage: String =
            createOrderSummary(
                calculatePrice(whippedCream.isChecked, chocolate.isChecked),
                toppings,
                note
            )

        orderNumber++

        //Log.v("MainActivity", "The order is " + orderNumber)
        displayMessage(priceMessage)

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // only email apps should handle this
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.extra_subject))
            putExtra(Intent.EXTRA_TEXT, priceMessage)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun display(number: Int) {
        val quantityTextView = findViewById<View>(R.id.quantity_text_view) as TextView
        quantityTextView.text = number.toString()
    }

    private fun displayMessage(message: String) {
        val orderSummaryTextView = findViewById<View>(R.id.order_summary_text_view) as TextView
        orderSummaryTextView.text = message
    }

    private fun calculatePrice(whippedCream: Boolean, chocolate: Boolean): Double {
        var basePrice = 5.0

        if (whippedCream)
            basePrice += 1.0

        if (chocolate)
            basePrice += 2.0

        return numberOfCoffees * basePrice
    }

    @SuppressLint("SetTextI18n", "StringFormatInvalid")
    private fun createOrderSummary(
        totalPrice: Double,
        toppings: String,
        noteMessage: String
    ): String {

        return getString(
            R.string.order_summary,
            orderNumber,
            toppings,
            noteMessage,
            numberOfCoffees,
            totalPrice
        )
    }
}
