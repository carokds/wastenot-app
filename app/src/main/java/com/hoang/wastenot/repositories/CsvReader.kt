package com.hoang.wastenot.repositories

import android.content.Context
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class CSVReader(val context: Context, private val fileName: String) {

    private var rows: MutableList<String> = ArrayList()

    @Throws(IOException::class)
    fun readCSV(): MutableList<String> {
        val input: InputStream = context.resources.openRawResource(
            context.resources.getIdentifier(fileName, "raw", context.packageName)
        )
        val isr = InputStreamReader(input)
        val br = BufferedReader(isr)

        var line = br.readLine()
        while (line != null) {
            rows.add(line)
            line = br.readLine()
        }
        return rows
    }

}