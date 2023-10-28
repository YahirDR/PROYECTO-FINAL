package hoods.com.noteapplication.domain.use_cases

import hoods.com.noteapplication.data.local.model.Note
import hoods.com.noteapplication.domain.repository.Repository
import javax.inject.Inject
//gregar nuevas notas en la fuente de datos
class AddUseCase @Inject constructor(
    private val repository: Repository
) {
    //Ayudar a la ejecucion
    suspend operator fun  invoke(note:Note) = repository.insert(note)
}
