package com.nxtlife.efkon.license.dao.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.efkon.license.entity.project.product.ProjectProductComment;
import com.nxtlife.efkon.license.view.project.product.ProjectProductCommentResponse;

public interface ProjectProductCommentJpaDao extends JpaRepository<ProjectProductComment, Long> {

	public List<ProjectProductCommentResponse> findByProjectProductId(Long projectProductId);

	@Query("select id from ProjectProductComment where project_product_id=?1 and active=?2")
	List<Long> findAllIdsByProjectProductIdAndActive(Long projectProductId, Boolean active);

	@Modifying
	@Query("update ProjectProductComment set active = false ,modified_by =?2, modified_at =?3 where id in ?1 ")
	List<Long> delete(List<Long> ids, Long userId, Date date);

}
