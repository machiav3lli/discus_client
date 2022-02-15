package com.machiav3lli.discus.client.ui.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.machiav3lli.discus.client.R

class EditStringDialog(
    private val titleString: String = "",
    private val oldValue: CharSequence,
    var confirmListener: (String) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val nameEdit = EditText(requireContext())
        nameEdit.setText(oldValue)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(titleString)
        builder.setView(nameEdit)
        builder.setPositiveButton(requireContext().getString(R.string.dialogSave)) { _: DialogInterface?, _: Int ->
            confirmListener(nameEdit.text.toString())
        }
        builder.setNegativeButton(requireContext().getString(R.string.dialogCancel)) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
        return builder.create()
    }
}
