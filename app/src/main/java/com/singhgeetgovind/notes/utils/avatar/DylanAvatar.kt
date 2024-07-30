package com.singhgeetgovind.notes.utils.avatar

/** https://api.dicebear.com/9.x/dylan/svg?seed=Felix&radius=50 */
enum class DylanAvatar  {
    Midnight,
    Missy,
    Salem,
    Oscar,
    Snickers,
    Sophie,
    Mia,
    Patches,
    Boo,
    Callie,
    Lucy,
    Annie,
    Bubba,
    Smokey,
    Casper,
    Mittens,
    Rocky,
    Tinkerbell,
    Miss_kitty,
    Garfield;

    fun getBASEURL():String{
       this.name.replace("_","")
        return "https://api.dicebear.com/9.x/dylan/png?seed=${this.name}$&radius=50&backgroundType=gradientLinear"
    }
}