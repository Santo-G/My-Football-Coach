package com.androidapp.myfootballcoach.networking

import com.androidapp.myfootballcoach.networking.api.*

data class PlayerDTO(
    val players: List<PlayerDTOITem>
)

data class PlayerDTOITem(
    val cell: String,
    val dob: Dob,
    val email: String,
    val gender: String,
    val id: Id,
    val location: Location,
    val login: Login,
    val name: Name,
    val nat: String,
    val phone: String,
    val picture: Picture,
    val registered: Registered
)

