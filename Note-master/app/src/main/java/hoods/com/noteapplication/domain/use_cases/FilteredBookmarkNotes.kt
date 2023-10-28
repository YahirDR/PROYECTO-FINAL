package hoods.com.noteapplication.domain.use_cases

import hoods.com.noteapplication.data.local.model.Note
import hoods.com.noteapplication.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
//btener notas marcadas como favoritas
class FilteredBookmarkNotes @Inject constructor(
    private val repository: Repository
) {
    //No agregamos suspend por que dovolvera un flow
    operator fun invoke(): Flow<List<Note>>{
        return repository.getBookMarkedNotes()
    }
}