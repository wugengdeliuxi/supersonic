package com.tencent.supersonic.headless.server.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tencent.supersonic.auth.api.authentication.pojo.User;
import com.tencent.supersonic.auth.api.authentication.utils.UserHolder;
import com.tencent.supersonic.headless.api.pojo.DBColumn;
import com.tencent.supersonic.headless.api.pojo.request.ColumnReq;
import com.tencent.supersonic.headless.api.pojo.request.DatabaseReq;
import com.tencent.supersonic.headless.api.pojo.request.SqlExecuteReq;
import com.tencent.supersonic.headless.api.pojo.response.DatabaseResp;
import com.tencent.supersonic.headless.api.pojo.response.SemanticQueryResp;
import com.tencent.supersonic.headless.server.pojo.DatabaseParameter;
import com.tencent.supersonic.headless.server.service.DatabaseService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/semantic/database")
public class DatabaseController {

    private DatabaseService databaseService;

    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @PostMapping("/testConnect")
    public boolean testConnect(
            @RequestBody DatabaseReq databaseReq,
            HttpServletRequest request,
            HttpServletResponse response) {
        User user = UserHolder.findUser(request, response);
        return databaseService.testConnect(databaseReq, user);
    }

    @PostMapping("/createOrUpdateDatabase")
    public DatabaseResp createOrUpdateDatabase(
            @RequestBody DatabaseReq databaseReq,
            HttpServletRequest request,
            HttpServletResponse response) {
        User user = UserHolder.findUser(request, response);
        return databaseService.createOrUpdateDatabase(databaseReq, user);
    }

    @GetMapping("/{id}")
    public DatabaseResp getDatabase(
            @PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) {
        User user = UserHolder.findUser(request, response);
        return databaseService.getDatabase(id, user);
    }

    @GetMapping("/getDatabaseList")
    public List<DatabaseResp> getDatabaseList(
            HttpServletRequest request, HttpServletResponse response) {
        User user = UserHolder.findUser(request, response);
        return databaseService.getDatabaseList(user);
    }

    @DeleteMapping("/{id}")
    public boolean deleteDatabase(@PathVariable("id") Long id) {
        databaseService.deleteDatabase(id);
        return true;
    }

    @PostMapping("/executeSql")
    public SemanticQueryResp executeSql(
            @RequestBody SqlExecuteReq sqlExecuteReq,
            HttpServletRequest request,
            HttpServletResponse response) {
        User user = UserHolder.findUser(request, response);
        return databaseService.executeSql(sqlExecuteReq, sqlExecuteReq.getId(), user);
    }

    @RequestMapping("/getDbNames")
    public List<String> getDbNames(@RequestParam("id") Long databaseId) throws SQLException {
        return databaseService.getDbNames(databaseId);
    }

    @RequestMapping("/getTables")
    public List<String> getTables(
            @RequestParam("databaseId") Long databaseId, @RequestParam("db") String db)
            throws SQLException {
        return databaseService.getTables(databaseId, db);
    }

    @RequestMapping("/getColumnsByName")
    public List<DBColumn> getColumnsByName(
            @RequestParam("databaseId") Long databaseId,
            @RequestParam("db") String db,
            @RequestParam("table") String table)
            throws SQLException {
        return databaseService.getColumns(databaseId, db, table);
    }

    @PostMapping("/listColumnsBySql")
    public List<DBColumn> listColumnsBySql(@RequestBody ColumnReq columnReq) throws SQLException {
        return databaseService.getColumns(columnReq.getDatabaseId(), columnReq.getSql());
    }

    @GetMapping("/getDatabaseParameters")
    public Map<String, List<DatabaseParameter>> getDatabaseParameters(
            HttpServletRequest request, HttpServletResponse response) {
        return databaseService.getDatabaseParameters();
    }
}
