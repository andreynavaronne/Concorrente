import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.*;

public class ContadorPalavras3 {
    private static long totalPalavras = 0;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Uso: java ContadorPalavras3 <arquivo1> <arquivo2> ...");
            return;
        }

        // Testar diferentes ExecutorServices
        testarExecutor(args, Executors.newSingleThreadExecutor(), "SingleThreadExecutor");
        testarExecutor(args, Executors.newCachedThreadPool(), "CachedThreadPool");
        testarExecutor(args, Executors.newFixedThreadPool(10), "FixedThreadPool(10)");
    }

    private static void testarExecutor(String[] arquivos, ExecutorService executor, String tipoExecutor) {
        totalPalavras = 0;
        System.out.println("\nTestando com " + tipoExecutor);
        long inicio = System.currentTimeMillis();

        for (String arquivo : arquivos) {
            executor.execute(() -> {
                try {
                    long palavras = contarPalavras(arquivo);
                    synchronized (ContadorPalavras3.class) {
                        totalPalavras += palavras;
                    }
                    System.out.println(arquivo + ": " + palavras + " palavras");
                } catch (IOException e) {
                    System.err.println("Erro ao ler o arquivo " + arquivo + ": " + e.getMessage());
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            System.err.println("Executor interrompido: " + e.getMessage());
        }

        long fim = System.currentTimeMillis();
        System.out.println("Total: " + totalPalavras + " palavras");
        System.out.println("Tempo total (" + tipoExecutor + "): " + (fim - inicio) + " ms");
    }

    private static long contarPalavras(String arquivo) throws IOException {
        String conteudo = new String(Files.readAllBytes(Paths.get(arquivo)));
        String[] palavras = conteudo.split("\\s+");
        return palavras.length;
    }
}
