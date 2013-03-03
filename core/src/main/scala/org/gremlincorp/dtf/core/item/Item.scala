package org.gremlincorp.dtf.core.item

trait Item {
    val name : String
}

trait Consumable extends Item {}

trait Equipable extends Item {}

trait Armor extends Equipable {
    val defense : Int
}

class Accessory(val name : String) extends Equipable {}

class Helmet(val name : String, val defense : Int) extends Armor {}

class BodyArmor(val name : String, val defense : Int) extends Armor {}

class Weapon(val name : String, val attackPower: Int, val accuracy: Int) extends Equipable {}