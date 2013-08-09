package com.epam.conway

import java.awt.Color
import swing._

import scalaz._
import Scalaz._


trait RenderInstance {
  implicit val toRenderModule: (Int, Int) => Int => RenderModule = {
    case (width, height) => blockSize => new RenderModule {

      val renderTo: Graphics2D => State => Graphics2D =
        graphics => state => {
          (clear                  ) >>>
          (renderGrid             ) >>>
          (renderState apply state)
        } apply graphics


      private val background: Color = new Color(200, 200, 200)
      private val foreground: Color = new Color(170, 170, 170)
      private val figure:     Color = new Color( 10,  10,  10)

      private val clear: Graphics2D => Graphics2D =
        graphics => {
          graphics setColor background
          graphics fillRect (0, 0, width, height)
          graphics
        }

      private val renderGrid: Graphics2D => Graphics2D =
        graphics => {

          def lines(p1: (Int, Int), p2: (Int, Int), d:(Int, Int), cond: (Int, Int)) {
            val (x1, y1) :: (x2, y2) :: (dx, dy) :: (cx, cy) :: Nil = p1 :: p2 :: d :: cond :: Nil
            if(x1 < cx && y1 < cy) {
              graphics drawLine (x1, y1, x2, y2)
              lines((x1 + dx, y1 + dy), (x2 + dx, y2 + dy), d, cond)
            }
          }

          graphics setColor foreground
          lines((0,0), (width, 0), (0, blockSize), (Int.MaxValue, height))
          lines((0,0), (0, height), (blockSize, 0), (width, Int.MaxValue))
          graphics
        }

      private val renderState: State => Graphics2D => Graphics2D = {
        case State(cells) => graphics => {
          graphics setColor figure
          for((x, y) <- cells)
            graphics fill (new Rectangle(
              x * blockSize + 2, y * blockSize + 2,
              blockSize - 3, blockSize - 3
            ))
        }
        graphics
      }
  }}
}
