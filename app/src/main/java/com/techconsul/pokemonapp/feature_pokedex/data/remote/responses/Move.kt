package com.techconsul.pokemonapp.feature_pokedex.data.remote.responses


import com.google.gson.annotations.SerializedName

data class Move(
    val move: MoveX,
    @SerializedName("version_group_details")
    val versionGroupDetails: List<VersionGroupDetail>
)