package hoods.com.noteapplication.domain.use_cases

import hoods.com.noteapplication.data.local.model.Note
import hoods.com.noteapplication.domain.repository.Repository
import javax.inject.Inject
// Constructor que recibe una instancia de Repository a través de inyección de dependencias.
class UpdateNoteUseCase @Inject constructor(
    private val repository: Repository,
) {
    // La función 'invoke' es una función especial que permite que la instancia de la clase sea llamada como una función.
    // La función está marcada como 'suspend' porque la operación de actualización en la fuente de datos puede ser asincrónica.
    suspend operator fun invoke(note: Note) {
        // En esta función, se utiliza el 'repository' para actualizar una nota  desde la fuente de datos.
        repository.update(note)
    }
}