package org.gremlincorp.dtf.core.character

import scala.collection.mutable
import org.gremlincorp.dtf.core.item.Item

class Party {
    val activeCharacters = new mutable.ArraySeq[Character](3)
    val inactiveCharacters = new mutable.HashSet[Character]()

    val inventory = new mutable.HashMap[Item, Int]()
    var money = 0
}