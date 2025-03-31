import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    static class Transaction {
        final int fromId;
        final int toId;
        final int amount;
        
        Transaction(int fromId, int toId, int amount) {
            this.fromId = fromId;
            this.toId = toId;
            this.amount = amount;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Чтение количества пользователей
        int n = scanner.nextInt();
        int[] balances = new int[n];
        
        // Чтение начальных балансов
        for (int i = 0; i < n; i++) {
            balances[i] = scanner.nextInt();
        }
        
        // Чтение количества транзакций
        int m = scanner.nextInt();
        Transaction[] transactions = new Transaction[m];
        
        // Чтение транзакций
        for (int i = 0; i < m; i++) {
            String[] parts = scanner.next().split("-");
            int fromId = Integer.parseInt(parts[0].trim());
            int amount = Integer.parseInt(parts[1].trim());
            int toId = Integer.parseInt(parts[2].trim());
            transactions[i] = new Transaction(fromId, toId, amount);
        }
        
        // Создание пула потоков
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        
        // Обработка транзакций
        for (Transaction transaction : transactions) {
            executor.submit(() -> {
                synchronized (balances) {
                    // Проверка достаточности средств
                    if (balances[transaction.fromId] >= transaction.amount) {
                        balances[transaction.fromId] -= transaction.amount;
                        balances[transaction.toId] += transaction.amount;
                    }
                }
            });
        }
        
        // Завершение работы пула потоков
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Вывод итоговых балансов
        for (int i = 0; i < n; i++) {
            System.out.println("User " + i + " final balance: " + balances[i]);
        }
    }
}
