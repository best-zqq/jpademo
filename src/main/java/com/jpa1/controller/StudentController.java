package com.jpa1.controller;

import com.alibaba.fastjson.JSONObject;
import com.jpa1.entity.Student;
import com.jpa1.dto.Student2;
import com.jpa1.repository.StudentRepository;
import com.jpa1.util.BeanUtil;
import com.jpa1.util.PageBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

//@sl4j输出日志要用
@Slf4j
@RestController
@RequestMapping(value = "/students", produces = {"application/json;charset=UTF-8"})
public class StudentController {
    @Autowired
    private StudentRepository repository;

    //查找指定用户信息
    @GetMapping(value = "/{findId}")
    public ResponseEntity student(@PathVariable("findId") String id) {
        Optional<Student> optional = repository.findById(id);
        if (!optional.isPresent()) {
            log.info("没有 {} 用户", id);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            Student student = optional.get();
            return new ResponseEntity<>(JSONObject.toJSONString(student), HttpStatus.OK);
        }
    }

    //查找全部用户信息
    @GetMapping
    public ResponseEntity list() {
        List<Student> list = repository.findAll();
        return new ResponseEntity<>(JSONObject.toJSONString(list), HttpStatus.OK);
    }

    //按分页查找
    @GetMapping(value = "/{pageNo}/{pageSize}")
    public ResponseEntity findByPage(@PathVariable("pageNo") int marker,
                                     @PathVariable("pageSize") int limit) {
        Pageable pageable = PageRequest.of(marker - 1, limit);
        Page<Student> page = repository.findAll(pageable);
        PageBean<Student> pageBean = PageBean.toPageBean(page);
        /*pageBean.setPageNo(marker);
        pageBean.setPageSize(page.getSize());
        pageBean.setData(page.getContent());
        pageBean.setTotalCount(page.getTotalElements());*/
        return new ResponseEntity<>(JSONObject.toJSONString(pageBean), HttpStatus.OK);
    }

    //删除某个id
    @DeleteMapping(value = "/{deleteId}")
    public ResponseEntity delete(@PathVariable("deleteId") String id) {
        log.info("start delete cert [===deleteId:[{}]===]", id);
        Optional<Student> studentOptional = repository.findById(id);
        if (studentOptional.isPresent()) {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("该{}不存在", id);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }


    }

    //添加某个实体
    //DTO的目的是：如果一个属性一个属性的返回，效率很低，直接传输一个实体的话，很容易造成数据库表格式的泄露。
    //所以创建一个DTO，把想放的属性放进去
    @PostMapping(value = "")
    public ResponseEntity save1(@RequestBody Student2 student) {
        Student stu = new Student();
        BeanUtil.copyProperties(student, stu);
        stu = repository.save(stu);
        return new ResponseEntity<>(JSONObject.toJSONString(stu), HttpStatus.OK);

    }

    /*@GetMapping(value ="/save2")
    public String save2(@PathParam("value")String id){

        Student stu = new Student();
        stu.setStudentSex("m");
        stu.setStudentName("lilei");
        repository.save(stu);
        return "add successful";
    }*/
    //修改某个实体
    @PutMapping(value = "/{updateId}")
    public ResponseEntity update(@PathVariable("updateId") String id,
                                 @RequestBody Student2 student) {
        //这里弄一个Student2是为了在Controller层传递参数的，在老师的程序中体现在了dto包中
        log.info("start update cert [===updateId:[{}]===certJson:[{}]===]", id, student);
        Optional<Student> optional = repository.findById(id);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Student stu = optional.get();
        //给赋值，第一个参数为当前对象，第二个参数为 目标
        BeanUtil.copyProperties(student, stu);
        stu = repository.save(stu);
        return new ResponseEntity<>(JSONObject.toJSONString(stu), HttpStatus.OK);


          /*log.info("start update cert [===certificateId:[{}]===certJson:[{}]===]", certificateId, certDTO);
        CertificateBean cert = new CertificateBean();
        BeanUtil.copyProperties(certDTO, cert);
        cert.setCertificateId(certificateId);
        CertificateBean certificateBean = certificateNewService.updateCert(cert);
        return new ResponseEntity<>(JSONObject.toJSONString(certificateBean), HttpStatus.OK);*/
    }

    @GetMapping(value = "/and")
    public ResponseEntity spec() {
        Specification<Student> specification = new Specification<Student>() {
            @Override
            public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<Object> studentName = root.get("studentName");
                Path<Object> studentSex = root.get("studentSex");
                Path<Object> studentAge = root.get("studentAge");
                Predicate p1 = cb.equal(studentAge, "16");
                Predicate p2 = cb.equal(studentSex, "m");
                //gt,lt,le,like:得到path对象，然后进行比较。 指定参数类型：path.as(类型的字节码对象)
                Predicate p3 = cb.like(studentName.as(String.class), "zqq");
                Predicate and = cb.or(cb.and(p2, p3), p1);
                return and;
            }
        };
        //Sort sort = new Sort(Sort.Direction.DESC,"studentName");
        Pageable pageable = PageRequest.of(0, 2);
        Page<Student> page = repository.findAll(specification, pageable);
        List<Student> list = page.getContent();
        return new ResponseEntity<>(JSONObject.toJSONString(list), HttpStatus.OK);
    }
}
