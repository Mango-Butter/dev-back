package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.staff.StaffEntity;

public interface StaffRepository {
    StaffEntity save(StaffEntity staff);
}
