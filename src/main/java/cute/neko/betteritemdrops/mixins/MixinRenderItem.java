package cute.neko.betteritemdrops.mixins;

import cute.neko.betteritemdrops.BIDConfig;
import net.minecraft.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(RenderItem.class)
public abstract class MixinRenderItem extends MixinRender {

    @Shadow
    private Random random;

    @Shadow
    public static boolean renderInFrame;

    @Shadow
    private RenderBlocks itemRenderBlocks;

    @Shadow
    public boolean renderWithColor;

    @Shadow
    @Final
    private static ResourceLocation RES_ITEM_GLINT;

    /**
     * @author yuchenxue
     * @reason sb
     */
    @Overwrite
    public void doRenderItem(EntityItem par1EntityItem, double par2, double par4, double par6, float par8, float par9) {
        this.bindEntityTexture(par1EntityItem);
        this.random.setSeed(187L);
        ItemStack var10 = par1EntityItem.getEntityItem();

        if (var10.getItem() != null) {
            GL11.glPushMatrix();

            // b
            if (BIDConfig.INSTANCE.isThreeDimensional()) {
                if (var10.getItem() instanceof ItemBlock) {
                    GL11.glTranslatef((float) par2, (float) par4 + 0.02F, (float) par6);
                } else {
                    GL11.glTranslatef((float) par2, (float) par4 + 0.01F, (float) par6);
                }

                float pitch = par1EntityItem.onGround ? 90.0F : par1EntityItem.rotationPitch;
                par1EntityItem.rotationPitch++;
                if (par1EntityItem.rotationPitch > 180) {
                    par1EntityItem.rotationPitch = -180;
                }

                if (renderInFrame) {
                    GL11.glRotatef(pitch, 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef(par1EntityItem.rotationYaw, 0.0F, 0.0F, 1.0F);
                }

            } else {
                float var11 = MathHelper.sin(((float) par1EntityItem.age + par9) / 10.0F + par1EntityItem.hoverStart) * 0.1F + 0.1F;
//                float var12 = (((float) par1EntityItem.age + par9) / 20.0F + par1EntityItem.hoverStart) * (180F / (float) Math.PI);
//                GL11.glTranslatef((float) par2, (float) par4 + var11, (float) par6);
//
//                GL11.glRotatef(var12, 0.0F, 1.0F, 0.0F);

                GL11.glTranslatef((float)par2, (float)par4 + var11, (float)par6);
            }

//            float var11 = MathHelper.sin(((float) par1EntityItem.age + par9) / 10.0F + par1EntityItem.hoverStart) * 0.1F + 0.1F;
            float var12 = (((float) par1EntityItem.age + par9) / 20.0F + par1EntityItem.hoverStart) * (180F / (float) Math.PI);
            byte var13 = 1;
            if (par1EntityItem.getEntityItem().stackSize > 1) {
                var13 = 2;
            }

            if (par1EntityItem.getEntityItem().stackSize > 5) {
                var13 = 3;
            }

            if (par1EntityItem.getEntityItem().stackSize > 20) {
                var13 = 4;
            }

            if (par1EntityItem.getEntityItem().stackSize > 40) {
                var13 = 5;
            }

//            if (!BIDConfig.INSTANCE.isThreeDimensional()) {
//                GL11.glTranslatef((float)par2, (float)par4 + var11, (float)par6);
//            }

            GL11.glEnable(32826);
            if (var10.getItemSpriteNumber() == 0 && var10.itemID < Block.blocksList.length && Block.blocksList[var10.itemID] != null && RenderBlocks.renderItemIn3d(Block.blocksList[var10.itemID].getRenderType())) {
                Block var21 = Block.blocksList[var10.itemID];

                // b
                if (!BIDConfig.INSTANCE.isThreeDimensional()) {
                    GL11.glRotatef(var12, 0.0F, 1.0F, 0.0F);
                }

                if (renderInFrame) {
                    GL11.glScalef(1.25F, 1.25F, 1.25F);
                    GL11.glTranslatef(0.0F, 0.05F, 0.0F);
                    GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                float var25 = 0.25F;
                int var24 = var21.getRenderType();
                if (var24 == 1 || var24 == 19 || var24 == 12 || var24 == 2) {
                    var25 = 0.5F;
                }

                GL11.glScalef(var25, var25, var25);

                for (int var26 = 0; var26 < var13; ++var26) {
                    GL11.glPushMatrix();
                    if (var26 > 0) {
                        float var18 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var25;
                        float var19 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var25;
                        float var20 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var25;
                        GL11.glTranslatef(var18, var19, var20);
                    }

                    float var18 = 1.0F;
                    this.itemRenderBlocks.renderBlockAsItem(var21, var10.getItemSubtype(), var18);
                    GL11.glPopMatrix();
                }
            } else if (var10.getItemSpriteNumber() == 1 && var10.getItem().requiresMultipleRenderPasses()) {
                if (renderInFrame) {
                    GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                    GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                } else {
                    GL11.glScalef(0.5F, 0.5F, 0.5F);
                }

                for (int var23 = 0; var23 <= 1; ++var23) {
                    this.random.setSeed(187L);
                    Icon var22 = var10.getItem().getIconFromSubtypeForRenderPass(var10.getItemSubtype(), var23);
                    float var16 = 1.0F;
                    if (this.renderWithColor) {
                        int var26 = Item.itemsList[var10.itemID].getColorFromItemStack(var10, var23);
                        float var18 = (float) (var26 >> 16 & 255) / 255.0F;
                        float var19 = (float) (var26 >> 8 & 255) / 255.0F;
                        float var20 = (float) (var26 & 255) / 255.0F;
                        GL11.glColor4f(var18 * var16, var19 * var16, var20 * var16, 1.0F);
                        this.renderDroppedItem(par1EntityItem, var22, var13, par9, var18 * var16, var19 * var16, var20 * var16);
                    } else {
                        this.renderDroppedItem(par1EntityItem, var22, var13, par9, 1.0F, 1.0F, 1.0F);
                    }
                }
            } else {
                if (renderInFrame) {
                    GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                    GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                } else {
                    GL11.glScalef(0.5F, 0.5F, 0.5F);
                }

                Icon var14 = var10.getIconIndex();
                if (this.renderWithColor) {
                    int var15 = Item.itemsList[var10.itemID].getColorFromItemStack(var10, 0);
                    float var16 = (float) (var15 >> 16 & 255) / 255.0F;
                    float var17 = (float) (var15 >> 8 & 255) / 255.0F;
                    float var18 = (float) (var15 & 255) / 255.0F;
                    float var19 = 1.0F;
                    this.renderDroppedItem(par1EntityItem, var14, var13, par9, var16 * var19, var17 * var19, var18 * var19);
                } else {
                    this.renderDroppedItem(par1EntityItem, var14, var13, par9, 1.0F, 1.0F, 1.0F);
                }
            }

            GL11.glDisable(32826);
            GL11.glPopMatrix();
        }
    }

    /**
     * @author yuchenxue
     * @reason sb
     */
    @Overwrite
    private void renderDroppedItem(EntityItem par1EntityItem, Icon par2Icon, int par3, float par4, float par5, float par6, float par7) {
        Tessellator var8 = Tessellator.instance;
        if (par2Icon == null) {
            TextureManager var9 = Minecraft.getMinecraft().getTextureManager();
            ResourceLocation var10 = var9.getResourceLocation(par1EntityItem.getEntityItem().getItemSpriteNumber());
            par2Icon = ((TextureMap) var9.getTexture(var10)).getAtlasSprite("missingno");
        }

        float var25 = par2Icon.getMinU();
        float var26 = par2Icon.getMaxU();
        float var11 = par2Icon.getMinV();
        float var12 = par2Icon.getMaxV();
        float var13 = 1.0F;
        float var14 = 0.5F;
        float var15 = 0.25F;

        if (this.renderManager.options.isFancyGraphicsEnabled())
//        if (BIDConfig.INSTANCE.isThreeDimensional())
        {
            GL11.glPushMatrix();

            // b
            if (BIDConfig.INSTANCE.isThreeDimensional()) {
                if (renderInFrame) {
                    if (par1EntityItem.onGround) {
//                GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                    } else {
                        GL11.glRotatef(
                                (((float) par1EntityItem.age + par4) / 20.0F + par1EntityItem.hoverStart) * (180F / (float) Math.PI),
                                0.0F, 1.0F, 0.0F
                        );
                    }
                }
            } else {
                if (renderInFrame) {
                    GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                } else {
                    GL11.glRotatef((((float) par1EntityItem.age + par4) / 20.0F + par1EntityItem.hoverStart) * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
                }
            }

//            if (renderInFrame) {
//                GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
//            } else {
//                GL11.glRotatef((((float) par1EntityItem.age + par4) / 20.0F + par1EntityItem.hoverStart) * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
//            }

            float var16 = 0.0625F;
            float var17 = 0.021875F;
            ItemStack var18 = par1EntityItem.getEntityItem();
            int var19 = var18.stackSize;
            byte var24;
            if (var19 < 2) {
                var24 = 1;
            } else if (var19 < 16) {
                var24 = 2;
            } else if (var19 < 32) {
                var24 = 3;
            } else {
                var24 = 4;
            }

            GL11.glTranslatef(-var14, -var15, -((var16 + var17) * (float) var24 / 2.0F));

            for (int var20 = 0; var20 < var24; ++var20) {
                GL11.glTranslatef(0.0F, 0.0F, var16 + var17);
                if (var18.getItemSpriteNumber() == 0 && Block.blocksList[var18.itemID] != null) {
                    this.bindTexture(TextureMap.locationBlocksTexture);
                } else {
                    this.bindTexture(TextureMap.locationItemsTexture);
                }

                GL11.glColor4f(par5, par6, par7, 1.0F);
                ItemRenderer.renderItemIn2D(var8, var26, var11, var25, var12, par2Icon.getIconWidth(), par2Icon.getIconHeight(), var16);
                if (var18.hasEffect()) {
                    GL11.glDepthFunc(514);
                    GL11.glDisable(2896);
                    this.renderManager.renderEngine.bindTexture(RES_ITEM_GLINT);
                    GL11.glEnable(3042);
                    GL11.glBlendFunc(768, 1);
                    float var21 = 0.76F;
                    GL11.glColor4f(0.5F * var21, 0.25F * var21, 0.8F * var21, 1.0F);
                    GL11.glMatrixMode(5890);
                    GL11.glPushMatrix();
                    float var22 = 0.125F;
                    GL11.glScalef(var22, var22, var22);
                    float var23 = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
                    GL11.glTranslatef(var23, 0.0F, 0.0F);
                    GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
                    ItemRenderer.renderItemIn2D(var8, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, var16);
                    GL11.glPopMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(var22, var22, var22);
                    var23 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
                    GL11.glTranslatef(-var23, 0.0F, 0.0F);
                    GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
                    ItemRenderer.renderItemIn2D(var8, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, var16);
                    GL11.glPopMatrix();
                    GL11.glMatrixMode(5888);
                    GL11.glDisable(3042);
                    GL11.glEnable(2896);
                    GL11.glDepthFunc(515);
                }
            }

            GL11.glPopMatrix();
        }
        else if (!BIDConfig.INSTANCE.isThreeDimensional())
        {
            for (int var27 = 0; var27 < par3; ++var27) {
                GL11.glPushMatrix();
                if (var27 > 0) {
                    float var17 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float var29 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float var28 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    GL11.glTranslatef(var17, var29, var28);
                }

                if (!renderInFrame) {
                    GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                }

                GL11.glColor4f(par5, par6, par7, 1.0F);
                var8.startDrawingQuads();
                var8.setNormal(0.0F, 1.0F, 0.0F);
                var8.addVertexWithUV(0.0F - var14, 0.0F - var15, 0.0F, var25, var12);
                var8.addVertexWithUV(var13 - var14, 0.0F - var15, 0.0F, var26, var12);
                var8.addVertexWithUV(var13 - var14, 1.0F - var15, 0.0F, var26, var11);
                var8.addVertexWithUV(0.0F - var14, 1.0F - var15, 0.0F, var25, var11);
                var8.draw();
                GL11.glPopMatrix();
            }
        }
    }
}
