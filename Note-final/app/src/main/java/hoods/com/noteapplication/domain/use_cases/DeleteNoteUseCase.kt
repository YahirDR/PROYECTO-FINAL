package hoods.com.noteapplication.domain.use_cases
//caso de uso para eliminar una nota en la aplicación.
import hoods.com.noteapplication.domain.repository.Repository
import javax.inject.Inject
// Constructor que recibe una instancia de Repository a través de inyección de dependencias.
class DeleteNoteUseCase @Inject constructor(
    private val repository: Repository,
) {
    // La función 'invoke' es una función especial que permite que la instancia de la clase sea llamada como una función.
    // En esta función se utiliza el 'repository' para eliminar una nota de la fuente de datos por su ID.
    suspend operator fun invoke(id: Long) = repository.delete(id)
}