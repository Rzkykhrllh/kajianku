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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlin.random.Random

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
                            tagId = document.get("tag") as? List<String> ?: emptyList(),
                            status = document.getString("status") ?: "",
                            startedAt = document.getDate("date")?.time ?: 1611912600,
                            speaker = document.getString("speaker") ?: "",
                            registerUrl = document.getString("registration") ?: "",
                            location = document.getString("Location") ?: "",
                            latitude = document.getGeoPoint("location_uri")?.latitude ?: 0.0,
                            longitude = document.getGeoPoint("location_uri")?.longitude ?: 0.0,
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
                list.add(
                    Kajian(
                        id = document.id,
                        title = document.getString("title") ?: "",
                        imageUrl = document.getString("pict") ?: "",
                        description = document.getString("description") ?: "",
                        organizer = document.getString("organizer") ?: "",
                        tagId = document.get("tag") as? List<String> ?: emptyList(),
                        status = document.getString("status") ?: "",
                        startedAt = document.getDate("date")?.time ?: 0,
                        speaker = document.getString("speaker") ?: "",
                        registerUrl = document.getString("registration") ?: "",
                        location = document.getString("Location") ?: "",
                        latitude = document.getGeoPoint("location_uri")?.latitude ?: 0.0,
                        longitude = document.getGeoPoint("location_uri")?.longitude ?: 0.0,
                        totalSaved = document.getLong("totalSaved") ?: 0L,
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
            val newTotalSaved = snapshot.getLong("totalSaved")?.plus(1L)
            transaction.update(
                saveKajianIdToUserRef,
                "saved_kajian",
                FieldValue.arrayUnion(kajianId)
            )
            transaction.update(selectedKajianRef, "totalSaved", newTotalSaved)
        }.addOnSuccessListener {

        }.addOnFailureListener {
        }
    }

    @ExperimentalCoroutinesApi
    fun queryAllKajianHistory(): Flow<Resource<List<Kajian>>> =
        callbackFlow<Resource<List<Kajian>>> {
            val historyRef = database.collection("users").document(currentUserId)
            val dataRef = database.collection("kajian")

            database.runTransaction { transaction ->
                val history = transaction.get(historyRef)["kajian_history"] as List<String>
                val list = ArrayList<Kajian>()
                history.forEach { kajianId ->
                    val data = transaction.get(dataRef.document(kajianId))
                    list.add(
                        Kajian(
                            id = kajianId,
                            title = data.getString("title") ?: "",
                            imageUrl = data.getString("pict") ?: "",
                            description = data.getString("description") ?: "",
                            organizer = data.getString("organizer") ?: "",
                            tagId = data.get("tag") as? List<String> ?: emptyList(),
                            status = data.getString("status") ?: "",
                            startedAt = data.getDate("date")?.time ?: 0,
                            speaker = data.getString("speaker") ?: "",
                            registerUrl = data.getString("registration") ?: "",
                            location = data.getString("Location") ?: "",
                            latitude = data.getGeoPoint("location_uri")?.latitude ?: 0.0,
                            longitude = data.getGeoPoint("location_uri")?.longitude ?: 0.0,
                            totalSaved = data.get("totalSaved") as? Long ?: 0,
                            time = data.get("time") as? String ?: "--:--"
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
                    val data = transaction.get(dataRef.document(kajianId))

                    list.add(
                        Kajian(
                            id = kajianId,
                            title = data.getString("title") ?: "",
                            imageUrl = data.getString("pict") ?: "",
                            description = data.getString("description") ?: "",
                            organizer = data.getString("organizer") ?: "",
                            tagId = data.get("tag") as? List<String> ?: emptyList(),
                            status = data.getString("status") ?: "",
                            startedAt = data.getDate("date")?.time ?: 0,
                            speaker = data.getString("speaker") ?: "",
                            registerUrl = data.getString("registration") ?: "",
                            location = data.getString("Location") ?: "",
                            latitude = data.getGeoPoint("location_uri")?.latitude ?: 0.0,
                            longitude = data.getGeoPoint("location_uri")?.longitude ?: 0.0,
                            totalSaved = data.get("totalSaved") as? Long ?: 0,
                            time = data.get("time") as? String ?: "--:--"
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
        }.addOnFailureListener {
        }
    }

    @ExperimentalCoroutinesApi
    fun queryAllSuggestedKajian(): Flow<Resource<List<Kajian>>> =
        callbackFlow<Resource<List<Kajian>>> {
            val userDataRef = database.collection("users").document(currentUserId)
            val dataKajianRef = database.collection("kajian")

            database.runTransaction { transaction ->
                val userKajianHistoryId =
                    transaction.get(userDataRef)["kajian_history"] as? List<String>
                val userSavedKajianId =
                    transaction.get(userDataRef)["saved_kajian"] as? List<String>
                val tags = mutableSetOf<String>()

                userKajianHistoryId?.let { kajianHistoryId ->
                    kajianHistoryId.forEach { kajianId ->
                        val listTag =
                            transaction.get(dataKajianRef.document(kajianId))["tag"] as List<String>
                        listTag.forEach {
                            tags.add(it)
                        }
                    }
                }
                Log.d("historytag", tags.toString())

                userSavedKajianId?.let { savedKajianId ->
                    savedKajianId.forEach { kajianId ->
                        val listTag =
                            transaction.get(dataKajianRef.document(kajianId))["tag"] as List<String>
                        listTag.forEach {
                            tags.add(it)
                        }
                    }
                }

                Log.d("savedhistorytag", tags.toString())

                if (tags.isEmpty()) {
                    dataKajianRef.limit(15).get().addOnSuccessListener { querySnapshot ->
                        val list = ArrayList<Kajian>()
                        querySnapshot.documents.forEach { documentSnapshot ->
                            list.add(
                                Kajian(
                                    id = documentSnapshot.id,
                                    title = documentSnapshot.getString("title") ?: "",
                                    imageUrl = documentSnapshot.getString("pict") ?: "",
                                    description = documentSnapshot.getString("description")
                                        ?: "",
                                    organizer = documentSnapshot.getString("organizer") ?: "",
                                    tagId = documentSnapshot.get("tag") as? List<String>
                                        ?: emptyList(),
                                    status = documentSnapshot.getString("status") ?: "",
                                    startedAt = documentSnapshot.getDate("date")?.time ?: 0,
                                    speaker = documentSnapshot.getString("speaker") ?: "",
                                    registerUrl = documentSnapshot.getString("registration")
                                        ?: "",
                                    location = documentSnapshot.getString("Location") ?: "",
                                    latitude = documentSnapshot.getGeoPoint("location_uri")?.latitude
                                        ?: 0.0,
                                    longitude = documentSnapshot.getGeoPoint("location_uri")?.longitude
                                        ?: 0.0,
                                    totalSaved = documentSnapshot.get("totalSaved") as? Long
                                        ?: 0,
                                    time = documentSnapshot.get("time") as? String ?: "--:--"
                                )
                            )
                        }
                        offer(Resource.Success(list))
                    }
                } else {
                    val random = Random(System.nanoTime()).nextInt(tags.size)

                    dataKajianRef.whereArrayContains("tag", tags.elementAt(random)).limit(20).get()
                        .addOnSuccessListener { querySnapshot ->
                            val list = ArrayList<Kajian>()
                            querySnapshot.documents.forEach { documentSnapshot ->
                                list.add(
                                    Kajian(
                                        id = documentSnapshot.id,
                                        title = documentSnapshot.getString("title") ?: "",
                                        imageUrl = documentSnapshot.getString("pict") ?: "",
                                        description = documentSnapshot.getString("description")
                                            ?: "",
                                        organizer = documentSnapshot.getString("organizer") ?: "",
                                        tagId = documentSnapshot.get("tag") as? List<String>
                                            ?: emptyList(),
                                        status = documentSnapshot.getString("status") ?: "",
                                        startedAt = documentSnapshot.getDate("date")?.time ?: 0,
                                        speaker = documentSnapshot.getString("speaker") ?: "",
                                        registerUrl = documentSnapshot.getString("registration")
                                            ?: "",
                                        location = documentSnapshot.getString("Location") ?: "",
                                        latitude = documentSnapshot.getGeoPoint("location_uri")?.latitude
                                            ?: 0.0,
                                        longitude = documentSnapshot.getGeoPoint("location_uri")?.longitude
                                            ?: 0.0,
                                        totalSaved = documentSnapshot.get("totalSaved") as? Long
                                            ?: 0,
                                        time = documentSnapshot.get("time") as? String ?: "--:--"
                                    )
                                )
                            }
                            offer(Resource.Success(list))
                        }
                }
            }
            awaitClose()
        }.flowOn(IO)

    fun deleteSavedKajian(kajianId: String) = CoroutineScope(IO).launch {
        database.collection("users").document(currentUserId)
            .update("saved_kajian", FieldValue.arrayRemove(kajianId))
    }
}