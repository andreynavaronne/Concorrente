import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ContadorPalavras {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Uso: java ContadorPalavras <arquivo1> <arquivo2> ...");
            return;
        }

        long totalPalavras = 0;
        long inicio = System.currentTimeMillis();

        for (String arquivo : args) {
            try {
                long palavras = contarPalavras(arquivo);
                System.out.println(arquivo + ": " + palavras + " palavras");
                totalPalavras += palavras;
            } catch (IOException e) {
                System.err.println("Erro ao ler o arquivo " + arquivo + ": " + e.getMessage());
            }
        }

        long fim = System.currentTimeMillis();
        System.out.println("Total: " + totalPalavras + " palavras");
        System.out.println("Tempo total (serial): " + (fim - inicio) + " ms");
    }

    private static long contarPalavras(String arquivo) throws IOException {
        String conteudo = new String(Files.readAllBytes(Paths.get(arquivo)));
        String[] palavras = conteudo.split("\\s+");
        return palavras.length;
    }
}