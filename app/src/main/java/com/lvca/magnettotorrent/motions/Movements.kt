package com.lvca.magnettotorrent.motions

fun toTheLeft(offset: Int, initialOffset: Float): Int {
    return (offset * initialOffset).toInt()
}

fun toTheRight(offset: Int, initialOffset: Float): Int {
    return -(offset * initialOffset).toInt()
}