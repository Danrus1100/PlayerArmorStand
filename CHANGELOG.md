##New Features:* Updated mod icon
* Updated armor stand item render: it will now display the player's head for the given armor stand by default (temporarily Fabric only)
* Improved determination of which texture should be used for the stand
* Removed support for 1.21.1

##Technical New Features* Added a new `special` model `pas:armor_stand`
* The model has a `state` parameter, where you need to specify the state of the body parts: `head`, `body`, `left_arm`, `right_arm`, `left_leg`, and `right_leg`
* Each body part has a `rotation` (a list of 3 numbers), which you can use to set the part's rotation, and `mode`, which determines the display behavior of the body part.
* There are 4 display modes:

1. `dynamic`: the model for the body part will be automatically selected based on whether a skin is loaded or not
2. `original`: this part will be displayed as an armor stand regardless
3. `invisible`: this part will not be displayed
4. `player`: the body part will be a player regardless

* You can find more details on how to work with this model by looking at the [built-in resource packs](https://github.com/Danrus1100/PlayerArmorStand/tree/dev/src/main/resources/resourcepacks) or in [Player Pickaxes](https://modrinth.com/resourcepack/player-pickaxes)