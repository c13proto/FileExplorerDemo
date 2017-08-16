package com.android.fileexplorerdemo

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.*

import java.io.File
import java.util.ArrayList


class MainActivity : Activity() {

    private var buttonUp: Button?=null//findViewById<View>(R.id.up) as Button
    private var textFolder: TextView?=null//findViewById<View>(R.id.folder) as TextView

    private var dialog_ListView: ListView?=null//findViewById<View>(R.id.dialoglist) as ListView
    private var root: File?=null//File(Environment.getExternalStorageDirectory().absolutePath)
    private var curFolder: File?=null//root
    private var KEY_TEXTPSS = "TEXTPSS"
    private var fileList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val buttonOpenDialog:Button = findViewById<View>(R.id.opendialog) as Button
        //buttonOpenDialog.setOnClickListener { showDialog(CUSTOM_DIALOG_ID) }
        (findViewById<View>(R.id.opendialog) as Button).setOnClickListener{showDialog(CUSTOM_DIALOG_ID)}

        root=File(Environment.getExternalStorageDirectory().absolutePath)
        curFolder=root
    }

    override fun onCreateDialog(id: Int): Dialog? {

        val dialog = Dialog(this@MainActivity)

        when (id) {
            CUSTOM_DIALOG_ID -> {
                dialog.setContentView(R.layout.dialoglayout)
                dialog.setTitle("Custom Dialog")
                dialog.setCancelable(true)
                dialog.setCanceledOnTouchOutside(true)

                textFolder = dialog.findViewById<View>(R.id.folder) as TextView

                buttonUp=dialog.findViewById<View>(R.id.up) as Button
                buttonUp?.setOnClickListener { ListDir(curFolder!!.parentFile) }

                dialog_ListView = dialog.findViewById<View>(R.id.dialoglist) as ListView
                (dialog_ListView as ListView).onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                    val selected = File(fileList[position])
                    if (selected.isDirectory) {
                        ListDir(selected)
                    } else {
                        Toast.makeText(this@MainActivity, selected.toString() + " selected",
                                Toast.LENGTH_LONG).show()
                        dismissDialog(CUSTOM_DIALOG_ID)
                    }
                }
            }
        }
        return dialog
    }

    override fun onPrepareDialog(id: Int, dialog: Dialog) {
        super.onPrepareDialog(id, dialog)
        when (id) {
            CUSTOM_DIALOG_ID -> ListDir(curFolder as File)
        }
    }

    private fun ListDir(f: File) {
//        if (f.path.equals(root?.path)) buttonUp?.isEnabled=false;
//        else  buttonUp?.isEnabled = true
        buttonUp?.isEnabled = (f.path != root?.path)

        curFolder = f
        textFolder?.text = f.path

        //val files = f.listFiles()
        fileList.clear()

        for (file in f.listFiles())  fileList.add(file.path)

        //val directoryList = ArrayAdapter(this,android.R.layout.simple_list_item_1, fileList)
        dialog_ListView?.adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, fileList)
    }

    companion object {
        internal val CUSTOM_DIALOG_ID = 0
    }
}
