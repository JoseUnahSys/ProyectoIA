
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.io.File;  // Import the File class
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;



import java.util.Random;

public class Main {

    public static void main(String[] args) throws Exception
    {
        System.out.println("Cargando Imagen y convirtiendo a valores flotantes (double).\n");

        File file = new File("./persona_gris_160x120.jpg");
        // File file = new File("./cachorro_gris_160x120.jpg");

        BufferedImage img = ImageIO.read(file);
        int width = img.getWidth();
        int height = img.getHeight();
        System.out.printf("imagen con ancho %s, y alto %s. Cargada.\n", width, height);
        int[][] imgArr = new int[width][height];
        Raster raster = img.getData();

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                imgArr[i][j] = raster.getSample(i, j, 0);
            }
        }

        double[] img_data = new double[width * height];

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                img_data[(i * height) + j] = ((double)(imgArr[i][j])) / 255.0;
                // System.out.printf("%s ", img_data[(i * width) + j]);
            }
            // System.out.println("\n ");
        }

        // Para el Entrenamiento
        ArrayList<double[]> entradas = new ArrayList<double[]>();
        ArrayList<double[]> salidas = new ArrayList<double[]>();

        entradas.add(img_data);
        salidas.add(img_data);

        RedNeuronal nn = new RedNeuronal(new int[]{ width * height, 100, 50, 20, 1 });
        double[] prediccionInicial = nn.Activacion(img_data); // Primer prediccion

        System.out.printf("--Resultado inicial de la Red Neuronal %.8f\n", prediccionInicial[0]);

        // Para hacer la prueba final
        ArrayList<double[]> X = new ArrayList<double[]>();
        ArrayList<double[]> Y = new ArrayList<double[]>();
        X.add(img_data);
        Y.add(img_data);
        System.out.println("----- Comenzando Entrenamiento... ----- \n");
        nn.EntrenarYEncontrarEpoch(entradas, salidas, 0.1, 0.000001);

        // int epoch = 10;
        // for (int e = 0; e < epoch; e++)
        // {
        //     double error = nn.Entrenar(entradas, salidas, 0.5);
        //     System.out.printf("ephoc = %s, error calculado = %.16f, error inverso = %.16f\n", e, error, 1.0 - error);
        // }
        System.out.println("----- Entrenamiento terminado ----- \n");

        System.out.println("----- Haciendo una prueba con la imagen ----- \n");
        double err = nn.ErrorTotal(X, Y);
        double[] res = nn.Activacion(img_data); // Haciendo una ultima prediccion
        System.out.printf("resultado de la Red Neuronal %.8f\n", res[0]);

        // segun https://en.wikipedia.org/wiki/Sigmoid_function
        if(res[0] > 0.0) {
            System.out.printf("La Imagen SI es correcta.\n");
        } else {
            System.out.printf("La Imagen NO es correcta.\n");
        }
    }

}
