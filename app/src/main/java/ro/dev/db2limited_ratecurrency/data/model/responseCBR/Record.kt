package ro.dev.db2limited_ratecurrency.data.model.responseCBR

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "Record")
data class Record constructor(
    @field:Attribute
    var Date: String = "",
    @field:Attribute
    var Id: String = "",
    @field:Element(name = "Nominal", required = true)
    var Nominal: String = "",
    @field:Element(name = "Value", required = true)
    var Value: String = ""
)
