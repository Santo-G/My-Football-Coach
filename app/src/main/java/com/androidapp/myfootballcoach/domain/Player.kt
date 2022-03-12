package com.androidapp.myfootballcoach.domain

import com.androidapp.myfootballcoach.networking.api.Name
import com.androidapp.myfootballcoach.networking.api.Picture
import java.io.Serializable

class Player(
    val name: Name,
    val picture: Picture
) : Serializable
