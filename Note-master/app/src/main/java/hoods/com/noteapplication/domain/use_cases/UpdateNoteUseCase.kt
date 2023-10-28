package hoods.com.noteapplication.domain.use_cases

import hoods.com.noteapplication.data.local.model.Note
import hoods.com.noteapplication.domain.repository.Repository
import javax.inject.Inject
//Actualizar la nota a traves del repositorio
class UpdateNoteUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(note: Note){
        repository.update(note)
    }
}