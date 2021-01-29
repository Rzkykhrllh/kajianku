package com.purplepotato.kajianku.core.data.remote.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.purplepotato.kajianku.core.Resource
import com.purplepotato.kajianku.core.domain.Kajian
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FireStore {

    companion object {
        @Volatile
        private var instance: FireStore? = null

        fun getInstance(): FireStore = instance ?: synchronized(this) {
            instance ?: FireStore()
        }
    }

    private val database by lazy { FirebaseFirestore.getInstance() }

    private val currentUserId by lazy {
        FirebaseAuth.getInstance().currentUser!!.uid
    }

    @ExperimentalCoroutinesApi
    fun queryAllPopularKajian(): Flow<Resource<List<Kajian>>> =
        callbackFlow<Resource<List<Kajian>>> {
            val dataRef =
                database.collection("kajian").orderBy("totalSaved", Query.Direction.DESCENDING)
                    .limit(6)
            val list = ArrayList<Kajian>()
            val subscription = dataRef.addSnapshotListener { querySnapshot, _ ->

                querySnapshot?.forEach { document ->
                    list.add(
                        Kajian(
                            id = document.id,
                            title = document.getString("title") ?: "",
                            imageUrl = document.getString("pict") ?: "",
                            description = document.getString("description") ?: "",
                            organizer = document.getString("organizer") ?: "",
                            tagId = emptyList(),
                            status = document.getString("status") ?: "",
                            startedAt = document.getDate("date")?.time ?: 1611912600,
                            speaker = document.getString("speaker") ?: "",
                            registerUrl = document.getString("registration") ?: "",
                            location = document.getString("Location") ?: "",
                            latitude = 0.0,
                            longitude = 0.0,
                            totalSaved = document.getLong("totalSaved") ?: 0L,
                            time = document.getString("time") ?: "--:--"
                        )
                    )
                }
                offer(Resource.Success(list))
            }

            awaitClose { subscription.remove() }
        }

    @ExperimentalCoroutinesApi
    fun queryAllKajian(): Flow<Resource<List<Kajian>>> = callbackFlow<Resource<List<Kajian>>> {
        val dataRef = database.collection("kajian")

        val subscription = dataRef.addSnapshotListener { querySnapshot, _ ->
            val list = ArrayList<Kajian>()

            querySnapshot?.forEach { document ->
                Log.d("queryAllKajian", document.getDate("date")?.time.toString())
                list.add(
                    Kajian(
                        id = document.id,
                        title = document.getString("title") ?: "",
                        imageUrl = document.getString("pict") ?: "",
                        description = document.getString("description") ?: "",
                        organizer = document.getString("organizer") ?: "",
                        tagId = emptyList(),
                        status = document.getString("status") ?: "",
                        startedAt = document.getDate("date")?.time ?: 0,
                        speaker = document.getString("speaker") ?: "",
                        registerUrl = document.getString("registration") ?: "",
                        location = document.getString("Location") ?: "",
                        latitude = 0.0,
                        longitude = 0.0,
                        totalSaved = document.getLong("total_saved") ?: 0L,
                        time = document.getString("time") ?: "--:--"
                    )
                )
                offer(Resource.Success(list))
            }
        }
        awaitClose { subscription.remove() }
    }

    fun insertSavedKajian(kajianId: String) = CoroutineScope(IO).launch {

        val selectedKajianRef = database.collection("kajian").document(kajianId)
        val saveKajianIdToUserRef = database.collection("users").document(currentUserId)

        database.runTransaction { transaction ->
            val snapshot = transaction.get(selectedKajianRef)
            val newTotalSaved = snapshot.getLong("total_saved")?.plus(1L)

            transaction.update(
                saveKajianIdToUserRef,
                "saved_kajian",
                FieldValue.arrayUnion(kajianId)
            )
            transaction.update(selectedKajianRef, "total_saved", newTotalSaved)

        }.addOnSuccessListener {
            Log.d("save kajian firestore", it.toString())
        }.addOnFailureListener {
            Log.d("save kajian firestore", it.message.toString())
        }
    }

    @ExperimentalCoroutinesApi
    fun queryAllKajianHistory(): Flow<Resource<List<Kajian>>> =
        callbackFlow<Resource<List<Kajian>>> {
            val historyRef = database.collection("users").document(currentUserId)
            val dataRef = database.collection("kajian")

            database.runTransaction { transaction ->
                val history = transaction.get(historyRef)["history"] as List<String>
                val list = ArrayList<Kajian>()
                history.forEach { kajianId ->
                    val data = transaction.get(dataRef.document(kajianId)).data
                    list.add(
                        Kajian(
                            id = kajianId,
                            title = data?.get("title") as? String ?: "",
                            imageUrl = data?.get("pict") as? String ?: "",
                            description = data?.get("description") as? String ?: "",
                            organizer = data?.get("organizer") as? String ?: "",
                            tagId = emptyList(),
                            status = data?.get("status") as? String ?: "",
                            startedAt = data?.get("date") as? Long ?: 0,
                            speaker = data?.get("speaker") as? String ?: "",
                            registerUrl = data?.get("registration") as? String ?: "",
                            location = data?.get("Location") as? String ?: "",
                            latitude = 0.0,
                            longitude = 0.0,
                            totalSaved = data?.get("total_saved") as? Long ?: 0,
                            time = data?.get("time") as? String ?: "--:--"
                        )
                    )
                }
                offer(Resource.Success(list))
            }
            awaitClose()
        }.flowOn(IO)

    @ExperimentalCoroutinesApi
    fun queryAllSavedKajian(): Flow<Resource<List<Kajian>>> =
        callbackFlow<Resource<List<Kajian>>> {
            val savedKajianIdRef = database.collection("users").document(currentUserId)
            val dataRef = database.collection("kajian")

            database.runTransaction { transaction ->
                val savedKajianId =
                    transaction.get(savedKajianIdRef)["saved_kajian"] as List<String>
                val list = ArrayList<Kajian>()
                savedKajianId.forEach { kajianId ->
                    val data = transaction.get(dataRef.document(kajianId)).data
                    Log.d("queryAllSavedKajian", kajianId)
                    list.add(
                        Kajian(
                            id = kajianId,
                            title = data?.get("title") as? String ?: "",
                            imageUrl = data?.get("pict") as? String ?: "",
                            description = data?.get("description") as? String ?: "",
                            organizer = data?.get("organizer") as? String ?: "",
                            tagId = emptyList(),
                            status = data?.get("status") as? String ?: "",
                            startedAt = 0,
                            speaker = data?.get("speaker") as? String ?: "",
                            registerUrl = data?.get("registration") as? String ?: "",
                            location = data?.get("Location") as? String ?: "",
                            latitude = 0.0,
                            longitude = 0.0,
                            totalSaved = data?.get("total_saved") as? Long ?: 0,
                            time = data?.get("time") as? String ?: "--:--"
                        )
                    )
                }
                offer(Resource.Success(list))
            }
            awaitClose()
        }.flowOn(IO)

    fun deleteSavedKajianAndMoveToUserHistory(kajianId: String) = CoroutineScope(IO).launch {
        val savedKajianIdRef = database.collection("users").document(currentUserId)

        database.runTransaction { transaction ->
            transaction.update(savedKajianIdRef, "kajian_history", FieldValue.arrayUnion(kajianId))
            transaction.update(savedKajianIdRef, "saved_kajian", FieldValue.arrayRemove(kajianId))
        }.addOnSuccessListener {
            Log.d("MoveKajianToHistory", it.toString())
        }.addOnFailureListener {
            Log.d("MoveKajianToHistory", it.message.toString())
        }
    }

    fun queryAllSuggestedKajian(): Flow<Resource<List<Kajian>>> =
        flow<Resource<List<Kajian>>> {
            emit(Resource.Loading())
            emit(Resource.Success(emptyList()))
        }.flowOn(IO).take(2)

    fun deleteSavedKajian(kajianId: String) = CoroutineScope(IO).launch {
        database.collection("users").document(currentUserId)
            .update("saved_kajian", FieldValue.arrayRemove(kajianId))
    }
}