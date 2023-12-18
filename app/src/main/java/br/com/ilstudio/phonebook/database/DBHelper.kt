package br.com.ilstudio.phonebook.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import br.com.ilstudio.phonebook.Utils
import br.com.ilstudio.phonebook.model.ContactModel
import br.com.ilstudio.phonebook.model.UserModel

class DBHelper(context: Context): SQLiteOpenHelper(context, "database.db", null, 1) {
    private val utils = Utils()
    private val sql = arrayOf(
        "CREATE TABLE users (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "username TEXT UNIQUE, " +
            "password TEXT" +
        ")",

        "INSERT INTO users " +
                "(username, password) " +
            "VALUES " +
                "('admin', 'd74ff0ee8da3b9806b18c877dbf29bbde50b5bd8e4dad7a3a725000feb82e8f1')",

        "CREATE TABLE contacts (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "user_id INTEGER, " +
            "name TEXT, " +
            "email TEXT, " +
            "phone TEXT, " +
            "image_id INTEGER, " +
            "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
        ")",

        "INSERT INTO contacts " +
                "(user_id, name, email, phone, image_id) " +
            "VALUES " +
                "(1, 'Junior', 'junior@email.com', '123456789', -1)",

        "INSERT INTO contacts " +
                "(user_id, name, email, phone, image_id) " +
            "VALUES " +
                "(1, 'Marta', 'marta@email.com', '123456789', -1)",
        )

    override fun onCreate(db: SQLiteDatabase?) {
        sql.forEach { query ->
            println(query)
            db?.execSQL(query)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun login(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val hashPassword = utils.generateHashString(password)

        val c = db.rawQuery(
            "SELECT * FROM users WHERE username=? AND password=?",
            arrayOf(username, hashPassword)
        )

        var response = false
        if(c.count == 1) response = true

        db.close()
        return response
    }


    /*  CRUD USERS   */
    fun insertUser(username: String, password: String): Long {
        val db = this.writableDatabase
        val contextValues = ContentValues()
        contextValues.put("username", username)

        val hashPassword = utils.generateHashString(password)

        contextValues.put("password", hashPassword)

        val response = db.insert("users", null, contextValues)
        db.close()
        return response
    }

    fun updateUser(id: Int, username: String, password: String): Int {
        val db = this.writableDatabase
        val contextValues = ContentValues()
        contextValues.put("username", username)
        contextValues.put("password", password)

        val response = db.update("users", contextValues, "id=?", arrayOf(id.toString()))
        db.close()
        return response
    }

    fun deleteUser(id: Int): Int {
        val db = this.writableDatabase

        val response = db.delete("users", "id=?", arrayOf(id.toString()))
        db.close()
        return response
    }

    fun getUser(username: String, password: String): UserModel {
        val db = this.readableDatabase

        val hashPassword = utils.generateHashString(password)

        val c = db.rawQuery(
            "SELECT * FROM users WHERE username=? AND password=?",
            arrayOf(username, hashPassword)
        )

        var userModel = UserModel()

        if(c.count == 1) {
            c.moveToFirst()
            val idIndex = c.getColumnIndex("id")
            val usernameIndex = c.getColumnIndex("username")
            val passwordIndex = c.getColumnIndex("password")

            userModel = UserModel(
                id = c.getInt(idIndex),
                username = c.getString(usernameIndex),
                password = c.getString(passwordIndex)
            )
        }

        db.close()
        return userModel
    }

    /*  CRUD CONTACTS   */
    fun insertContact(userId: Int, name: String, email: String, phone: String, imageId: Int): Long {
        val db = this.writableDatabase
        val contextValues = ContentValues()
        contextValues.put("user_id", userId)
        contextValues.put("name", name)
        contextValues.put("email", email)
        contextValues.put("phone", phone)
        contextValues.put("image_id", imageId)

        val response = db.insert("contacts", null, contextValues)
        db.close()
        return response
    }

    fun updateContact(id: Int, name: String, email: String, phone: String, imageId: Int): Int {
        val db = this.writableDatabase
        val contextValues = ContentValues()
        contextValues.put("name", name)
        contextValues.put("email", email)
        contextValues.put("phone", phone)
        contextValues.put("image_id", imageId)

        val response = db.update("contacts", contextValues, "id=?", arrayOf(id.toString()))
        db.close()
        return response
    }

    fun deleteContact(id: Int): Int {
        val db = this.writableDatabase

        val response = db.delete("contacts", "id=?", arrayOf(id.toString()))
        db.close()
        return response
    }

    fun getContact(id: Int): ContactModel {
        val db = this.readableDatabase
        val c = db.rawQuery(
            "SELECT * FROM contacts WHERE id=?",
            arrayOf(id.toString())
        )

        var contactModel = ContactModel()

        if(c.count == 1) {
            c.moveToFirst()
            val idIndex = c.getColumnIndex("id")
            val nameIndex = c.getColumnIndex("name")
            val emailIndex = c.getColumnIndex("email")
            val phoneIndex = c.getColumnIndex("phone")
            val imageIdIndex = c.getColumnIndex("image_id")


            contactModel = ContactModel(
                id = c.getInt(idIndex),
                name = c.getString(nameIndex),
                email = c.getString(emailIndex),
                phone = c.getString(phoneIndex),
                imageId = c.getInt(imageIdIndex)
            )
        }

            db.close()
        return contactModel
    }

    fun getAllContact(userId: Int): ArrayList<ContactModel> {
        val db = this.readableDatabase
        val c = db.rawQuery(
            "SELECT * FROM contacts WHERE user_id=?",
            arrayOf(userId.toString())
        )
        var listContactModel = ArrayList<ContactModel>()

        if(c.count > 0) {
            c.moveToFirst()
            val idIndex = c.getColumnIndex("id")
            val nameIndex = c.getColumnIndex("name")
            val emailIndex = c.getColumnIndex("email")
            val phoneIndex = c.getColumnIndex("phone")
            val imageIdIndex = c.getColumnIndex("image_id")

            do {
                val contactModel = ContactModel(
                    id = c.getInt(idIndex),
                    name = c.getString(nameIndex),
                    email = c.getString(emailIndex),
                    phone = c.getString(phoneIndex),
                    imageId = c.getInt(imageIdIndex)
                )

                listContactModel.add(contactModel)
            }while (c.moveToNext())
        }

        db.close()
        return listContactModel
    }
}