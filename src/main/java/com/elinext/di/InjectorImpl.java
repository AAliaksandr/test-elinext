package com.elinext.di;


import com.elinext.di.exception.BindingNotFoundException;
import com.elinext.di.exception.ConstructorNotFoundException;
import com.elinext.di.exception.CyclicDependencyException;
import com.elinext.di.exception.NoInjectConstructorException;
import com.elinext.di.exception.TooManyConstructorsException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class InjectorImpl implements Injector {

    private Map<Class, Binding> bindings = new HashMap<>();
    private Set<Class> instantiatedClasses = new HashSet<>();
    private Set<Class> requestedClasses = new HashSet<>();

    @Override
    public <T> Provider<T> getProvider(Class<T> intfType) {
        return () -> {
            Class<T> implType;

            if (intfType.isInterface() && bindings.containsKey(intfType)) {
                implType = bindings.get(intfType).getImplType();
            } else {
                throw new BindingNotFoundException("No binding found");
            }
            if (requestedClasses.contains(implType) && !instantiatedClasses.contains(implType)) {
                throw new CyclicDependencyException("Cyclic dependency for the " + implType + " is detected");
            } else {
                requestedClasses.add(implType);
            }
            if (bindings.containsKey(intfType)) {
                Binding binding = bindings.get(intfType);
                if (binding.isSingleton() && binding.getInstance() != null) {
                    return (T) binding.getInstance();
                }
            }
            return createInstance(implType , intfType);
        };
    }

    private <T> T createInstance(Class<T> classType, Class<T> intfType){
        Constructor<T> constructor = getConstructor(classType);
        Parameter[] parameters = constructor.getParameters();

        List<Object> arguments = Arrays.stream(parameters)
                .map(parameter -> getProvider(parameter.getType()).getInstance())
                .collect(Collectors.toList());

        try {
            T newInstance = constructor.newInstance(arguments.toArray());
            instantiatedClasses.add(classType);
            if (isSingleton(intfType)){
                Binding binding = bindings.get(intfType);
                binding.setInstance(newInstance);
            }
            return newInstance;
        } catch (Exception e) {
            throw new RuntimeException("Exception occurs during the instantiation of a new instance");
        }
    }

    private <T> Constructor <T> getConstructor(Class type){
        Constructor<?>[] constructors = type.getConstructors();
        if (constructors.length == 0) {
            throw new ConstructorNotFoundException("The class has no public constructor");
        }
        if (constructors.length > 1) {
            List<Constructor<?>> constructorsWithInject = Arrays.stream(constructors)
                    .filter(constructor -> constructor.isAnnotationPresent(Inject.class))
                    .collect(Collectors.toList());
            if (constructorsWithInject.isEmpty()){
                throw new NoInjectConstructorException("There is more than one public constructor without inject annotation");
            }
            if (constructorsWithInject.size() != 1) {
                throw new TooManyConstructorsException("More than one constructor");
            }
            return (Constructor<T>) constructorsWithInject.get(0);
        } else {
            return (Constructor<T>) constructors[0];
        }
    }

    private boolean isSingleton(Class type){
        Binding binding = bindings.get(type);
        return binding != null && binding.isSingleton();
    }

    @Override
    public <T> void bind(Class<T> intf, Class<? extends T> impl) {
        checkNotNull(intf, impl);

        if (intf.isInterface()) {
            if (impl.isInterface() || isAbstractClass(impl)) {
                throw new IllegalArgumentException("The second argument type is not a class");
            } else {
                Binding<T> binding = new Binding<>(null, intf, impl, false);
                bindings.put(intf, binding);
            }
        } else {
            throw new IllegalArgumentException("The first argument type is not an interface");
        }
    }

    private boolean isAbstractClass(Class type){
        return !type.isInterface() && Modifier.isAbstract(type.getModifiers());
    }

    @Override
    public <T> void bindSingleton(Class<T> intf, Class<? extends T> impl) {
        checkNotNull(intf, impl);

        if (impl.isInterface()) {
            throw new IllegalArgumentException("The given type is an interface. Expecting the param to be an actual class");
        }
        if (bindings.getOrDefault(intf, null) == null) {
            Binding<T> binding = new Binding<>(null, intf, impl, true);
            bindings.put(intf, binding);
        }
    }

    private <T> void checkNotNull(Class<T> intf, Class<? extends T> impl) {
        if (intf == null || impl == null) {
            throw new IllegalArgumentException("No types are provided");
        }
    }
}
