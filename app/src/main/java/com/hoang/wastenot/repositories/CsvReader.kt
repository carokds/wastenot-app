package com.hoang.wastenot.repositories

import android.content.Context
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class CSVReader(val context: Context, val fileName: String) {

    var rows: MutableList<Array<String>> = ArrayList()

    @Throws(IOException::class)
    fun readCSV(): MutableList<Array<String>> {
        val input: InputStream = context.resources.openRawResource(
            context.resources.getIdentifier(
                fileName,
                "raw", context.packageName
            )
        )
        val isr = InputStreamReader(input)
        val br = BufferedReader(isr)
        val csvSplitBy = ";"

        var line = br.readLine()
        while (line != null) {
            val row = line.split(csvSplitBy.toRegex()).toTypedArray()
            rows.add(row)
            line = br.readLine()
        }
        return rows
    }

}