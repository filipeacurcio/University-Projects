package pt.isec.pa.apoio_poe.ui.text;

import java.util.Scanner;
/** Representa a classe de auxilio à introdução dos dados do utilizador.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */
public final class PAInput {
    /** Cria um classe de auxilio à introdução de dados.
     */
    private PAInput() {}

    private static Scanner sc;

    static {
        resetScanner();
    }
    /** Reset o scanner.
     */
    public static void resetScanner() {
        sc = new Scanner(System.in);
    }
    /** Recebe uma frase ou palavra do utilizador
     * @param title O titulo para ajuda com a interção com o utilizador
     * @param onlyOneWord Representa a necessidade de ler uma ou mais palavras
     * @return Retorna a String introduzido pelo utilizador.
     */
    public static String readString(String title,boolean onlyOneWord) {
        String value;
        do {
            if (title != null)
                System.out.print(title);
            else
                System.out.print("> ");
            value = sc.nextLine().trim();
        } while (value.isBlank());
        if (onlyOneWord) {
            Scanner auxsc = new Scanner(value);
            value = auxsc.next();
        }
        return value;
    }
    /** Recebe um valor inteiro do utilizador
     * @param title O titulo para ajuda com a interção com o utilizador
     * @return Retorna a inteiro introduzido pelo utilizador.
     */
    public static int readInt(String title) {
        while (true) {
            if (title != null)
                System.out.print(title);
            else
                System.out.print("> ");
            if (sc.hasNextInt()) {
                int intValue = sc.nextInt();
                sc.nextLine();
                return intValue;
            } else
                sc.nextLine();
        }
    }
    /** Recebe um valor double do utilizador
     * @param title O titulo para ajuda com a interção com o utilizador
     * @return Retorna a double introduzido pelo utilizador.
     */
    public static double readNumber(String title) {
        while (true) {
            if (title != null)
                System.out.print(title);
            else
                System.out.print("> ");
            if (sc.hasNextDouble()) {
                double doubleValue = sc.nextDouble();
                sc.nextLine();
                return doubleValue;
            } else
                sc.nextLine();
        }
    }
    /** Recebe um valor long do utilizador
     * @param title O titulo para ajuda com a interção com o utilizador
     * @return Retorna a long introduzido pelo utilizador.
     */
    public static long readLong(String title) {
        while (true) {
            if (title != null)
                System.out.print(title);
            else
                System.out.print("> ");
            if (sc.hasNextLong()) {
                long doubleValue = sc.nextLong();
                sc.nextLine();
                return doubleValue;
            } else
                sc.nextLine();
        }
    }
    /** Recebe um valor inteiro do utilizador
     * @param title O titulo para ajuda com a interção com o utilizador
     * @param options Opções para o utilizador escolher
     * @return Retorna a inteiro introduzido pelo utilizador.
     */
    public static int chooseOption(String title, String ... options) {
        int option = -1;
        do {
            if (title != null)
                System.out.println(System.lineSeparator()+title);
            System.out.println();
            for(int i = 0; i < options.length; i++) {
                System.out.printf("%3d - %s\n",i+1,options[i]);
            }
            System.out.print("\nOption: ");
            if (sc.hasNextInt())
                option = sc.nextInt();
            sc.nextLine();
        } while (option < 1 || option > options.length);
        return option;
    }

}