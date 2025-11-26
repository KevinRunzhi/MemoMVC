package com.memo.controller;

import com.memo.model.Memo;
import com.memo.model.User;
import com.memo.service.MemoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

@WebServlet("/memo/edit")
public class MemoEditController extends HttpServlet {

    private MemoService memoService = new MemoService();
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        Long userId = user.getId();

        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect(request.getContextPath() + "/memo/list");
            return;
        }
        Long id = Long.parseLong(idStr);

        Memo memo = memoService.getMemo(id, userId);
        if (memo == null) {
            response.sendRedirect(request.getContextPath() + "/memo/list");
            return;
        }

        request.setAttribute("mode", "edit");
        request.setAttribute("memo", memo);
        request.getRequestDispatcher("/memo_form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        Long userId = user.getId();

        String idStr = request.getParameter("id");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String importanceStr = request.getParameter("importance");
        String dueTimeStr = request.getParameter("dueTime");

        if (idStr == null) {
            response.sendRedirect(request.getContextPath() + "/memo/list");
            return;
        }
        Long id = Long.parseLong(idStr);

        if (title == null || title.trim().isEmpty()
                || importanceStr == null || importanceStr.trim().isEmpty()
                || dueTimeStr == null || dueTimeStr.trim().isEmpty()) {
            request.setAttribute("error", "标题、重要程度、截止时间不能为空");
            request.setAttribute("mode", "edit");
            Memo memo = memoService.getMemo(id, userId);
            request.setAttribute("memo", memo);
            request.getRequestDispatcher("/memo_form.jsp").forward(request, response);
            return;
        }

        int importance = Integer.parseInt(importanceStr);
        dueTimeStr = dueTimeStr.replace("T", " ");
        Date dueTime;
        try {
            dueTime = SDF.parse(dueTimeStr);
        } catch (ParseException e) {
            request.setAttribute("error", "截止时间格式不正确");
            request.setAttribute("mode", "edit");
            Memo memo = memoService.getMemo(id, userId);
            request.setAttribute("memo", memo);
            request.getRequestDispatcher("/memo_form.jsp").forward(request, response);
            return;
        }

        Memo memo = new Memo();
        memo.setId(id);
        memo.setUserId(userId);
        memo.setTitle(title);
        memo.setContent(content);
        memo.setImportance(importance);
        memo.setDueTime(dueTime);

        boolean ok = memoService.updateMemo(memo);
        if (ok) {
            response.sendRedirect(request.getContextPath() + "/memo/list");
        } else {
            request.setAttribute("error", "更新失败，请稍后再试");
            request.setAttribute("mode", "edit");
            memo = memoService.getMemo(id, userId);
            request.setAttribute("memo", memo);
            request.getRequestDispatcher("/memo_form.jsp").forward(request, response);
        }
    }
}
