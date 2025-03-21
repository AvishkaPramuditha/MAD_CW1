package com.example.w1871523_mad_cw1

object ScoreData {
    private var _humanPlayerScore = 0
    private var _computerPlayerScore = 0

    val humanPlayerScore: Int get() = _humanPlayerScore
    val computerPlayerScore: Int get() = _computerPlayerScore


    fun setHumanPlayerScore(score: Int) {
        _humanPlayerScore = score
    }

    fun setComputerPlayerScore(score: Int) {
        _computerPlayerScore = score
    }
}