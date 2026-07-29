## Changes

* Added Pirate Speak, Upside Down English, and LOLCAT translations
* Dinnerbone and Grumm are now rendered upside down in the inventory when the built-in resource pack is enabled
* When the built-in resource pack is enabled, the armor stand item now uses its vanilla appearance if the player's skin has not been downloaded yet
* Replaced the `/pas reload_failed` command with `/pas reload failed`
* Added the `/pas reload all` command

## Technical Changes

* Added the `pas:downloaded` property for `condition` item models. It selects `on_true` if the skin associated with the nickname specified in the item's name has already been downloaded

## Fixes

* Fixed a crash that could occur when the game or another mod attempted to render an armor stand, including inside user interfaces
* Fixed an issue where reloading a skin could accidentally remove an actual player's texture from the game's memory
* Fixed Z-fighting in ||Easter egg images||
* Fixed the geometry of the small armor stand model
* Fixed an issue where the visible parts specified in `state` were not applied to the `pas:armor_stand` special item model
