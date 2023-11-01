package hoods.com.noteapplication.common

//Se define una   clase sellada que puede tener tres subclases distintas:
// Loading,
// Success
// Error.
//<out T> permite que esta clase pueda contener diferentes tipos de datos.
sealed class ScreenViewState<out T> {
    // La subclase "Loading" sirve para representar  el estado de carga, es decir,
    // cuando la pantalla se encuentra en proceso de carga de datos.
    object Loading:ScreenViewState<Nothing>()

    // La subclase "Success" representa el estado exitoso,
    // cuando se han cargado datos correctamente.
    // Contiene un valor de tipo genérico "T" que representa los datos cargados.
    data class Success<T>(val data:T):ScreenViewState<T>()

    // La subclase "Error" representa un estado de error,
    // cuando ocurre un problema al cargar datos.
    data class Error(val message:String?):ScreenViewState<Nothing>()
}