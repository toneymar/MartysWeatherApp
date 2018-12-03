package com.example.marty.martysweatherapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import com.example.marty.martysweatherapp.data.City
import kotlinx.android.synthetic.main.dialog_add.*
import kotlinx.android.synthetic.main.dialog_add.view.*

class AddDialog : DialogFragment() {

    interface ItemHandler {
        fun itemCreated(item: City)
    }

    private lateinit var itemHandler: ItemHandler

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is ItemHandler) {
            itemHandler = context
        } else {
            throw RuntimeException(getString(R.string.d_couldnotbeattached))
        }
    }


    private lateinit var cityName: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(getString(R.string.dialog_title))

        val rootView = requireActivity().layoutInflater.inflate(R.layout.dialog_add, null)
        cityName = rootView.etCity
        builder.setView(rootView)

        builder.setPositiveButton("Ok") {
            dialog, which -> //empty
        }

        return builder.create()
    }

    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (cityName.text.isNotEmpty()) {
                handleItemCreate()
                dialog.dismiss()
            }
            else {
                cityName.error = getString(R.string.error_fieldempty)
            }
        }
    }

    private fun handleItemCreate() {
        itemHandler.itemCreated(
                City(null, cityName.text.toString())
        )
    }
}