package com.epam.conway

import scalaz._
import Scalaz._



/* Conway's Game of Life rules:
 * 1. Any live cell with fewer than two live neighbors dies, as if caused by under-population.
 * 2. Any live cell with two or three live neighbors lives on to the next generation.
 * 3. Any live cell with more than three live neighbors dies, as if by overcrowding.
 * 4. Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.
 */

trait MachineInstance {
  implicit val toMachineModule: (Int, Int) => MachineModule = {
    case (width, height) => new MachineModule {

      type Cells = List[(Int, Int)]

      val init: Cells => State =
        cells =>
          State(cells)

      val next: State => State =
        state =>
          state copy (cells = step(state.cells))

      private val offset =  List(-1, 0, 1)                                              // -- all posible neigbors

      private val neighbors =                                                           // -- func list for neighbors generation
        for(dx <- offset; dy <- offset if (dx, dy) != (0, 0))                           // -- for 8 posible neighbors
        yield ((x: Int, y: Int) => (x + dx, y + dy)).tupled                             // -- generate 8 funcs (dx, dx) => (x + dx, y + dy)

      private val step: Cells => Cells =
        cells =>
          (cells <*> neighbors)                                                         // -- generate all neighbors
          .filter { case (x, y) => x >= 0 && x <= width && y >= 0 && y <= height }      // -- discard out of field cells
          .groupBy { identity }                                                         // -- calculate cell frequincies
          .map { case (cell, items) => (cell, items.size) }                             // -- by count generated cell
          .filter {                                                                     // -- apply conwey's Life rules:
            case (cell, 2)  => cells contains cell                                      // -- rules 2
            case (_, 3)     => true                                                     // -- rules 2 & 4
            case _          => false                                                    // -- rules 1 & 3
          }
          .toList                                                                       // -- convert Map to List
          .map { case(cell, _) => cell }                                                // -- keep only cells
  }}
}
