package ind.xwm.gui.service;

import ind.xwm.gui.AppTest;
import ind.xwm.gui.model.Product;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by XuWeiman on 2017/10/3.
 * Product service 单元测试
 */
public class ProductServiceTest extends AppTest {
    @Resource
    private ProductService productService;
    @Test
    public void findAll() throws Exception {
    }

    @Test
    public void findAllVector() throws Exception {
    }

    @Test
    public void save() throws Exception {
        Product product = new Product();
        product.setName("西装");
        product.setUnit("套");
        product.setPrice("30");
        productService.save(product);

        product = new Product();
        product.setName("西服");
        product.setUnit("件");
        product.setPrice("20");
        productService.save(product);

        product = new Product();
        product.setName("西裤");
        product.setUnit("条");
        product.setPrice("20");
        productService.save(product);
    }

}