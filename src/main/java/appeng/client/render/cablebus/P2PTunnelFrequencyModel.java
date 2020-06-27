package appeng.client.render.cablebus;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

import com.mojang.datafixers.util.Pair;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.IModelTransform;
import net.minecraft.client.render.model.IUnbakedModel;
import net.minecraft.client.render.model.ItemOverrideList;
import net.minecraft.client.render.model.Material;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Identifier;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometry;

import appeng.core.AppEng;

public class P2PTunnelFrequencyModel implements IModelGeometry<P2PTunnelFrequencyModel> {
    private static final Material TEXTURE = new Material(AtlasTexture.LOCATION_BLOCKS_TEXTURE,
            new Identifier(AppEng.MOD_ID, "parts/p2p_tunnel_frequency"));

    @Override
    public BakedModel bake(IModelConfiguration owner, ModelLoader bakery,
                           Function<Material, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform,
                           ItemOverrideList overrides, Identifier modelLocation) {
        try {
            final TextureAtlasSprite texture = spriteGetter.apply(TEXTURE);
            return new P2PTunnelFrequencyBakedModel(texture);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Material> getTextures(IModelConfiguration owner,
                                            Function<Identifier, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        return Collections.singleton(TEXTURE);
    }

}
