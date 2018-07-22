

package com.charlatano.scripts

import com.charlatano.game.netvars.NetVarOffsets.iCompetitiveRanking
import com.charlatano.game.forEntities
import com.charlatano.game.entity.EntityType.Companion.ccsPlayer
import com.charlatano.game.CSGO.clientDLL
import com.charlatano.game.CSGO.csgoEXE
import com.charlatano.game.offsets.ClientOffsets.dwPlayerResource
import com.charlatano.game.offsets.ClientOffsets.dwRadarBase
import com.charlatano.utils.extensions.uint
val ranklist = listOf("Unranked","Silver I","Silver II","Silver III","Silver IV","Silver Elite","Silver Elite Master","Nova I","Nova II","Nova III","Nova Master","Master Guardian I","Master Guardian II","Master Guardian Elite","Distinguished Master Guardian","Legendary Eagle","Legendary Eagle Master","Supreme Master First Class","The Global Elite")
val playerResource = clientDLL.uint(dwPlayerResource)
val playerNameBase = clientDLL.uint(dwRadarBase)
val namebase = csgoEXE.uint(playerNameBase + 0x54)
@Volatile var rankIndex = 1

fun getRanks() {
rankIndex = 1	
System.out.println("=============WARNING=============")
System.out.println("It's only working on Matchmaking")
System.out.println("If name's are glitchy turn back game and wait another round")
forEntities(ccsPlayer) {
rankIndex++

//Get Name List
//Player name is unicode and kotlin didn't get unicode strings xd
val nameplayer1 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x24, 1,false)!!.getString(0)
val nameplayer2 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x26, 1,false)!!.getString(0)
val nameplayer3 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x28, 1,false)!!.getString(0)
val nameplayer4 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x2A, 1,false)!!.getString(0)
val nameplayer5 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x2C, 1,false)!!.getString(0)
val nameplayer6 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x2E, 1,false)!!.getString(0)
val nameplayer7 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x30, 1,false)!!.getString(0)
val nameplayer8 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x32, 1,false)!!.getString(0)
val nameplayer9 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x34, 1,false)!!.getString(0)
val nameplayer10 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x36, 1,false)!!.getString(0)
val nameplayer11 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x38, 1,false)!!.getString(0)
val nameplayer12 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x3A, 1,false)!!.getString(0)
val nameplayer13 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x3C, 1,false)!!.getString(0)
val nameplayer14 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x3E, 1,false)!!.getString(0)
val nameplayer15 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x40, 1,false)!!.getString(0)
val nameplayer16 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x42, 1,false)!!.getString(0)
val nameplayer17 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x44, 1,false)!!.getString(0)
val nameplayer18 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x46, 1,false)!!.getString(0)
val nameplayer19 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x48, 1,false)!!.getString(0)
val nameplayer20 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x4A, 1,false)!!.getString(0)
val nameplayer21 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x4C, 1,false)!!.getString(0)
val nameplayer22 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x4E, 1,false)!!.getString(0)
val nameplayer23 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x50, 1,false)!!.getString(0)
val nameplayer24 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x52, 1,false)!!.getString(0)
val nameplayer25 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x54, 1,false)!!.getString(0)
val nameplayer26 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x56, 1,false)!!.getString(0)
val nameplayer27 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x58, 1,false)!!.getString(0)
val nameplayer28 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x5A, 1,false)!!.getString(0)
val nameplayer29 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x5C, 1,false)!!.getString(0)
val nameplayer30 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x5E, 1,false)!!.getString(0)
val nameplayer31 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x60, 1,false)!!.getString(0)
val nameplayer32 = csgoEXE.read(namebase + (0x1E0 * rankIndex) + 0x62, 1,false)!!.getString(0)
//End of the getting name


val rankprint = ranklist[csgoEXE.int((playerResource + iCompetitiveRanking) + (rankIndex * 4))] // selecting rank with rank number
System.out.println(nameplayer1 + nameplayer2 + nameplayer3 + nameplayer4 + nameplayer5 + nameplayer6 + nameplayer7 + nameplayer8 + nameplayer9 + nameplayer10+ nameplayer11+nameplayer12+nameplayer13+nameplayer14+nameplayer15+nameplayer16+nameplayer17+nameplayer18+nameplayer19+nameplayer20+nameplayer21+nameplayer22+nameplayer23+nameplayer24+nameplayer25+nameplayer26+nameplayer27+nameplayer28+nameplayer29+nameplayer30+nameplayer31+nameplayer32 + " rank is ; " +	rankprint)//And Print to Console!!
}
}
