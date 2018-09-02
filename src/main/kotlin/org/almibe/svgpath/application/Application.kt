/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.almibe.svgpath.application

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
        canvas.height = 500.0
        canvas.width = 500.0
        groovyOutput.isEditable = false
        setupGroovy()
        splitPane.orientation = Orientation.VERTICAL
        splitPane.items.addAll(scrollPane, groovyEditor, groovyOutput)
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

fun main(args: Array<String>) {
    Application.launch(App::class.java)
}
