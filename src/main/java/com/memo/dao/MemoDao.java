package com.memo.dao;

import com.memo.model.Memo;
import com.memo.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MemoDao {

    public List<Memo> findByUserWithin7Days(Long userId) {
        List<Memo> list = new ArrayList<>();
        String sql =
                "SELECT * FROM t_memo " +
                "WHERE is_deleted = 0 AND user_id = ? " +
                "AND (" +
                " (complete_time IS NULL AND due_time >= DATE_SUB(NOW(), INTERVAL 7 DAY)) " +
                " OR (complete_time IS NOT NULL AND complete_time >= DATE_SUB(NOW(), INTERVAL 7 DAY))" +
                " OR (complete_time IS NULL AND due_time >= NOW())" +
                ") " +
                "ORDER BY importance DESC, due_time ASC";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setLong(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Memo memo = new Memo();
                memo.setId(rs.getLong("id"));
                memo.setUserId(rs.getLong("user_id"));
                memo.setTitle(rs.getString("title"));
                memo.setContent(rs.getString("content"));
                memo.setImportance(rs.getInt("importance"));
                memo.setDueTime(rs.getTimestamp("due_time"));
                memo.setCreateTime(rs.getTimestamp("create_time"));
                memo.setCompleteTime(rs.getTimestamp("complete_time"));
                memo.setDeleted(rs.getInt("is_deleted") == 1);
                memo.setRemark(rs.getString("remark"));
                calculateStatus(memo);
                list.add(memo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    private void calculateStatus(Memo memo) {
        Date now = new Date();
        Date due = memo.getDueTime();
        Date complete = memo.getCompleteTime();

        if (complete != null) {
            memo.setStatus(Memo.STATUS_DONE);
            return;
        }
        if (due.before(now)) {
            memo.setStatus(Memo.STATUS_OVERDUE);
        } else {
            long diffMillis = due.getTime() - now.getTime();
            long hours = diffMillis / (1000 * 60 * 60);
            if (hours < 24) {
                memo.setStatus(Memo.STATUS_URGENT);
            } else {
                memo.setStatus(Memo.STATUS_TODO);
            }
        }
    }

    public boolean insert(Memo memo) {
        String sql = "INSERT INTO t_memo(user_id, title, content, importance, due_time, status, is_deleted) " +
                     "VALUES (?,?,?,?,?,0,0)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setLong(1, memo.getUserId());
            ps.setString(2, memo.getTitle());
            ps.setString(3, memo.getContent());
            ps.setInt(4, memo.getImportance());
            ps.setTimestamp(5, new Timestamp(memo.getDueTime().getTime()));
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(null, ps, conn);
        }
    }

    public Memo findByIdAndUser(Long id, Long userId) {
        String sql = "SELECT * FROM t_memo WHERE id = ? AND user_id = ? AND is_deleted = 0";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Memo memo = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setLong(1, id);
            ps.setLong(2, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                memo = new Memo();
                memo.setId(rs.getLong("id"));
                memo.setUserId(rs.getLong("user_id"));
                memo.setTitle(rs.getString("title"));
                memo.setContent(rs.getString("content"));
                memo.setImportance(rs.getInt("importance"));
                memo.setDueTime(rs.getTimestamp("due_time"));
                memo.setCreateTime(rs.getTimestamp("create_time"));
                memo.setCompleteTime(rs.getTimestamp("complete_time"));
                memo.setDeleted(rs.getInt("is_deleted") == 1);
                memo.setRemark(rs.getString("remark"));
                calculateStatus(memo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return memo;
    }

    public boolean update(Memo memo) {
        String sql = "UPDATE t_memo SET title=?, content=?, importance=?, due_time=? " +
                     "WHERE id=? AND user_id=? AND is_deleted=0";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, memo.getTitle());
            ps.setString(2, memo.getContent());
            ps.setInt(3, memo.getImportance());
            ps.setTimestamp(4, new Timestamp(memo.getDueTime().getTime()));
            ps.setLong(5, memo.getId());
            ps.setLong(6, memo.getUserId());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(null, ps, conn);
        }
    }

    public boolean logicalDelete(Long id, Long userId) {
        String sql = "UPDATE t_memo SET is_deleted=1 WHERE id=? AND user_id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setLong(1, id);
            ps.setLong(2, userId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(null, ps, conn);
        }
    }

    public boolean markComplete(Long id, Long userId) {
        String sql = "UPDATE t_memo SET complete_time=NOW(), status=3 " +
                     "WHERE id=? AND user_id=? AND is_deleted=0";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setLong(1, id);
            ps.setLong(2, userId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(null, ps, conn);
        }
    }
}
