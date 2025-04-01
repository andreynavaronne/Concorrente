import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.*;

public class ContadorPalavras4 {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Uso: java ContadorPalavras4 <arquivo1> <arquivo2> ...");
            return;
        }

        ExecutorService executor = Executors.newFixedThreadPool(10);
        long inicio = System.currentTimeMillis();

        try {
            long totalPalavras = 0;
            Future<Long>[] futures = new Future[args.length];

            for (int i = 0; i < args.length; i++) {
                String arquivo = args[i];
                futures[i] = executor.submit(() -> contarPalavras(arquivo));
            }

            for (int i = 0; i < args.length; i++) {
                try {
                    long palavras = futures[i].get();
                    System.out.println(args[i] + ": " + palavras + " palavras");
                    totalPalavras += palavras;
                } catch (InterruptedException | ExecutionException e) {
                    System.err.println("Erro ao processar arquivo " + args[i] + ": " + e.getMessage());
                }
            }

            System.out.println("Total: " + totalPalavras + " palavras");
        } finally {
            executor.shutdown();
        }

        long fim = System.currentTimeMillis();
        System.out.println("Tempo total (com Future): " + (fim - inicio) + " ms");
    }

    private static long contarPalavras(String arquivo) throws IOException {
        String conteudo = new String(Files.readAllBytes(Paths.get(arquivo)));
        String[] palavras = conteudo.split("\\s+");
        return palavras.length;
    }
}
