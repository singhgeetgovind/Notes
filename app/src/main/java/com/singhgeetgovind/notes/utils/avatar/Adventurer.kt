package com.singhgeetgovind.notes.utils.avatar


enum class Adventurer : Avatar {
    Misty,
    Garfield,
    Jasmine,
    Chester,
    Charlie,
    Bella,
    Baby,
    Patches,
    Ginger,
    Casper,
    Pumpkin,
    Samantha,
    Luna,
    Simon,
    Gizmo,
    Missy,
    Lily,
    Precious,
    Coco,Sadie,
    Cookie,Mia,Harley,
    Lola,Jasper,Chloe,Annie,Milo,Cali,Loki,Dusty;
    override fun getBASEURL():String{
        this.name.replace("_","")
        return "$URL${this::class.simpleName?.lowercase()}/png?seed=${this.name}$&radius=50&backgroundColor=ffdfbf"
    }
}