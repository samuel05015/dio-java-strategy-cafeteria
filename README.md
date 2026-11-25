# Caixa de cafeteria com Java e Strategy

Exemplo para o desafio **Design Patterns com Java** do bootcamp Itaú – Java com Inteligência Artificial, da DIO. Implementação desenvolvida com apoio de IA.

O caixa calcula o subtotal e recebe uma regra de desconto por meio da interface `PricingStrategy`. O mesmo pedido pode usar preço normal, desconto de fidelidade de 10% ou desconto de grupo de 15% a partir de R$ 50. Para adicionar uma nova regra, basta implementar a interface e passá-la ao caixa. Os valores monetários usam `BigDecimal`, com arredondamento de centavos.

## Executar

Use Java 17 ou superior. Na raiz do repositório, execute:

```sh
java src/Cafeteria.java
```

No exemplo, o subtotal é R$ 56. Os totais são R$ 56,00 no preço normal, R$ 50,40 com fidelidade e R$ 47,60 no pedido em grupo.
