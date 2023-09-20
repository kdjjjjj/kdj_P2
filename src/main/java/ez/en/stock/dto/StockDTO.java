package ez.en.stock.dto;

import ez.en.order.dto.OrderDTO;
import ez.en.support.domain.Contract;
import ez.en.support.domain.Product;
import ez.en.support.dto.ContractDTO;
import ez.en.support.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockDTO {

    private int sno;
    private int scount;
    private String snote;
    private Product productDTO;
    private Contract contractDTO;
}
