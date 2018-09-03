/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.almibe.svgpath

sealed class SVGCommand {
    abstract fun pathDimensions(): PathDimensions
    abstract fun translate(right: Double, down: Double): SVGCommand
    abstract fun scale(scaleBy: Double): SVGCommand
    abstract fun toPathString(): String
}

data class Move(val x: Double, val y: Double): SVGCommand() {
    override fun toPathString(): String = "M $x $y"
    override fun translate(right: Double, down: Double): SVGCommand = Move(x + right, y + down)
    override fun scale(scaleBy: Double): SVGCommand = Move(x * scaleBy, y * scaleBy)
    override fun pathDimensions() = PathDimensions(x, y, x, y)
}

data class RelativeMove(val x: Double, val y: Double): SVGCommand() {
    override fun toPathString(): String = "m $x $y"
    override fun translate(right: Double, down: Double): SVGCommand = RelativeMove(x + right, y + down)
    override fun scale(scaleBy: Double): SVGCommand = RelativeMove(x * scaleBy, y * scaleBy)
    override fun pathDimensions() = PathDimensions(x, y, x, y)
}

data class Line(val x: Double, val y: Double): SVGCommand() {
    override fun toPathString(): String = "L $x $y"
    override fun translate(right: Double, down: Double): SVGCommand = Line(x + right, y + down)
    override fun scale(scaleBy: Double): SVGCommand = Line(x * scaleBy, y * scaleBy)
    override fun pathDimensions() = PathDimensions(x, y, x, y)
}

data class RelativeLine(val x: Double, val y: Double): SVGCommand() {
    override fun toPathString(): String = "l $x $y"
    override fun translate(right: Double, down: Double): SVGCommand = RelativeLine(x + right, y + down)
    override fun scale(scaleBy: Double): SVGCommand = RelativeLine(x * scaleBy, y * scaleBy)
    override fun pathDimensions() = PathDimensions(x, y, x, y)
}

data class HorizontalLine(val x: Double): SVGCommand() {
    override fun toPathString(): String = "H $x"
    override fun translate(right: Double, down: Double): SVGCommand = HorizontalLine(x + right)
    override fun scale(scaleBy: Double): SVGCommand = HorizontalLine(x * scaleBy)
    override fun pathDimensions() = PathDimensions(x, Double.MAX_VALUE, x, Double.MIN_VALUE)
}

data class RelativeHorizontalLine(val x: Double): SVGCommand() {
    override fun toPathString(): String = "h $x"
    override fun translate(right: Double, down: Double): SVGCommand = RelativeHorizontalLine(x + right)
    override fun scale(scaleBy: Double): SVGCommand = RelativeHorizontalLine(x * scaleBy)
    override fun pathDimensions() = PathDimensions(x, Double.MAX_VALUE, x, Double.MIN_VALUE)
}

data class VerticalLine(val y: Double): SVGCommand() {
    override fun toPathString(): String = "V $y"
    override fun translate(right: Double, down: Double): SVGCommand = VerticalLine(y + down)
    override fun scale(scaleBy: Double): SVGCommand = VerticalLine(y * scaleBy)
    override fun pathDimensions() = PathDimensions(Double.MAX_VALUE, y, Double.MIN_VALUE, y)
}

data class RelativeVerticalLine(val y: Double): SVGCommand() {
    override fun toPathString(): String = "v $y"
    override fun translate(right: Double, down: Double): SVGCommand = RelativeVerticalLine(y + down)
    override fun scale(scaleBy: Double): SVGCommand = RelativeVerticalLine(y * scaleBy)
    override fun pathDimensions() = PathDimensions(Double.MAX_VALUE, y, Double.MIN_VALUE, y)
}

class ClosePath: SVGCommand() {
    override fun toPathString(): String = "z"
    override fun translate(right: Double, down: Double): SVGCommand = ClosePath()
    override fun scale(scaleBy: Double): SVGCommand = ClosePath()
    override fun pathDimensions() = PathDimensions(Double.MAX_VALUE, Double.MAX_VALUE, Double.MIN_VALUE, Double.MIN_VALUE)
}

data class CubicBezierCurve(val x0: Double, val y0: Double, val x1: Double, val y1: Double, val x2: Double, val y2: Double): SVGCommand() {
    override fun toPathString(): String = "C $x0 $y0 $x1 $y1 $x2 $y2"
    override fun translate(right: Double, down: Double): SVGCommand = CubicBezierCurve(x0 + right, y0 + down, x1 + right, y1 + down, x2 + right, y2 + down)
    override fun scale(scaleBy: Double): SVGCommand = CubicBezierCurve(x0 * scaleBy, y0 * scaleBy, x1 * scaleBy, y1 * scaleBy, x2 * scaleBy, y2 * scaleBy)
    override fun pathDimensions() = PathDimensions(listOf(x0, x1, x2).min()!!,listOf(y0, y1, y2).min()!!,
            listOf(x0, x1, x2).max()!!,listOf(y0, y1, y2).max()!!)
}

data class RelativeCubicBezierCurve(val x0: Double, val y0: Double, val x1: Double, val y1: Double, val x2: Double, val y2: Double): SVGCommand() {
    override fun toPathString(): String = "c $x0 $y0 $x1 $y1 $x2 $y2"
    override fun translate(right: Double, down: Double): SVGCommand = RelativeCubicBezierCurve(x0 + right, y0 + down, x1 + right, y1 + down, x2 + right, y2 + down)
    override fun scale(scaleBy: Double): SVGCommand = RelativeCubicBezierCurve(x0 * scaleBy, y0 * scaleBy, x1 * scaleBy, y1 * scaleBy, x2 * scaleBy, y2 * scaleBy)
    override fun pathDimensions() = PathDimensions(listOf(x0, x1, x2).min()!!,listOf(y0, y1, y2).min()!!,
            listOf(x0, x1, x2).max()!!,listOf(y0, y1, y2).max()!!)
}

data class SmoothCubicBezierCurve(val x0: Double, val y0: Double, val x1: Double, val y1: Double): SVGCommand() {
    override fun toPathString(): String = "S $x0 $y0 $x1 $y1"
    override fun translate(right: Double, down: Double): SVGCommand = SmoothCubicBezierCurve(x0 + right, y0 + down, x1 + right, y1 + down)
    override fun scale(scaleBy: Double): SVGCommand = SmoothCubicBezierCurve(x0 * scaleBy, y0 * scaleBy, x1 * scaleBy, y1 * scaleBy)
    override fun pathDimensions() = PathDimensions(listOf(x0, x1).min()!!,listOf(y0, y1).min()!!,
            listOf(x0, x1).max()!!,listOf(y0, y1).max()!!)
}

data class SmoothRelativeCubicBezierCurve(val x0: Double, val y0: Double, val x1: Double, val y1: Double): SVGCommand() {
    override fun toPathString(): String = "s $x0 $y0 $x1 $y1"
    override fun translate(right: Double, down: Double): SVGCommand = SmoothRelativeCubicBezierCurve(x0 + right, y0 + down, x1 + right, y1 + down)
    override fun scale(scaleBy: Double): SVGCommand = SmoothRelativeCubicBezierCurve(x0 * scaleBy, y0 * scaleBy, x1 * scaleBy, y1 * scaleBy)
    override fun pathDimensions() = PathDimensions(listOf(x0, x1).min()!!,listOf(y0, y1).min()!!,
            listOf(x0, x1).max()!!,listOf(y0, y1).max()!!)
}

data class QuadraticBezierCurve(val x0: Double, val y0: Double, val x1: Double, val y1: Double): SVGCommand() {
    override fun toPathString(): String = "Q $x0 $y0 $x1 $y1"
    override fun translate(right: Double, down: Double): SVGCommand = QuadraticBezierCurve(x0 + right, y0 + down, x1 + right, y1 + down)
    override fun scale(scaleBy: Double): SVGCommand = QuadraticBezierCurve(x0 * scaleBy, y0 * scaleBy, x1 * scaleBy, y1 * scaleBy)
    override fun pathDimensions() = PathDimensions(listOf(x0, x1).min()!!,listOf(y0, y1).min()!!,
            listOf(x0, x1).max()!!,listOf(y0, y1).max()!!)
}

data class RelativeQuadraticBezierCurve(val x0: Double, val y0: Double, val x1: Double, val y1: Double): SVGCommand() {
    override fun toPathString(): String = "q $x0 $y0 $x1 $y1"
    override fun translate(right: Double, down: Double): SVGCommand = RelativeQuadraticBezierCurve(x0 + right, y0 + down, x1 + right, y1 + down)
    override fun scale(scaleBy: Double): SVGCommand = RelativeQuadraticBezierCurve(x0 * scaleBy, y0 * scaleBy, x1 * scaleBy, y1 * scaleBy)
    override fun pathDimensions() = PathDimensions(listOf(x0, x1).min()!!,listOf(y0, y1).min()!!,
            listOf(x0, x1).max()!!,listOf(y0, y1).max()!!)
}

data class SmoothQuadraticBezierCurve(val x: Double, val y: Double): SVGCommand() {
    override fun toPathString(): String = "T $x $y"
    override fun translate(right: Double, down: Double): SVGCommand = SmoothQuadraticBezierCurve(x + right, y + down)
    override fun scale(scaleBy: Double): SVGCommand = SmoothQuadraticBezierCurve(x * scaleBy, y * scaleBy)
    override fun pathDimensions() = PathDimensions(x, y, x, y)
}

data class SmoothRelativeQuadraticBezierCurve(val x: Double, val y: Double): SVGCommand() {
    override fun toPathString(): String = "t $x $y"
    override fun translate(right: Double, down: Double): SVGCommand = SmoothRelativeQuadraticBezierCurve(x + right, y + down)
    override fun scale(scaleBy: Double): SVGCommand = SmoothRelativeQuadraticBezierCurve(x * scaleBy, y * scaleBy)
    override fun pathDimensions() = PathDimensions(x, y, x, y)
}

data class Arc(val rx: Double, val ry: Double, val xRotation: Double, val largeArcFlag: Double,
               val sweepFlag: Double, val x: Double, val y: Double): SVGCommand() {
    override fun toPathString(): String = TODO()
    override fun translate(right: Double, down: Double): SVGCommand = TODO()
    override fun scale(scaleBy: Double): SVGCommand = TODO()
    override fun pathDimensions() = TODO()
}

data class RelativeArc(val rx: Double, val ry: Double, val xRotation: Double, val largeArcFlag: Double,
                       val sweepFlag: Double, val x: Double, val y: Double): SVGCommand() {
    override fun toPathString(): String = TODO()
    override fun translate(right: Double, down: Double): SVGCommand = TODO()
    override fun scale(scaleBy: Double): SVGCommand = TODO()
    override fun pathDimensions() = TODO()
}
