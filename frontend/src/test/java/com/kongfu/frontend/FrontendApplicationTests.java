package com.kongfu.frontend;

import com.kongfu.frontend.entity.Category;
import com.kongfu.frontend.entity.Tag;
import com.kongfu.frontend.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class FrontendApplicationTests {
  @Autowired private CategoryService categoryService;

  @Test
  void contextLoads() {
    String test = "/心得体会/C#精通";
    System.out.println(test.split("/")[2]);
  }

  @Test
  void testCount() {
    List<Category> categories = categoryService.findCategories("dd");
    int i = categoryService.findCategoryCount(categories);
    System.out.println(i);
  }

  @Test
  void deepCopyTest() {
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
