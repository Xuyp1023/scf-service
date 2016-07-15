# 供应链金融业务后台服务工程 -- @Service
> 描述：该工程用于编写供应链金融业务后台服务类的相关代码

# 1、源码路径命名规范
### com.betterjr.modules.{module name}.dao
### com.betterjr.modules.{module name}.dubbo
### com.betterjr.modules.{module name}.entity
### com.betterjr.modules.{module name}.service

## 如: 授信管理模块
### com.betterjr.modules.credit.dao
### com.betterjr.modules.credit.dubbo
### com.betterjr.modules.credit.entity
### com.betterjr.modules.credit.service

# 2、资源文件路径命名规范
### sqlmap.modules.{module name}

## 如: 授信管理模块
### sqlmap.modules.credit

# 3、scf-dubbo-provider.xml配置
### dubbo:annotation package="com.betterjr.modules.{module name}",多个{module name}用逗号","隔开

## 4、spring-context-scf-dubbo-provider.xml配置

### context:component-scan base-package="com.betterjr.modules.{module name}",多个{module name}用分号";"隔开

# end