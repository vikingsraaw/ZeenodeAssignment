package ZeenodeJava;

import java.util.*;

class Product {
    private String name;
    private double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }
}

class ShoppingCart {
    private Map<String, Product> products;
    private Map<String, Integer> quantities;
    private Map<String, Boolean> giftWrapping;
    private Map<String, Double> discounts;

    public ShoppingCart(Map<String, Product> products) {
        this.products = products;
        this.quantities = new HashMap<>();
        this.giftWrapping = new HashMap<>();
        this.discounts = new HashMap<>();
    }

    public void inputQuantities() {
        Scanner scanner = new Scanner(System.in);
        for (Product product : products.values()) {
            System.out.print("Enter quantity for " + product.getName() + ": ");
            int quantity = scanner.nextInt();
            quantities.put(product.getName(), quantity);
        }
    }

    public void inputGiftWrapping() {
        Scanner scanner = new Scanner(System.in);
        for (Product product : products.values()) {
            System.out.print("Is " + product.getName() + " wrapped as a gift? (yes/no): ");
            String wrap = scanner.next().toLowerCase();
            giftWrapping.put(product.getName(), wrap.equals("yes"));
        }
    }

    public double calculateSubtotal() {
        return products.entrySet().stream()
                .mapToDouble(entry -> entry.getValue().getPrice() * quantities.get(entry.getKey()))
                .sum();
    }

    public void inputDiscount() {
        int totalQuantity = quantities.values().stream().mapToInt(Integer::intValue).sum();
        double cartTotal = products.entrySet().stream()
                .mapToDouble(entry -> entry.getValue().getPrice() * quantities.get(entry.getKey()))
                .sum();

        if (cartTotal > 200) {
            discounts.put("flat_10_discount", 10.0);
        }

        double maxFor10 = 0; 
        for (Product product : products.values()) {
            int quantity = quantities.get(product.getName());

            if (quantity > 10) {
                double discountAmount = product.getPrice() * quantity * 0.05;
                maxFor10 = Math.max(discountAmount, maxFor10);
            }
        }
        discounts.put("bulk_5_discount", maxFor10);

        if (totalQuantity > 20) {
            double discountAmount = cartTotal * 0.1;
            discounts.put("bulk_10_discount", discountAmount);
        }

        if (totalQuantity > 30) {
            double maxFor50 = 0; 
            for (Product product : products.values()) {
                int quantity = quantities.get(product.getName());
                if (quantity > 15) {
                    double discountAmount = product.getPrice() * (quantity - 15) * 0.5;
                    maxFor50 = Math.max(discountAmount, maxFor50);
                }
            }
            discounts.put("tiered_50_discount", maxFor50);
        }
    }

    public double calculateShippingFee() {
        int totalQuantity = quantities.values().stream().mapToInt(Integer::intValue).sum();
        int itemsPerPackage = 10;
        int packages = (int) Math.ceil((double) totalQuantity / itemsPerPackage);
        double shippingFeePerPackage = 5.0;
        return packages * shippingFeePerPackage;
    }

    public double calculateGiftWrapFee() {
         int totalQuantity=0;
        for (Map.Entry<String, Boolean> gift : giftWrapping.entrySet()) {
    if (gift.getValue()) {
        String giftKey = gift.getKey();
        if (quantities.containsKey(giftKey)) {
            totalQuantity += quantities.get(giftKey);
                 }
             }
         }
        double giftWrapFee = 1.0;
        return giftWrapFee * totalQuantity;
    }

    public double calculateDiscount() {
        this.inputDiscount();
        String discountRule = "";
        double maxDiscount = 0.0;

        discountRule = discounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("");
        maxDiscount = discounts.getOrDefault(discountRule, 0.0);
        return maxDiscount;
    }

    public double calculateTotal() {
        double subtotal = calculateSubtotal();
        double discount = calculateDiscount();
        double shippingFee = calculateShippingFee();
        double giftWrapFee = calculateGiftWrapFee();
        return subtotal - discount + shippingFee + giftWrapFee;
    }

    public void displayReceipt() {
        System.out.println("\nReceipt:");

        for (Map.Entry<String, Product> entry : products.entrySet()) {
            String productName = entry.getKey();
            int quantity = quantities.get(productName);
            double totalAmount = entry.getValue().getPrice() * quantity;
            System.out.printf("%s - Quantity: %d, Total: $%.2f%n", productName, quantity, totalAmount);
        }

        double subtotal = calculateSubtotal();
        System.out.println("\nSubtotal: $" + subtotal);

        double discount = calculateDiscount();
        if (discount > 0.0) {
            System.out.println("Discount Applied: -$" + discount);
        }

        double shippingFee = calculateShippingFee();
        System.out.println("Shipping Fee: $" + shippingFee);

        double giftWrapFee = calculateGiftWrapFee();
        System.out.println("Gift Wrap Fee: $" + giftWrapFee);

        double total = calculateTotal();
        System.out.println("\nTotal: $" + total);
    }
}

public class Main {
    public static void main(String[] args) {
        Map<String, Product> products = new HashMap<>();
        products.put("Product A", new Product("Product A", 20));
        products.put("Product B", new Product("Product B", 40));
        products.put("Product C", new Product("Product C", 50));

        ShoppingCart cart = new ShoppingCart(products);

        cart.inputQuantities();
        cart.inputGiftWrapping();
        cart.displayReceipt();
    }
}





