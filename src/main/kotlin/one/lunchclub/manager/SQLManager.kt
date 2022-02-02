package one.lunchclub.manager

import one.lunchclub.MissingNo
import java.sql.*

abstract class SQLManager(plugin: MissingNo) {
    abstract val url: String
    private lateinit var connection: Connection
    abstract fun setupTables()

    init {
        plugin.server.scheduler.runTaskAsynchronously(plugin, (Runnable {
            setupTables()
        }))
    }

    private fun connect() {
        try {
            connection = DriverManager.getConnection(url)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    private fun isConnected(): Boolean {
        return try {
            this::connection.isInitialized && !connection.isClosed
        } catch (e: SQLException) {
            e.printStackTrace()
            false
        }
    }

    fun closeConnection() {
        if (isConnected())
            connection.close()
    }

    fun prepareStatement(sql: String): PreparedStatement {
        if (!isConnected())
            connect()

        return connection.prepareStatement(sql)
    }

    fun executeUpdate(sql: PreparedStatement) {
        if (!isConnected())
            connect()

        try {
            sql.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun executeUpdate(sql: String) {
        if (!isConnected())
            connect()

        try {
            connection.createStatement().executeUpdate(sql)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun executeQuery(sql: PreparedStatement): ResultSet? {
        if (!isConnected())
            connect()

        return try {
            sql.executeQuery()
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }

    fun executeQuery(sql: String): ResultSet? {
        if (!isConnected())
            connect()

        return try {
            connection.createStatement().executeQuery(sql)
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }
}