package com.fahed.perupass.data.model.firebase

import com.fahed.perupass.domain.model.User
import com.google.firebase.auth.FirebaseUser

object FirebaseUserMapper {
    fun FirebaseUser.toDomain() = User(
        uid = uid,
        displayName = displayName,
        email = email,
        photoUrl = photoUrl?.toString()
    )
}