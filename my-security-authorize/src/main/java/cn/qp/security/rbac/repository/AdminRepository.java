/**
 * 
 */
package cn.qp.security.rbac.repository;

import cn.qp.security.rbac.domain.Admin;
import org.springframework.stereotype.Repository;

/**
 * @author zhailiang
 *
 */
@Repository
public interface AdminRepository extends ImoocRepository<Admin> {

	Admin findByUsername(String username);

}
