/*
Método par calcular el tiempo de lectura de un libro en base a su dificultad:
3: alta, 2:media, 1:baja
*/

public double calcularTiempoLectura(int dificultad, int nH, double tiempo){
    double tiempoPromedioLecturaPorPagina = 3.0;
    double tiempoLecturaTotal = 0.0;
    double númeroHojas = nH;
    if(dificultad == 3){
        //Añadir 50% de tiempo para dificultad 3
        tiempoPromedioLecturaPorPagina *=1.5;
    } else if(dificultad == 2){
        // No hay cambio en el tiempo para dificultad 2
        tiempoPromedioLecturaPorPagina *=1.0;
    } else if(dificultad == 1){
        // Reducir a la mitad el tiempo para dificultad 1
        tiempoPromedioLecturaPorPagina *=0.5;
    } else {
        System.out.println("Dificultad no válida");
    }

    for(int i=0; i<númeroHojas; i++){
        tiempoLecturaTotal += tiempoPromedioLecturaPorPagina;
    }
    return tiempoLecturaTotal;
}

// Refactorización del código anterior para simplificar el cálculo del tiempo de lectura
public double calcularTiempoLecturaRefactorizado(int dificultad, int numeroHojas, double tiempo){
    double tiempoPromedioLecturaPorPagina;
    switch(dificultad){
        case 3:
            tiempoPromedioLecturaPorPagina = 1.5
            break;
        case 2:
            tiempoPromedioLecturaPorPagina = 1.0; 
            break;
        case 1:
            tiempoPromedioLecturaPorPagina = 0.5; 
            break;
        default:
            System.out.println("Dificultad no válida");
            return 0.0;
    }
    double tiempoPorPagina = tiempo * tiempoPromedioLecturaPorPagina
    return tiempoPorPagina * numeroHojas;
}