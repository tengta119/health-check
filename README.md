# health-check

## 1.导入并运行项目脚手架

### 后端

数据库配置

![image-20241108091919201](images/001.png)



### 前端

配置环境

```vue
npm install
```



## 2.普通体检类型管理功能

此功能与脚手架中的系统公告差不多，直接复制粘贴

### 后端

#### entity

体检类型***ExaminationType***

```java
public class ExaminationType {

    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```





#### controller

前端访问的接口***ExaminationTypeController.java***

```java
@RestController
@RequestMapping("/examinationType")
public class ExaminationTypeController {

    @Autowired
    private ExaminationTypeService examinationTypeService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody ExaminationType examinationType) {
        examinationTypeService.add(examinationType);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody ExaminationType examinationType) {
        examinationTypeService.updateById(examinationType);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        examinationTypeService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        examinationTypeService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        ExaminationType examinationType = examinationTypeService.selectById(id);
        return Result.success(examinationType);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(ExaminationType examinationType) {
        List<ExaminationType> list = examinationTypeService.selectAll(examinationType);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(ExaminationType examinationType,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<ExaminationType> pageInfo = examinationTypeService.selectPage(examinationType, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
```



#### service和mapper

***ExaminationTypeService***

```java
package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.ExaminationType;
import com.example.mapper.ExaminationTypeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExaminationTypeService {

    @Autowired
    private ExaminationTypeMapper examinationTypeMapper;

    public void add(ExaminationType examinationType) {
        examinationTypeMapper.insert(examinationType);
    }

    public void updateById(ExaminationType examinationType) {
        examinationTypeMapper.updateById(examinationType);
    }

    public void deleteById(Integer id) {
        examinationTypeMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            examinationTypeMapper.deleteById(id);
        }
    }

    public ExaminationType selectById(Integer id) {
        return examinationTypeMapper.selectById(id);
    }

    public List<ExaminationType> selectAll(ExaminationType examinationType) {
        return examinationTypeMapper.selectAll(examinationType);
    }

    public PageInfo<ExaminationType> selectPage(ExaminationType examinationType, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ExaminationType> list = examinationTypeMapper.selectAll(examinationType);
        return PageInfo.of(list);
    }

}

```

***ExaminationTypeMapper***

```java
public interface ExaminationTypeMapper {

    int insert(ExaminationType examinationType);

    void updateById(ExaminationType examinationType);

    void deleteById(Integer id);

    @Select("select * from `examination_type` where id = #{id}")
    ExaminationType selectById(Integer id);

    List<ExaminationType> selectAll(ExaminationType examinationType);

}
```

***ExaminationTypeMapper.xml***

```xml
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.mapper.ExaminationTypeMapper">

    <select id="selectAll" resultType="com.example.entity.ExaminationType">
        select * from `examination_type`
        <where>
            <if test="name != null"> and name like concat('%', #{name}, '%')</if>
        </where>
        order by id desc
    </select>

    <delete id="deleteById">
        delete from `examination_type`
        where id = #{id}
    </delete>

    <insert id="insert" parameterType="com.example.entity.ExaminationType" useGeneratedKeys="true">
        insert into `examination_type`
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="id != null">id,</if>
            <if test="name != null">name,</if>
        </trim>
        <trim prefix="values (" suffix=")" suffixOverrides=",">
            <if test="id != null">#{id},</if>
            <if test="name != null">#{name},</if>
        </trim>
    </insert>

    <update id="updateById" parameterType="com.example.entity.ExaminationType">
        update `examination_type`
        <set>
            <if test="name != null">
                name = #{name},
            </if>
        </set>
        where id = #{id}
    </update>

</mapper>
```

### 前端

#### 路由

添加路由

![002](images/002.png)





## 3.医生管理功能

### 后端

没什么好说的还是复制粘贴，不过要注意mybatis和sql语句的命名规范

![003](images/003.png)

![004](images/004.png)



## 4.开发科室和职位信息管理

没什么好说，还是复制粘贴

<font color="red">注意</font>不能单独写一个mapper.xml文件，否则会报错



## 5.开发普通用户管理功能

cv



## 6.开发登录、注册、个人信息、修改密码功能

### 后端

#### JWT

由于除了`登录`,`注册`，`退出`不需要JWT验证，其余访问都需要验证，又因在此模块中添加了普通**用户**和**医生**的角色，所以需要对拦截器进行更改

**JWTInterceptor**

```java
//JWt拦截器
@Component
public class JWTInterceptor implements HandlerInterceptor {

    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;
    @Autowired
    private DoctorService doctorService;

    //在请求处理之前调用
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从请求中拿到token
        String token = request.getHeader(Constants.TOKEN);
        if (ObjectUtil.isNull(token)) {
            //从请求参数中拿一次
            token =  request.getParameter(Constants.TOKEN);
        }
        //开始认证
        if (ObjectUtil.isNull(token)) {
            throw new CustomException(ResultCodeEnum.TOKEN_INVALID_ERROR);
        }
        Account account = null;

        try {
            System.out.println(token);
            String audience = JWT.decode(token).getAudience().get(0);
            String userId = audience.split("-")[0];
            String role = audience.split("-")[1];
            //根据角色判断属于那个数据库
            if (RoleEnum.ADMIN.name().equals(role)) {
                account = adminService.selectById(Integer.valueOf(userId));
            } else if (RoleEnum.USER.name().equals(role)) {
                account = userService.selectById(Integer.valueOf(userId));
            } else if (RoleEnum.DOCTOR.name().equals(role)) {
                account = doctorService.selectById(Integer.valueOf(userId));
            } else {
                throw new CustomException("500","非法请求");
            }

        } catch (Exception e) {
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }
        if (account == null) {
            //用户不存在
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }
        try {
            //通过用户密码作为密钥验证密钥的合法行
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(account.getPassword())).build();
            jwtVerifier.verify(token); //验证token
        } catch (JWTVerificationException e) {
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }

        return true;
    }
}
```



## 6.健康科普信息管理

**FileController**

```java
    /**
     * wang-editor编辑器文件上传接口
     */
    @PostMapping("/wang/upload")
    public Map<String, Object> wangEditorUpload(MultipartFile file) {
        String flag = System.currentTimeMillis() + "";
        String fileName = file.getOriginalFilename();
        try {
            // 文件存储形式：时间戳-文件名
            FileUtil.writeBytes(file.getBytes(), filePath + flag + "-" + fileName);
            System.out.println(fileName + "--上传成功");
            Thread.sleep(1L);
        } catch (Exception e) {
            System.err.println(fileName + "--文件上传失败");
        }
        String http = fileBaseUrl + "/files/download/";
        Map<String, Object> resMap = new HashMap<>();
        // wangEditor上传图片成功后， 需要返回的参数
        resMap.put("errno", 0);
        resMap.put("data", CollUtil.newArrayList(Dict.create().set("url", http + flag + "-" + fileName)));
        return resMap;
    }
```



## 7.知识科普页面

cv

