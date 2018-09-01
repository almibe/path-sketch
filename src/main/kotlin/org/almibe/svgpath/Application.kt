/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.almibe.svgpath

import groovy.lang.Binding
import groovy.lang.GroovyShell
import javafx.application.Application
import javafx.event.EventHandler
import javafx.geometry.Orientation
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.control.ScrollPane
import javafx.scene.control.SplitPane
import javafx.scene.control.TextArea
import javafx.scene.input.KeyCode
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.stage.Stage

class App : Application() {
    private val pane = Pane()
    private val groovyEditor = TextArea()
    private val groovyOutput = TextArea()
    private val canvas = Canvas()

    private val scriptData = Binding()
    private val shell = GroovyShell(scriptData)

    override fun start(stage: Stage) {
        stage.title = "Sketch Path"
        stage.scene = Scene(createMainPane())
        stage.show()
    }

    private fun createMainPane(): Pane {
        val splitPane = SplitPane()
        val scrollPane = ScrollPane(canvas)
        canvas.height = 780.0
        canvas.width = 500.0
        groovyOutput.isEditable = false
        setupGroovy()
        splitPane.orientation = Orientation.VERTICAL
        splitPane.items.addAll(scrollPane, groovyEditor, groovyOutput)
        splitPane.setDividerPositions(0.33, 0.33)
        pane.children.add(splitPane)
        return pane
    }

    private fun setupGroovy() {
        scriptData.setVariable("canvas", canvas)
        scriptData.setVariable("sketch", SVGController(canvas))

        pane.onKeyPressed = EventHandler {
            if (it.code == KeyCode.R && it.isControlDown) {
                it.consume()
                val code = groovyEditor.text
                val result = shell.evaluate(code)
                if (result != null) {
                    groovyOutput.appendText(result.toString())
                }
            }
        }
    }
}

class SVGController(val canvas: Canvas) {
    private val parser = SVGPathParser()
    private val trebleClefPath: SVGPath = parser.parse("M 9.125,0.0030002594 C 4.03428,0.18520026 0,2.5856003 0,5.5030003 C 0,8.5390003 4.368,11.003 9.75,11.003 C 15.132,11.003 19.5,8.5390003 19.5,5.5030003 C 19.5,2.4670003 15.132,0.0030002594 9.75,0.0030002594 C 9.53977,0.0030002594 9.33194,-0.0043997406 9.125,0.0030002594 z M 7.5,1.0655003 C 8.8579,0.92650026 10.56798,1.5561003 12,2.8467003 C 14.14502,4.7799003 14.87122,7.4906003 13.625,8.9092003 L 13.59375,8.9405003 C 12.32289,10.3506 9.53145,9.9153003 7.375,7.9717003 C 5.21855,6.0282003 4.51039,3.2881003 5.78125,1.8780003 C 6.20818,1.4043003 6.81306,1.1358003 7.5,1.0655003 z")

    fun drawPath(path: String) {
        canvas.graphicsContext2D.beginPath()
        canvas.graphicsContext2D.appendSVGPath(path)
        canvas.graphicsContext2D.closePath()
    }

    fun drawTrebleClef(x: Double, y: Double) {
        canvas.graphicsContext2D.fill = Color.BLACK
        val path = trebleClefPath.translate(x, y)
        canvas.graphicsContext2D.appendSVGPath(path.toPathString())
        canvas.graphicsContext2D.fill()
    }
}

fun main(args: Array<String>) {
    Application.launch(App::class.java)
}
