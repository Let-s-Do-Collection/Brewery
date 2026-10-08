[2.1.12]

**Added**
* Brewing now starts with the button on the Brew Timer. The info tooltip of the Brewing Station tells you when everything is ready
* The liquid in the Brew Kettle now rises and sinks smoothly 
* The lid of the Brew Kettle moves now: it stands open while idle, lowers when brewing starts, rattles up and down when the kettle overflows and opens up a bit when the water runs low, all with smooth transitions
* The Brew Timer came to life: the needle of the tachometer follows the brewing progress and jitters wildly when the timer rings, and the button springs in with a click when pressed
* The steam event of the Brew Whistle got an overhaul: thick steam clouds instead of smoke, a steady hiss under the whistle and the whistle shakes while the steam is up
* The Big Barrel now ages drinks: right-click with a beer or whiskey to store it (9 spots), shift + right-click to take the last one out. Every 7 in-game days in the barrel raise the quality by one, up to the new fourth level shown as a golden barrel. Barrel-aged drinks last longer, hit harder and make you less drunk. The info tooltip shows what is inside and how far it has aged
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
* The Kettle of the Brewing Station is now two blocks tall. Needs one more free block above the Kettle when placing
* Beer and Whiskey quality is now shown with three beer barrel icons instead of a number

**Fixed**
* Beer quality is harder to get: quality 2 needs at least 3 solved brew events with at most one missed, quality 3 needs at least 4 solved events and none missed. Before, a single solved event could already give the best quality
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






