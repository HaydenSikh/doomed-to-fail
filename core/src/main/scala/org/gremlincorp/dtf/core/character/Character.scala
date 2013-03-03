package org.gremlincorp.dtf.core.character

import org.gremlincorp.dtf.core.item.{ Helmet, BodyArmor, Accessory, Weapon }

class Character(
        val charClass : String,
        var level : Int,
        var weapon : Weapon,
        var helmet : Helmet,
        var bodyArmor : BodyArmor,
        var accessory : Accessory
) {}