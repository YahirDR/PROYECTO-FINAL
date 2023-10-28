package hoods.com.noteapplication.domain.use_cases

import hoods.com.noteapplication.domain.repository.Repository
import javax.inject.Inject
//Obtener una nota por su ID a traves del repositorio
class GetNoteByIdUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(id:Long) = repository.getNoteById(id)
}