package hoods.com.noteapplication.domain.use_cases

import hoods.com.noteapplication.domain.repository.Repository
import javax.inject.Inject
// Constructor que recibe una instancia de Repository a través de inyección de dependencias.
class GetNoteByIdUseCase @Inject constructor(
    private val repository: Repository
) {
    // La función 'invoke' es una función especial que permite que la instancia de la clase sea llamada como una función.
    // En esta función, se utiliza el 'repository' para obtener una nota por su id desde la fuente de datos.
    operator fun invoke(id:Long) = repository.getNoteById(id)
}