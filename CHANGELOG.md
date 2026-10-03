[2.1.12]

**Added**
* Brewfest Garb set bonus Harddrinking: wearing the full set makes you immune to Drunkenness. The set tooltip shows every piece and lights up the bonus once the set is complete
* Brewing Station now shows an info tooltip once something is in the Kettle: the ingredients, the next valid or missing ingredients, the possible drinks and what is still needed to start brewing (water, heat or a better Brewing Station)
* New config with the categories Effects, Brewing, Drunkenness, Info Tooltips and Food:
  * Effects: fine-tuning of Combustion, Repulsion, Stoutheart, Mining and Pacify, and flying with Amorous Glide on or off
  * Brewing: brew time, brew events on or off, time between brew events, Beer Elementals on or off
  * Drunkenness: turn drunkenness, swaying, slowness, blackouts and the blackout teleport on or off, blackout chance and teleport range
  * Info Tooltips: turn off the Brewing Station info tooltip or only show it while wearing Dungarees from Farm & Charm
  * Food: hunger and saturation of all food items
* VanillaBlend: an optional built-in resource pack with muted, vanilla-friendly colors for beers, whiskeys, food, the Brewing Station, Breathalyzer, Mob Effect icons and more. 
* Patterned Wool, Patterned Carpet and the Tablecloth can be dyed: right-click with any dye. Dyed Wool crafts into Carpet of the same color, and a Carpet placed on a Table keeps its color

**Changed**
* The Kettle of the Brewing Station is now two blocks tall and has a collision box at its back wall. Needs one more free block above the Kettle when placing
* Beer and Whiskey quality is now shown with three beer barrel icons instead of a number
* Patterned Wool, Patterned Carpet and the Tablecloth are now light blue by default
* Wild Hops item now uses the top part of the plant as its texture, like vanilla tall plants

**Fixed**
* Placed Beer and Whiskey can be stacked again: right-click with another bottle to add it, right-click with an empty hand to take one
* German tooltip of the Brewery Banner now correctly says it grants Haste II
* Breaking a Table with a Tablecloth now also drops the Carpet

***

[2.1.11]

**Fixed**
* Netherite Brewingstation no longer always yields only 1 beer despite maximum quality

***

[2.1.10]

**Added**
* Wild Hops can now be grown using bonemeal

**Fixed**
* Beer and Whiskey no longer always apply the weakest potion effect level regardless of brew quality
* Hops now correctly drop items when harvested with Create's Harvester or Deployer
* Patterned Carpet no longer drops nothing when broken
* Picking up a Brewing Station no longer secretly consumes already-brewed beer

**Changed**
* Updated ru_ru translation (thanks to Tefny)

***

[2.1.9]

**Fixed**
* Crash on NeoForge caused by concurrent item property registration for the Breathalyzer during client setup

*** 


[2.1.8]

**Fixed**
* Brewing output no longer increases with each subsequent brew in the same kettle
* Crash when interacting with the Brew Oven while Decorative Blocks: Reborn is installed

**Changed**
* Netherite Brewing Stations now work in comfort mode and no longer require the brewing minigame
* Brewing quality rebalanced:
  * 0 when no events are solved
  * 2 for solving 2–4 events
  * 3 only when all events are solved

*** 

[2.1.7]

**Fixed**
* BrewfestArmor being HUGE when placed inside AlpineWhispers / Meadows wardrobe
* Brewery Items not being compostable on Neoforge

**Changed**
* Drying recipes for Corn now use c:crops/corn tags instead of direct item IDs for better mod compatibility

**Added**
* Wild Hops are now obtainable

***

[2.1.6]

**Requires Farm & Charm 1.1.15+**

**Fixed**
* Effect duration was displayed in an incorrect format
* Beer and Whiskey effect levels were not applied correctly
* Text written on Gingerbread was not displayed properly

**Changed**
* Reduced overly saturated textures (work in progress)
* Based on frequent feedback: hops do not use seeds in real-life cultivation. Seeds have been removed. Hops can now be replanted using hops themselves
* Slight adjustments to armor sizing

***

[2.1.5]

**Fixed**
* CompletionistBanner applying the wrong effect to nearby Players 
* Haley effect no longer overrides other flight sources; mayfly is granted once on start and revoked only when the effect ends

***

[2.1.4]

**Changed**
* Intoxication effect with progressive camera sway and slight random drift.
  * Movement speed penalty scaling with amplifier.
  * Periodic Nausea at severe intoxication levels.
  * High intoxication can trigger a Blackout; duration increased to **12s**.
* Empty Beer Mugs can now simply be picked up with a right-click.

***

[2.1.3]

**Fixed**
* Brewfest armor pieces no longer render as black/red when dyed leather is equipped. Fixed by updating custom armor models to use the correct `renderToBuffer(int color)` signature introduced in 1.21.1, restoring proper leather tinting.

***

[2.1.2]

**Fixed**
* ArmorItems not being rendered properly on NeoForge
* Startup crash

***

[2.1.1]

**Fixed**
* Server crashing upon startup
* Crash caused by unregistered custom MobEffects not being saved correctly

***

[2.1.0]

** Ported to 1.21.1 ** 

***

[2.0.6] 

**Added**
* You can now add your own Text to Gingerbread Wall Decoration

**Fixed**
* Bucket stack consumption in BrewKettleBlock to only consume one bucket at a time
* Baby Zombies spawning with slightly oversizd Brewery Clothing
* Single Brewing Station Parts being movable by using Pistons

***

[2.0.5] 

**Added**
* Zombies have a really low Chance to spawn wearing a Brewfest Outfit and Holding a Bottle of Whiskey

**Changed**
* Increased Bar Counter crafting result count from "1" to "2"
* Increased Sideboard crafting result count from "1" to "2"
* Adjusted all Recipe .json the the new format
* All Brewery Armor Parts are now craftable
* Improved Plate, Bowl and Large Plate Textures
* Improved Sideboard Model & Logic 

**Fixed**
* All beers and brews from the same batch now have identical effects and durations






