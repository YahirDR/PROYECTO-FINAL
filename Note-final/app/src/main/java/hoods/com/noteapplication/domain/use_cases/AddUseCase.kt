package hoods.com.noteapplication.domain.use_cases
//caso de uso para agregar una nueva nota.
import hoods.com.noteapplication.data.local.model.Note
import hoods.com.noteapplication.domain.repository.Repository
import javax.inject.Inject
// El constructor inyecta una instancia de Repository en esta clase  a través de la inyección de dependencias.
class AddUseCase @Inject constructor(
    private val repository: Repository
) {
    // La función `invoke` es una función especial que permite que la instancia de la clase AddUseCase sea llamada como una función.
    suspend operator fun invoke(note: Note) = repository.insert(note)
    //Se utiliza `repository.insert(note)` para insertar la nota en la fuente de datos a través del repositorio.
    // Se usa `suspend`
    // porque la operación de inserción en la fuente de datos puede ser asincrónica y,
    // por lo tanto, debe ser llamada dentro de una corrutina o un contexto asincrónico.

}








