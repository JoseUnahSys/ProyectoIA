
import java.util.ArrayList;
import java.util.List;

import java.util.Random;

public class RedNeuronal {

    public ArrayList<Capa> capas;
    ArrayList<double[]> sigmas;
    ArrayList<double[][]> deltas;

    public RedNeuronal(int[] numNeuronasPorCapa)
    {
        capas = new ArrayList<Capa>(numNeuronasPorCapa.length);
        Random r = new Random();

        for(int i = 0; i < numNeuronasPorCapa.length; i++)
        {
            if(i == 0) {
                capas.add(new Capa(numNeuronasPorCapa[i], numNeuronasPorCapa[i], r));
            } else {
                capas.add(new Capa(numNeuronasPorCapa[i - 1], numNeuronasPorCapa[i], r));
            }
        }

        sigmas = new ArrayList<double[]>(capas.size());
        for(int i = 0; i <  capas.size(); i++){
            sigmas.add(new double[capas.get(i).neuronas.size()]);
        }

        deltas = new ArrayList<double[][]>(capas.size());
        for(int i = 0; i < capas.size(); i++)
        {
            deltas.add(new double[capas.get(i).neuronas.size()][capas.get(i).neuronas.get(0).pesos.length]);
        }

    }


    public double Sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    public double SigmoidDerivada(double x) {
        double y = Sigmoid(x);
        return y*(1 - y);
    }

    public double ReLu(double x) {
        return Math.max(x, 0);
    }

    public double ReLuDerivada(double x) {
        if (x >= 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public double[] Activacion(double[] entradas) {
        double[] salidas = new double[0]; // Creando solo un apuntador
        for(int i = 0; i < capas.size(); i++) {
            // System.out.printf("[RedNeuronal]: capa[%s] {\n", i);
            salidas = capas.get(i).Activacion(entradas);
            entradas = salidas;
            // System.out.printf("}\n", i);
        }
        return salidas;
    }

    // Calculando el error Cuadratico
    public double Error(double[] salidaReal, double[] salidaEsperada)
    {
        double error_acumulado = 0;
        for(int i = 0; i < salidaReal.length; i++) {
            error_acumulado += 0.5 * Math.pow(salidaReal[i] - salidaEsperada[i], 2);
        }
        return error_acumulado;
    }

    public double ErrorTotal(ArrayList<double[]> entradas, ArrayList<double[]> salidaEsperada)
    {
        double error_acumulado = 0;
        for(int i = 0; i < entradas.size(); i++) {
            error_acumulado += Error(Activacion(entradas.get(i)), salidaEsperada.get(i));
        }
        return error_acumulado;
    }

    public void iniciarDeltas()
    {
        for(int i = 0; i < capas.size(); i++)
        {
            for(int j = 0; j < capas.get(i).neuronas.size(); j++)
            {
                for(int k = 0; k < capas.get(i).neuronas.get(0).pesos.length; k++)
                {
                    deltas.get(i)[j][k] = 0;
                }
            }
        }
    }

    /*
        Desenso del gradiente para calcular los sigmas
        Usando Descenso del gradiente
        En base a la salida esperada (resultado esperado)
    */
    public void calcularSigmas(double[] salidaEsperada)
    {
        for(int i = capas.size() - 1; i >= 0; i--) {
            for(int j = 0; j < capas.get(i).neuronas.size(); j++){
                if(i == capas.size() - 1) {
                    double y = capas.get(i).salidas[j];
                    sigmas.get(i)[j] = (y - salidaEsperada[j]) * SigmoidDerivada(y);
                    // sigmas.get(i)[j] = (y - salidaEsperada[j]) * ReLuDerivada(y);
                } else {
                    double sum = 0;
                    for(int k = 0; k < capas.get(i + 1).neuronas.size(); k++){
                        sum += capas.get(i + 1).neuronas.get(k).pesos[j] * sigmas.get(i + 1)[k];
                    }
                    sigmas.get(i)[j] = SigmoidDerivada(capas.get(i).neuronas.get(j).sumaActual) * sum;
                    // sigmas.get(i)[j] = ReLuDerivada(capas.get(i).neuronas.get(j).sumaActual) * sum;
                }
            }
        }
    }

    public void calcularDeltas()
    {
        for(int i = 1; i < capas.size(); i++)
        {
            for(int j = 0; j < capas.get(i).neuronas.size(); j++)
            {
                for(int k = 0; k < capas.get(i).neuronas.get(j).pesos.length; k++)
                {
                    deltas.get(i)[j][k] += sigmas.get(i)[j] * capas.get(i - 1).salidas[k];
                }
            }
        }
    }

    public void activarPesos(double alfa)
    {
        for(int i = 0; i < capas.size(); i++)
        {
            for(int j = 0; j < capas.get(i).neuronas.size(); j++)
            {
                for(int k = 0; k < capas.get(i).neuronas.get(j).pesos.length; k++)
                {
                    capas.get(i).neuronas.get(j).pesos[k] -= alfa * deltas.get(i)[j][k];
                }
            }
        }
    }

    public void activarSesgos(double alfa)
    {
        for(int i = 0; i < capas.size(); i++)
        {
            for(int j = 0; j < capas.get(i).neuronas.size(); j++)
            {
                capas.get(i).neuronas.get(j).sesgo -= alfa * sigmas.get(i)[j];
            }
        }
    }

    public void RetroPropagar(ArrayList<double[]> entradas, ArrayList<double[]> salidaEsperada, double alfa)
    {
        iniciarDeltas();
        for(int i = 0; i < entradas.size(); i++)
        {
            double[] activacion = Activacion(entradas.get(i));
            System.out.printf("[RedNeuronal]: activacion = %.16f\n", activacion[0]);
            calcularSigmas(salidaEsperada.get(i));
            calcularDeltas();
            activarSesgos(alfa);
        }
        activarPesos(alfa);
    }

    public void EntrenarYEncontrarEpoch(ArrayList<double[]> entradasPruebas, ArrayList<double[]> salidasPruebas, double alfa, double maxError)
    {
        // calcular la cantidad de epochs necesarios

        double error = 99999999;
        int epoch = 0;
        while (error > maxError)
        {
            RetroPropagar(entradasPruebas, salidasPruebas, alfa );
            error = ErrorTotal(entradasPruebas, salidasPruebas);
            System.out.printf("ephoc = %s, error = %.16f\n", epoch, error);
            epoch++ ;
        }
    }

    public double Entrenar(ArrayList<double[]> entradasPruebas, ArrayList<double[]> salidasPruebas, double alfa)
    {
        double err = 99999999;
        RetroPropagar(entradasPruebas, salidasPruebas, alfa);
        err = ErrorTotal(entradasPruebas, salidasPruebas);
        return err;
    }


}
