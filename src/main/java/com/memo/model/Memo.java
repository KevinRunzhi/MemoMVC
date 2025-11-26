package com.memo.model;

import java.util.Date;

public class Memo {

    public static final int STATUS_TODO = 0;      // 待办
    public static final int STATUS_URGENT = 1;    // 加急（距离截止 24 小时内）
    public static final int STATUS_OVERDUE = 2;   // 未完成（逾期）
    public static final int STATUS_DONE = 3;      // 已完成

    private Long id;
    private Long userId;
    private String title;
    private String content;
    private Integer importance;
    private Date dueTime;
    private Date createTime;
    private Date completeTime;
    private Integer status;
    private Boolean deleted;
    private String remark;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public Integer getImportance() { return importance; }

    public void setImportance(Integer importance) { this.importance = importance; }

    public Date getDueTime() { return dueTime; }

    public void setDueTime(Date dueTime) { this.dueTime = dueTime; }

    public Date getCreateTime() { return createTime; }

    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getCompleteTime() { return completeTime; }

    public void setCompleteTime(Date completeTime) { this.completeTime = completeTime; }

    public Integer getStatus() { return status; }

    public void setStatus(Integer status) { this.status = status; }

    public Boolean getDeleted() { return deleted; }

    public void setDeleted(Boolean deleted) { this.deleted = deleted; }

    public String getRemark() { return remark; }

    public void setRemark(String remark) { this.remark = remark; }
}
