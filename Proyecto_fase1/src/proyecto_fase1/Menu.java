package proyecto_fase1;

import java.io.IOException;
import java.util.Scanner;

import org.json.simple.parser.ParseException;


public class Menu {
    Scanner scanner = new Scanner(System.in);
    Administrator admin = new Administrator();
    
    public Menu() throws IOException, ParseException{
        menu();
    }

    public void menu() throws IOException, ParseException{
        int option = 0;
        System.out.println("***************** MENU ********************");
        System.out.println("* 1. Parámetros Iniciales                 *");
        System.out.println("* 2. Ejecutar Paso                        *");
        System.out.println("* 3. Estado en Memoria de las Estructuras *");
        System.out.println("* 4. Reportes                             *");
        System.out.println("* 5. Acerca de...                         *");
        System.out.println("* 6. Salir                                *");
        System.out.println("*******************************************");
        option = Integer.parseInt(scanner.nextLine());

        switch (option) {
            case 1:
                String op_submenu = "";
                System.out.println("********* Parametros Iniciales *********");
                System.out.println("* a. Carga Masiva de Clientes          *");
                System.out.println("* b. Cantidad de Ventanillas           *");
                System.out.println("* c. Regresar                          *");
                System.out.println("****************************************");  
                op_submenu = scanner.nextLine();
                switch (op_submenu) {
                    case "a":
                        System.out.println("Ingrese la ruta del archivo: ");
                        String path = scanner.nextLine();
                        admin.read_json(path);
                        break;
                    case "b":
                        System.out.println("Ingrese la cantidad de ventanillas a crear: ");
                        int cant = Integer.parseInt(scanner.nextLine());
                        admin.create_ventanilla(cant);
                        break;
                    case "c":
                        break;
                    default:
                        break;
                }
                break;
            case 2:
                admin.realizar_paso();
                break;
            case 3:
                admin.estructuras_memoria();
                break;
            case 4:
                String op_submenu2 = "";
                System.out.println("**************************************** Reportes ****************************************");
                System.out.println("* a. Top 5 de Clientes con Mayor Cantidad de Imagenes a Color                            *");
                System.out.println("* b. Top 5 de Clientes con Menor Cantidad de Imagenes en Blanco y Negro                  *");
                System.out.println("* c. Informacion del Cliente que mas Pasos estuvo en el Sistema                          *");
                System.out.println("* d. Datos Cliente Especifico                                                            *");
                System.out.println("* e. Regresar                                                                            *");
                System.out.println("******************************************************************************************");
                op_submenu2 = scanner.nextLine();
                switch (op_submenu2) {
                    case "a":
                        admin.top5_mas_color();
                        break;
                    case "b":
                        admin.top5_menos_bn();
                        break;
                    case "c":
                        admin.cliente_mas_pasos();
                        break;
                    case "d":
                        int op_submenu3 = 0;
                        System.out.println("***************** Busqueda *****************");
                        System.out.println("* 1. Buscar por ID                         *");
                        System.out.println("* 2. Regresar                              *");
                        System.out.println("********************************************");
                        op_submenu3 = Integer.parseInt(scanner.nextLine());
                        switch (op_submenu3) {
                            case 1:
                                System.out.println("Ingrese el ID del cliente a buscar: ");
                                int id = Integer.parseInt(scanner.nextLine());
                                admin.datos_cliente(id);
                                break;
                            case 2:
                                break;                        
                            default:
                                break;
                        }
                        break;
                    case "e":
                        break;
                    default:
                        break;
                }
                break;
            case 5:
                System.out.println("--------------------------- ACERCA DE ---------------------------");
                System.out.println("* Nombre:    Dennis Mauricio Corado Muñóz                       *");
                System.out.println("* Carnet:    202010406                                          *");
                System.out.println("* CUI:       3032329780108                                      *");
                System.out.println("* Curso:     Laboratorio de Estructura de Datos                 *");
                System.out.println("* Carrera:   Ingenieria en Ciencias y Sistemas                  *");
                System.out.println("* Semestro:  1er Semestre 2022                                  *");
                System.out.println("-----------------------------------------------------------------");
                break;
            case 6:
                System.exit(0);
                break;
            default:    
                menu();
                break;
        }
        menu();
    }
}