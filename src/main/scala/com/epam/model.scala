package com.epam.conway

import swing.{Frame, Graphics2D}


case class State (cells: List[(Int, Int)])

trait MachineModule {
  val init: List[(Int, Int)] => State
  val next: State => State
}

trait RenderModule {
  val renderTo: Graphics2D => State => Graphics2D
}

trait FormModule {
  val gui: Frame
}


object Resolver {
  def formModule(w: Int, h: Int)
    (implicit renderM: RenderModule, machineM: MachineModule, init: (Int, Int) => MachineModule => RenderModule => FormModule) =
      init apply (w, h) apply machineM apply renderM

  def renderModule(w: Int, h: Int, blockSize: Int)
    (implicit init: (Int, Int) => Int => RenderModule) =
      init apply (w, h) apply blockSize

  def machineModule(w: Int, h: Int)
    (implicit init: (Int, Int) => MachineModule) =
      init apply (w, h)
}

