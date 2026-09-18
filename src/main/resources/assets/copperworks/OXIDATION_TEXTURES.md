# Copper equipment oxidation textures

All nine equipment items use `copperworks:oxidation`, registered during client setup,
to select inventory/held models. The value comes from `CopperEquipment.stage(stack)`:
0 = fresh, 1 = exposed, 2 = weathered, 3 = oxidized. Overrides are ordered 1, 2, 3
so the highest matching threshold wins. State models use matching `_exposed`,
`_weathered`, and `_oxidized` item textures.

`CopperArmorItem.getArmorTexture` reads the equipped stack using the same stage helper.
It selects `textures/models/armor/copper_layer_1{suffix}.png` for outer armor and
`copper_layer_2{suffix}.png` for leggings (the inner model). Fresh uses an empty suffix.
Waxing does not change texture selection. Registry IDs and gameplay are unchanged.
