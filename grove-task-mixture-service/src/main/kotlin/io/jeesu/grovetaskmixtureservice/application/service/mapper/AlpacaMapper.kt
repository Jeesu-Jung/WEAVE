package io.jeesu.grovetaskmixtureservice.application.service.mapper

import io.jeesu.grovetaskmixtureservice.domain.instruction.BestInstruction
import io.jeesu.grovetaskmixtureservice.domain.instruction.Instruction
import io.jeesu.grovetaskmixtureservice.domain.mixture.BestModel
import io.jeesu.grovetaskmixtureservice.presentation.dto.AlpacaDto
import org.springframework.stereotype.Component

@Component
class AlpacaMapper {
    fun toSearchResponseItem(instruction: BestInstruction): AlpacaDto.SearchResponseItem {
        return AlpacaDto.SearchResponseItem(
            input = instruction.input,
            inputs = instruction.inputs,
            constraint = instruction.constraint,
            output = instruction.output,
            instruction = instruction.instruction
        )
    }

    fun toBestModelDataSizeInfo(bestModel: BestModel): AlpacaDto.BestModelDataSizeInfo {
        return AlpacaDto.BestModelDataSizeInfo(
            programming = bestModel.programming,
            math = bestModel.math,
            creativeWriting = bestModel.creativeWriting,
            grammar = bestModel.grammar,
            history = bestModel.history
        )
    }

    fun toSearchResponse(instructions: List<BestInstruction>, bestModel: BestModel): AlpacaDto.SearchResponse {
        return AlpacaDto.SearchResponse(
            items = instructions.map { toSearchResponseItem(it) },
            bestModelDataSizeInfo = toBestModelDataSizeInfo(bestModel)
        )
    }

    fun toBestInstruction(instruction: Instruction): BestInstruction {
        return BestInstruction(
            id = instruction.id,
            input = instruction.input,
            inputs = instruction.inputs,
            constraint = instruction.constraint,
            output = instruction.output,
            instruction = instruction.instruction
        )
    }
}
