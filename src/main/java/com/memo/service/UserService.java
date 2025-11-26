package com.memo.service;

import com.memo.dao.UserDao;
import com.memo.model.User;

public class UserService {

    private UserDao userDao = new UserDao();

    public User login(String username, String rawPassword) {
        if (username == null || username.trim().isEmpty()
                || rawPassword == null || rawPassword.trim().isEmpty()) {
            return null;
        }
        User user = userDao.findByUsername(username);
        if (user == null) {
            return null;
        }
        // 直接比较原始密码
        if (!rawPassword.equals(user.getPasswordHash())) {
            return null;
        }
        return user;
    }

    public boolean register(User user, String rawPassword,
                            String confirmPassword, StringBuilder errorMsg) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()
                || rawPassword == null || rawPassword.trim().isEmpty()
                || confirmPassword == null || confirmPassword.trim().isEmpty()) {
            errorMsg.append("用户名、密码不能为空");
            return false;
        }
        if (!rawPassword.equals(confirmPassword)) {
            errorMsg.append("两次密码不一致");
            return false;
        }
        User existing = userDao.findByUsername(user.getUsername());
        if (existing != null) {
            errorMsg.append("用户名已存在");
            return false;
        }
        user.setPasswordHash(rawPassword);  // 使用原始密码
        boolean ok = userDao.insert(user);
        if (!ok) {
            errorMsg.append("注册失败，请稍后再试");
        }
        return ok;
    }
}
