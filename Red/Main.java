
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.io.File;  // Import the File class
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.Random;


class Image {
    int width, height;
    double[] data;

    Image(int width, int height, double[] data) {
        this.width = width;
        this.height = height;
        this.data = data;
    }
}

public class Main {

    public static Image CargarImagen(String nombre) throws Exception
    {
        File file = new File(nombre);

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
            }
        }

        return new Image(width, height, img_data);
    }

    public static void main(String[] args) throws Exception
    {
        System.out.println("Cargando Imagenes y convirtiendo a valores flotantes (double).\n");

        Image imagen1 = CargarImagen("./persona_gris_160x120.jpg");
        Image imagen2 = CargarImagen("./ave_gris_160x120.jpeg");
        Image imagen3 = CargarImagen("./cachorro_gris_160x120.jpg");

        // Para el Entrenamiento
        ArrayList<double[]> entradas = new ArrayList<double[]>();
        ArrayList<double[]> salidas = new ArrayList<double[]>();
        entradas.add(imagen1.data);
        salidas.add(new double[] { 1.0 });
        entradas.add(imagen1.data);
        salidas.add(new double[] { 1.0 });
        entradas.add(imagen1.data);
        salidas.add(new double[] { 1.0 });
        entradas.add(imagen1.data);
        salidas.add(new double[] { 1.0 });

        entradas.add(imagen2.data);
        salidas.add(new double[] { 0.0 });

        entradas.add(imagen3.data);
        salidas.add(new double[] { 0.0 });

        RedNeuronal nn = new RedNeuronal(new int[]
        {
            imagen1.data.length,
            100,
            50,
            1
        });

        // Predicciones iniciales
        double[] prediccionInicial = nn.Activacion(imagen1.data);
        System.out.printf("-- Resultado inicial con imagen1 de la Red Neuronal %.16f\n", prediccionInicial[0]);

        prediccionInicial = nn.Activacion(imagen2.data);
        System.out.printf("-- Resultado inicial con imagen2 de la Red Neuronal %.16f\n", prediccionInicial[0]);

        prediccionInicial = nn.Activacion(imagen3.data);
        System.out.printf("-- Resultado inicial con imagen3 de la Red Neuronal %.16f\n", prediccionInicial[0]);

        System.out.printf("----- Comenzando Entrenamiento... ----- \n");

        nn.EntrenarYEncontrarEpoch(entradas, salidas, 0.3, 0.01);

        // int epochs = 10;
        // for (int e = 0; e < epochs; e++)
        // {
        //     double error = nn.Entrenar(entradas, salidas, 0.4); // Ritmo de aprendizaje
        //     System.out.printf("ephoc = %s, error calculado = %.16f\n", e, error);
        // }

        System.out.println("----- Entrenamiento terminado ----- \n");

        System.out.println("----- Haciendo una prueba con las imagenes ----- \n");

        double[] res = nn.Activacion(imagen1.data); // Haciendo una ultima prediccion
        System.out.printf("Activacion de la Red Neuronal con imagen1 (correcta) %.16f.\n", res[0]);

        res = nn.Activacion(imagen2.data); // Haciendo una prediccion con imagen incorrecta
        System.out.printf("Activacion de la Red Neuronal con imagen2 (incorrecta), %.16f.\n", res[0]);

        res = nn.Activacion(imagen3.data); // Haciendo una prediccion con imagen incorrecta
        System.out.printf("Activacion de la Red Neuronal con imagen3 (incorrecta), %.16f.\n", res[0]);
    }

}
