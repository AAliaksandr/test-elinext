package com.elinext.di;

import static org.assertj.core.api.Assertions.*;

import com.elinext.di.exception.BindingNotFoundException;
import com.elinext.di.exception.ConstructorNotFoundException;
import com.elinext.di.exception.CyclicDependencyException;
import com.elinext.di.exception.NoInjectConstructorException;
import com.elinext.di.exception.TooManyConstructorsException;
import com.elinext.di.testdata.AbstractInMemoryEventDAOImpl;
import com.elinext.di.testdata.EventDAO;
import com.elinext.di.testdata.EventService;
import com.elinext.di.testdata.EventServiceCyclicException;
import com.elinext.di.testdata.EventServiceImpl;
import com.elinext.di.testdata.EventServiceNoInjectPublicConstructor;
import com.elinext.di.testdata.EventServiceNoPublicConstructor;
import com.elinext.di.testdata.EventServiceToManyConstructors;
import com.elinext.di.testdata.InMemoryEventDAOImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InjectorImplTest {

    @Test
    void test_getProvider() {
        Injector injector = new InjectorImpl();
        injector.bind(EventDAO.class, InMemoryEventDAOImpl.class);
        injector.bind(EventService.class, EventServiceImpl.class);
        Provider<EventService> daoProvider = injector.getProvider(EventService.class);
        assertNotNull(daoProvider);
        assertNotNull(daoProvider.getInstance());
        assertSame(EventServiceImpl.class, daoProvider.getInstance().getClass());
    }

    @Test
    void test_getProvider_noBindingFoundException() {
        Injector injector = new InjectorImpl();
        BindingNotFoundException bindingNotFoundException = assertThrows(BindingNotFoundException.class, () -> {
            Provider<EventService> daoProvider = injector.getProvider(EventService.class);
         daoProvider.getInstance();
        });
        assertThat(bindingNotFoundException).hasStackTraceContaining("No binding found");
    }

    @Test
    void test_getProvider_cyclicDependencyException() {
        Injector injector = new InjectorImpl();
        CyclicDependencyException cyclicDependencyException = assertThrows(CyclicDependencyException.class, () -> {
            injector.bind(EventDAO.class, InMemoryEventDAOImpl.class);
            injector.bind(EventService.class, EventServiceCyclicException.class);
            Provider<EventService> daoProvider = injector.getProvider(EventService.class);
            daoProvider.getInstance();
        });
        assertThat(cyclicDependencyException).hasStackTraceContaining("Cyclic dependency for the class " +
                "com.elinext.di.testdata.EventServiceCyclicException is detected");
    }

    @Test
    void test_getProvider_noPublicConstructor() {
        Injector injector = new InjectorImpl();
        ConstructorNotFoundException constructorNotFoundException = assertThrows(ConstructorNotFoundException.class, () -> {
            injector.bind(EventDAO.class, InMemoryEventDAOImpl.class);
            injector.bind(EventService.class, EventServiceNoPublicConstructor.class);
            Provider<EventService> daoProvider = injector.getProvider(EventService.class);
            daoProvider.getInstance();
        });
        assertThat(constructorNotFoundException).hasStackTraceContaining("The class has no public constructor");
    }

    @Test
    void test_getProvider_noInjectPublicConstructor() {
        Injector injector = new InjectorImpl();
        NoInjectConstructorException noInjectConstructorException = assertThrows(NoInjectConstructorException.class, () -> {
            injector.bind(EventDAO.class, InMemoryEventDAOImpl.class);
            injector.bind(EventService.class, EventServiceNoInjectPublicConstructor.class);
            Provider<EventService> daoProvider = injector.getProvider(EventService.class);
            daoProvider.getInstance();
        });
        assertThat(noInjectConstructorException).hasStackTraceContaining("There is more than one public constructor without inject annotation");
    }

    @Test
    void test_getProvider_tooManyConstructorException() {
        Injector injector = new InjectorImpl();
        TooManyConstructorsException tooManyConstructorsException = assertThrows(TooManyConstructorsException.class, () -> {
            injector.bind(EventDAO.class, InMemoryEventDAOImpl.class);
            injector.bind(EventService.class, EventServiceToManyConstructors.class);
            Provider<EventService> daoProvider = injector.getProvider(EventService.class);
            daoProvider.getInstance();
        });
        assertThat(tooManyConstructorsException).hasStackTraceContaining("More than one constructor");
    }


    @Test
    void test_bind() {
        Injector injector = new InjectorImpl();
        injector.bind(EventDAO.class, InMemoryEventDAOImpl.class);
        Provider<EventDAO> daoProvider = injector.getProvider(EventDAO.class);
        assertNotNull(daoProvider);
        assertNotNull(daoProvider.getInstance());
        assertSame(InMemoryEventDAOImpl.class, daoProvider.getInstance().getClass());
    }

    @Test
    void test_bind_noTypesAreProvided() {
        Injector injector = new InjectorImpl();
       IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
           injector.bind(null, InMemoryEventDAOImpl.class);
        });
        assertThat(illegalArgumentException).hasStackTraceContaining("No types are provided");
    }

    @Test
    void test_bind_secondArgumentInterface() {
        Injector injector = new InjectorImpl();
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            injector.bind(EventDAO.class, EventDAO.class);
        });
        assertThat(illegalArgumentException).hasStackTraceContaining("The second argument type");
    }

    @Test
    void test_bind_SecondArgumentAbstract() {
        Injector injector = new InjectorImpl();
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            injector.bind(EventDAO.class, AbstractInMemoryEventDAOImpl.class);
        });
        assertThat(illegalArgumentException).hasStackTraceContaining("The second argument type");
    }

    @Test
    void test_bind_firstNotInterface() {
        Injector injector = new InjectorImpl();
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            injector.bind(InMemoryEventDAOImpl.class, InMemoryEventDAOImpl.class);
        });
        assertThat(illegalArgumentException).hasStackTraceContaining("The first argument type is not an interface");
    }

    @Test
    void test_bindSingleton() {
        Injector injector = new InjectorImpl();
        injector.bindSingleton(EventDAO.class, InMemoryEventDAOImpl.class);
        Provider<EventDAO> daoProvider = injector.getProvider(EventDAO.class);
        assertNotNull(daoProvider);
        assertNotNull(daoProvider.getInstance());
        assertSame(InMemoryEventDAOImpl.class, daoProvider.getInstance().getClass());
    }

    @Test
    void test_bindSingleton_returnTheSameObject() {
        Injector injector = new InjectorImpl();
        injector.bindSingleton(EventDAO.class, InMemoryEventDAOImpl.class);
        Provider<EventDAO> daoProvider1 = injector.getProvider(EventDAO.class);
        Provider<EventDAO> daoProvider2 = injector.getProvider(EventDAO.class);
        assertSame(daoProvider1.getInstance(), daoProvider2.getInstance());
    }

    @Test
    void test_BindSingleton_noTypesAreProvided() {
        Injector injector = new InjectorImpl();
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            injector.bindSingleton(null, InMemoryEventDAOImpl.class);
        });
        assertThat(illegalArgumentException).hasStackTraceContaining("No types are provided");
    }

    @Test
    void test_bindSingleton_typeIsNotInterface() {
        Injector injector = new InjectorImpl();
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            injector.bindSingleton(EventDAO.class, EventDAO.class);
        });
        assertThat(illegalArgumentException).hasStackTraceContaining("The given type is an interface. Expecting the param to be an actual class");
    }
}