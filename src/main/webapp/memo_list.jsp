<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.memo.model.User,com.memo.model.Memo,java.util.List" %>
<html>
<head>
    <title>我的备忘录</title>
</head>
<body>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    String ctx = request.getContextPath();
    List<Memo> memos = (List<Memo>) request.getAttribute("memos");
    String statusParam = (String) request.getAttribute("statusParam");
    if (statusParam == null) statusParam = "all";
    String keyword = (String) request.getAttribute("keyword");
%>

<h2>备忘录系统 - 我的备忘录</h2>
<p>欢迎你，<b><%=user.getUsername()%></b> |
    <a href="<%=ctx%>/logout">退出登录</a>
</p>

<hr/>

<form method="get" action="<%=ctx%>/memo/list">
    状态：
    <select name="status">
        <option value="all" <%= "all".equals(statusParam) ? "selected" : "" %>>全部</option>
        <option value="0" <%= "0".equals(statusParam) ? "selected" : "" %>>待办</option>
        <option value="1" <%= "1".equals(statusParam) ? "selected" : "" %>>加急</option>
        <option value="2" <%= "2".equals(statusParam) ? "selected" : "" %>>未完成(逾期)</option>
        <option value="3" <%= "3".equals(statusParam) ? "selected" : "" %>>已完成</option>
    </select>
    &nbsp;&nbsp;
    关键词：
    <input type="text" name="keyword" value="<%= keyword == null ? "" : keyword %>"/>
    <input type="submit" value="查询"/>
</form>

<p>
    <a href="<%=ctx%>/memo/add">新增备忘录</a>
</p>

<table border="1" cellpadding="6" cellspacing="0">
    <tr>
        <th>ID</th>
        <th>标题</th>
        <th>重要程度</th>
        <th>截止时间</th>
        <th>状态</th>
        <th>操作</th>
    </tr>
    <%
        if (memos != null) {
            for (Memo m : memos) {
                String statusText;
                switch (m.getStatus()) {
                    case Memo.STATUS_TODO: statusText = "待办"; break;
                    case Memo.STATUS_URGENT: statusText = "加急"; break;
                    case Memo.STATUS_OVERDUE: statusText = "未完成(逾期)"; break;
                    case Memo.STATUS_DONE: statusText = "已完成"; break;
                    default: statusText = "未知";
                }
    %>
    <tr>
        <td><%=m.getId()%></td>
        <td><%=m.getTitle()%></td>
        <td><%=m.getImportance()%></td>
        <td><%=m.getDueTime()%></td>
        <td><%=statusText%></td>
        <td>
            <a href="<%=ctx%>/memo/edit?id=<%=m.getId()%>">编辑</a>
            &nbsp;|&nbsp;
            <a href="<%=ctx%>/memo/delete?id=<%=m.getId()%>"
               onclick="return confirm('确定删除这条备忘录吗？');">删除</a>
            <% if (m.getStatus() != Memo.STATUS_DONE) { %>
                &nbsp;|&nbsp;
                <a href="<%=ctx%>/memo/complete?id=<%=m.getId()%>">标记完成</a>
            <% } %>
        </td>
    </tr>
    <%      }
        }
    %>
</table>

</body>
</html>
