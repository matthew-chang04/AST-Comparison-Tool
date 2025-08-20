package io.joern.c2cpgTypeRecovery

import io.shiftleft.semanticcpg.layers.{LayerCreator, LayerCreatorContext}

object CPPTypeRecoveryExt {
  val overlayName = "Type Recovery Extension"
  val description = "Recovers ambiguous type information lost in CPG parsing for C and C++ analysis"

  // TODO: IMPLEMENT OPTIONS FOR RUNNING THE PASS (LayerCreatorOptions)
}
class CPPTypeRecoveryExt extends LayerCreator {
  override val overlayName: String = CPPTypeRecoveryExt.overlayName
  override val description: String = CPPTypeRecoveryExt.description

  override def create(context: LayerCreatorContext): Unit = {
    val cpg = context.cpg
    new CPPTypeRecoveryPass(cpg).createAndApply()
  }

}
