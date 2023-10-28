package hoods.com.noteapplication.common
//Definimos una clase sellada para crear una jerarqui de clases
//El uso de T  significa que las s
// ubclases pueden tener datos de diferentes tipos.
sealed class pantallaVista<out T> {
    //SUBCLASES:
    //Subclase: Estado Inicial de la pantalla
    object  Cargar:pantallaVista<Nothing>()
    //Subclase: Esto de exito
    data class  Exito<T>(val  data:T):pantallaVista<T>()
    //Subclase: Esto de error (SAD)
    data class  Error(val message:String?):pantallaVista<Nothing>() //Ocurrio un error
}