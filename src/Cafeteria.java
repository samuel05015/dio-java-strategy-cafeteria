import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

public final class Cafeteria {
    private static final BigDecimal DISCOUNT_MEMBER = new BigDecimal("0.10");
    private static final BigDecimal DISCOUNT_GROUP = new BigDecimal("0.15");
    private static final BigDecimal GROUP_THRESHOLD = new BigDecimal("50.00");

    interface PricingStrategy {
        BigDecimal discount(BigDecimal subtotal);
    }

    record Item(String name, BigDecimal price, int quantity) {
        Item {
            if (name == null || name.isBlank() || price == null || price.signum() < 0 || quantity < 1) {
                throw new IllegalArgumentException("Informe nome, preço não negativo e quantidade positiva.");
            }
        }

        BigDecimal total() {
            return price.multiply(BigDecimal.valueOf(quantity));
        }
    }

    record Receipt(BigDecimal subtotal, BigDecimal discount, BigDecimal total) {}

    static final class Checkout {
        private final PricingStrategy pricing;

        Checkout(PricingStrategy pricing) {
            this.pricing = Objects.requireNonNull(pricing);
        }

        Receipt close(List<Item> items) {
            if (items.isEmpty()) {
                throw new IllegalArgumentException("Adicione ao menos um item ao pedido.");
            }
            BigDecimal subtotal = currency(items.stream()
                    .map(Item::total)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            BigDecimal discount = currency(pricing.discount(subtotal));
            if (discount.signum() < 0 || discount.compareTo(subtotal) > 0) {
                throw new IllegalArgumentException("O desconto precisa estar entre zero e o subtotal.");
            }
            return new Receipt(subtotal, discount, subtotal.subtract(discount));
        }
    }

    static PricingStrategy normalPrice() {
        return subtotal -> BigDecimal.ZERO;
    }

    static PricingStrategy memberPrice() {
        return subtotal -> subtotal.multiply(DISCOUNT_MEMBER);
    }

    static PricingStrategy groupPrice() {
        return subtotal -> subtotal.compareTo(GROUP_THRESHOLD) >= 0
                ? subtotal.multiply(DISCOUNT_GROUP) : BigDecimal.ZERO;
    }

    private static BigDecimal currency(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    public static void main(String[] args) {
        List<Item> items = List.of(
                new Item("Café filtrado", new BigDecimal("8.50"), 2),
                new Item("Pão de queijo", new BigDecimal("6.50"), 6));
        printReceipt("Preço normal", new Checkout(normalPrice()).close(items));
        printReceipt("Clube de fidelidade", new Checkout(memberPrice()).close(items));
        printReceipt("Pedido em grupo", new Checkout(groupPrice()).close(items));
    }

    private static void printReceipt(String label, Receipt receipt) {
        System.out.printf("%s | subtotal R$ %s | desconto R$ %s | total R$ %s%n",
                label, receipt.subtotal(), receipt.discount(), receipt.total());
    }
}
