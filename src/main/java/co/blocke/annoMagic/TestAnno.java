package co.blocke.annoMagic;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TestAnno {
}
