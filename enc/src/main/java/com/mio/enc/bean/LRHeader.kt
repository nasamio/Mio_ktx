package com.mio.enc.bean

open class LRHeader(
    var recordLength: Int = 0,
    var exchangeLevel: Int = 0,
    var leaderIdentifier: String = "",
    var codeIndicator: String = "",
    var versionNumber: Int = 0,
    var applicationIndicator: String = "",
    var fieldControlLength: Int = 0,
    var fieldAreaBaseAddress: Int = 0, // 基地址 应该是字段区开始的标志
    var exCharacterSetIndicator: String = "",
    var fieldLengthSize: Int = 0,
    var fieldPositionSize: Int = 0,
    var reversed: Int = 0,
    var fieldIndicatorSize: Int = 0,
) {
    // 打印每个字段
    override fun toString(): String {
        return """
            Record Length:              $recordLength
            Exchange Level:             $exchangeLevel
            Leader Identifier:          $leaderIdentifier
            Code Indicator:             $codeIndicator
            Version Number:             $versionNumber
            Application Indicator:      $applicationIndicator
            Field Control Length:       $fieldControlLength
            Field Area Base Address:    $fieldAreaBaseAddress
            Character Set Indicator:    $exCharacterSetIndicator
            Field Length Size:          $fieldLengthSize
            Field Position Size:        $fieldPositionSize
            Reversed:                   $reversed
            Field Indicator Size:       $fieldIndicatorSize
        """.trimIndent()
    }
}
