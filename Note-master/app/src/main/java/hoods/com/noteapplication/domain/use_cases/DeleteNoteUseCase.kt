package hoods.com.noteapplication.domain.use_cases

import hoods.com.noteapplication.domain.repository.Repository
import javax.inject.Inject
//una abstracción para la operación de eliminación de una nota
class DeleteNoteUseCase @Inject constructor(
    private val  repository: Repository
) {
    suspend operator fun  invoke(id:Long) =repository.delete(id)
}