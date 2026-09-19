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






