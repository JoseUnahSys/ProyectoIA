
import java.util.ArrayList;
import java.util.List;

import java.util.Random;

public class Capa {
    public ArrayList<Neurona> neuronas;
    public double[] salidas;

    Capa(int numEntradas, int numNeuronas, Random r)
    {
        neuronas = new ArrayList<Neurona>(numNeuronas);

        for(int i = 0; i < numNeuronas; i++) {
            neuronas.add(new Neurona(numEntradas, r));
        }

        salidas = new double[neuronas.size()];
    }

    public double[] Activacion(double[] entradas)
    {
        for(int i = 0; i < neuronas.size(); i++) {
            salidas[i] = neuronas.get(i).Activacion(entradas);
            // System.out.printf("\t[Capa]: Neurona[%s] activacion %.16f\n", i, salidas[i]);
        }
        return salidas;
    }
}
