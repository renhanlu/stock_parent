package com.itheima.stock.mapper;

import com.itheima.stock.common.domain.StockBusinessDomain;
import com.itheima.stock.common.domain.StockSearchDomain;
import com.itheima.stock.pojo.StockBusiness;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Renhanlu
* @description 针对表【stock_business(主营业务表)】的数据库操作Mapper
* @createDate 2022-05-08 16:06:52
* @Entity com.itheima.stock.pojo.StockBusiness
*/
@Mapper
public interface StockBusinessMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockBusiness record);

    int insertSelective(StockBusiness record);

    StockBusiness selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockBusiness record);

    int updateByPrimaryKey(StockBusiness record);

    List<StockBusiness> getAll();

    /**
     * 查询个股主营业务
     * @param code 股票编码
     * @return
     */
    StockBusinessDomain getIndividualStocks(@Param("code") String code);



    /**
     * 获取所有股票的code
     * @return
     */
    List<String> getStockIds();

    /**
     * 模糊查询股票
     * @param searchStr 股票代码
     * @return
     */
    List<StockSearchDomain> selectStockByLike(@Param("searchStr") String searchStr);
}
