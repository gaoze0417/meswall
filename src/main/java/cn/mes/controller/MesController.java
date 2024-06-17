package cn.mes.controller;

import cn.mes.entity.FormData;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@RestController
public class MesController {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 留言列表
     *
     * @return
     */
    @GetMapping(value = "/mesList")
    public List<Map<String, Object>> mesList() {
        String sql = "SELECT id, name, email,message, istop FROM message ORDER BY istop DESC,id DESC limit 30";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> map : maps) {
            String message = (String) map.get("message");
            String mes = StringEscapeUtils.escapeHtml3(message);
            map.put("message", mes);
        }
        return maps;
    }

    /**
     * 新增留言
     *
     * @param formData
     * @return
     */
    @PostMapping(value = "/submitForm")
    public String submitForm(@RequestBody FormData formData, HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        ip = request.getRemoteAddr();
        System.out.println("姓名：" + formData.getName() +
                " 邮箱：" + formData.getEmail() +
                " 留言：" + formData.getMessage() +
                "X-Forwarded-For：" + ip);
        if (formData.getMessage().isEmpty()) {
            return "留言不能为空";
        }
        String sql = "INSERT INTO message (name, email, message, istop,ip) SELECT ?,?,?,?,? " +
                "FROM dual WHERE NOT EXISTS (SELECT 1 FROM message WHERE name =? )";
        int result = jdbcTemplate.update(sql, formData.getName(), formData.getEmail(), formData.getMessage(), 0, ip, formData.getName());
        if (result > 0) {
            return "数据已成功提交：" + formData.getName();
        } else {
            return "数据提交失败";
        }
    }

    /**
     /**
     * 修改留言
     * @param formData
     * @return
     */
//    @PostMapping(value = "/update")
//    public String updateMessage(@RequestBody FormData formData) {
//        if (formData.getMessage().isEmpty()){
//            return "留言不能为空";
//        }
//        String sql = "UPDATE message SET message = ? WHERE id = ?";
//        int result = jdbcTemplate.update(sql, formData.getMessage(), formData.getId());
//        if (result > 0) {
//            return "数据已成功更新：" + formData.getName();
//        } else {
//            return "数据更新失败";
//        }
//    }

    /**
     * 删除留言
     * @param id
     * @return
     */
//    @GetMapping(value = "/delete")
//    public String deleteMessage(String id) {
//        String sql = "DELETE FROM message WHERE id = ?";
//        int result = jdbcTemplate.update(sql, id);
//        if (result > 0) {
//            return "数据已成功删除";
//        } else {
//            return "数据删除失败";
//        }
//    }

    /**
     * 留言列表
     *
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/index")
    public ResponseEntity<InputStreamResource> index() throws IOException {
        // 获取 HTML 文件的输入流
        InputStream inputStream = new FileInputStream("../demo/crud/src/main/resources/html/index.html");
        System.out.println("主页访问成功");
        // 创建输入流资源
        InputStreamResource resource = new InputStreamResource(inputStream);
        // 设置响应的内容类型为 HTML
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        // 返回包含输入流资源的响应实体
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    /**
     * 留言admin列表
     *
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/admin")
    public ResponseEntity<InputStreamResource> admin() throws IOException {
        // 获取 HTML 文件的输入流
        InputStream inputStream = new FileInputStream("../demo/crud/src/main/resources/html/admin.html");
        System.out.println("主页访问成功");
        // 创建输入流资源
        InputStreamResource resource = new InputStreamResource(inputStream);
        // 设置响应的内容类型为 HTML
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        // 返回包含输入流资源的响应实体
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    /**
     * 新增页面
     *
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/addMes")
    public ResponseEntity<InputStreamResource> introduce() throws IOException {
        // 获取 HTML 文件的输入流
        InputStream inputStream = new FileInputStream("D:/demo/crud/src/main/resources/html/addMes.html");
        System.out.println("访问成功");
        // 创建输入流资源
        InputStreamResource resource = new InputStreamResource(inputStream);
        // 设置响应的内容类型为 HTML
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        // 返回包含输入流资源的响应实体
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

}
