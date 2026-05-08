package dam_A15316.catapiapp.model

import java.io.Serializable

data class Breed(
    val id: String?,
    val name: String?,
    val origin: String?,
    val temperament: String?,
    val description: String?,
    val life_span: String?
) : Serializable
