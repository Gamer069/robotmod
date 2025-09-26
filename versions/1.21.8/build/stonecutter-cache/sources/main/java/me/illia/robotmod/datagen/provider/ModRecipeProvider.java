package me.illia.robotmod.datagen.provider;

import me.illia.robotmod.Robotmod;
import me.illia.robotmod.block.ModBlocks;
import me.illia.robotmod.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
//? if >= 1.21.8 {
import net.minecraft.data.recipe.RecipeExporter;
//?} else {
/*import net.minecraft.data.server.recipe.RecipeExporter;
*///?}
import net.minecraft.data.server.recipe.RecipeGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
	public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup wrapperLookup, RecipeExporter recipeExporter) {
		return new RecipeGenerator(wrapperLookup, recipeExporter) {
			@Override
			public void generate() {
				RegistryWrapper.Impl<Item> itemLookup = registries.getOrThrow(RegistryKeys.ITEM);

				createShaped(RecipeCategory.TRANSPORTATION, ModItems.TELEPORTER, 1)
					.pattern("SES")
					.pattern("ESE")
					.pattern("SES")
					.input('E', ModItems.PACKED_ENDER_PEARL)
					.input('S', Items.STICK)
					.criterion(hasItem(ModItems.PACKED_ENDER_PEARL), conditionsFromItem(ModItems.PACKED_ENDER_PEARL))
					.offerTo(recipeExporter);

				createShaped(RecipeCategory.TRANSPORTATION, ModBlocks.TELEPORTER_BLOCK.asItem(), 1)
					.pattern("IEI")
					.pattern("ERE")
					.pattern("IEI")
					.input('E', ModItems.PACKED_ENDER_PEARL)
					.input('I', Items.IRON_INGOT)
					.input('R', Items.REDSTONE_BLOCK)
					.criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
					.criterion(hasItem(ModItems.PACKED_ENDER_PEARL), conditionsFromItem(ModItems.PACKED_ENDER_PEARL))
					.offerTo(recipeExporter);

				createShaped(RecipeCategory.TRANSPORTATION, ModItems.PACKED_ENDER_PEARL, 1)
					.pattern("EEE")
					.pattern("EEE")
					.pattern("EEE")
					.input('E', Items.ENDER_PEARL)
					.criterion(hasItem(Items.ENDER_PEARL), conditionsFromItem(Items.ENDER_PEARL))
					.offerTo(recipeExporter);
			}
		};
	}

	@Override
	public String getName() {
		return Robotmod.MODID + " recipes";
	}
}
