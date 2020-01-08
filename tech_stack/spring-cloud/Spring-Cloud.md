# Spring-Cloud





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

























