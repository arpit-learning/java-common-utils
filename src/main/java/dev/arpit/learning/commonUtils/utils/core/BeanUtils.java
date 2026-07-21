package dev.arpit.learning.commonUtils.utils.core;

import lombok.NonNull;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class BeanUtils implements ApplicationContextAware {
  private static ApplicationContext context;

  @Override
  public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
    setContext(applicationContext);
  }

  private static void setContext(ApplicationContext applicationContext) {
    context = applicationContext;
  }

  public static <T> @NonNull T getBean(@NonNull Class<T> beanClass) {
    return context.getBean(beanClass);
  }
}
