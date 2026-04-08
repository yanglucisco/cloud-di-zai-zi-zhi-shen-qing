使用spring boot、spring cloud、spring cloud alibaba实现微服务
1、首先启动nacos docker
2、登录nacos，地址http://127.0.0.1:8848/nacos
3、默认密码ncos
4、项目根目录新建.env文件
AUTH_CENTER_DB_PASSWORD=Htht123.com
ROLE_MANAGE_DB_PASSWORD=Htht123.com
NACOS_HOST=127.0.0.1
NACOS_PORT=8848
NACOS_PASSWORD=Htht123.com
DB_HOST=127.0.0.1
DB_PORT=13306
DB_NAME=auth_center
DB_USER=root
DB_PASSWORD=Htht123.com
AUTH_CENTER_REDIS_HOST=127.0.0.1
AUTH_CENTER_REDIS_PORT=16379
AUTH_CENTER_HOST=127.0.0.1
AUTH_CENTER_PORT=20001
ROLE_MANAGE_DB_HOST=127.0.0.1
ROLE_MANAGE_DB_PORT=13307
ROLE_MANAGE_DB_NAME=role_manage
ROLE_MANAGE_DB_USER=root
ROLE_MANAGE_DB_PASSWORD=Htht123.com

值根据实际情况修改

5、修改host
127.0.0.1 vue-front-before-gateway.clouddizai.com
127.0.0.1 auth-center
127.0.0.1 gateway.clouddizai.com

6、前端项目vue-front-before-gateway，启动命令npm run dev -- --port 8089

使用k8s kustomize启动时
1、修改common-configmap.yaml中的nacos用户名和密码；
2、修改各个服务overlays下的相关信息，例如数据库连接信息；
3、如果使用minikube，记得启动端口转发功能；

