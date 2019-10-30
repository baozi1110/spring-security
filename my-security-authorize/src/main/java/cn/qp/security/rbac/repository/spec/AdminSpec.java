/**
 * 
 */
package cn.qp.security.rbac.repository.spec;

import cn.qp.security.rbac.domain.Admin;
import cn.qp.security.rbac.dto.AdminCondition;
import cn.qp.security.rbac.repository.support.ImoocSpecification;
import cn.qp.security.rbac.repository.support.QueryWraper;

/**
 * @author zhailiang
 *
 */
public class AdminSpec extends ImoocSpecification<Admin, AdminCondition> {

	public AdminSpec(AdminCondition condition) {
		super(condition);
	}

	@Override
	protected void addCondition(QueryWraper<Admin> queryWraper) {
		addLikeCondition(queryWraper, "username");
	}

}
