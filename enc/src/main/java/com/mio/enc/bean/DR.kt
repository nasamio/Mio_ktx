package com.mio.enc.bean

class DR(drHeader: DRHeader, drDir: DRDir, drField: DRField) :
    LR<DRHeader, DRDir, DRField>(drHeader, drDir, drField) {
}