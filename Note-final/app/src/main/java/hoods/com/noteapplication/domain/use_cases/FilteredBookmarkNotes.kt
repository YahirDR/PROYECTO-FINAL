package hoods.com.noteapplication.domain.use_cases

import hoods.com.noteapplication.data.local.model.Note
import hoods.com.noteapplication.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
// Constructor que recibe una instancia de Repository a través de inyección de dependencias.
class FilteredBookmarkNotes @Inject constructor(
    private val repository: Repository,
) {
    // La función 'invoke' es una función especial que permite que la instancia de la clase sea llamada como una función.
    operator fun invoke(): Flow<List<Note>> {
        // En esta función, se utiliza el 'repository' para obtener las notas marcadas como favoritas desde la fuente de datos.
        return repository.getBookMarkedNotes()
    }
}