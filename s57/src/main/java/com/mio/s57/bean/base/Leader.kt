package com.mio.s57.bean.base

/**
 * 见https://iho.int/uploads/user/pubs/standards/s-57/31Main.pdf第94页
 */
class Leader {
    var recordLength: Int = 0 // Record length
    var interchangeLevel: Int = 0 //  Interchange level
    var leaderIdentifier: String = ""
    var extensionIndicator: String = ""
    var versionNumber: Int = 0
    var applicationIndicator: String = ""
    var filedControlLength: Int = 0
    var baseAddress: Int = 0
    var extendedCharacterSetIndicator: String = ""

    // Entry map
    var filedLengthSize: Int = 0
    var filedPositionSize: Int = 0
    var reversed: Int = 0
    var filedTagSize: Int = 0
    override fun toString(): String {
        return "Leader(recordLength=$recordLength, interchangeLevel=$interchangeLevel, leaderIdentifier='$leaderIdentifier', extensionIndicator='$extensionIndicator', versionNumber=$versionNumber, applicationIndicator='$applicationIndicator', filedControlLength=$filedControlLength, baseAddress=$baseAddress, extendedCharacterSetIndicator='$extendedCharacterSetIndicator', filedLengthSize=$filedLengthSize, filedPositionSize=$filedPositionSize, reversed=$reversed, filedTagSize=$filedTagSize)"
    }


}