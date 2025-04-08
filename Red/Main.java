
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
                // img_data[(i * height) + j] = ((double)(imgArr[i][j])) ;
                // System.out.printf("%s ", img_data[(i * height) + j]);
            }
            // System.out.println("\n ");
        }

        return new Image(width, height, img_data);
    }

    public static void main(String[] args) throws Exception
    {
        System.out.println("Cargando Imagenes y convirtiendo a valores flotantes (double).\n");

        Image imagen1 = CargarImagen("./persona_gris_160x120.jpg");
        Image imagen2 = CargarImagen("./cachorro_gris_160x120.jpg");
        Image imagen3 = CargarImagen("./ave_gris_160x120.jpeg");

        // Para el Entrenamiento
        ArrayList<double[]> entradas = new ArrayList<double[]>();
        ArrayList<double[]> salidas = new ArrayList<double[]>();
        entradas.add(imagen1.data); entradas.add(imagen1.data);
        entradas.add(imagen1.data); entradas.add(imagen1.data);
        entradas.add(imagen1.data); entradas.add(imagen1.data);
        entradas.add(imagen1.data); entradas.add(imagen1.data);
        entradas.add(imagen1.data); entradas.add(imagen1.data);
        entradas.add(imagen1.data); entradas.add(imagen1.data);
        salidas.add(new double[] { 1.0 }); salidas.add(new double[] { 1.0 });
        salidas.add(new double[] { 1.0 }); salidas.add(new double[] { 1.0 });
        salidas.add(new double[] { 1.0 }); salidas.add(new double[] { 1.0 });
        salidas.add(new double[] { 1.0 }); salidas.add(new double[] { 1.0 });
        salidas.add(new double[] { 1.0 }); salidas.add(new double[] { 1.0 });
        salidas.add(new double[] { 1.0 }); salidas.add(new double[] { 1.0 });

        RedNeuronal nn = new RedNeuronal(new int[]
        {
            imagen1.width * imagen1.height,
            50,
            10,
            1
        });

        double[] prediccionInicial = nn.Activacion(imagen1.data); // Primer prediccion
        System.out.printf("-- Resultado inicial con imagen1 de la Red Neuronal %.16f\n", prediccionInicial[0]);
        prediccionInicial = nn.Activacion(imagen2.data); // Primer prediccion
        System.out.printf("-- Resultado inicial con imagen2 de la Red Neuronal %.16f\n", prediccionInicial[0]);

        System.out.println("----- Comenzando Entrenamiento... ----- \n");

        nn.EntrenarYEncontrarEpoch(entradas, salidas, 0.5, 0.05);

        // int epoch = 10;
        // for (int e = 0; e < epoch; e++)
        // {
        //     double error = nn.Entrenar(entradas, salidas, 0.5); // Ritmo de aprendizaje
        //     System.out.printf("ephoc = %s, error calculado = %.16f\n", e, error);
        // }

        System.out.println("----- Entrenamiento terminado ----- \n");

        System.out.println("----- Haciendo una prueba con la imagen ----- \n");
        // Para hacer la prueba final
        double[] res = nn.Activacion(imagen1.data); // Haciendo una ultima prediccion
        double err = nn.Error(res, new double[] { 1.0 });
        System.out.printf("Activacion de la Red Neuronal con imagen1 (correcta) %.16f, Error %.16f\n", res[0], err);

        double[] res2 = nn.Activacion(imagen2.data); // Haciendo una prediccion con imagen incorrecta
        err = nn.Error(res2, new double[] { 0.0 });
        System.out.printf("Activacion de la Red Neuronal con imagen2 (incorrecta), %.16f, Error %.16f\n", res2[0], err);
    }

}
