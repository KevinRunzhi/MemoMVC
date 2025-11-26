package com.memo.service;

import com.memo.dao.MemoDao;
import com.memo.model.Memo;

import java.util.ArrayList;
import java.util.List;

public class MemoService {

    private MemoDao memoDao = new MemoDao();

    // 列表 + 状态筛选 + 关键字过滤
    public List<Memo> listMemos(Long userId, String statusParam, String keyword) {
        List<Memo> all = memoDao.findByUserWithin7Days(userId);
        List<Memo> filtered = new ArrayList<>();

        Integer filterStatus = null;
        if (statusParam != null && !"".equals(statusParam) && !"all".equals(statusParam)) {
            try {
                filterStatus = Integer.parseInt(statusParam);
            } catch (NumberFormatException ignored) { }
        }

        for (Memo m : all) {
            if (filterStatus != null && !filterStatus.equals(m.getStatus())) {
                continue;
            }
            if (keyword != null && !keyword.trim().isEmpty()) {
                if (m.getTitle() == null || !m.getTitle().contains(keyword.trim())) {
                    continue;
                }
            }
            filtered.add(m);
        }
        return filtered;
    }

    public boolean addMemo(Memo memo) {
        return memoDao.insert(memo);
    }

    public Memo getMemo(Long id, Long userId) {
        return memoDao.findByIdAndUser(id, userId);
    }

    public boolean updateMemo(Memo memo) {
        return memoDao.update(memo);
    }

    public boolean deleteMemo(Long id, Long userId) {
        return memoDao.logicalDelete(id, userId);
    }

    public boolean completeMemo(Long id, Long userId) {
        return memoDao.markComplete(id, userId);
    }
}
