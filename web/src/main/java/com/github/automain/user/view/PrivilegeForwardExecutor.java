package com.github.automain.user.view;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.automain.common.vo.BreadcrumbVO;
import com.github.automain.user.bean.TbPrivilege;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequestUrl("/privilege/forward")
public class PrivilegeForwardExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String forwardType = getString("forwardType", request, "");
        String jspPath = null;
        Long parentId = getLong("parentId", request, 0L);
        request.setAttribute("parentId", parentId);
        Long topId = getLong("topId", request, 0L);
        request.setAttribute("topId", topId);
        Long privilegeId = getLong("privilegeId", request);
        request.setAttribute("privilegeId", privilegeId);
        switch (forwardType) {
            case "add":
                jspPath = "user/privilege_add";
                TbPrivilege parent = TB_PRIVILEGE_SERVICE.selectTableById(connection, parentId);
                request.setAttribute("parent", parent);
                break;
            case "update":
                TbPrivilege bean = TB_PRIVILEGE_SERVICE.selectTableById(connection, privilegeId);
                request.setAttribute("bean", bean);
                jspPath = "user/privilege_update";
                break;
            case "role":
                jspPath = "user/privilege_role";
                break;
            default:
                List<BreadcrumbVO> parentList = new ArrayList<BreadcrumbVO>();
                if (parentId.equals(0L)) {
                    request.setAttribute("cite", "顶级");
                } else {
                    TbPrivilege privilege = TB_PRIVILEGE_SERVICE.selectTableById(connection, parentId);
                    if (privilege != null) {
                        request.setAttribute("cite", privilege.getPrivilegeName());
                        setBreadcrumb(connection, privilege, parentList);
                        Collections.reverse(parentList);
                        if (parentList.size() > 1) {
                            request.setAttribute("topId", parentList.get(1).getParentId());
                        } else {
                            request.setAttribute("topId", privilege.getPrivilegeId());
                        }
                        request.setAttribute("parentList", parentList);
                    }
                }
                jspPath = "user/privilege_tab";
        }
        return jspPath;
    }

    private void setBreadcrumb(ConnectionBean connection, TbPrivilege privilege, List<BreadcrumbVO> parentList) throws SQLException {
        BreadcrumbVO vo = new BreadcrumbVO();
        if (privilege != null && !privilege.getParentId().equals(0L)) {
            TbPrivilege parent = TB_PRIVILEGE_SERVICE.selectTableById(connection, privilege.getParentId());
            if (parent != null) {
                vo.setMenuName(parent.getPrivilegeName());
                vo.setParentId(parent.getPrivilegeId());
                parentList.add(vo);
            }
            setBreadcrumb(connection, parent, parentList);
        } else {
            vo.setMenuName("顶级");
            vo.setParentId(0L);
            parentList.add(vo);
        }
    }

}