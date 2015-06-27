package org.foo;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.SuperMethodCall;
import static net.bytebuddy.matcher.ElementMatchers.named;

import org.agenttest.ToString;

import net.bytebuddy.implementation.attribute.MethodAttributeAppender;
import net.bytebuddy.implementation.attribute.TypeAttributeAppender;

public class AnnotationsProxyByByteBuddy {

	public static void main(String[] args) {
		try {
			Class<?> runtimeType = new ByteBuddy().withAttribute(TypeAttributeAppender.ForSuperType.INSTANCE)
					.withDefaultMethodAttributeAppender(MethodAttributeAppender.ForInstrumentedMethod.INSTANCE)
					.subclass(Foob.class)
					.method(named("getString")).intercept(SuperMethodCall.INSTANCE)
					.method(named("toStringAgent")).intercept(SuperMethodCall.INSTANCE)
					.make()
					.load(
							ClassLoader.getSystemClassLoader(),
							//AnnotationsProxyByByteBuddy.class.getClass().getClassLoader(),
							ClassLoadingStrategy.Default.WRAPPER)
					.getLoaded();
			if (runtimeType.equals(Foob.class)) {
				System.out.println("class is not Foo.class");
			}

			if (runtimeType.isAnnotationPresent(NBFoo.class)) {
				System.out.println("is NBFoo Annotation");
			}

			if (runtimeType.getDeclaredMethod("getString").isAnnotationPresent(NBFoo.class)) {
				System.out.println("method is NBFoo Annotation");
			}
			
		if (runtimeType.getDeclaredMethod("toStringAgent").isAnnotationPresent(ToString.class)) {
				System.out.println("is ToString Annotation");
			}
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
