package anura.ddts

import com.ubertob.pesticide.DomainUnderTest
import com.ubertob.pesticide.ScenarioSteps

data class Scenario<D : DomainUnderTest<*>>(val name: String, val steps: ScenarioSteps<D>)