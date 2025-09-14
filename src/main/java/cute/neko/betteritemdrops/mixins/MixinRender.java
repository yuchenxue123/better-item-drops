package cute.neko.betteritemdrops.mixins;

import net.minecraft.Entity;
import net.minecraft.Render;
import net.minecraft.RenderManager;
import net.minecraft.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Render.class)
public abstract class MixinRender {
    @Shadow
    protected abstract void bindTexture(ResourceLocation par1ResourceLocation);

    @Shadow
    protected RenderManager renderManager;

    @Shadow
    protected abstract void bindEntityTexture(Entity par1Entity);
}
