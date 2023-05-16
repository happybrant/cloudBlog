package com.kongfu.frontend;

import com.kongfu.frontend.entity.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class FrontendApplicationTests {

  @Test
  void contextLoads() {
    String test = "/心得体会/C#精通";
    System.out.println(test.split("/")[3]);
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
