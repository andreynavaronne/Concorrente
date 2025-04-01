import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ContadorPalavras2 {
    private static long totalPalavras = 0;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Uso: java ContadorPalavras2 <arquivo1> <arquivo2> ...");
            return;
        }

        long inicio = System.currentTimeMillis();
        Thread[] threads = new Thread[args.length];

        for (int i = 0; i < args.length; i++) {
            String arquivo = args[i];
            threads[i] = new Thread(() -> {
                try {
                    long palavras = contarPalavras(arquivo);
                    synchronized (ContadorPalavras2.class) {
                        totalPalavras += palavras;
                    }
                    System.out.println(arquivo + ": " + palavras + " palavras");
                } catch (IOException e) {
                    System.err.println("Erro ao ler o arquivo " + arquivo + ": " + e.getMessage());
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.err.println("Thread interrompida: " + e.getMessage());
            }
        }

        long fim = System.currentTimeMillis();
        System.out.println("Total: " + totalPalavras + " palavras");
        System.out.println("Tempo total (concorrente): " + (fim - inicio) + " ms");
    }

    private static long contarPalavras(String arquivo) throws IOException {
        String conteudo = new String(Files.readAllBytes(Paths.get(arquivo)));
        String[] palavras = conteudo.split("\\s+");
        return palavras.length;
    }
}
