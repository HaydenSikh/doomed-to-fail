package org.gremlincorp.dtf.core.skill

import org.gremlincorp.dtf.core.skill.Element._

object Skill extends Enumeration {
    type Skill = Value

    val Heal = Value("Heal", Light, 5)
    val GreaterHeal = Value("Greater Heal", Light, 15)
    val Antidote = Value("Antidote", Light, 5)

    val Corrode = Value("Corrode", Dark, 5)

    class SkillVal(name: String, val element : Element.Value, val cost : Int) extends Val(nextId, name)
    protected final def Value(name: String, element : Element.Value, cost : Int): SkillVal = new SkillVal(name, element, cost)
}