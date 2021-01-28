package com.purplepotato.kajianku.core.data.remote.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.purplepotato.kajianku.core.Resource
import com.purplepotato.kajianku.core.data.remote.ApiResponse
import com.purplepotato.kajianku.core.domain.Kajian
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.take
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

    fun queryAllPopularKajian(): Flow<Resource<List<Kajian>>> =
        flow<Resource<List<Kajian>>> {
            emit(Resource.Loading())
            database.collection("kajian").orderBy("total_saved", Query.Direction.DESCENDING)
                .limit(6).get().addOnSuccessListener { querySnapshot ->
                    val list = ArrayList<Kajian>()
                    querySnapshot.forEach { document ->
                        list.add(
                            Kajian(
                                id = document.id,
                                title = document.getString("title") ?: "",
                                imageUrl = document.getString("pict") ?: "",
                                description = document.getString("description") ?: "",
                                organizer = document.getString("organizer") ?: "",
                                tagId = emptyList(),
                                status = document.getString("status") ?: "",
                                startedAt = document.getTimestamp("date")?.seconds ?: 0,
                                speaker = document.getString("speaker") ?: "",
                                registerUrl = document.getString("registration") ?: "",
                                location = document.getString("Location") ?: "",
                                latitude = 0.0,
                                longitude = 0.0,
                                totalSaved = document.getLong("total_saved") ?: 0L,
                            )
                        )
                    }
                    CoroutineScope(IO).launch {
                        emit(Resource.Success(list))
                    }
                }.addOnFailureListener {
                    CoroutineScope(IO).launch {
                        emit(Resource.Error(it.message.toString(), emptyList()))
                    }
                }
        }.flowOn(IO).take(2)

    fun queryAllKajian(): Flow<Resource<List<Kajian>>> = flow<Resource<List<Kajian>>> {
        emit(Resource.Loading())
        database.collection("kajian").get().addOnSuccessListener { querySnapshot ->
            val list = ArrayList<Kajian>()
            querySnapshot.forEach { document ->
                list.add(
                    Kajian(
                        id = document.id,
                        title = document.getString("title") ?: "",
                        imageUrl = document.getString("pict") ?: "",
                        description = document.getString("description") ?: "",
                        organizer = document.getString("organizer") ?: "",
                        tagId = document.get("tag") as List<String>,
                        status = document.getString("status") ?: "",
                        startedAt = document.getTimestamp("date")?.seconds ?: 0,
                        speaker = document.getString("speaker") ?: "",
                        registerUrl = document.getString("registration") ?: "",
                        location = document.getString("Location") ?: "",
                        latitude = 0.0,
                        longitude = 0.0,
                        totalSaved = document.getLong("total_saved") ?: 0L,
                    )
                )
            }
            CoroutineScope(IO).launch {
                emit(Resource.Success(list))
            }
        }.addOnFailureListener {
            CoroutineScope(IO).launch {
                emit(Resource.Error(it.message.toString(), emptyList()))
            }
        }

    }.flowOn(IO).take(2)

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

    fun queryAllKajianHistory(): Flow<Resource<List<Kajian>>> = flow<Resource<List<Kajian>>> {
        emit(Resource.Loading())
        emit(Resource.Success(emptyList()))
    }.flowOn(IO).take(1)

    fun queryAllSavedKajian(): Flow<ApiResponse<List<Kajian>>> =
        flow<ApiResponse<List<Kajian>>> {
            val userTagRef = database.collection("users").document(currentUserId)
            val allKajianRef = database.collection("kajian").document()
            val list = ArrayList<Kajian>()
            database.runTransaction { transaction ->
                val tagSnapshot = transaction.get(userTagRef).get("saved_kajian") as List<String>
                val kajianSnapshot = transaction.get(allKajianRef)
                for (i in tagSnapshot.indices) {

                }
                list
            }.addOnSuccessListener {
                Log.d("queryAllSavedKajian", it.toString())
                CoroutineScope(IO).launch {
                    emit(ApiResponse.Success(it))
                }
            }.addOnFailureListener {
                Log.d("queryAllSavedKajian", it.toString())
                CoroutineScope(IO).launch {
                    emit(ApiResponse.Empty)
                }
            }

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
}