# Spring Cloud



## Distributed/versioned configuration

### Environment

> Interface representing the environment in which the current application is running. Models two key aspects of the application environment: profiles and properties. Methods related to property access are exposed via the `PropertyResolver` super interface.



#### profiles

> profile is a named, logical group of bean definitions to be registered with the container only if the given profile is active. Beans may be assigned to a profile whether defined in XML or via annotations; see the spring-beans 3.1 schema or the `@Profile` annotation for syntax details. The role of the `Environment` object with relation to profiles is in determining which profiles (if any) are currently active, and which profiles (if any) should be active by default.

 

#### Properties

> Properties play an important role in almost all applications, and may originate from a variety of sources: properties files, JVM system properties, system environment variables, JNDI, servlet context parameters, ad-hoc Properties objects, Maps, and so on. The role of the environment object with relation to properties is to provide the user with a convenient service interface for configuring property sources and resolving properties from them.





## Service registration and discovery



### eureka



### zookeeper



### consul





## Routing







## Service-to-service calls







## Load balancing







## Circuit Breakers







## Distributed messaging





Netflix Achaius -- 配置动态写入和更新



## Spring Framework  Environment 抽象

### 语义

存储配置

存储profile

转换类型

### API

#### 读

- `org.springframework.core.env.Environment`
  - PropertyResolver 读取配置
  - profile

#### 写

- `org.springframework.core.env.ConfigurableEnvironment`
  - 配置 -- 获取 MutablePropertySources
  - profile



### 配置源 

#### API

propertySource

propertySources

#### 注解

@propertySource

@propertySources

#### Spring Profile





### 类型转换

#### API

`org.springframework.core.convert.ConversionService`

`org.springframework.core.convert.converter.Converter`





## Spring Cloud 配置

### 配置来源

BootStrapApplicationListener

























