# 功能
### 1. 功能介绍
一个跨语言的全平台测试框架，以Jenkins为底层，支持API自动化、GUI自动化、shell脚本等。

### 2. 功能逻辑

![image](https://user-images.githubusercontent.com/26532614/174438647-5f3b7ee1-e4c6-4d71-83e3-69f5059b978a.png)

### 3. 功能点

- 用户管理
  - 注册
  - 登录
  - 登出
- 用例管理
  - 添加用例: 用例名称、类型、用例数据、备注
    - 文本型
    - 文件型
  - 修改用例
  - 删除用例
- 任务管理
  - 生成任务：任务id、任务名称、备注
  - 执行任务
  - 修改任务
  - 删除任务
  - 跳转Jenkins查看报告
- Jenkisn管理
  - 添加Jenkins：Jenkins名称、命令、地址、Jenkins用户名密码、测试用例类型（文本型要有后缀）、备注、是否默认
  - 修改Jenkins
  - 删除Jenkins
- 报告管理
  - 测试任务统计
### 4. 功能概览

<img width="661" alt="image" src="https://user-images.githubusercontent.com/26532614/174421387-4fc9d02b-4351-497f-a579-99ae58275890.png">

<img width="1438" alt="image" src="https://user-images.githubusercontent.com/26532614/174421553-f95190a7-a27d-4c3c-96b2-7bd5271afdac.png">

<img width="1436" alt="image" src="https://user-images.githubusercontent.com/26532614/174430640-96bdef6d-3330-42f3-91c4-3267f6fe1f20.png">

<img width="1439" alt="image" src="https://user-images.githubusercontent.com/26532614/174421575-fe7f5139-8a8a-4246-b061-b3cf6cb80534.png">

<img width="1436" alt="image" src="https://user-images.githubusercontent.com/26532614/174421583-77603cdd-a83e-4130-89ee-b3cef6ac10f0.png">

### 5. GUI 项目演示
以 `java + allure + testNG + webdriver + maven` 框架的 GUI 自动化 demo 为例，地址 https://github.com/rikkizhu/myguitest

1，创建运行脚本

```
git clone git@github.com:rikkizhu/myguitest.git
cp -r myguitest/. .
rm -rf myguitest
mvn clean test -DsuiteXmlFile=testng.xml
allure generate allure-results -o allure-report
```

2,生成任务

<img width="1434" alt="image" src="https://user-images.githubusercontent.com/26532614/177477983-f0d50336-5796-4fd8-a54c-82c08464f1a6.png">

3，执行任务

<img width="263" alt="image" src="https://user-images.githubusercontent.com/26532614/177478117-6dd9179e-d272-4176-96ce-21764e7537b5.png">

4，查看测试报告

![image](https://user-images.githubusercontent.com/26532614/177482254-aa8a3b5b-d306-42a7-bbfb-54032d6285b9.png)

5，点击【执行完成】，跳转到 allure 报告地址

![image](https://user-images.githubusercontent.com/26532614/177482339-828e0df9-598b-42e9-a3e2-e9d28d6c7c0b.png)

6，查看构建日志
![image](https://user-images.githubusercontent.com/26532614/177482392-060a9d36-c163-446a-9e1f-ad09dc76f53e.png)


### 6. API 项目演示

以 `java  + Junit4 + rest assured + maven` 框架的 API 自动化 demo 为例，地址 https://github.com/rikkizhu/myapitest

1，创建运行脚本

```
git clone git@github.com:rikkizhu/myapitest.git 
cd myapitest 
pwd 
mvn test	
```

2,生成任务，同上

3，执行任务，同上

4，点击【执行完成】，跳转到 jenkins job，查看构建日志
![image](https://user-images.githubusercontent.com/26532614/177483377-8829e423-3db9-4c16-8753-ba2665c811cc.png)

5，同 GUI 自动化，可以集成 allure 或其他插件

