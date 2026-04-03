package com.kongfu.frontend;

import com.framework.backend.entity.Category;
import com.framework.backend.entity.Tag;
import com.framework.backend.service.CategoryService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FrontendApplicationTests {
  @Autowired private CategoryService categoryService;

  @Test
  public void contextLoads() {
    String test = "/心得体会/C#精通";
    System.out.println(test.split("/")[2]);
  }

  @Test
  public void testCount() {
    List<Category> categories = categoryService.findCategories("dd");
    int i = categoryService.findCategoryCount(categories);
    System.out.println(i);
  }

  @Test
  public void deepCopyTest() {
    List<Tag> list1 = new ArrayList<>();
    List<Tag> list2 = new ArrayList<>();
    list1.add(new Tag("张三", 12));
    list1.add(new Tag("李四", 32));
    list2 = (List<Tag>) ((ArrayList<Tag>) list1).clone();
    list2.remove(0);
    System.out.println(list1);
    System.out.println(list2);
  }
}
