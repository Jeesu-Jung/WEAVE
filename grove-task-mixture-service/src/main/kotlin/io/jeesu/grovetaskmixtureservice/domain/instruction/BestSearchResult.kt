package io.jeesu.grovetaskmixtureservice.domain.instruction

import io.jeesu.grovetaskmixtureservice.domain.mixture.BestModel

data class BestSearchResult(
    val bestInstructions: List<BestInstruction>,
    val bestModel: BestModel
)

data class BestInstruction(
    val id: Long,
    val input: String,
    val inputs: String,
    val constraint: String,
    val output: String,
    val instruction: String
)
