package com.charlatano.utils

import java.util.concurrent.ThreadLocalRandom.current as tlr

fun randDouble(min: Double, max: Double) = tlr().nextDouble(min, max)
fun randDouble() = tlr().nextDouble()

fun randInt(min: Int, max: Int) = tlr().nextInt(min, max)
fun randInt(min: Int) = tlr().nextInt(min)
fun randInt() = tlr().nextInt()

fun randLong(min: Long, max: Long) = tlr().nextLong(min, max)
fun randLong(min: Long) = tlr().nextLong(min)
fun randLong() = tlr().nextLong()

fun randBoolean() = tlr().nextBoolean()