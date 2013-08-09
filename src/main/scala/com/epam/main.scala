package com.epam.conway

import swing._

object Main extends ConwayApp

trait ConwayApp
  extends SimpleSwingApplication {

  val instances = new AnyRef
    with FormInstance
    with RenderInstance
    with MachineInstance

  import instances._

  val blockSize: Int = 10
  val (width, height) = (50, 50)
  val (canvasWidth, canvasHeight) = (width * blockSize, height * blockSize)


  implicit val machineModule  = Resolver.machineModule(width, height)
  implicit val renderModule   = Resolver.renderModule(canvasWidth, canvasHeight, blockSize)
  implicit val formModule     = Resolver.formModule(canvasWidth, canvasHeight)

  lazy val top: Frame = formModule.gui
}

