package com.anwesh.uiprojects.treerotview

/**
 * Created by anweshmishra on 16/06/19.
 */

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color

val nodes : Int = 5
val edges : Int = 2
val parts : Int = 2
val scGap : Float = 2.9f
val scDiv : Double = 0.51
val strokeFactor : Int = 90
val sizeFactor : Float = 2.9f
val foreColor : Int = Color.parseColor("#311B92")
val backColor : Int = Color.parseColor("#BDBDBD")
val rFactor : Float = 2.2f
val rotDeg : Float = 90f

fun Int.inverse() : Float = 1f / this
fun Float.scaleFactor() : Float = Math.floor(this / scDiv).toFloat()
fun Float.mirrorValue(a : Int, b : Int) : Float {
    val k : Float = scaleFactor()
    return (1 - k) * a.inverse() + k * b.inverse()
}
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
fun Float.updateValue(dir : Float, a : Int, b : Int) : Float = mirrorValue(a, b) * dir * scGap

fun Canvas.drawTree(i : Int, size : Float, sc : Float, paint : Paint) {
    val sci : Float = sc.divideScale(i, parts)
    var x : Float = 0f
    var y : Float = -size
    var x1 : Float = 0f
    var y1 : Float = 0f
    for (j in 0..(edges)) {
        val yFactor = size * sci.divideScale(j, edges)
        val xFactor = (size / 2) * sci.divideScale(j, edges)
        y += yFactor
        x -= xFactor
        drawCircle(x, y, (size / rFactor), paint)
        drawLine(x1, y1, x, y, paint)
        x1 += xFactor
        y1 += yFactor
    }
}

fun Canvas.drawTRNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = h / (nodes + 1)
    val size : Float = gap / sizeFactor
    val sc1 : Float = scale.divideScale(0, 2)
    val sc2 : Float = scale.divideScale(1, 2)
    paint.color = foreColor
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    paint.strokeCap = Paint.Cap.ROUND
    save()
    translate(w / 2, gap * (i + 1))
    rotate(90f * sc2)
    for (j in 0..(parts - 1)) {
        save()
        scale(1f - 2 * j, 1f)
        drawTree(j, size, sc1, paint)
        restore()
    }
    restore()
}
