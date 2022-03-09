package com.androidapp.myfootballcoach.domain

import com.androidapp.myfootballcoach.networking.api.Id
import com.androidapp.myfootballcoach.networking.api.Name
import com.androidapp.myfootballcoach.networking.api.Picture
import java.io.Serializable

class Player(
    val id: Id,
    val name: Name,
    val gender: String,
    val picture: Picture,
) : Serializable
