package one.lunchclub.manager

import one.lunchclub.MissingNo
import java.io.File

class DataManager(plugin: MissingNo) : SQLManager(plugin) {
    override val url: String = "jdbc:sqlite:${plugin.dataFolder}${File.separatorChar}data.db"
    override fun setupTables() {
        setupNameTable()
    }

    private fun setupNameTable() {
        executeUpdate("""CREATE TABLE IF NOT EXISTS name (
            |player_uuid VARCHAR(255) UNIQUE,
            |player_name VARCHAR(255)
            |);""".trimMargin())
    }
}