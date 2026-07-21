package dev.arpit.learning.commonUtils.utils.core;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

@ExtendWith(MockitoExtension.class)
class BeanUtilsTest {
  @Mock private ApplicationContext applicationContext;
  @InjectMocks private BeanUtils beanUtils;

  @Test
  public void test_setApplicationContext_withValidApplicationContext_shouldUpdateIt() {
    // act
    beanUtils.setApplicationContext(applicationContext);
  }

  @Test
  public void test_setApplicationContext_withNullApplicationContext_shouldThrowNPE() {
    // act & assert
    assertThrows(NullPointerException.class, () -> beanUtils.setApplicationContext(null));
  }

  @Test
  public void test_getBean_withValidBeanClass_shouldReturnBean() {
    // arrange
    beanUtils.setApplicationContext(applicationContext);
    String expectedBean = "mockedStringBean";
    when(applicationContext.getBean(String.class)).thenReturn(expectedBean);

    // act
    String actualBean = BeanUtils.getBean(String.class);

    // assert
    assertNotNull(actualBean);
    assertEquals(expectedBean, actualBean);
  }

  @Test
  public void test_getBean_withNullBeanClass_shouldThrowNPE() {
    // Arrange
    beanUtils.setApplicationContext(applicationContext);

    // act & assert
    assertThrows(NullPointerException.class, () -> BeanUtils.getBean(null));
  }
}
