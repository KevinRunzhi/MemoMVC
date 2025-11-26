package com.memo.controller;

import com.memo.model.Memo;
import com.memo.model.User;
import com.memo.service.MemoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/memo/list")
public class MemoListController extends HttpServlet {

    private MemoService memoService = new MemoService();

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
        String statusParam = request.getParameter("status");
        String keyword = request.getParameter("keyword");

        List<Memo> memos = memoService.listMemos(userId, statusParam, keyword);

        request.setAttribute("memos", memos);
        request.setAttribute("statusParam", statusParam == null ? "all" : statusParam);
        request.setAttribute("keyword", keyword);
        request.getRequestDispatcher("/memo_list.jsp").forward(request, response);
    }
}
