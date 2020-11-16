package ro.dev.db2limited_ratecurrency.data.model.responseCBR

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "ValCurs")
data class ValCurs constructor(
    @field:Attribute(name = "DateRange1",
    empty = "",
    required = true)
    var DateRange1: String = "",
    @field:Attribute(name = "DateRange2",
        empty = "",
        required = true)
    var DateRange2: String = "",

    @field:Attribute(name = "ID", required = true)
    var ID: String = "",

    @field:Attribute(name = "name", required = true)
    var name: String = "",

    @field:ElementList(name = "Record", entry = "Record",
    inline = true,
    empty = true,
    required = false)
    var Record: List<Record>? = null
)

/*
<ValCurs ID="R01235" DateRange1="01.04.2020" DateRange2="27.04.2020" name="Foreign Currency Market Dynamic">
    <Record Date="07.04.2020" Id="R01235">
        <Nominal>1</Nominal>
        <Value>76,4074</Value>
    </Record>
 */