import java.util.*;

public class AlgebraRecur {

    public static int parseInt(char input) {
        return input - 48;
    }

    public static boolean parseBoolean(char input) {
        return input != '0';
    }

    public static String substring(String input, int start, int end) {
        return substringRecursivo(input, start, end, "");
    }
    
    private static String substringRecursivo(String input, int start, int end, String resultado) {
        if (start >= end) {
            return resultado;
        }
        return substringRecursivo(input, start + 1, end, resultado + input.charAt(start));
    }

    public static String replace(String input, int start, int end, String subReplace) {
        String newStr1 = substring(input, 0, start);
        String newStr2 = substring(input, end, input.length());
        return newStr1 + subReplace + newStr2;
    }

    public static char[] getArgs(String input, int index) {
        int contador = contarArgs(input, index + 1, 0);
        char[] args = new char[contador];
        preencherArgs(input, index + 1, args, 0, 0);
        return args;
    }
    
    private static int contarArgs(String input, int i, int contador) {
        if (input.charAt(i) == ')') {
            return contador;
        }
        if (input.charAt(i) == '0' || input.charAt(i) == '1') {
            contador++;
        }
        return contarArgs(input, i + 1, contador);
    }
    
    private static void preencherArgs(String input, int i, char[] args, int j, int depth) {
        if (input.charAt(i) == ')' || j >= args.length) {
            return;
        }
        if (input.charAt(i) == '0' || input.charAt(i) == '1') {
            args[j] = input.charAt(i);
            preencherArgs(input, i + 1, args, j + 1, depth + 1);
        } else {
            preencherArgs(input, i + 1, args, j, depth + 1);
        }
    }

    public static char not(char[] args) {
        return (args[0] == '0') ? '1' : '0';
    }

    public static char and(char[] args) {
        return andRecursivo(args, 0);
    }
    
    private static char andRecursivo(char[] args, int i) {
        if (i >= args.length) {
            return '1';
        }
        if (args[i] == '0') {
            return '0';
        }
        return andRecursivo(args, i + 1);
    }

    public static char or(char[] args) {
        return orRecursivo(args, 0);
    }
    
    private static char orRecursivo(char[] args, int i) {
        if (i >= args.length) {
            return '0';
        }
        if (args[i] == '1') {
            return '1';
        }
        return orRecursivo(args, i + 1);
    }

    public static int encontrarFimParenteses(String input, int inicio) {
        if (input.charAt(inicio) == ')') {
            return inicio;
        }
        return encontrarFimParenteses(input, inicio + 1);
    }

    public static String equacionando(String input) throws Exception {
        return equacionandoRecursivo(input, input.length() - 1);
    }
    
    private static String equacionandoRecursivo(String input, int i) throws Exception {
        if (i < 0) {
            return substring(input, 0, 1);
        }
        
        if (input.charAt(i) == '(') {
            char operador = i > 0 ? input.charAt(i - 1) : ' ';
            char[] args;
            char result;
            int end;
            
            switch (operador) {
                case 'd':  // and
                    args = getArgs(input, i);
                    result = and(args);
                    end = encontrarFimParenteses(input, i);
                    input = replace(input, i - 3, end + 1, result + "");
                    return equacionandoRecursivo(input, i - 3);
                    
                case 't':  // not
                    args = getArgs(input, i);
                    result = not(args);
                    input = replace(input, i - 3, i + 3, result + "");
                    return equacionandoRecursivo(input, i - 3);
                    
                case 'r':  // or
                    args = getArgs(input, i);
                    result = or(args);
                    end = encontrarFimParenteses(input, i);
                    input = replace(input, i - 2, end + 1, result + "");
                    return equacionandoRecursivo(input, i - 2);
                    
                default:
                    throw new Exception("Erro");
            }
        }
        
        return equacionandoRecursivo(input, i - 1);
    }
    
    public static String substituirVariaveis(String input, boolean[] array, int i) {
        if (i >= input.length()) {
            return input;
        }
        
        if (input.charAt(i) == 'A') {
            input = replace(input, i, i + 1, array[0] ? "1" : "0");
        } else if (input.charAt(i) == 'B') {
            input = replace(input, i, i + 1, array[1] ? "1" : "0");
        } else if (input.charAt(i) == 'C' && array.length > 2) {
            input = replace(input, i, i + 1, array[2] ? "1" : "0");
        }
        
        return substituirVariaveis(input, array, i + 1);
    }

    public static void main(String[] args) throws Exception {
        Scanner scanf = new Scanner(System.in);
        String input = scanf.nextLine();

        while (input.charAt(0) != '0') {
            int cont = parseInt(input.charAt(0));
            boolean[] array = new boolean[cont];

            if (cont == 2) {
                array[0] = parseBoolean(input.charAt(2));
                array[1] = parseBoolean(input.charAt(4));
                input = substring(input, 6, input.length());
            } else if (cont == 3) {
                array[0] = parseBoolean(input.charAt(2));
                array[1] = parseBoolean(input.charAt(4));
                array[2] = parseBoolean(input.charAt(6));
                input = substring(input, 8, input.length());
            }

            input = substituirVariaveis(input, array, 0);
            input = equacionando(input);
            System.out.println(input);

            input = scanf.nextLine();
        }

        scanf.close();
    }
}