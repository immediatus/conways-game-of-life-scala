package com.epam.conway

import swing._

import scalaz._
import Scalaz._


trait FormInstance {
  implicit val toFormModule: (Int, Int) => MachineModule => RenderModule => FormModule = {
    case size => machineModule => renderModule => new FormModule {

      private var _state = machineModule init List(
        (48,0),(47,1),(47,2),(48,2),(49,2),
        (1,0),(2,1),(0,2),(1,2),(2,2),
        (24,25),(25,25), (26,25)
      )

      lazy val gui: Frame =
        (sizeInit >>> canvasInit >>> panelInit) apply size

      private val sizeInit: ((Int, Int)) => Dimension = {
        case (w, h) => new Dimension(w, h)
      }

      private val canvasInit: Dimension => Panel =
        sz => new Panel {
          preferredSize = sz
          focusable     = true

          override def paint(graphics: Graphics2D): Unit = renderModule renderTo graphics apply _state

          Timer(200) {
            _state = machineModule.next(_state)
            repaint
          }
        }

      private val panelInit: Panel => Frame =
        canv => new MainFrame {
          title       = "Conway's Life"
          resizable   = false
          size        = canv.preferredSize
          minimumSize = canv.preferredSize
          contents    = canv
        }

  }}
}

object Timer {
    import javax.swing.{AbstractAction, Timer}

    def apply(interval: Int, repeats: Boolean = true)(op: => Unit) {
      ((t: Timer) => {
        t.setRepeats(repeats)
        t.start()
      }) apply (new Timer(interval, new AbstractAction() {
          def actionPerformed(e : java.awt.event.ActionEvent) = op
        }
      ))
    }
}
