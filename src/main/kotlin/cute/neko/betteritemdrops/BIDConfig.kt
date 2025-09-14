package cute.neko.betteritemdrops

import cute.neko.kawakaze.config.Config

object BIDConfig : Config("better-item-drops", BetterItemDrops.MOD_ID) {

    val RENDER_MODE by setting("RenderMode", arrayOf("3D"))


    fun isThreeDimensional() = RENDER_MODE == "3D"

//    fun isThreeDimensional() = false

//    fun isTwoDimensional() = RENDER_MODE == ""
}