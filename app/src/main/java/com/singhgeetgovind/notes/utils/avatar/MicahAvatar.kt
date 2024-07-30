package com.singhgeetgovind.notes.utils.avatar

enum class MicahAvatar {
    George,
    Bandit,
    Shadow,
    Snuggles,
    Rocky,
    Trouble,
    Lucky,
    Jasmine,
    Whiskers,
    Abby,
    Smokey,
    Luna,
    Kitty,
    Lily,
    Bob,
    Midnight,
    Cuddles,
    Toby,
    Gizmo,
    Kiki;
    fun getBASEURL():String{
        this.name.replace("_","")
        return "https://api.dicebear.com/9.x/micah/png?seed=${this.name}$&radius=50&backgroundColor=c0aede,ffdfbf"
    }
}