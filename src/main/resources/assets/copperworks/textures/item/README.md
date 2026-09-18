# Copperworks tool textures

Create these standard 16x16 item textures in this directory:

- `copper_sword.png`
- `copper_pickaxe.png`
- `copper_axe.png`
- `copper_shovel.png`
- `copper_hoe.png`
- `copper_helmet.png`
- `copper_chestplate.png`
- `copper_leggings.png`
- `copper_boots.png`

The mod builds without these PNG files. Minecraft will show missing-texture placeholders until they are supplied.

## Worn armor textures

Minecraft 1.21.1 uses the `ArmorMaterial.Layer` texture paths for humanoid armor.
Create these standard 64x32 PNG files:

- `src/main/resources/assets/copperworks/textures/models/armor/copper_layer_1.png` for the helmet, chestplate, and boots
- `src/main/resources/assets/copperworks/textures/models/armor/copper_layer_2.png` for the leggings

The armor material uses the `copper` layer name, so these exact paths are required. No equipment-model JSON is needed for this 1.21.1 implementation.
