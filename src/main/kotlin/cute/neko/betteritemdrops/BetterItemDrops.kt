package cute.neko.betteritemdrops

import cute.neko.kawakaze.config.ConfigTask
import net.fabricmc.api.ModInitializer

object BetterItemDrops : ModInitializer {

    const val MOD_ID = "betteritemdrops"

    override fun onInitialize() {
        ConfigTask.before { registrar ->
            registrar.register(BIDConfig)
        }
    }
}