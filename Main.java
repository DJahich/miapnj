import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        // Пример входных данных
        double a = 3.0;
        double b = 4.0;
        double c = 10.0;
        double d = 16.0;

        try {
            double result = calculateFormulaAsync(a, b, c, d).get();
            System.out.println("Final result of the formula: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static CompletableFuture<Double> calculateFormulaAsync(double a, double b, double c, double d) {
        // Асинхронные вычисления каждой части формулы
        CompletableFuture<Double> sumOfSquares = CompletableFuture.supplyAsync(() -> calculateSumOfSquares(a, b));
        CompletableFuture<Double> logC = CompletableFuture.supplyAsync(() -> calculateLog(c));
        CompletableFuture<Double> sqrtD = CompletableFuture.supplyAsync(() -> calculateSqrt(d));

        // Комбинирование результатов асинхронно
        return sumOfSquares
                .thenCombine(logC, (sum, log) -> sum * log)
                .thenCombine(sqrtD, (product, sqrt) -> product / sqrt);
    }

    private static double calculateSumOfSquares(double a, double b) {
        try {
            TimeUnit.SECONDS.sleep(5); // Задержка 5 секунд
            double result = Math.pow(a, 2) + Math.pow(b, 2);
            System.out.println("Calculating sum of squares: " + result);
            return result;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Calculation interrupted", e);
        }
    }

    private static double calculateLog(double c) {
        try {
            TimeUnit.SECONDS.sleep(15); // Задержка 15 секунд
            double result = Math.log(c);
            System.out.println("Calculating log(c): " + result);
            return result;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Calculation interrupted", e);
        }
    }

    private static double calculateSqrt(double d) {
        try {
            TimeUnit.SECONDS.sleep(10); // Задержка 10 секунд
            double result = Math.sqrt(d);
            System.out.println("Calculating sqrt(d): " + result);
            return result;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Calculation interrupted", e);
        }
    }
}
