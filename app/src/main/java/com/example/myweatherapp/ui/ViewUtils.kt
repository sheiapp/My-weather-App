package com.example.myweatherapp.ui

import android.app.Activity
import android.view.View
import androidx.core.content.ContextCompat
import com.example.myweatherapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar


/**
 * show simple snack bar
 */
fun View.showSnackBar(message: String, length: Int = Snackbar.LENGTH_LONG) {
    val snack = Snackbar.make(this, message, length)
    snack.setBackgroundTint(ContextCompat.getColor(this.context, R.color.black))
    snack.setTextColor(ContextCompat.getColor(this.context, R.color.white))
    snack.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
    snack.show()
}

fun Activity.showDialogBoxWithOutTitle(
    message: String,
    positiveActionName: String, negativeActionName: String,
    positiveAction: MaterialAlertDialogBuilder.() -> Unit,
    negativeAction: MaterialAlertDialogBuilder.() -> Unit
) {
    val dialog = MaterialAlertDialogBuilder(
        this,
        R.style.MaterialAlertDialogStyle
    )
        .setMessage(message)
    dialog.setPositiveButton(positiveActionName) { mDialog, _ ->
        // Respond to positive button press
        dialog.positiveAction()
        mDialog.cancel()
    }
    dialog.setNegativeButton(negativeActionName) { mDialog, _ ->
        // Respond to negative button press
        dialog.negativeAction()
        mDialog.cancel()
    }
    dialog.setCancelable(false)
    dialog.show()

}
