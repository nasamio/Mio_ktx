package com.mio.enc.bean

class DDR(ddrHeader: DDRHeader, ddrDir: DDRDir, ddrField: DDRField) :
    LR<DDRHeader, DDRDir, DDRField>(ddrHeader, ddrDir, ddrField) {
}