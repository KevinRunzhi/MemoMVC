<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.memo.model.Memo,java.text.SimpleDateFormat" %>
<html>
<head>
    <title>编辑备忘录</title>
</head>
<body>
<%
    String ctx = request.getContextPath();
    String mode = (String) request.getAttribute("mode"); // add / edit
    if (mode == null) mode = "add";
    Memo memo = (Memo) request.getAttribute("memo");

    String error = (String) request.getAttribute("error");
    if (error != null) {
%>
<p style="color: red;"><%=error%></p>
<%
    }

    boolean isEdit = "edit".equals(mode);
    String actionUrl = isEdit ? ctx + "/memo/edit" : ctx + "/memo/add";

    String title = "";
    String content = "";
    int importance = 1;
    String dueTimeStr = "";

    if (memo != null) {
        title = memo.getTitle() == null ? "" : memo.getTitle();
        content = memo.getContent() == null ? "" : memo.getContent();
        importance = memo.getImportance() == null ? 1 : memo.getImportance();
        if (memo.getDueTime() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            dueTimeStr = sdf.format(memo.getDueTime());
        }
    }
%>

<h2><%= isEdit ? "编辑备忘录" : "新增备忘录" %></h2>

<form action="<%=actionUrl%>" method="post">
    <% if (isEdit && memo != null) { %>
        <input type="hidden" name="id" value="<%=memo.getId()%>"/>
    <% } %>

    <label>标题：
        <input type="text" name="title" value="<%=title%>" size="40"/>
    </label><br/><br/>

    <label>重要程度（1=一般，2=重要，3=非常重要）：
        <select name="importance">
            <option value="1" <%= importance == 1 ? "selected" : "" %>>1</option>
            <option value="2" <%= importance == 2 ? "selected" : "" %>>2</option>
            <option value="3" <%= importance == 3 ? "selected" : "" %>>3</option>
        </select>
    </label><br/><br/>

    <label>截止时间：
        <input type="datetime-local" name="dueTime" value="<%=dueTimeStr%>"/>
    </label><br/><br/>

    <label>详细内容：<br/>
        <textarea name="content" rows="5" cols="50"><%=content%></textarea>
    </label><br/><br/>

    <input type="submit" value="保存"/>
    &nbsp;
    <a href="<%=ctx%>/memo/list">返回列表</a>
</form>

</body>
</html>
