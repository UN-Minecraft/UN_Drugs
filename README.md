# Drug Items

Drug Items is a plugin for Minecraft servers. This adds elements that simulate effects generated by psychoactive substances.

## General Configs
```
config:
  pluginDisplayName: "Plugin Display Name"
  generalDescription:
    - "Add items contains this lore"
  generalItemsDescription:
      drugs:
        - "General lore for all drugs items"
      baseItems:
        - "General lore for all base items"
```

## Add Drugs
Create a new item, which when interacting with it applies effects to the player:
```
  # Example of implementation
  drugs:
      itemName:
        displayName: "&r&2You can use colors"
        baseMaterial: TROPICAL_FISH # Material reference
        effects:
          # PotionEffectType reference;Level of effect
          - HUNGER;1
          - REGENERATION;2
        labelEffects: "&l&7Description of the applied effects"
        durationInSeconds: 60
        customModelData: 1
        additionalLore:
          - ""
        ingredients:
          - P;PAPER # ";" for original game items
          - C,Cogote # "," for baseItems plugin items
        shape:
          - "P"
          - "C"
          - "P"
```

## Add Base Items
Create a new base item, its use is based on the crafting of drugs
```
baseItems:
  itemName:
    displayName: "&r&2You can use colors"
    baseMaterial: BEETROOT
    lore:
      - "&r&fItem unique lore"
    customModelData: 1
    obtainingInfo:
      type: crop
      blockMaterial: BEETROOTS
      dropItems:
        # Material or self reference;range of random items to drop
        - self;1-4
        - BEETROOT_SEEDS;0-2
```

## Modify Overdose Status
```
states:
  overdose:
    durationInSeconds: 30
    effects:
      # PotionEffectType;Level
      - WITHER;1
      - SLOW;1
      - CONFUSION;1
      - BLINDNESS;1
      - HUNGER;1
```

## Custom Messages
```
messages:
  overdose: "Message sent to player when overdosed"
  consume: "Allows the player to indicate the item he consumed: %drugDisplayName%"
  cooldownConsume: "Message to alert the player about excessive consumption"
  insufficientAgeCrop: "When a plant has not grown enough to be cultivated"
  validCrop: "Indicate how many items: %stackAmountDrop%, and the display name of the item: %itemDisplayName%"
```