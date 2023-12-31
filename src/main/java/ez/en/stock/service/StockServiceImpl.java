package ez.en.stock.service;

import com.querydsl.core.types.Order;
import ez.en.config.PageRequestDTO;
import ez.en.config.PageResponseDTO;
import ez.en.login.domain.Login;
import ez.en.order.domain.Orders;
import ez.en.order.dto.OrderDTO;
import ez.en.order.dto.PopContractDTO;
import ez.en.order.repository.OrderRepository;
import ez.en.stock.domain.Stock;
import ez.en.stock.domain.Stockin;
import ez.en.stock.domain.Stockout;
import ez.en.stock.dto.StockDTO;
import ez.en.stock.dto.StockInDTO;
import ez.en.stock.dto.StockOutDTO;
import ez.en.stock.repository.StockRepository;
import ez.en.stock.repository.StockinRepository;
import ez.en.stock.repository.StockoutRepository;
import ez.en.support.domain.Contract;
import ez.en.support.domain.Product;
import ez.en.support.domain.Supportplan;
import ez.en.support.dto.ProductDTO;
import ez.en.support.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class StockServiceImpl implements StockService{

    private final ModelMapper modelMapper;
    private final StockRepository stockRepository;
    private final StockinRepository stockinRepository;
    private final StockoutRepository stockoutRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public PageResponseDTO<StockDTO> sList(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("sno");

        Page<Stock> result = stockRepository.searchStock(types, keyword, pageable);

        List<StockDTO> sList = result.getContent().stream()
                .map(i -> modelMapper.map(i,StockDTO.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<StockDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(sList)
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<StockInDTO> inList(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("sino");

        Page<Stockin> result = stockinRepository.searchIn(types, keyword, pageable);

        List<StockInDTO> inList = result.getContent().stream()
                .map(i -> modelMapper.map(i,StockInDTO.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<StockInDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(inList)
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<StockOutDTO> outList(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("sono");

        Page<Stockout> result = stockoutRepository.searchOut(types, keyword, pageable);

        List<StockOutDTO> outList = result.getContent().stream()
                .map(i -> modelMapper.map(i,StockOutDTO.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<StockOutDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(outList)
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<OrderDTO> oList(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("ono");

        Page<Orders> result = orderRepository.searchOrder(types, keyword, pageable);

        List<OrderDTO> oList = result.getContent().stream()
                .map(i -> modelMapper.map(i,OrderDTO.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<OrderDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(oList)
                .total((int)result.getTotalElements())
                .build();
    }

//    @Override
//    public List<OrderDTO> getOrder() {
//        List<Orders> result = orderRepository.getOrder();
//        List<OrderDTO> oList = result.stream()
//                .map(i -> modelMapper.map(i,OrderDTO.class))
//                .collect(Collectors.toList());
//
//        return oList;
//    }
    @Override // 입고목록
    public List<StockInDTO> getIn() {
        List<Stockin> result = stockinRepository.getIn();
        List<StockInDTO> inList = result.stream()
                .map(i -> modelMapper.map(i,StockInDTO.class))
                .collect(Collectors.toList());

        return inList;
    }
    @Override // 출고목록
    public List<StockOutDTO> getOut() {
        List<Stockout> result = stockoutRepository.getOut();
        List<StockOutDTO> outList = result.stream()
                .map(i -> modelMapper.map(i,StockOutDTO.class))
                .collect(Collectors.toList());

        return outList;
    }
    @Override // 자재디테일
    public List<StockDTO> stockDetail(int sno) {
        List<Stock> result = stockRepository.stockDetail(sno);
        List<StockDTO> detail = result.stream()
                .map(i -> modelMapper.map(i,StockDTO.class))
                .collect(Collectors.toList());

        return detail;
    }

    @Override // 발주디테일
    public List<OrderDTO> orderDetail(int ono) {
        List<Orders> result = orderRepository.orderDetail(ono);
        List<OrderDTO> detail = result.stream()
                .map(i -> modelMapper.map(i,OrderDTO.class))
                .collect(Collectors.toList());

        return detail;
    }
    @Override
    public void updateOstate(int ono) {
        orderRepository.updateOstate(ono);
    }
    @Override // 입고테이블 등록
    public void insertIn(int ono, String email, String sidate, int pno, int sicount) {
        Stockin stockin = Stockin.builder()
                .order(Orders.builder().ono(ono).build())
                .login(Login.builder().email(email).build())
                .product(Product.builder().pno(pno).build())
                .sidate(sidate)
                .sicount(sicount)
                .build();
        stockinRepository.save(stockin);

    }
    @Override // 출고테이블 등록
    public void insertOut(int sno, String email, String sodate,int socount) {
        Stockout stockout = Stockout.builder()
                .stock(Stock.builder().sno(sno).build())
                .login(Login.builder().email(email).build())
                .sodate(sodate)
                .socount(socount)
                .build();
        stockoutRepository.save(stockout);

    }
    @Override
    public List<Integer> getPno() {
        List<Product> result = productRepository.findAll();
        List<Integer> pnoList = result.stream()
                .map(i -> modelMapper.map(i.getPno(),Integer.class))
                .collect(Collectors.toList());

        return pnoList;
    }

    @Override
    public List<Integer> getSno() {
        List<Stock> result = stockRepository.getStock();
        List<Integer> snoList = result.stream()
                .map(i -> modelMapper.map(i.getSno(),Integer.class))
                .collect(Collectors.toList());

        return snoList;
    }

    @Override
    public int getSicountAll(int pno) {
        int all = stockinRepository.getSicountAll(pno);
        return all;
    }

    @Override
    public int getSocountAll(int sno) {
        int all = stockoutRepository.getSocountAll(sno);
        return all;
    }

    @Override
    public void sicountAll(int pno, int sicountAll) {

        stockRepository.sicountAll(pno,sicountAll);
    }
    @Override
    public void socountAll(int sno, int socountAll) {

        stockRepository.socountAll(sno,socountAll);
    }
    @Override
    public Integer checkStock(int pno){

        Integer sno = stockRepository.checkStock(pno);
        return sno;
    }
    @Override
    public void addStock(int pno, int cno){
        Stock stock = Stock.builder()
                .product(Product.builder().pno(pno).build())
                .contract(Contract.builder().cno(cno).build())
                .build();
        stockRepository.save(stock);
    }
}
