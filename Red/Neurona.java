
import java.util.ArrayList;
import java.util.List;

import java.util.Random;

public class Neurona {
    public double sesgo;
    public double[] pesos;
    double sumaActual;

    Neurona(int numEntradas, Random r)
    {
        sesgo = r.nextDouble() - 0.5;
        pesos = new double[numEntradas];

        for(int i = 0; i < pesos.length; i++) {
            pesos[i] = r.nextDouble() - 0.5;
        }
    }

    public double Activacion(double[] entradas)
    {
        sumaActual = sesgo;

        for(int i = 0; i < entradas.length; i++) {
            sumaActual += entradas[i] * pesos[i];
        }

        // return ReLu(sumaActual);
        return Sigmoid(sumaActual); // Funcion de activacion, usando Sigmoid
    }

    public static double Sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    public static double ReLu(double x) {
        return Math.max(x, 0);
    }
}


