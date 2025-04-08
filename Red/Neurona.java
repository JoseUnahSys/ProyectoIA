
import java.util.ArrayList;
import java.util.List;

import java.util.Random;

public class Neurona {
    public double sesgo;
    public double[] pesos;
    double sumaActual;

    Neurona(int numEntradas, Random r)
    {
        // sesgo = r.nextDouble();
        sesgo = -0.5;
        pesos = new double[numEntradas];

        for(int i = 0; i < pesos.length; i++) {
            // pesos[i] = r.nextDouble();
            pesos[i] = -0.5;
        }
    }

    public double Activacion(double[] entradas)
    {
        sumaActual = sesgo;

        for(int i = 0; i < entradas.length; i++) {
            sumaActual += entradas[i] * pesos[i];
        }

        return Sigmoid(sumaActual); // Funcion de activacion, usando Sigmoid
    }

    public double Sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }
}


