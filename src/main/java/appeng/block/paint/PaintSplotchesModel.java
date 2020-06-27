
package appeng.block.paint;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

import com.mojang.datafixers.util.Pair;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.IModelTransform;
import net.minecraft.client.render.model.IUnbakedModel;
import net.minecraft.client.render.model.ItemOverrideList;
import net.minecraft.client.render.model.Material;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Identifier;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometry;

public class PaintSplotchesModel implements IModelGeometry<PaintSplotchesModel> {

    @Override
    public BakedModel bake(IModelConfiguration owner, ModelLoader bakery,
                           Function<Material, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform,
                           ItemOverrideList overrides, Identifier modelLocation) {
        return new PaintSplotchesBakedModel(spriteGetter);
    }

    @Override
    public Collection<Material> getTextures(IModelConfiguration owner,
                                            Function<Identifier, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        return PaintSplotchesBakedModel.getRequiredTextures();
    }

}
