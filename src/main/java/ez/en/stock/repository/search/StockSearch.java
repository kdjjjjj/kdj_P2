package ez.en.stock.repository.search;

import ez.en.stock.domain.Stock;
import ez.en.stock.domain.Stockin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StockSearch {

    Page<Stock> searchStock(String[] types, String keyword,Pageable pageable);

}
